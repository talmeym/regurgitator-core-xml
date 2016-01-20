package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.*;

import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class SequenceXmlLoader implements XmlLoader<Step> {
    private static final Log log = Log.getLog(SequenceXmlLoader.class);
    private static final XmlLoaderUtil<XmlLoader<Step>> loaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        List<Step> steps = new ArrayList<Step>();

        for(Iterator<Element> iterator = element.elementIterator(); iterator.hasNext(); ) {
            Element stepElement = iterator.next();
            steps.add(loaderUtil.deriveLoader(stepElement).load(stepElement, allIds));
        }

        String id = loadId(element, allIds);
        log.debug("Loaded sequence '" + id + "'");
        return new Sequence(id, steps, loadIsolate(element));
    }

	private Isolate loadIsolate(Element element) {
		String isolateStr = element.attributeValue(CoreConfigConstants.ISOLATE);
		return isolateStr != null ? new Isolate(isolateStr.contains("session"), isolateStr.contains("param")) : null;
	}
}
