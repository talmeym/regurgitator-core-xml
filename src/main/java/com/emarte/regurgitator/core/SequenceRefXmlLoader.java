package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.XmlConfigUtil.getAttribute;

public class SequenceRefXmlLoader implements XmlLoader<Step> {
	private static final Log log = getLog(SequenceRefXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		log.debug("Loading sequence ref");
		Sequence sequence = (Sequence) ConfigurationFile.loadFile(getAttribute(element, FILE));
		String newId = getAttribute(element, ID);

		if(newId != null) {
			log.debug("Repackaged sequence '" + sequence.getId() + "' as '" + newId + "'");
			return new Sequence(newId, sequence);
		}

		log.debug("Using sequence '" + sequence.getId() + "' as is");
		return sequence;
    }
}
