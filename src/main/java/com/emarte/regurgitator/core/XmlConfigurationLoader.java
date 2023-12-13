/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashSet;

import static com.emarte.regurgitator.core.FileUtil.getInputStreamForFile;

class XmlConfigurationLoader implements ConfigurationLoader {
    private static final XmlLoaderUtil<XmlLoader<Step>> loaderUtil = new XmlLoaderUtil<>();

    public Step load(InputStream input) throws RegurgitatorException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setValidating(true);
            dbFactory.setNamespaceAware(true);
            dbFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            dBuilder.setEntityResolver((publicId, systemId) -> {
                String resolvePath = "classpath:/" + systemId.substring(systemId.lastIndexOf("/") + 1);
                return new InputSource(getInputStreamForFile(resolvePath));
            });

            dBuilder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) {

                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw new SAXException("Error: ", exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw new SAXException("Error: ", exception);
                }
            });

            Document doc = dBuilder.parse(input);
            Element rootElement = doc.getDocumentElement();
            rootElement.normalize();
            return loaderUtil.deriveLoader(rootElement).load(rootElement, new HashSet<>());
        } catch (Exception e) {
            throw new RegurgitatorException("Error loading regurgitator configuration", e);
        }
    }
}
