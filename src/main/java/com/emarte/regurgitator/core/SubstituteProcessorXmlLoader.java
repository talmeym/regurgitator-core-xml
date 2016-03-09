package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;

public class SubstituteProcessorXmlLoader implements XmlLoader<SubstituteProcessor> {
	private static final Log log = getLog(SubstituteProcessorXmlLoader.class);

	@Override
	public SubstituteProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		log.debug("Loaded substitute value");
		return new SubstituteProcessor(element.attributeValue(TOKEN), element.attributeValue(REPLACEMENT));
	}
}
