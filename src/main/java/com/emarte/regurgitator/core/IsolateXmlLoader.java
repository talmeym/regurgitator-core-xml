package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.INCLUDE_SESSION;
import static com.emarte.regurgitator.core.XmlConfigUtil.getChild;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class IsolateXmlLoader implements XmlLoader<Isolate> {
	private static final Log log = Log.getLog(IsolateXmlLoader.class);
	private static final XmlLoaderUtil<XmlLoader<Step>> loaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();

	@Override
	public Isolate load(Element element, Set<Object> allIds) throws RegurgitatorException {
		Element child = getChild(element);
		Step step = loaderUtil.deriveLoader(child).load(child, allIds);
		String includeSessionStr = element.attributeValue(INCLUDE_SESSION);
		boolean includeSession = includeSessionStr != null ? Boolean.valueOf(includeSessionStr) : false;
		String id = loadId(element, allIds);
		log.debug("Loaded isolate '" + id + "'");
		return new Isolate(id, step, includeSession);
	}
}
