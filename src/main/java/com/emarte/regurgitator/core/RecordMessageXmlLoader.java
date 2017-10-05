package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.FOLDER;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.getAttribute;
import static com.emarte.regurgitator.core.XmlConfigUtil.loadId;

public class RecordMessageXmlLoader implements XmlLoader<RecordMessage> {
    private static final Log log = getLog(RecordMessageXmlLoader.class);

    @Override
    public RecordMessage load(Element element, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(element, allIds);
        String folderPath = getAttribute(element, FOLDER);
        log.debug("Loaded record message '{}'", id);
        return new RecordMessage(id, folderPath);
    }
}
