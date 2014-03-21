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
	public final UserSecurityHandler getUsh() {
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

	public static void setXmlUs(final XmlUserSecurity xmlUsIn) {
		xmlUs = xmlUsIn;
	}

	@Override
	@After
	public void dispose() {

	}

	@Override
	public final int getNumDirs() {
		return 7;
	}

	@Override
	public final int getNumApps() {
		return 6;
	}

	@Override
	public final int getNumSettings() {
		return 3;
	}

	@Override
	public final int getNumMembers() {
		return 7;
	}

	@Override
	public final int getNumUsers() {
		return 8;
	}

	@Override
	public final int getNumAccountMembers() {
		return 2;
	}

	@Override
	public final int getNumAccountApps() {
		return 2;
	}

	@Override
	public final int getNumUserAppPerms() {
		return 2;
	}

	@Override
	public final int getNumUserAppPermsMember() {
		return 1;
	}

	@Override
	public final int getNumAppPermGroups() {
		return 3;
	}

	@Override
	public final int getNumAppPermGroupsGroup() {
		return 1;
	}

	@Override
	public final int getNumAppUsers() {
		return 4;
	}
}
