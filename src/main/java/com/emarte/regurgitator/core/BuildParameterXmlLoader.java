package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

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
		String builderAttr = getAttribute(element, BUILDER);
		ValueBuilder valueBuilder;
		int processorIndex = 0;

		if(builderAttr != null) {
			valueBuilder = valueBuilder(builderAttr);
		} else {
			if(getChildElements(element).size() == 0) {
				throw new RegurgitatorException("no builder defined");
			}

			Element builderElement = getFirstChild(element);
			valueBuilder = builderLoaderUtil.deriveLoader(builderElement).load(builderElement, allIds);
			processorIndex++;
		}

		ValueProcessor processor = loadOptionalValueProcessor(element, processorIndex, allIds);

		String id = loadId(element, allIds);
		log.debug("Loaded built parameter extractor '" + id + '\'');
        return new BuildParameter(id, loadPrototype(element), loadContext(element), valueBuilder, processor);
    }
}
