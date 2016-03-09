package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static java.lang.Integer.parseInt;

public class ExtractProcessorXmlLoader implements XmlLoader<ExtractProcessor> {
	private static final Log log = getLog(ExtractProcessorXmlLoader.class);

	@Override
	public ExtractProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		log.debug("Loaded extract processor");
		return new ExtractProcessor(element.attributeValue(FORMAT), parseInt(element.attributeValue(INDEX)));
	}
}
