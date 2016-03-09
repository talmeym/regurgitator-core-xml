package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class IdentifySessionXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(IdentifySessionXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String id = loadId(element, allIds);
        log.debug("Loaded sesion identifier '" + id + "'");
        return new IdentifySession(id, new ContextLocation(element.attributeValue(SOURCE)));
    }
}
