package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class CreateResponseXmlLoader extends CreateResponseLoader implements XmlLoader<Step> {
    private static final Log log = getLog(CreateResponseXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
        String source = getAttribute(element, SOURCE);
        String value = getAttribute(element, VALUE);
        String file = getAttribute(element, FILE);
        ValueProcessor processor = loadOptionalValueProcessor(element, 0, allIds);
        return buildCreateResponse(id, source, value, file, processor, log);
    }
}
