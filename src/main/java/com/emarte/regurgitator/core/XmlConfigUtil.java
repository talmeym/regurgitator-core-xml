package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.ConflictPolicy.REPLACE;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.EntityLookup.parameterType;
import static com.emarte.regurgitator.core.EntityLookup.valueProcessor;

public class XmlConfigUtil {
	private static XmlLoaderUtil<XmlLoader<ValueProcessor>> processorLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueProcessor>>() ;

    public static String loadId(Element element, Set<Object> ids) throws RegurgitatorException {
        String id = element.attributeValue(ID) != null ? element.attributeValue(ID) : element.getName() + "-" + new Random().nextInt(100000);

        if(!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static ParameterPrototype loadPrototype(Element element) throws RegurgitatorException {
		return new ParameterPrototype(loadName(element), loadType(element), loadConflictPolicy(element));
    }

    public static String loadName(Element element) {
		return new ContextLocation(element.attributeValue(NAME)).getName();
    }

    public static ParameterType loadType(Element element) throws RegurgitatorException {
		String type = element.attributeValue(TYPE);
		return type != null ? parameterType(type) : STRING;
    }

    public static ConflictPolicy loadConflictPolicy(Element element) {
        return loadConflictPolicy(element.attributeValue(MERGE));
    }

    public static ConflictPolicy loadConflictPolicy(String conflictPolicy) {
        return conflictPolicy != null ? ConflictPolicy.valueOf(conflictPolicy) : REPLACE;
    }

	public static ContextLocation loadContextLocation(Element element) {
		return new ContextLocation(element.attributeValue(SOURCE));
	}

	public static Element getChild(Element element) {
		return (Element) element.elements().get(0);
	}

	public static String loadContext(Element element) {
		return new ContextLocation(element.attributeValue(NAME)).getContext();
	}

	public static String loadStringFromElementOrAttribute(Element element, String name) throws RegurgitatorException {
		return loadStringFromElementOrAttribute(element, name, true);
	}

	public static String loadStringFromElementOrAttribute(Element element, String name, boolean mandatory) throws RegurgitatorException {
		String value = element.elementText(name);

		if(element.attributeValue(name) != null) {
			if(value == null) {
				return element.attributeValue(name);
			}

			throw new RegurgitatorException(name + " cannot be specified twice");
		} else if (value == null) {
			if(mandatory) {
				throw new RegurgitatorException(name + " not specified");
			}
		}

		return value;
	}

	public static ValueProcessor loadOptionalValueProcessor(Element element, Set<Object> allIds) throws RegurgitatorException {
		String processorAttr = element.attributeValue(PROCESSOR);
		ValueProcessor processor = null;

		if (processorAttr != null) {
			processor = valueProcessor(processorAttr);
		} else {
			Element processorContainerElement = element.element(PROCESSOR);

			if (processorContainerElement != null) {
				Element processorElement = getChild(processorContainerElement);
				processor = processorLoaderUtil.deriveLoader(processorElement).load(processorElement, allIds);
			}
		}

		return processor;
	}
}