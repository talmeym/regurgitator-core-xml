/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.REPLACEMENT;
import static uk.emarte.regurgitator.core.CoreConfigConstants.TOKEN;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class SubstituteProcessorXmlLoader implements XmlLoader<SubstituteProcessor> {
    private static final Log log = getLog(SubstituteProcessorXmlLoader.class);

    @Override
    public SubstituteProcessor load(Element element, Set<Object> allIds) {
        log.debug("Loaded substitute value");
        return new SubstituteProcessor(loadOptionalStr(element, TOKEN), loadOptionalStr(element, REPLACEMENT));
    }
}
