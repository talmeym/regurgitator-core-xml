/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.GENERATOR;
import static com.emarte.regurgitator.core.EntityLookup.valueGenerator;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class GenerateParameterXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(GenerateParameterXmlLoader.class);

    private static final XmlLoaderUtil<XmlLoader<ValueGenerator>> generatorLoaderUtil = new XmlLoaderUtil<>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String generatorAttr = loadOptionalStr(element, GENERATOR);
        ValueGenerator generator;
        int processorIndex = 0;

        if (generatorAttr != null) {
            generator = valueGenerator(generatorAttr);
        } else {
            if(getChildElements(element).size() == 0) {
                throw new RegurgitatorException("No generator defined");
            }

            Element generatorElement = getFirstChild(element);
            generator = generatorLoaderUtil.deriveLoader(generatorElement).load(generatorElement, allIds);
            processorIndex++;
        }

        List<ValueProcessor> processors = loadOptionalValueProcessors(element, processorIndex, allIds);

        String id = loadId(element, allIds);
        log.debug("Loaded generate parameter '{}'", id);
        return new GenerateParameter(id, loadPrototype(element), loadContext(element), generator, processors);
    }
}
