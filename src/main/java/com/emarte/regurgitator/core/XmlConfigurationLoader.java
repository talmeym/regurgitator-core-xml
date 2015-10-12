package com.emarte.regurgitator.core;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.xml.sax.*;

import java.io.*;
import java.util.HashSet;

public class XmlConfigurationLoader implements ConfigurationLoader {
	private static XmlLoaderUtil<SequenceXmlLoader> loaderUtil = new XmlLoaderUtil<SequenceXmlLoader>();

	public Step load(InputStream input) throws RegurgitatorException {
		try {
			SAXReader reader = new SAXReader();
			reader.setValidation(true);
			reader.setFeature("http://apache.org/xml/features/validation/schema", true);
			reader.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws IOException {
					String resolvePath = "classpath:/" + systemId.substring(systemId.lastIndexOf("/") + 1);
					FileUtil.checkResource(resolvePath);
					return new InputSource(FileUtil.getInputStreamForFile(resolvePath));
				}
			});

			Document doc = reader.read(input);
			Element rootElement = doc.getRootElement();
			HashSet<Object> allIds = new HashSet<Object>();
			return loaderUtil.deriveLoader(rootElement).load(rootElement, allIds);
		} catch (Exception e) {
			throw new RegurgitatorException("Error loading regurgitator configuration", e);
		}
	}
}
