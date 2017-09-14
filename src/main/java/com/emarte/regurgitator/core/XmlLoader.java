package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

public interface XmlLoader<TYPE> extends Loader<Element, TYPE> {
	TYPE load(Element element, Set<Object> allIds) throws RegurgitatorException;
}
