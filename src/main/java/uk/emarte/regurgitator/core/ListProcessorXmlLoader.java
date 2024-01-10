/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadMandatoryValueProcessors;

public class ListProcessorXmlLoader implements XmlLoader<ListProcessor> {
    private static final Log log = getLog(ListProcessorXmlLoader.class);

    @Override
    public ListProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        List<ValueProcessor> processors = loadMandatoryValueProcessors(element, 0, allIds);
        log.debug("Loaded list processor");
        return new ListProcessor(processors);
    }
}
