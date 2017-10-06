/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

public class CoreXmlEntityPack extends AbstractEntityPack {
    public CoreXmlEntityPack() {
        addConfigurationLoader("xml", new XmlConfigurationLoader());
    }
}
