/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.AS_INDEX;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalBool;

public class SizeProcessorXmlLoader implements XmlLoader<SizeProcessor> {
    private static final Log log = getLog(SizeProcessorXmlLoader.class);

    @Override
    public SizeProcessor load(Element element, Set<Object> allIds) {
        boolean lastIndex = loadOptionalBool(element, AS_INDEX);
        log.debug("Loaded size processor");
        return new SizeProcessor(lastIndex);
    }
}
