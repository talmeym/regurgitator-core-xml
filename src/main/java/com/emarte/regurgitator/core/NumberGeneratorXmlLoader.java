package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.MAX;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadOptionalInt;

public class NumberGeneratorXmlLoader implements XmlLoader<ValueGenerator> {
	private static final Log log = Log.getLog(NumberGeneratorXmlLoader.class);

	@Override
	public ValueGenerator load(Element element, Set<Object> allIds) throws RegurgitatorException {
		log.debug("Loaded number generator");
		return new NumberGenerator(loadOptionalInt(element, MAX));
	}
}
