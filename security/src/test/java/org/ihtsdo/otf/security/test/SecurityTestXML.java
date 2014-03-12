package org.ihtsdo.otf.security.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.query.SecurityClient;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.dto.query.queries.AppPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.GroupPermDTO;
import org.ihtsdo.otf.security.xml.XmlUserSecurity;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class SecurityTestXML {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(SecurityTestXML.class
			.getName());

	private static String fn = "./TextFiles/Example.xml";
	private static XmlUserSecurity xmlUs;

	private static SecurityService secS;

	private final String testUser = "Bob";
	private final String testpw = "Sn0m3dDefPass";
	private final String testApp = "Mapping";
	private final String testMember = "INTL";
	private final String testGroup = "Reviewer";

	@BeforeClass
	public static void createTestInstance() {
		xmlUs = new XmlUserSecurity(fn);
		try {
			xmlUs.initFromFile();
			secS = new SecurityService(xmlUs);
			// LOG.info("defpw = " + xmlUs.getUserSecurity().getDefaultpw());
			// LOG.info("num top dirs = "
			// + xmlUs.getUserSecurity().getDirs().getDirectories().size());
			// LOG.info("num apps = "
			// + xmlUs.getUserSecurity().getApps().getApplications()
			// .size());
			//
			LOG.info(xmlUs.getXMLFromUserSecurityAsStringSortByName());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public final void testNumDirs() {
		assertEquals(7, xmlUs.getUserSecurity().getDirs().getDirectories()
				.size());
	}

	@Test
	public final void testNumApps() {

		// test service and client
		String json = secS.getApps();
		// LOG.info("JSON = " + json);

		List<String> member2 = SecurityClient.getApps(json);
		int jsonI = member2.size();
		int origI = xmlUs.getUserSecurity().getApps().getApplications().size();
		assertEquals(6, origI);
		assertEquals(origI, jsonI);
	}

	@Test
	public final void testNumSettings() {
		assertEquals(3, xmlUs.getUserSecurity().getSettings().size());
	}

	@Test
	public final void testSettingsVals() {
		assertEquals(testpw, xmlUs.getUserSecurity().getDefaultpw());
		assertEquals("OTF Users", xmlUs.getUserSecurity().getUsersApp());
		assertEquals("Members", xmlUs.getUserSecurity().getMembersApp());

	}

	@Test
	public final void testNumMembers() {
		// test service and client
		String json = secS.getMembers();
		// LOG.info("JSON = " + json);

		List<String> member2 = SecurityClient.getMembers(json);
		int jsonI = member2.size();
		int origI = xmlUs.getUserSecurity().getMembers().size();

		// MembersListQueryDTO mlq2 = new MembersListQueryDTO(json);
		assertEquals(7, origI);
		assertEquals(origI, jsonI);

	}

	@Test
	public final void testNumUsers() {
		// test service and client
		String json = secS.getUsers();
		// LOG.info("users JSON = " + json);

		List<OftAccountMin> user2 = SecurityClient.getUsers(json);

		int jsonI = user2.size();
		int origI = xmlUs.getUserSecurity().getUsers().size();

		assertEquals(8, origI);
		assertEquals(origI, jsonI);

	}

	@Test
	public final void testAccount() {
		OtfAccount testAcc = xmlUs.getUserSecurity().getUserAccountByName(
				testUser);
		assertNotNull(testAcc);
		if (testAcc != null) {
			// LOG.info("User name = " + testAcc.getName());
			OtfCustomData accCd = testAcc.getCustData();
			// check num custfields
			assertEquals(5, accCd.getCustFields().size());
			// Check is member of INTL
			assertTrue(accCd.isaMemberOf("INTL"));
			// Check Has mapping app setting.
			assertTrue(accCd.getAppsByAppName("Mapping").size() > 0);
		}
	}

	@Test
	public final void testAccountAuth() {
		OtfAccount testAcc = xmlUs.authAccount(testUser, testpw);
		String json = secS.getUserByName(testUser, testpw);
		// LOG.info("test acc JSON = " + json);

		String name = "";
		OftAccountMin user = SecurityClient.getUserByName(json);
		if (user != null) {
			name = user.getName();
		}
		assertEquals(testUser, name);
		assertNotNull(testAcc);
	}

	// @Test
	// public final void testAccountFull() {
	// String json = secS.getUserByNameFull(testUser);
	// // LOG.info("test acc JSON = " + json);
	//
	// String name = "";
	// OftAccountMin user = SecurityClient.getUserByNameFull(json);
	// if (user != null) {
	// name = user.getName();
	// }
	// assertEquals(testUser, name);
	//
	// }

	@Test
	public final void testAccountMembers() {
		List<String> members = new ArrayList<String>();
		OtfAccount oacc = xmlUs.getUserSecurity()
				.getUserAccountByName(testUser);
		if (oacc != null) {
			List<OtfCustomField> mems = oacc.getCustData().getMembers();
			for (OtfCustomField cf : mems) {
				members.add(cf.getVals()[1]);
			}
		}

		String json = secS.getUserMemberships(testUser);

		List<String> member2 = SecurityClient.getUserMemberships(json);
		int jsonI = member2.size();
		int origI = members.size();

		assertEquals(2, origI);
		assertEquals(origI, jsonI);

	}

	@Test
	public final void testAccountApps() {
		List<String> members = new ArrayList<String>();
		OtfAccount oacc = xmlUs.getUserSecurity()
				.getUserAccountByName(testUser);
		if (oacc != null) {
			List<OtfCustomField> mems = oacc.getCustData().getApps();
			for (OtfCustomField cf : mems) {
				members.add(cf.getVals()[1]);
			}
		}

		String json = secS.getUserApps(testUser);

		List<String> member2 = SecurityClient.getUserApps(json);
		int jsonI = member2.size();
		int origI = members.size();

		assertEquals(3, origI);
		assertEquals(origI, jsonI);

	}

	@Test
	public final void testAccountPerms() {

		String json = secS.getUserAppPerms(testUser, testApp);

		List<AppPermDTO> perms = SecurityClient.getUserAppPerms(json);
		int jsonI = perms.size();
		assertEquals(2, jsonI);

		String json2 = secS.getUserAppPerms(testUser, testApp, testMember);
		List<AppPermDTO> perms2 = SecurityClient.getUserAppPerms(json2);
		int jsonI2 = perms2.size();
		assertEquals(1, jsonI2);
	}

	@Test
	public final void testAppPerms() {

		String json = secS.getAppPermGroups(testApp);

		List<GroupPermDTO> perms = SecurityClient.getAppPermGroups(json);
		int jsonI = perms.size();
		assertEquals(3, jsonI);

		String json2 = secS.getAppPermGroups(testApp, testGroup);
		List<GroupPermDTO> perms2 = SecurityClient.getAppPermGroups(json2);
		int jsonI2 = perms2.size();
		assertEquals(1, jsonI2);
	}

	@After
	public void dispose() {

	}

}
