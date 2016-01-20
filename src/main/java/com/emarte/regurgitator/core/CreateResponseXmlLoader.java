package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class CreateResponseXmlLoader extends CreateResponseLoader implements XmlLoader<Step> {
    private static final Log log = Log.getLog(CreateResponseXmlLoader.class);

	@Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
		String source = element.attributeValue(SOURCE);
        String value = element.attributeValue(VALUE);
        String file = element.attributeValue(FILE);
		ValueProcessor processor = loadOptionalValueProcessor(element, 0, allIds);
		return buildCreateResponse(id, source, value, file, processor, log);
    }
}
