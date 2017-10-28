/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.FORMAT;
import static com.emarte.regurgitator.core.CoreConfigConstants.INDEX;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadMandatoryStr;
import static java.lang.Integer.parseInt;

public class ExtractProcessorXmlLoader implements XmlLoader<ExtractProcessor> {
    private static final Log log = getLog(ExtractProcessorXmlLoader.class);

    @Override
    public ExtractProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded extract processor");
        return new ExtractProcessor(loadOptionalStr(element, FORMAT), parseInt(loadMandatoryStr(element, INDEX)));
    }
}
