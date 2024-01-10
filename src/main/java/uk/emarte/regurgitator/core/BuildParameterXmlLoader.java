/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.BUILDER;
import static uk.emarte.regurgitator.core.EntityLookup.valueBuilder;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.*;

public class BuildParameterXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(BuildParameterXmlLoader.class);

    private static final XmlLoaderUtil<XmlLoader<ValueBuilder>> builderLoaderUtil = new XmlLoaderUtil<>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String builderAttr = loadOptionalStr(element, BUILDER);
        ValueBuilder builder;
        int processorIndex = 0;

        if(builderAttr != null) {
            builder = valueBuilder(builderAttr);
        } else {
            if(getChildElements(element).size() == 0) {
                throw new RegurgitatorException("No builder defined");
            }

            Element builderElement = getFirstChild(element);
            builder = builderLoaderUtil.deriveLoader(builderElement).load(builderElement, allIds);
            processorIndex++;
        }

        List<ValueProcessor> processors = loadOptionalValueProcessors(element, processorIndex, allIds);

        String id = loadId(element, allIds);
        log.debug("Loaded built parameter '{}'", id);
        return new BuildParameter(id, loadPrototype(element), loadContext(element), builder, processors);
    }
}
