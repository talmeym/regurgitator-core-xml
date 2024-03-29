/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static uk.emarte.regurgitator.core.CoreConfigConstants.*;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class IndexOfProcessorXmlLoader implements XmlLoader<IndexOfProcessor> {
    private static final Log log = getLog(IndexOfProcessorXmlLoader.class);

    @Override
    public IndexOfProcessor load(Element element, Set<Object> allIds) {
        String source = loadOptionalStr(element, SOURCE);
        String value = loadOptionalStr(element, VALUE);
        String backwards = loadOptionalStr(element, LAST);
        log.debug("Loaded index of processor");
        return new IndexOfProcessor(new ValueSource(source != null ? new ContextLocation(source) : null, value), parseBoolean(backwards));
    }
}
