package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

public interface XmlLoader<TYPE> extends Loader {
	public TYPE load(Element element, Set<Object> allIds) throws RegurgitatorException;
}
