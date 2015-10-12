package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.GENERATOR;
import static com.emarte.regurgitator.core.EntityLookup.valueGenerator;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class GenerateParameterXmlLoader implements XmlLoader<Step> {
	private static final Log log = Log.getLog(GenerateParameterXmlLoader.class);

	private static XmlLoaderUtil<XmlLoader<ValueGenerator>> generatorLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueGenerator>>();

	@Override
	public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String generatorAttr = element.attributeValue(GENERATOR);
		ValueGenerator generator;

		if (generatorAttr != null) {
			generator = valueGenerator(generatorAttr);
		} else {
			Element generatorElement = element.element(GENERATOR);

			if(generatorElement == null) {
				throw new RegurgitatorException("no generator defined");
			}

			Element childElement = getChild(generatorElement);
			generator = generatorLoaderUtil.deriveLoader(childElement).load(childElement, allIds);
		}

		ValueProcessor processor = loadOptionalValueProcessor(element, allIds);

		String id = loadId(element, allIds);
		log.debug("Loaded generate parameter extractor '" + id + "'");
		return new GenerateParameter(id, loadPrototype(element), loadContext(element), generator, processor);
	}
}
