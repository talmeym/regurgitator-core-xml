/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.MAX;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalInt;

public class NumberGeneratorXmlLoader implements XmlLoader<ValueGenerator> {
    private static final Log log = getLog(NumberGeneratorXmlLoader.class);

    @Override
    public ValueGenerator load(Element element, Set<Object> allIds) {
        log.debug("Loaded number generator");
        return new NumberGenerator(loadOptionalInt(element, MAX));
    }
}
