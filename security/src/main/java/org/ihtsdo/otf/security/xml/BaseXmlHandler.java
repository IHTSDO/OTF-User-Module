package org.ihtsdo.otf.security.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BaseXmlHandler {

	public static String E_NAME;
	public Document doc;

	public abstract void addContent(Element el);

	public Element getXML() {
		return createElement();
	}

	public static final String getE_NAME() {
		return E_NAME;
	}

	public Element createElement() {
		Element el = getDoc().createElement(getE_NAME());
		addContent(el);
		return el;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document docIn) {
		doc = docIn;
	}

}
