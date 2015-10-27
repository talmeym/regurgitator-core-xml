package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadStringFromElementOrAttribute;

public class IndexProcessorXmlLoader implements XmlLoader<ValueProcessor> {
	private static final Log log = Log.getLog(IndexProcessorXmlLoader.class);

	@Override
	public ValueProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String source = loadStringFromElementOrAttribute(element, SOURCE, false);
		String value = loadStringFromElementOrAttribute(element, VALUE, false);
		log.debug("Loaded index processor");
		return new IndexProcessor(source != null ? new ContextLocation(source) : null, value);
	}
}
