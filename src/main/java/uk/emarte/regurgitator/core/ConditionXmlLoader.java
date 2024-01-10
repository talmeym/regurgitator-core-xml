/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.EXPECTATION;
import static uk.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static uk.emarte.regurgitator.core.EntityLookup.conditionBehaviour;
import static uk.emarte.regurgitator.core.EntityLookup.hasConditionBehaviour;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.*;

class ConditionXmlLoader {
    private static final Log log = getLog(ConditionXmlLoader.class);
    private static final XmlLoaderUtil<XmlLoader<ConditionBehaviour>> conditionBehaviourLoaderUtil = new XmlLoaderUtil<>();

    static Condition load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String source = loadMandatoryStr(element, SOURCE);
        String expectation = loadOptionalStr(element, EXPECTATION);

        Attr behaviourAttr = getBehaviourAttribute(element);
        ConditionBehaviour behaviour;
        String value;

        if(behaviourAttr != null) {
            behaviour = conditionBehaviour(behaviourAttr.getName());
            value = behaviourAttr.getValue();
        } else {
            Element innerElement = getFirstChild(element);
            behaviour = conditionBehaviourLoaderUtil.deriveLoader(innerElement).load(innerElement, allIds);
            value = innerElement.getTextContent();
        }

        String id = loadId(element, allIds);
        log.debug("Loaded condition '{}'", id);
        return new Condition(id, new ContextLocation(source), value, expectation == null || Boolean.parseBoolean(expectation), behaviour);
    }

    private static Attr getBehaviourAttribute(Element element) throws RegurgitatorException {
        List<Attr> behavioursFound = new ArrayList<>();
        NamedNodeMap attributes = element.getAttributes();

        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.item(i).getNodeName();

            if (hasConditionBehaviour(name)) {
                behavioursFound.add(element.getAttributeNode(name));
            }
        }

        boolean childElementFound = getChildElements(element).size() > 0;

        if(behavioursFound.size() == 0 && !childElementFound) {
                throw new RegurgitatorException("No valid condition behaviour is defined");
        }

        if(behavioursFound.size() > 0) {
            if(behavioursFound.size() > 1 || childElementFound) {
                throw new RegurgitatorException("More than one valid condition behaviour was found");
            }

            return behavioursFound.get(0);
        }

        return null;
    }
}
