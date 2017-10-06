/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.getAttribute;
import static java.lang.Boolean.parseBoolean;

public class IndexOfProcessorXmlLoader implements XmlLoader<IndexOfProcessor> {
    private static final Log log = getLog(IndexOfProcessorXmlLoader.class);

    @Override
    public IndexOfProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String source = getAttribute(element, SOURCE);
        String value = getAttribute(element, VALUE);
        String backwards = getAttribute(element, LAST);
        log.debug("Loaded index of processor");
        return new IndexOfProcessor(new ValueSource(source != null ? new ContextLocation(source) : null, value), parseBoolean(backwards));
    }
}
