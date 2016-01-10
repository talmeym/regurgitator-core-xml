package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.*;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class IsolateXmlLoader implements XmlLoader<Isolate> {
	private static final Log log = Log.getLog(IsolateXmlLoader.class);
	private static final XmlLoaderUtil<XmlLoader<Step>> loaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();

	@Override
	public Isolate load(Element element, Set<Object> allIds) throws RegurgitatorException {
		List<Step> steps = new ArrayList<Step>();

		for(Iterator<Element> iterator = element.elementIterator(); iterator.hasNext(); ) {
			Element stepElement = iterator.next();
			steps.add(loaderUtil.deriveLoader(stepElement).load(stepElement, allIds));
		}

		boolean includeSession = loadOptionalBoolean(element.attributeValue(INCLUDE_SESSION));
		boolean includeParameters = loadOptionalBoolean(element.attributeValue(INCLUDE_PARAMETERS));

		String id = loadId(element, allIds);
		log.debug("Loaded isolate '" + id + "'");
		return new Isolate(id, steps, includeSession, includeParameters);
	}
}
