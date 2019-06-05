/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.INDEX_SOURCE;
import static com.emarte.regurgitator.core.CoreConfigConstants.VALUE_SOURCE;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadMandatoryStr;

public class SetAtIndexProcessorXmlLoader implements XmlLoader<SetAtIndexProcessor> {
    private static final Log log = Log.getLog(SetAtIndexProcessorXmlLoader.class);

    @Override
    public SetAtIndexProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String indexStr = loadMandatoryStr(element, INDEX_SOURCE);
        String valueStr = loadMandatoryStr(element, VALUE_SOURCE);
        log.debug("Loaded set at processor");
        return new SetAtIndexProcessor(new ValueSource(new ContextLocation(indexStr), null), new ValueSource(new ContextLocation(valueStr), null));
    }
}
