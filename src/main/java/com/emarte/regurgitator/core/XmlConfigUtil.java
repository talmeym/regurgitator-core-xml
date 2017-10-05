package com.emarte.regurgitator.core;

import org.w3c.dom.*;

import java.util.*;

import static com.emarte.regurgitator.core.ConflictPolicy.REPLACE;
import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.EntityLookup.*;
import static java.lang.Boolean.parseBoolean;

public class XmlConfigUtil {
    private static final XmlLoaderUtil<XmlLoader<ValueProcessor>> processorLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueProcessor>>() ;
    private static final Random RANDOM = new Random();

    public static String loadId(Element element, Set<Object> ids) throws RegurgitatorException {
        String idAttr = getAttribute(element, ID);
        String id = idAttr != null ? idAttr : element.getNodeName() + "-" + RANDOM.nextInt(100000);

        if(!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static ParameterPrototype loadPrototype(Element element) throws RegurgitatorException {
        return new ParameterPrototype(loadName(element), loadType(element), loadConflictPolicy(element));
    }

    private static String loadName(Element element) {
        return new ContextLocation(getAttribute(element, NAME)).getName();
    }

    private static ParameterType loadType(Element element) throws RegurgitatorException {
        String type = getAttribute(element, TYPE);
        return type != null ? parameterType(type) : STRING;
    }

    private static ConflictPolicy loadConflictPolicy(Element element) {
        String conflictPolicy = getAttribute(element, MERGE);
        return conflictPolicy != null ? ConflictPolicy.valueOf(conflictPolicy) : REPLACE;
    }

    public static List<Element> getChildElements(Element element) {
        return getElements(element.getChildNodes());
    }

    public static List<Element> getChildElements(Element element, String name) {
        return getElements(element.getChildNodes(), name);
    }

    private static List<Element> getElements(NodeList nodes) {
        return getElements(nodes, null);
    }

    private static List<Element> getElements(NodeList nodes, String name) {
        List<Element> elements = new ArrayList<Element>();

        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE && (name == null || node.getLocalName().equals(name))) {
                elements.add((Element) node);
            }
        }
        return elements;
    }

    public static Element getFirstChild(Element element) throws RegurgitatorException {
        List<Element> children = getChildElements(element);

        if(children.size() > 0) {
            return children.get(0);
        }

        throw new RegurgitatorException("Element has no children: " + element.getLocalName());
    }

    public static Element getChildElement(Element element, String name) {
        List<Element> elements = getChildElements(element, name);

        if(elements.size() > 0) {
            return elements.get(0);
        }

        return null;
    }

    public static String getMandatoryAttribute(Element element, String name) throws RegurgitatorException {
        Attr attr = element.getAttributeNode(name);

        if(attr != null) {
            return attr.getValue();
        }

        throw new RegurgitatorException("Xml element missing mandatory attribute: " + name);
    }

    public static String getAttribute(Element element, String name) {
        Attr attr = element.getAttributeNode(name);
        return attr != null ? attr.getValue() : null;
    }

    public static boolean loadOptionalBoolean(Element element, String name) {
        String value = getAttribute(element, name);
        return value != null && parseBoolean(value);
    }

    public static Integer loadOptionalInt(Element element, String name) {
        String value = getAttribute(element, name);
        return value != null ? Integer.valueOf(value) : null;
    }

    public static String loadContext(Element element) {
        return new ContextLocation(getAttribute(element, NAME)).getContext();
    }

    private static Element getChildElement(Element element, int index) {
        List<Element> elements = getChildElements(element);
        return index < elements.size() ? elements.get(index) : null;
    }

    public static ValueProcessor loadOptionalValueProcessor(Element element, int expectedChildIndex, Set<Object> allIds) throws RegurgitatorException {
        String processorAttr = getAttribute(element, PROCESSOR);
        ValueProcessor processor = null;

        if (processorAttr != null) {
            processor = valueProcessor(processorAttr);
        } else {
            Element processorElement = getChildElement(element, expectedChildIndex);

            if (processorElement != null) {
                processor = processorLoaderUtil.deriveLoader(processorElement).load(processorElement, allIds);
            }
        }

        return processor;
    }
}