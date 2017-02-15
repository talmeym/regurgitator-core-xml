package com.emarte.regurgitator.core;

import org.w3c.dom.Element;

import static com.emarte.regurgitator.core.StringUtil.*;

public class XmlLoaderUtil<TYPE extends Loader> extends LoaderUtil<TYPE> {

	public TYPE deriveLoader(Element element) throws RegurgitatorException {
		return buildFromClass(deriveClass(element));
	}

	static String deriveClass(Element element) throws RegurgitatorException {
		if (element.getNamespaceURI() != null) {
			String packageUri = stripOptionalBeginning(element.getNamespaceURI(), "http://");
			return deriveClassName(reverseAndJoinWithDots(getMandatoryParts(packageUri, '.')), dashesToCamelCase(element.getLocalName()));
		}

		throw new RegurgitatorException("Invalid namespace: namespace missing");
	}

	static String deriveClassName(String packageName, String className) throws RegurgitatorException {
		if (packageName == null) {
			throw new RegurgitatorException("Package not found for class: " + className);
		}

		return packageName + "." + className + "XmlLoader";
	}
}
