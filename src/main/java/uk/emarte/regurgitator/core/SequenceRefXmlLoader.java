/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import java.util.Set;

import static uk.emarte.regurgitator.core.CoreConfigConstants.FILE;
import static uk.emarte.regurgitator.core.CoreConfigConstants.ID;
import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.XmlConfigUtil.loadOptionalStr;

public class SequenceRefXmlLoader implements XmlLoader<Step> {
    private static final Log log = getLog(SequenceRefXmlLoader.class);

    @Override
    public Step load(Element element, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loading sequence ref");
        Sequence sequence = (Sequence) ConfigurationFile.loadFile(loadOptionalStr(element, FILE));
        String newId = loadOptionalStr(element, ID);

        if(newId != null) {
            log.debug("Repackaged sequence '{}' as '{}'", sequence.getId(), newId);
            return new Sequence(newId, sequence);
        }

        log.debug("Using sequence '{}' as is", sequence.getId());
        return sequence;
    }
}
