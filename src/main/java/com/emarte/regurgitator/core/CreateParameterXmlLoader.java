package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class CreateParameterXmlLoader extends CreateParameterLoader implements XmlLoader<Step> {
	private static final Log log = Log.getLog(CreateParameterXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);

        String source = loadStringFromElementOrAttribute(element, SOURCE, false);
		String value = loadStringFromElementOrAttribute(element, VALUE, false);
        String file = loadStringFromElementOrAttribute(element, FILE, false);

		ValueProcessor processor = loadOptionalValueProcessor(element, allIds);

		return buildCreateParameter(id, loadPrototype(element), loadContext(element), source, value, file, processor, log);
    }
}
