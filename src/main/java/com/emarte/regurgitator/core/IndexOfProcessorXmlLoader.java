package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadStringFromElementOrAttribute;
import static java.lang.Boolean.parseBoolean;

public class IndexOfProcessorXmlLoader implements XmlLoader<ValueProcessor> {
	private static final Log log = Log.getLog(IndexOfProcessorXmlLoader.class);

	@Override
	public ValueProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String source = loadStringFromElementOrAttribute(element, SOURCE, false);
		String value = loadStringFromElementOrAttribute(element, VALUE, false);
		String backwards = loadStringFromElementOrAttribute(element, LAST, false);
		log.debug("Loaded index of processor");
		return new IndexOfProcessor(source != null ? new ContextLocation(source) : null, value, parseBoolean(backwards));
	}
}
