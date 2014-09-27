package org.ihtsdo.otf.security.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
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

	public static final String TEST_USER = "Bob";
	public static final String TEST_PW = "Sn0m3dDefPass";
	public static final String TEST_APP = "Mapping";
	public static final String TEST_MEMBER = "INTL";
	public static final String TEST_GROUP = "Reviewer";

	public static final String TEST_USERS_APP = "OTF Users";
	public static final String TEST_MENBERS_APP = "Members";

	@After
	public abstract void dispose();

	public abstract UserSecurityHandler getUsh();

	// Tests
	public abstract int getNumDirs();

	@Test
	public final void testNumDirs() {
		assertEquals(getNumDirs(), getUsh().getUserSecurityModel().getModel()
				.getDirs().getDirectories().size());
	}

	public abstract int getNumApps();

	@Test
	public final void testNumApps() {

		// test service and client
		String json = secS.getApps();
		List<String> member2 = SecurityClient.getApps(json);
		int jsonI = member2.size();
		int origI = getUsh().getUserSecurityModel().getModel().getApps()
				.getApplications().size();
		assertEquals(getNumApps(), origI);
		assertEquals(origI, jsonI);
	}

	public abstract int getNumSettings();

	@Test
	public final void testNumSettings() {
		assertEquals(getNumSettings(), getUsh().getUserSecurityModel()
				.getSettings().getSettings().size());
	}

	@Test
	public final void testSettingsVals() {
		assertEquals(TEST_PW, getUsh().getUserSecurityModel().getSettings()
				.getDefPw());
		assertEquals(TEST_USERS_APP, getUsh().getUserSecurityModel()
				.getSettings().getUsers());
		assertEquals(TEST_MENBERS_APP, getUsh().getUserSecurityModel()
				.getSettings().getMembers());

	}

	public abstract int getNumMembers();

	@Test
	public final void testNumMembers() {
		// test service and client
		String json = secS.getMembers();
		List<String> member2 = SecurityClient.getMembers(json);
		int jsonI = member2.size();
		int origI = getUsh().getUserSecurityModel().getMembers().size();
		assertEquals(getNumMembers(), origI);
		assertEquals(origI, jsonI);

	}

	public abstract int getNumUsers();

	@Test
	public final void testNumUsers() {
		// test service and client
		String json = secS.getUsers();
		List<OtfAccountMin> user2 = SecurityClient.getUsers(json);

		int jsonI = user2.size();
		int origI = getUsh().getUserSecurityModel().getUsers().size();

		assertEquals(getNumUsers(), origI);
		assertEquals(origI, jsonI);

	}

	@Test
	public final void testAccount() {
		OtfAccount testAcc = getUsh().getUserSecurityModel()
				.getUserAccountByName(TEST_USER);
		assertNotNull(testAcc);
		if (testAcc != null) {
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
		OtfAccount testAcc = getUsh().authAccount(TEST_USER, TEST_PW, null);
		String json = secS.getUserByName(TEST_USER, TEST_PW);

		String name = "";
		OtfAccountMin user = SecurityClient.getUserByName(json);
		if (user != null) {
			name = user.getName();
		}
		assertEquals(TEST_USER, name);
		assertNotNull(testAcc);
	}

	public abstract int getNumAccountMembers();

	@Test
	public final void testAccountMembers() {
		List<String> members = new ArrayList<String>();
		OtfAccount oacc = getUsh().getUserSecurityModel().getUserAccountByName(
				TEST_USER);
		if (oacc != null) {
			List<OtfCustomField> mems = oacc.getCustData().getMembers();
			for (OtfCustomField cf : mems) {
				members.add(cf.getVals()[1]);
			}
		}

		String json = secS.getUserMemberships(TEST_USER);

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
		OtfAccount oacc = getUsh().getUserSecurityModel().getUserAccountByName(
				TEST_USER);
		if (oacc != null) {
			List<OtfCustomField> mems = oacc.getCustData().getApps();
			for (OtfCustomField cf : mems) {
				String app = cf.getVals()[1];
				if (!members.contains(app)) {
					members.add(cf.getVals()[1]);
				}
			}
		}

		String json = secS.getUserApps(TEST_USER);

		List<String> member2 = SecurityClient.getUserApps(json);
		int jsonI = member2.size();
		int origI = members.size();

		assertEquals(getNumAccountApps(), origI);
		assertEquals(origI, jsonI);

	}

	public abstract int getNumUserAppPerms();

	public abstract int getNumUserAppPermsMember();

	@Test
	public final void testUserAppPerms() {

		String json = secS.getUserAppPerms(TEST_USER, TEST_APP);

		List<AppPermDTO> perms = SecurityClient.getUserAppPerms(json);
		int jsonI = perms.size();
		assertEquals(getNumUserAppPerms(), jsonI);

		String json2 = secS.getUserAppPerms(TEST_USER, TEST_APP, TEST_MEMBER);
		List<AppPermDTO> perms2 = SecurityClient.getUserAppPerms(json2);
		int jsonI2 = perms2.size();
		assertEquals(getNumUserAppPermsMember(), jsonI2);
	}

	public abstract int getNumAppPermGroups();

	public abstract int getNumAppPermGroupsGroup();

	@Test
	public final void testAppPermGroups() {

		String json = secS.getAppPermGroups(TEST_APP);

		List<GroupPermDTO> perms = SecurityClient.getAppPermGroups(json);
		int jsonI = perms.size();
		assertEquals(getNumAppPermGroups(), jsonI);

		String json2 = secS.getAppPermGroups(TEST_APP, TEST_GROUP);
		List<GroupPermDTO> perms2 = SecurityClient.getAppPermGroups(json2);
		int jsonI2 = perms2.size();
		assertEquals(getNumAppPermGroupsGroup(), jsonI2);
	}

	public abstract int getNumAppUsers();

	@Test
	public final void testAppUsers() {

		String json = secS.getAppUsers(TEST_APP);

		List<String> users = SecurityClient.getAppUsers(json);
		int jsonI = users.size();
		LOG.info("testAppUsers = " + jsonI);
		assertEquals(getNumAppUsers(), jsonI);
	}

	public final SecurityService getSecS() {
		if (secS == null) {
			secS = new SecurityService(getUsh());
		}
		return secS;
	}

	public final void setSecS(final SecurityService secSIn) {
		secS = secSIn;
	}

}
