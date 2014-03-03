package org.ihtsdo.otf.security;

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

	String fn = "./TextFiles/Example.xml";

	public static void main(String[] args) {
		XMLTest xxmlt = new XMLTest();
		xxmlt.init();

	}

	private void init() {

		XmlUserSecurity xmlUs = new XmlUserSecurity(fn);
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
