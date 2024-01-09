/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.*;
import static uk.emarte.regurgitator.core.EntityLookup.rulesBehaviour;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.RuleXmlLoader.loadRule;
import static uk.emarte.regurgitator.core.XmlConfigUtil.*;

public class DecisionXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(DecisionXmlLoader.class);

    private static final XmlLoaderUtil<XmlLoader<Step>> stepLoaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();
    private static final XmlLoaderUtil<XmlLoader<RulesBehaviour>> rulesBehaviourLoaderUtil = new XmlLoaderUtil<XmlLoader<RulesBehaviour>>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
        List<Step> steps = loadSteps(getMandatoryChildElement(element, STEPS), allIds);
        Set<Object> stepIds = stepIds(steps);
        Element rulesElement = getMandatoryChildElement(element, RULES);
        List<Rule> rules = loadRules(rulesElement, stepIds, allIds);

        String behaviourAttr = loadOptionalStr(rulesElement, BEHAVIOUR);
        RulesBehaviour behaviour;

        if(behaviourAttr != null) {
            behaviour = rulesBehaviour(behaviourAttr);
        } else {
            Element behaviourElement = getOptionalChildElement(rulesElement, BEHAVIOUR);

            if(behaviourElement != null) {
                Element childElement = getFirstChild(behaviourElement);
                behaviour = rulesBehaviourLoaderUtil.deriveLoader(childElement).load(childElement, allIds);
            } else {
                behaviour = new FirstMatchBehaviour();
            }
        }

        log.debug("Loaded decision '{}'", id);
        return new Decision(id, steps, rules, behaviour, checkDefaultStepId(loadOptionalStr(rulesElement, DEFAULT_STEP), stepIds));
    }

    private List<Step> loadSteps(Element element, Set<Object> allIds) throws RegurgitatorException {
        List<Step> allSteps = new ArrayList<Step>();
        List<Element> children = getChildElements(element);

        for (Element innerElement : children) {
            if (!innerElement.getNodeName().equals(RULES)) {
                allSteps.add(stepLoaderUtil.deriveLoader(innerElement).load(innerElement, allIds));
            }
        }

        return allSteps;
    }

    private List<Rule> loadRules(Element element, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
        List<Rule> rules = new ArrayList<Rule>();
        List<Element> rulesElements = getChildElements(element, RULE);

        for (Element rulesElement : rulesElements) {
            rules.add(loadRule(rulesElement, stepIds, allIds));
        }

        return rules;
    }

    private String checkDefaultStepId(String defaultStepId, Set<Object> stepIds) {
        if (defaultStepId != null && !stepIds.contains(defaultStepId)) {
            throw new IllegalArgumentException("Error with configuration: decision default step not found: " + defaultStepId);
        }

        return defaultStepId;
    }

    private Set<Object> stepIds(List<Step> steps) {
        Set<Object> stepIds = new HashSet<Object>(steps.size());

        for (Step step : steps) {
            stepIds.add(step.getId());
        }

        return stepIds;
    }
}
