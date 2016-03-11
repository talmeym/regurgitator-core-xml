package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class IdentifySessionXmlLoader extends IdentifySessionLoader implements XmlLoader<Step> {
    private static final Log log = getLog(IdentifySessionXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String id = loadId(element, allIds);
		String source = element.attributeValue(SOURCE);
		String value = element.attributeValue(VALUE);
		return buildIdentifySession(id, source, value, log);
    }
}
