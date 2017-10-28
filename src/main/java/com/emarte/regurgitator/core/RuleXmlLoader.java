/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.CONDITION;
import static com.emarte.regurgitator.core.CoreConfigConstants.STEP;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

class RuleXmlLoader {
    private static final Log log = getLog(RuleXmlLoader.class);

    static Rule loadRule(Element element, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
        List<Condition> conditions = new ArrayList<Condition>();
        List<Element> conditionElements = getChildElements(element, CONDITION);

        for (int i = 0; i < conditionElements.size(); i++) {
            conditions.add(ConditionXmlLoader.load(conditionElements.get(i), allIds));
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
