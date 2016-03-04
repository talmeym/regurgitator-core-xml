package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.ConflictPolicy.REPLACE;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.EntityLookup.parameterType;
import static com.emarte.regurgitator.core.EntityLookup.valueProcessor;
import static java.lang.Boolean.parseBoolean;

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
		String conflictPolicy = element.attributeValue(MERGE);
        return conflictPolicy != null ? ConflictPolicy.valueOf(conflictPolicy) : REPLACE;
    }

	public static ContextLocation loadContextLocation(Element element) {
		return new ContextLocation(element.attributeValue(SOURCE));
	}

	public static Element getChild(Element element) {
		return (Element) element.elements().get(0);
	}

	public static Element getOptionalChild(Element element, int index) {
		List elements = element.elements();
		return index < elements.size() ? (Element) elements.get(index) : null;
	}

	public static boolean loadOptionalBoolean(Element element, String name) {
		String value = element.attributeValue(name);
		return value != null && parseBoolean(value);
	}

	public static Integer loadOptionalInt(Element element, String name) {
		String value = element.attributeValue(name);
		return value != null ? Integer.valueOf(value) : null;
	}

	public static String loadContext(Element element) {
		return new ContextLocation(element.attributeValue(NAME)).getContext();
	}

	public static ValueProcessor loadOptionalValueProcessor(Element element, int expectedChildIndex, Set<Object> allIds) throws RegurgitatorException {
		String processorAttr = element.attributeValue(PROCESSOR);
		ValueProcessor processor = null;

		if (processorAttr != null) {
			processor = valueProcessor(processorAttr);
		} else {
			Element processorElement = getOptionalChild(element, expectedChildIndex);

			if (processorElement != null) {
				processor = processorLoaderUtil.deriveLoader(processorElement).load(processorElement, allIds);
			}
		}

		return processor;
	}
}