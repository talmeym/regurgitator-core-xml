package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.EntityLookup.valueBuilder;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class BuildParameterXmlLoader implements XmlLoader<Step> {
    private static final Log log = Log.getLog(BuildParameterXmlLoader.class);

    private static final XmlLoaderUtil<XmlLoader<ValueBuilder>> builderLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueBuilder>>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String builderAttr = element.attributeValue(BUILDER);
		ValueBuilder valueBuilder;

		if(builderAttr != null) {
			valueBuilder = valueBuilder(builderAttr);
		} else {
			Element builderElement = element.element(BUILDER);

			if(builderElement == null) {
				throw new RegurgitatorException("no builder defined");
			}

			Element childElement = getChild(builderElement);
			valueBuilder = builderLoaderUtil.deriveLoader(childElement).load(childElement, allIds);
		}

		ValueProcessor processor = loadOptionalValueProcessor(element, allIds);

		String id = XmlConfigUtil.loadId(element, allIds);
		log.debug("Loaded built parameter extractor '" + id + '\'');
        return new BuildParameter(id, loadPrototype(element), loadContext(element), valueBuilder, processor);
    }

	private Element getChild(Element element) {
        return (Element) element.elements().get(0);
    }
}
