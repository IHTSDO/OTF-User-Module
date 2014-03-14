package org.ihtsdo.otf.security.test;

import java.util.Properties;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.xml.XmlUserSecurity;
import org.junit.After;
import org.junit.BeforeClass;

public class SecurityTestXML extends AbstractSecurityTest {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(SecurityTestXML.class
			.getName());

	private static String fn = "./TextFiles/Example.xml";
	private static XmlUserSecurity xmlUs;

	@BeforeClass
	public static void createTestInstance() {
		SecurityTestXML stx = new SecurityTestXML();
		try {
			stx.getSecS();
			LOG.info(xmlUs.getXMLFromUserSecurityAsStringSortByName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public UserSecurityHandler getUsh() {
		return getXmlUs();
	}

	public static XmlUserSecurity getXmlUs() {

		if (xmlUs == null) {
			Properties xmlP = new Properties();
			xmlP.setProperty(XmlUserSecurity.CONF_PROPS_FN, fn);
			xmlUs = new XmlUserSecurity(xmlP);
		}
		return xmlUs;
	}

	public static void setXmlUs(XmlUserSecurity xmlUsIn) {
		xmlUs = xmlUsIn;
	}

	@Override
	@After
	public void dispose() {

	}

	@Override
	public int getNumDirs() {
		return 7;
	}

	@Override
	public int getNumApps() {
		return 6;
	}

	@Override
	public int getNumSettings() {
		return 3;
	}

	@Override
	public int getNumMembers() {
		return 7;
	}

	@Override
	public int getNumUsers() {
		return 8;
	}

	@Override
	public int getNumAccountMembers() {
		return 2;
	}

	@Override
	public int getNumAccountApps() {
		return 2;
	}

	@Override
	public int getNumUserAppPerms() {
		return 2;
	}

	@Override
	public int getNumUserAppPermsMember() {
		return 1;
	}

	@Override
	public int getNumAppPermGroups() {
		return 3;
	}

	@Override
	public int getNumAppPermGroupsGroup() {
		return 1;
	}
}
