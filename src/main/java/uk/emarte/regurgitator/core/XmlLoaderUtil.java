/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.core;

import org.w3c.dom.Element;

import static uk.emarte.regurgitator.core.StringUtil.*;

public class XmlLoaderUtil<TYPE extends Loader<?, ?>> extends LoaderUtil<Element, TYPE> {

    @Override
    public TYPE deriveLoader(Element element) throws RegurgitatorException {
        return buildFromClass(deriveClass(element));
    }

    @Override
    String deriveClass(Element element) throws RegurgitatorException {
        if (element.getNamespaceURI() != null) {
            String packageUri = stripOptionalBeginning(element.getNamespaceURI(), "http://");
            return deriveClass(reverseAndJoinWithDots(getMandatoryParts(packageUri, '.')), dashesToCamelCase(element.getLocalName()));
        }

        throw new RegurgitatorException("Invalid namespace: namespace missing");
    }

    @Override
    String deriveClass(String packageName, String className) throws RegurgitatorException {
        if (packageName == null) {
            throw new RegurgitatorException("Package not found for class: " + className);
        }

        return packageName + "." + className + "XmlLoader";
    }
}
