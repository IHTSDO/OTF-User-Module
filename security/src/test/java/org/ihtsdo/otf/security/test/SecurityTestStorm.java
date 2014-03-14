package org.ihtsdo.otf.security.test;

import java.util.Properties;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.stormpath.StormPathBaseDTO;
import org.ihtsdo.otf.security.stormpath.StormPathUserSecurity;
import org.ihtsdo.otf.security.xml.XmlUserSecurity;
import org.junit.After;
import org.junit.BeforeClass;

public class SecurityTestStorm extends AbstractSecurityTest {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(SecurityTestStorm.class
			.getName());

	private static String fn = "./TextFiles/Example.xml";
	private static String apiKeyFile = "C:/Users/adamf/stormpath/apiKey.properties";
	private static String apiKeyId = "2ZCPRU919FXQYF49HRUXK8905";
	private static String apiKeySecret = "ORDQyayhl1wmFputDIrEh8xIKi4amFGGcKsF9zn94L0";

	private static XmlUserSecurity xmlUs;
	private static StormPathUserSecurity spu;

	@BeforeClass
	public static void createTestInstance() {
		SecurityTestStorm sts = new SecurityTestStorm();
		try {
			getSpu();
			sts.getSecS();
			// clear existing records in SP
			getSpu().clearSP();
			// Then build using the model generated from XML
			refreshSpu();
			getSpu().sendUserSecuritytoStormPath(getXmlUs().getUserSecurity());
			// the build from Stormpath and test.
			refreshSpu();
			getSpu().buildUserSecurity();
			// refreshSpu();
			getXmlUs().setUserSecurity(getSpu().getUserSecurity());
			// LOG.info("storm2Xml : \n"
			// + xmlUs.getXMLFromUserSecurityAsStringSortByName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void refreshSpu() {
		getSpu().getSpbd().load();
	}

	@Override
	@After
	public void dispose() {

	}

	@Override
	public UserSecurityHandler getUsh() {
		return getSpu();
	}

	public static StormPathUserSecurity getSpu() {
		if (spu == null) {
			Properties spuP = new Properties();
			spuP.setProperty(StormPathBaseDTO.KEY_PATH, apiKeyFile);
			spuP.setProperty(StormPathBaseDTO.API_KEY_ID, apiKeyId);
			spuP.setProperty(StormPathBaseDTO.API_KEY_SECRET, apiKeySecret);
			spu = new StormPathUserSecurity(spuP);
		}
		return spu;
	}

	public static void setSpu(StormPathUserSecurity spuIn) {
		spu = spuIn;
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
	public int getNumDirs() {
		return 8;
	}

	@Override
	public int getNumApps() {
		return 7;
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
