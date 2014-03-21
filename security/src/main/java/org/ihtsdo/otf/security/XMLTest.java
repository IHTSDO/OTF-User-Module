package org.ihtsdo.otf.security;

import java.util.Properties;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.xml.XmlUserSecurity;

public class XMLTest {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(XmlUserSecurity.class
			.getName());

	private final String fn = "./TextFiles/Example.xml";

	public static void main(final String[] args) {
		XMLTest xxmlt = new XMLTest();
		xxmlt.init();

	}

	private void init() {

		Properties xmlP = new Properties();
		xmlP.setProperty(XmlUserSecurity.CONF_PROPS_FN, fn);
		XmlUserSecurity xmlUs = new XmlUserSecurity(xmlP);
		try {
			xmlUs.initFromFile();
			// LOG.info("defpw = " + xmlUs.getUserSecurity().getDefaultpw());
			// LOG.info("num top dirs = "
			// + xmlUs.getUserSecurity().getDirs().getDirectories().size());
			// LOG.info("num apps = "
			// + xmlUs.getUserSecurity().getApps().getApplications()
			// .size());

			LOG.info(xmlUs.getXMLFromUserSecurityAsString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
