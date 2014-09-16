package org.ihtsdo.otf.security.test;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.stormpath.StormPathBaseDTO;
import org.ihtsdo.otf.security.stormpath.StormPathUserSecurityHandler;
import org.ihtsdo.otf.security.xml.XmlUserSecurityHandler;
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

	private static XmlUserSecurityHandler xmlUs;
	private static StormPathUserSecurityHandler spu;

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
			getSpu().sendUserSecuritytoStormPath(
					getXmlUs().getUserSecurityModel().getModel());
			// the build from Stormpath and test.
			refreshSpu();
			getSpu().buildUserSecurity();
			getXmlUs().getUserSecurityModel().setModel(
					(getSpu().getUserSecurityModel().getModel()));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "An exception has occurred", e);
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
	public final UserSecurityHandler getUsh() {
		return getSpu();
	}

	public static StormPathUserSecurityHandler getSpu() {
		if (spu == null) {
			Properties spuP = new Properties();
			spuP.setProperty(StormPathBaseDTO.KEY_PATH, apiKeyFile);
			spuP.setProperty(StormPathBaseDTO.API_KEY_ID, apiKeyId);
			spuP.setProperty(StormPathBaseDTO.API_KEY_SECRET, apiKeySecret);
			spu = new StormPathUserSecurityHandler(spuP);
		}
		return spu;
	}

	public static void setSpu(final StormPathUserSecurityHandler spuIn) {
		spu = spuIn;
	}

	public static XmlUserSecurityHandler getXmlUs() {
		if (xmlUs == null) {
			Properties xmlP = new Properties();
			xmlP.setProperty(XmlUserSecurityHandler.CONF_PROPS_FN, fn);
			xmlUs = new XmlUserSecurityHandler(xmlP);
		}
		return xmlUs;
	}

	public static void setXmlUs(final XmlUserSecurityHandler xmlUsIn) {
		xmlUs = xmlUsIn;
	}

	@Override
	public final int getNumDirs() {
		return 8;
	}

	@Override
	public final int getNumApps() {
		return 7;
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
