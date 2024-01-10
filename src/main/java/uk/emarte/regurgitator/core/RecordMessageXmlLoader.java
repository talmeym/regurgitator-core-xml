/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.FOLDER;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadId;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class RecordMessageXmlLoader implements XmlLoader<RecordMessage> {
    private static final Log log = getLog(RecordMessageXmlLoader.class);

    @Override
    public RecordMessage load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
        String folderPath = loadOptionalStr(element, FOLDER);
        log.debug("Loaded record message '{}'", id);
        return new RecordMessage(id, folderPath);
    }
}
