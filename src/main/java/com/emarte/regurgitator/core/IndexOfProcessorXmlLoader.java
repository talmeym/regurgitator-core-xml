package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static java.lang.Boolean.parseBoolean;

public class IndexOfProcessorXmlLoader implements XmlLoader<ValueProcessor> {
	private static final Log log = Log.getLog(IndexOfProcessorXmlLoader.class);

	@Override
	public ValueProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String source = element.attributeValue(SOURCE);
		String value = element.attributeValue(VALUE);
		String backwards = element.attributeValue(LAST);

		log.debug("Loaded index of processor");
		return new IndexOfProcessor(source != null ? new ContextLocation(source) : null, value, parseBoolean(backwards));
	}
}
