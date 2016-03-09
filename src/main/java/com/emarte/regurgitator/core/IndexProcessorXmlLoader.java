package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;

public class IndexProcessorXmlLoader implements XmlLoader<IndexProcessor> {
	private static final Log log = getLog(IndexProcessorXmlLoader.class);

	@Override
	public IndexProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String source = element.attributeValue(SOURCE);
		String value = element.attributeValue(VALUE);

		log.debug("Loaded index processor");
		return new IndexProcessor(new ValueSource(source != null ? new ContextLocation(source) : null, value));
	}
}
