/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.REPLACEMENT;
import static com.emarte.regurgitator.core.CoreConfigConstants.TOKEN;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class SubstituteProcessorXmlLoader implements XmlLoader<SubstituteProcessor> {
    private static final Log log = getLog(SubstituteProcessorXmlLoader.class);

    @Override
    public SubstituteProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded substitute value");
        return new SubstituteProcessor(loadOptionalStr(element, TOKEN), loadOptionalStr(element, REPLACEMENT));
    }
}
