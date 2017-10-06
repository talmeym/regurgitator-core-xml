/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.Log.getLog;

public class UuidGeneratorXmlLoader implements XmlLoader<ValueGenerator> {
    private static final Log log = getLog(UuidGeneratorXmlLoader.class);

    @Override
    public UuidGenerator load(Element element, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded uuid generator");
        return new UuidGenerator();
    }
}
