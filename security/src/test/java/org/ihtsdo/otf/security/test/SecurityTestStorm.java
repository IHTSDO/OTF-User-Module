package org.ihtsdo.otf.security.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.stormpath.StormPathUserSecurity;
import org.ihtsdo.otf.security.xml.XmlUserSecurity;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class SecurityTestStorm {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(SecurityTestStorm.class
			.getName());

	private static String fn = "./TextFiles/Example.xml";
	private static XmlUserSecurity xmlUs;
	private static StormPathUserSecurity spu = new StormPathUserSecurity();

	@BeforeClass
	public static void createTestInstance() {
		xmlUs = new XmlUserSecurity(fn);
		try {
			xmlUs.initFromFile();
			// clear existing records in SP
			spu.clearSP();
			// Then build using the model generated from XML
			refreshSpu();
			spu.sendUserSecuritytoStormPath(xmlUs.getUserSecurity());
			// the build from Stormpath and test.
			refreshSpu();
			spu.buildUserSecurity();
			// refreshSpu();
			xmlUs.setUserSecurity(spu.getUserSecurity());
			// LOG.info("storm2Xml : \n"
			// + xmlUs.getXMLFromUserSecurityAsStringSortByName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void refreshSpu() {
		spu.getSpbd().load();
	}

	@Test
	public final void testNumDirs() {
		assertEquals(8, spu.getUserSecurity().getDirs().getDirectories().size());
	}

	@Test
	public final void testNumApps() {
		assertEquals(7, spu.getUserSecurity().getApps().getApplications()
				.size());
	}

	@Test
	public final void testNumSettings() {
		assertEquals(3, spu.getUserSecurity().getSettings().size());
	}

	@Test
	public final void testSettingsVals() {

		assertEquals("Sn0m3dDefPass", spu.getUserSecurity().getDefaultpw());
		assertEquals("OTF Users", spu.getUserSecurity().getUsersApp());
		assertEquals("Members", spu.getUserSecurity().getMembersApp());

	}

	@Test
	public final void testNumMembers() {
		assertEquals(7, spu.getUserSecurity().getMembers().size());
	}

	@Test
	public final void testNumUsers() {
		assertEquals(8, spu.getUserSecurity().getUsers().size());
	}

	@Test
	public final void testAccount() {
		OtfAccount testAcc = spu.getUserSecurity().getUserAccountByName("Bob");
		assertNotNull(testAcc);
		if (testAcc != null) {
			// LOG.info("User name = " + testAcc.getName());
			OtfCustomData accCd = testAcc.getCustData();
			// check num custfields
			assertEquals(3, accCd.getCustFields().size());
			// Check is member of INTL
			assertTrue(accCd.isaMemberOf("INTL"));
			// Check Has mapping app setting.
			assertTrue(accCd.getAppsByAppName("Mapping").size() > 0);
		}
	}

	@Test
	public final void testAccountAuth() {
		OtfAccount testAcc = spu.authAccount("Bob", "Sn0m3dDefPass");
		assertNotNull(testAcc);

	}

	// <account name="Bob" email="bob@test.com" givenName="Bob"
	// surname="Bobbin">
	// <customField key="" value="APP:Mapping:Specialist:INTL" />
	// <customField value="MEMBER:INTL" />
	// <customField value="MEMBER:UK" />
	// </account>

	@After
	public void dispose() {

	}

}
