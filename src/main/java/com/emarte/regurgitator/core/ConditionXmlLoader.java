package com.emarte.regurgitator.core;

import org.w3c.dom.*;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.EntityLookup.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class ConditionXmlLoader {
	private static final Log log = getLog(ConditionXmlLoader.class);
	private static final XmlLoaderUtil<XmlLoader<ConditionBehaviour>> conditionBehaviourLoaderUtil = new XmlLoaderUtil<XmlLoader<ConditionBehaviour>>();

	public static Condition load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String source = getAttribute(element, SOURCE);
		String expectation = getAttribute(element, EXPECTATION);

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
		log.debug("Loaded condition '" + id + "'");
		return new Condition(id, new ContextLocation(source), value, expectation != null ? Boolean.valueOf(expectation) : true, behaviour);
	}

	private static Attr getBehaviourAttribute(Element element) throws RegurgitatorException {
		List<Attr> behavioursFound = new ArrayList<Attr>();
		NamedNodeMap attributes = element.getAttributes();

		for (int i = 0; i < attributes.getLength(); i++) {
			String name = attributes.item(i).getNodeName();

			if (hasConditionBehaviour(name)) {
				behavioursFound.add(element.getAttributeNode(name));
			}
		}

		boolean childElementFound = getChildElements(element).size() > 0;

		if(behavioursFound.size() == 0 && !childElementFound) {
				throw new RegurgitatorException("no valid condition behaviour is defined");
		}

		if(behavioursFound.size() > 0) {
			if(behavioursFound.size() > 1 || childElementFound) {
				throw new RegurgitatorException("more than one valid condition behaviour was found");
			}

			return behavioursFound.get(0);
		}

		return null;
	}
}
