/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static uk.emarte.regurgitator.core.CoreConfigConstants.VALUE;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class AtIndexProcessorXmlLoader extends AtIndexProcessorLoader implements XmlLoader<AtIndexProcessor> {
    private static final Log log = getLog(AtIndexProcessorXmlLoader.class);

    @Override
    public AtIndexProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        return buildAtIndexProcessor(loadOptionalStr(element, SOURCE), loadOptionalStr(element, VALUE), log);
    }
}
