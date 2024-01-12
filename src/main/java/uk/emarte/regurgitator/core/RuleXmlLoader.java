/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.CONDITION;
import static uk.emarte.regurgitator.core.CoreConfigConstants.STEP;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.*;

class RuleXmlLoader {
    private static final Log log = getLog(RuleXmlLoader.class);

    static Rule loadRule(Element element, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
        List<Condition> conditions = new ArrayList<>();
        List<Element> conditionElements = getChildElements(element, CONDITION);

        for (Element conditionElement : conditionElements) {
            conditions.add(ConditionXmlLoader.load(conditionElement, allIds));
        }

        String stepId = loadOptionalStr(element, STEP);

        if(!stepIds.contains(stepId)) {
            throw new RegurgitatorException("Error with configuration: rule step not found: " + stepId);
        }

        String id = loadId(element, allIds);
        log.debug("Loaded rule '{}'", id);
        return new Rule(id, conditions, stepId);
    }
}
