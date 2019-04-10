/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.BUILDER;
import static com.emarte.regurgitator.core.EntityLookup.valueBuilder;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class BuildParameterXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(BuildParameterXmlLoader.class);

    private static final XmlLoaderUtil<XmlLoader<ValueBuilder>> builderLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueBuilder>>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String builderAttr = loadOptionalStr(element, BUILDER);
        ValueBuilder valueBuilder;
        int processorIndex = 0;

        if(builderAttr != null) {
            valueBuilder = valueBuilder(builderAttr);
        } else {
            if(getChildElements(element).size() == 0) {
                throw new RegurgitatorException("No builder defined");
            }

            Element builderElement = getFirstChild(element);
            valueBuilder = builderLoaderUtil.deriveLoader(builderElement).load(builderElement, allIds);
            processorIndex++;
        }

        List<ValueProcessor> processors = loadOptionalValueProcessors(element, processorIndex, allIds);

        String id = loadId(element, allIds);
        log.debug("Loaded built parameter '{}'", id);
        return new BuildParameter(id, loadPrototype(element), loadContext(element), valueBuilder, processors);
    }
}
