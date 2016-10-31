package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.FOLDER;
import static com.emarte.regurgitator.core.Log.getLog;

public class RecordMessageXmlLoader implements XmlLoader<RecordMessage> {
	private static final Log log = getLog(RecordMessageXmlLoader.class);

	@Override
	public RecordMessage load(Element element, Set<Object> allIds) throws RegurgitatorException {
		String folderPath = element.attributeValue(FOLDER);

		String id = XmlConfigUtil.loadId(element, allIds);
		log.debug("Loaded record message '" + id + "'");
		return new RecordMessage(id, folderPath);
	}
}
