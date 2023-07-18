/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.emarte.regurgitator.core.ConflictPolicy.REPLACE;
import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.EntityLookup.parameterType;
import static com.emarte.regurgitator.core.EntityLookup.valueProcessor;
import static java.lang.Boolean.parseBoolean;

public class XmlConfigUtil {
    private static final XmlLoaderUtil<XmlLoader<ValueProcessor>> processorLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueProcessor>>() ;
    private static final Random RANDOM = new Random();

    public static String loadId(Element element, Set<Object> ids) throws RegurgitatorException {
        String idAttr = loadOptionalStr(element, ID);
        String id = idAttr != null ? idAttr : element.getNodeName() + "-" + RANDOM.nextInt(100000);

        if(!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static ParameterPrototype loadPrototype(Element element) throws RegurgitatorException {
        return new ParameterPrototype(loadName(element), loadType(element), loadConflictPolicy(element));
    }

    private static String loadName(Element element) throws RegurgitatorException {
        return new ContextLocation(loadMandatoryStr(element, NAME)).getName();
    }

    private static ParameterType<?> loadType(Element element) throws RegurgitatorException {
        String type = loadOptionalStr(element, TYPE);
        return type != null ? parameterType(type) : STRING;
    }

    private static ConflictPolicy loadConflictPolicy(Element element) {
        String conflictPolicy = loadOptionalStr(element, MERGE);
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

    public static Element getMandatoryChildElement(Element element, String name) throws RegurgitatorException {
        List<Element> elements = getChildElements(element, name);

        if(elements.size() > 0) {
            return elements.get(0);
        }

        throw new RegurgitatorException("Xml element missing mandatory child element: " + name);
    }

    public static Element getOptionalChildElement(Element element, String name) {
        List<Element> elements = getChildElements(element, name);

        if(elements.size() > 0) {
            return elements.get(0);
        }

        return null;
    }

    public static String loadMandatoryStr(Element element, String name) throws RegurgitatorException {
        Attr attr = element.getAttributeNode(name);

        if(attr != null) {
            return attr.getValue();
        }

        throw new RegurgitatorException("Xml element missing mandatory attribute: " + name);
    }

    public static String loadOptionalStr(Element element, String name) {
        Attr attr = element.getAttributeNode(name);
        return attr != null ? attr.getValue() : null;
    }

    public static boolean loadOptionalBool(Element element, String name) {
        String value = loadOptionalStr(element, name);
        return parseBoolean(value);
    }

    public static Integer loadOptionalInt(Element element, String name) {
        String value = loadOptionalStr(element, name);
        return value != null ? Integer.parseInt(value) : null;
    }

    public static Long loadOptionalLong(Element element, String name) {
        String value = loadOptionalStr(element, name);
        return value != null ? Long.parseLong(value) : null;
    }

    public static String loadContext(Element element) throws RegurgitatorException {
        return new ContextLocation(loadMandatoryStr(element, NAME)).getContext();
    }

    private static List<Element> getChildElements(Element element, int startIndex) {
        List<Element> elements = getChildElements(element);
        return startIndex < elements.size() ? elements.subList(startIndex, elements.size()) : new ArrayList<Element>();
    }

    public static List<ValueProcessor> loadMandatoryValueProcessors(Element element, int expectedChildIndex, Set<Object> allIds) throws RegurgitatorException {
        List<ValueProcessor> processors = loadOptionalValueProcessors(element, expectedChildIndex, allIds);

        if(processors.size() > 0) {
            return processors;
        }

        throw new RegurgitatorException("element missing mandatory processor or processors");
    }

    public static List<ValueProcessor> loadOptionalValueProcessors(Element element, int expectedChildIndex, Set<Object> allIds) throws RegurgitatorException {
        List<ValueProcessor> processors = new ArrayList<ValueProcessor>();
        String processorAttr = loadOptionalStr(element, PROCESSOR);
        String processorsAttr = loadOptionalStr(element, PROCESSORS);

        if(processorAttr != null && processorsAttr != null) {
            throw new RegurgitatorException("One of processor or processors is required");
        }

        if (processorAttr != null) {
            processors.add(valueProcessor(processorAttr));
        } else if(processorsAttr != null) {
            for(String part: processorsAttr.split(",")) {
                processors.add(valueProcessor(part));
            }
        }

        List<Element> processorElements = getChildElements(element, expectedChildIndex);

        if(processorElements.size() > 0 && processors.size() > 0) {
            throw new RegurgitatorException("One of processor[s] attribute or child element required");
        }

        for(Element processorElement: processorElements) {
            processors.add(processorLoaderUtil.deriveLoader(processorElement).load(processorElement, allIds));
        }

        return processors;
    }
}