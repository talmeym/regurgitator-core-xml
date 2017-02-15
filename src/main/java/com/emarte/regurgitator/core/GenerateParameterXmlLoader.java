package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.GENERATOR;
import static com.emarte.regurgitator.core.EntityLookup.valueGenerator;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class GenerateParameterXmlLoader implements XmlLoader<Step> {
	private static final Log log = getLog(GenerateParameterXmlLoader.class);

	private static final XmlLoaderUtil<XmlLoader<ValueGenerator>> generatorLoaderUtil = new XmlLoaderUtil<XmlLoader<ValueGenerator>>();

	@Override
	public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String generatorAttr = getAttribute(element, GENERATOR);
		ValueGenerator generator;
		int processorIndex = 0;

		if (generatorAttr != null) {
			generator = valueGenerator(generatorAttr);
		} else {
			if(getChildElements(element).size() == 0) {
				throw new RegurgitatorException("no generator defined");
			}

			Element generatorElement = getFirstChild(element);
			generator = generatorLoaderUtil.deriveLoader(generatorElement).load(generatorElement, allIds);
			processorIndex++;
		}

		ValueProcessor processor = loadOptionalValueProcessor(element, processorIndex, allIds);

		String id = loadId(element, allIds);
		log.debug("Loaded generate parameter extractor '" + id + "'");
		return new GenerateParameter(id, loadPrototype(element), loadContext(element), generator, processor);
	}
}
