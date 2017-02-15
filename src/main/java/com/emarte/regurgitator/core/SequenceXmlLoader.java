package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class SequenceXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(SequenceXmlLoader.class);
    private static final XmlLoaderUtil<XmlLoader<Step>> loaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        List<Step> steps = new ArrayList<Step>();

		List<Element> children = getChildElements(element);

		for(int i = 0; i < children.size(); i++) {
            Element stepElement = children.get(i);
            steps.add(loaderUtil.deriveLoader(stepElement).load(stepElement, allIds));
        }

        String id = loadId(element, allIds);
        log.debug("Loaded sequence '" + id + "'");
        return new Sequence(id, steps, loadIsolate(element));
    }

	private Isolate loadIsolate(Element element) {
		String isolateStr = getAttribute(element, CoreConfigConstants.ISOLATE);
		return isolateStr != null ? new Isolate(isolateStr.contains("session"), isolateStr.contains("param")) : null;
	}
}
