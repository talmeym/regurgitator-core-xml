/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.*;

public class CreateParameterXmlLoader extends CreateParameterLoader implements XmlLoader<Step> {
    private static final Log log = getLog(CreateParameterXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
        String source = loadOptionalStr(element, SOURCE);
        String value = loadOptionalStr(element, VALUE);
        String file = loadOptionalStr(element, FILE);
        List<ValueProcessor> processors = loadOptionalValueProcessors(element, 0, allIds);
        return buildCreateParameter(id, loadPrototype(element), loadContext(element), source, value, file, processors, log);
    }
}
