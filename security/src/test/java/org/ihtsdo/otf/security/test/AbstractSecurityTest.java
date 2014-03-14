package org.ihtsdo.otf.security.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.query.SecurityClient;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.dto.query.queries.AppPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.GroupPermDTO;
import org.junit.After;
import org.junit.Test;

public abstract class AbstractSecurityTest {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(AbstractSecurityTest.class.getName());

	// protected static UserSecurityHandler ush;
	private static SecurityService secS;

	public final static String testUser = "Bob";
	public final static String testpw = "Sn0m3dDefPass";
	public final static String testApp = "Mapping";
	public final static String testMember = "INTL";
	public final static String testGroup = "Reviewer";

	public final static String testUsersApp = "OTF Users";
	public final static String testMembersApp = "Members";

	@After
	public abstract void dispose();

	public abstract UserSecurityHandler getUsh();

	// Tests
	public abstract int getNumDirs();

	@Test
	public final void testNumDirs() {
		assertEquals(getNumDirs(), getUsh().getUserSecurity().getDirs()
				.getDirectories().size());
	}

	public abstract int getNumApps();

	@Test
	public final void testNumApps() {

		// test service and client
		String json = secS.getApps();
		// LOG.info("JSON = " + json);

		List<String> member2 = SecurityClient.getApps(json);
		int jsonI = member2.size();
		int origI = getUsh().getUserSecurity().getApps().getApplications()
				.size();
		assertEquals(getNumApps(), origI);
		assertEquals(origI, jsonI);
	}

	public abstract int getNumSettings();

	@Test
	public final void testNumSettings() {
		assertEquals(getNumSettings(), getUsh().getUserSecurity().getSettings()
				.size());
	}

	@Test
	public final void testSettingsVals() {
		assertEquals(testpw, getUsh().getUserSecurity().getDefaultpw());
		assertEquals(testUsersApp, getUsh().getUserSecurity().getUsersApp());
		assertEquals(testMembersApp, getUsh().getUserSecurity().getMembersApp());

	}

	public abstract int getNumMembers();

	@Test
	public final void testNumMembers() {
		// test service and client
		String json = secS.getMembers();
		// LOG.info("JSON = " + json);

		List<String> member2 = SecurityClient.getMembers(json);
		int jsonI = member2.size();
		int origI = getUsh().getUserSecurity().getMembers().size();

		// MembersListQueryDTO mlq2 = new MembersListQueryDTO(json);
		assertEquals(getNumMembers(), origI);
		assertEquals(origI, jsonI);

	}

	public abstract int getNumUsers();

	@Test
	public final void testNumUsers() {
		// test service and client
		String json = secS.getUsers();
		// LOG.info("users JSON = " + json);

		List<OftAccountMin> user2 = SecurityClient.getUsers(json);

		int jsonI = user2.size();
		int origI = getUsh().getUserSecurity().getUsers().size();

		assertEquals(getNumUsers(), origI);
		assertEquals(origI, jsonI);

	}

	@Test
	public final void testAccount() {
		OtfAccount testAcc = getUsh().getUserSecurity().getUserAccountByName(
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
		OtfAccount testAcc = getUsh().authAccount(testUser, testpw);
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

	public abstract int getNumAccountMembers();

	@Test
	public final void testAccountMembers() {
		List<String> members = new ArrayList<String>();
		OtfAccount oacc = getUsh().getUserSecurity().getUserAccountByName(
				testUser);
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

		assertEquals(getNumAccountMembers(), origI);
		assertEquals(origI, jsonI);

	}

	public abstract int getNumAccountApps();

	@Test
	public final void testAccountApps() {
		List<String> members = new ArrayList<String>();
		OtfAccount oacc = getUsh().getUserSecurity().getUserAccountByName(
				testUser);
		if (oacc != null) {
			List<OtfCustomField> mems = oacc.getCustData().getApps();
			for (OtfCustomField cf : mems) {
				String app = cf.getVals()[1];
				if (!members.contains(app)) {
					members.add(cf.getVals()[1]);
				}
			}
		}

		String json = secS.getUserApps(testUser);

		List<String> member2 = SecurityClient.getUserApps(json);
		int jsonI = member2.size();
		int origI = members.size();

		LOG.info("jsonI = " + jsonI + " origI = " + origI);

		assertEquals(getNumAccountApps(), origI);
		assertEquals(origI, jsonI);

	}

	public abstract int getNumUserAppPerms();

	public abstract int getNumUserAppPermsMember();

	@Test
	public final void testUserAppPerms() {

		String json = secS.getUserAppPerms(testUser, testApp);

		List<AppPermDTO> perms = SecurityClient.getUserAppPerms(json);
		int jsonI = perms.size();
		assertEquals(getNumUserAppPerms(), jsonI);

		String json2 = secS.getUserAppPerms(testUser, testApp, testMember);
		List<AppPermDTO> perms2 = SecurityClient.getUserAppPerms(json2);
		int jsonI2 = perms2.size();
		assertEquals(getNumUserAppPermsMember(), jsonI2);
	}

	public abstract int getNumAppPermGroups();

	public abstract int getNumAppPermGroupsGroup();

	@Test
	public final void testAppPermGroups() {

		String json = secS.getAppPermGroups(testApp);

		List<GroupPermDTO> perms = SecurityClient.getAppPermGroups(json);
		int jsonI = perms.size();
		assertEquals(getNumAppPermGroups(), jsonI);

		String json2 = secS.getAppPermGroups(testApp, testGroup);
		List<GroupPermDTO> perms2 = SecurityClient.getAppPermGroups(json2);
		int jsonI2 = perms2.size();
		assertEquals(getNumAppPermGroupsGroup(), jsonI2);
	}

	public SecurityService getSecS() {
		if (secS == null) {
			secS = new SecurityService(getUsh());
		}
		return secS;
	}

	public void setSecS(SecurityService secSIn) {
		secS = secSIn;
	}

}
