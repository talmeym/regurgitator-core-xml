/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.FORMAT;
import static uk.emarte.regurgitator.core.CoreConfigConstants.INDEX;
import static uk.emarte.regurgitator.core.Log.getLog;
import static java.lang.Integer.parseInt;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadMandatoryStr;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class ExtractProcessorXmlLoader implements XmlLoader<ExtractProcessor> {
    private static final Log log = getLog(ExtractProcessorXmlLoader.class);

    @Override
    public ExtractProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded extract processor");
        return new ExtractProcessor(loadOptionalStr(element, FORMAT), parseInt(loadMandatoryStr(element, INDEX)));
    }
}
