package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class RuleXmlLoader {
    private static final Log log = Log.getLog(RuleXmlLoader.class);

    public static Rule load(Element element, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
        List<Condition> conditions = new ArrayList<Condition>();

        for (Iterator iterator = element.elementIterator(CONDITION); iterator.hasNext(); ) {
            conditions.add(ConditionXmlLoader.load((Element) iterator.next(), allIds));
        }

        String stepId = element.attributeValue(STEP);

        if(!stepIds.contains(stepId)) {
            throw new RegurgitatorException("Error with configuration: rule step not found: " + stepId);
        }

        String id = loadId(element, allIds);
        log.debug("Loaded rule '" + id + "'");
        return new Rule(id, conditions, stepId);
    }
}
