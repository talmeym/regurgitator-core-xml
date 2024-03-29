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

public class RemoveAtIndexProcessorXmlLoader extends RemoveAtIndexProcessorBuilder implements XmlLoader<RemoveAtIndexProcessor> {
    private static final Log log = getLog(RemoveAtIndexProcessorXmlLoader.class);

    @Override
    public RemoveAtIndexProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        return buildRemoveAtIndexProcessor(loadOptionalStr(element, SOURCE), loadOptionalStr(element, VALUE), log);
    }
}
