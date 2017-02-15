package com.emarte.regurgitator.core;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.HashSet;

public class XmlConfigurationLoader implements ConfigurationLoader {
	private static XmlLoaderUtil<XmlLoader<Step>> loaderUtil = new XmlLoaderUtil<XmlLoader<Step>>();

	public Step load(InputStream input) throws RegurgitatorException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(true);
			dbFactory.setNamespaceAware(true);
			dbFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			dBuilder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws IOException {
					String resolvePath = "classpath:/" + systemId.substring(systemId.lastIndexOf("/") + 1);
					FileUtil.checkResource(resolvePath);
					return new InputSource(FileUtil.getInputStreamForFile(resolvePath));
				}
			});

			Document doc = dBuilder.parse(input);
			Element rootElement = doc.getDocumentElement();
			rootElement.normalize();
			HashSet<Object> allIds = new HashSet<Object>();
			return loaderUtil.deriveLoader(rootElement).load(rootElement, allIds);
		} catch (Exception e) {
			throw new RegurgitatorException("Error loading regurgitator configuration", e);
		}
	}
}
