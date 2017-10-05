package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.getAttribute;

public class SubstituteProcessorXmlLoader implements XmlLoader<SubstituteProcessor> {
    private static final Log log = getLog(SubstituteProcessorXmlLoader.class);

    @Override
    public SubstituteProcessor load(Element element, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded substitute value");
        return new SubstituteProcessor(getAttribute(element, TOKEN), getAttribute(element, REPLACEMENT));
    }
}
