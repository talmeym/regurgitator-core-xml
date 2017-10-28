/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.CoreConfigConstants.VALUE;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class IndexProcessorXmlLoader implements XmlLoader<IndexProcessor> {
    private static final Log log = getLog(IndexProcessorXmlLoader.class);

    @Override
    public IndexProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String source = loadOptionalStr(element, SOURCE);
        String value = loadOptionalStr(element, VALUE);

        log.debug("Loaded index processor");
        return new IndexProcessor(new ValueSource(source != null ? new ContextLocation(source) : null, value));
    }
}
