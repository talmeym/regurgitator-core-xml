package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.EntityLookup.rulesBehaviour;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class DecisionXmlLoader implements XmlLoader<Step> {
	private static final Log log = getLog(DecisionXmlLoader.class);

	private static final XmlLoaderUtil<XmlLoader<Step>> stepLoaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();
	private static final XmlLoaderUtil<XmlLoader<RulesBehaviour>> rulesBehaviourLoaderUtil = new XmlLoaderUtil<XmlLoader<RulesBehaviour>>();

	@Override
	public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String id = loadId(element, allIds);
		List<Step> steps = loadSteps(element.element(STEPS), allIds);
		Set<Object> stepIds = stepIds(steps);
		Element rulesElement = element.element(RULES);
		List<Rule> rules = loadRules(rulesElement, stepIds, allIds);

		String behaviourAttr = rulesElement.attributeValue(BEHAVIOUR);
		RulesBehaviour behaviour;

		if(behaviourAttr != null) {
			behaviour = rulesBehaviour(behaviourAttr);
		} else {
			Element behaviourElement = rulesElement.element(BEHAVIOUR);

			if(behaviourElement != null) {
				Element childElement = getChild(behaviourElement);
				behaviour = rulesBehaviourLoaderUtil.deriveLoader(childElement).load(childElement, allIds);
			} else {
				behaviour = new FirstMatchBehaviour();
			}
		}

		log.debug("Loaded decision '" + id + "'");
		return new Decision(id, steps, rules, behaviour, checkDefaultStepId(rulesElement.attributeValue(DEFAULT_STEP), stepIds));
	}

	private List<Step> loadSteps(Element element, Set<Object> allIds) throws RegurgitatorException {
		List<Step> allSteps = new ArrayList<Step>();

		for (Iterator<Element> iterator = element.elementIterator(); iterator.hasNext(); ) {
			Element innerElement = iterator.next();

			if (!innerElement.getName().equals(RULES)) {
				allSteps.add(stepLoaderUtil.deriveLoader(innerElement).load(innerElement, allIds));
			}
		}

		return allSteps;
	}

	private List<Rule> loadRules(Element element, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
		List<Rule> rules = new ArrayList<Rule>();

		for (Iterator i = element.elementIterator(RULE); i.hasNext(); ) {
			rules.add(RuleXmlLoader.load((Element) i.next(), stepIds, allIds));
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
