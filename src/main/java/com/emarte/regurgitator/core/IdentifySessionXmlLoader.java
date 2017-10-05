package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.getAttribute;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class IdentifySessionXmlLoader extends IdentifySessionLoader implements XmlLoader<Step> {
    private static final Log log = getLog(IdentifySessionXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
        String source = getAttribute(element, SOURCE);
        String value = getAttribute(element, VALUE);
        return buildIdentifySession(id, source, value, log);
    }
}
