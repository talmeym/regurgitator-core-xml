package com.emarte.regurgitator.core;

import org.dom4j.*;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.EntityLookup.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class ConditionXmlLoader {
	private static final Log log = getLog(ConditionXmlLoader.class);
	private static final XmlLoaderUtil<XmlLoader<ConditionBehaviour>> conditionBehaviourLoaderUtil = new XmlLoaderUtil<XmlLoader<ConditionBehaviour>>();

	public static Condition load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String source = element.attributeValue(SOURCE);
		String expectation = element.attributeValue(EXPECTATION);

		Attribute behaviourAttr = getBehaviourAttribute(element);
		ConditionBehaviour behaviour;
		String value;

		if(behaviourAttr != null) {
			behaviour = conditionBehaviour(behaviourAttr.getName());
			value = behaviourAttr.getValue();
		} else {
			Element innerElement = getChild(element);
			behaviour = conditionBehaviourLoaderUtil.deriveLoader(innerElement).load(innerElement, allIds);
			value = innerElement.getText();
		}

		String id = loadId(element, allIds);
		log.debug("Loaded condition '" + id + "'");
		return new Condition(id, new ContextLocation(source), value, expectation != null ? Boolean.valueOf(expectation) : true, behaviour);
	}

	private static Attribute getBehaviourAttribute(Element element) throws RegurgitatorException {
		List<Attribute> behavioursFound = new ArrayList<Attribute>();

		for (Attribute attr : (List<Attribute>) element.attributes()) {
			String name = attr.getName();

			if (hasConditionBehaviour(name)) {
				behavioursFound.add(attr);
			}
		}

		boolean childElementFound = element.elements().size() > 0;

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
