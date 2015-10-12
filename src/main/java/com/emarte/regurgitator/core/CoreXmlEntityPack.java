package com.emarte.regurgitator.core;

public class CoreXmlEntityPack extends AbstractEntityPack {
	public CoreXmlEntityPack() {
		addConfigurationLoader("xml", new XmlConfigurationLoader());
	}
}
