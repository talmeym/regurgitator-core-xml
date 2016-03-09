package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.AS_INDEX;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadOptionalBoolean;

public class SizeProcessorXmlLoader implements XmlLoader<SizeProcessor> {
	private static final Log log = getLog(SizeProcessorXmlLoader.class);

	@Override
	public SizeProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
		boolean lastIndex = loadOptionalBoolean(element, AS_INDEX);

		log.debug("Loaded size processor");
		return new SizeProcessor(lastIndex);
	}
}
