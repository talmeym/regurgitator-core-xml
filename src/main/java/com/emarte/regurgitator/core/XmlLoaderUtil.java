package com.emarte.regurgitator.core;

import org.dom4j.Element;

import static com.emarte.regurgitator.core.StringUtil.*;

public class XmlLoaderUtil<TYPE extends Loader> extends LoaderUtil<TYPE> {

	public TYPE deriveLoader(Element element) throws RegurgitatorException {
		return buildFromClass(deriveClass(element));
	}

	static String deriveClass(Element element) throws RegurgitatorException {
		if (element.getNamespace() != null) {
			String packageUri = stripOptionalBeginning(element.getNamespace().getURI(), "http://");
			return deriveClassName(reverseAndJoinWithDots(getMandatoryParts(packageUri, '.')), dashesToCamelCase(element.getName()));
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
