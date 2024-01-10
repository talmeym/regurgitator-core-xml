/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

public interface XmlLoader<TYPE> extends Loader<Element, TYPE> {
    TYPE load(Element element, Set<Object> allIds) throws RegurgitatorException;
}
