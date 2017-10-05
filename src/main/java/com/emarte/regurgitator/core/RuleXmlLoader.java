package com.emarte.regurgitator.core;

import org.w3c.dom.*;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

class RuleXmlLoader {
    private static final Log log = getLog(RuleXmlLoader.class);

    static Rule load(Element element, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
        List<Condition> conditions = new ArrayList<Condition>();
        List<Element> conditionElements = getChildElements(element, CONDITION);

        for (int i = 0; i < conditionElements.size(); i++) {
            conditions.add(ConditionXmlLoader.load(conditionElements.get(i), allIds));
        }

        String stepId = getAttribute(element, STEP);

        if(!stepIds.contains(stepId)) {
            throw new RegurgitatorException("Error with configuration: rule step not found: " + stepId);
        }

        String id = loadId(element, allIds);
        log.debug("Loaded rule '{}'", id);
        return new Rule(id, conditions, stepId);
    }
}
