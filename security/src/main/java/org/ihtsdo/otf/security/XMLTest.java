package org.ihtsdo.otf.security;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.xml.XmlUserSecurityHandler;

public class XMLTest {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(XmlUserSecurityHandler.class
			.getName());

	private final String fn = "./TextFiles/Example.xml";

	public static void main(final String[] args) {
		XMLTest xxmlt = new XMLTest();
		xxmlt.init();

	}

	private void init() {

		Properties xmlP = new Properties();
		xmlP.setProperty(XmlUserSecurityHandler.CONF_PROPS_FN, fn);
		XmlUserSecurityHandler xmlUs = new XmlUserSecurityHandler(xmlP);
		try {
			xmlUs.initFromFile();
			LOG.info(xmlUs.getXMLFromUserSecurityAsString());

		} catch (Exception e) {
			LOG.log(Level.SEVERE, "An exception has occured", e);
		}

	}

}
