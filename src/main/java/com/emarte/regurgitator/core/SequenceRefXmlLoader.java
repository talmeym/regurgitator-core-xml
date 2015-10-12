package com.emarte.regurgitator.core;

import org.dom4j.Element;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;

public class SequenceRefXmlLoader implements XmlLoader<Step> {
	private static final Log log = Log.getLog(SequenceRefXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
		log.debug("Loading sequence ref");
		Sequence sequence = (Sequence) ConfigurationFile.loadFile(element.attributeValue(FILE));
		String newId = element.attributeValue(ID);

		if(newId != null) {
			log.debug("Repackaged sequence '" + sequence.getId() + "' as '" + newId + "'");
			return new Sequence(newId, sequence.getAll());
		}

		log.debug("Using sequence '" + sequence.getId() + "' as is");
		return sequence;
    }
}
