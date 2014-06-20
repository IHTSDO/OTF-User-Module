package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;

public class UserSecurity {

	public UserSecurity() {
		super();
	}

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(UserSecurity.class
			.getName());

	private OtfCachedListsDTO cachedListMaps = new OtfCachedListsDTO();

	private OtfDirectories dirs = new OtfDirectories();
	private OtfApplications apps = new OtfApplications();
	private String defaultpw;
	private String usersApp;
	private String membersApp;

	private String adminApp;
	private String handlerAdminDir;

	public static final String SETTINGS = "OTFSettings";
	public static final String ALL = "*";

	public final void initCachedValues() {
		getAllAccounts();
		getDirsMap();
		getAppsMap();
		if (getSettings() != null && getSettings().getSettings().size() > 0) {
			getMembers();
			getSettings();
			getAppsNotMembersOrUsers();
			getAdminUsers();
		}
	}

	public final void resetAllCachedValues() {
		resetAllAccounts();
		resetDirsMap();
		resetAppsMap();
		resetMembers();
		resetSettings();
		resetAppsNotMembersOrUsers();
		resetAdminUsers();

	}

	public final String getDefaultpw() {
		if (!stringOK(defaultpw)) {
			defaultpw = getSettings().getSettings()
					.get(OtfCustomFieldSetting.DEFPW).getVal().trim();
			LOG.info("getDefaultpw deffpass = " + defaultpw);
		}
		return defaultpw;
	}

	public final void setDefaultpw(final String defaultpwIn) {
		defaultpw = defaultpwIn;
	}

	public final String getUsersApp() {
		if (!stringOK(usersApp)) {
			usersApp = getSettings().getSettings()
					.get(OtfCustomFieldSetting.USERS).getVal().trim();
		}
		return usersApp;
	}

	public final void setUsersApp(final String usersAppIn) {
		usersApp = usersAppIn;
	}

	public final String getMembersApp() {
		if (!stringOK(membersApp)) {
			membersApp = getSettings().getSettings()
					.get(OtfCustomFieldSetting.MEMBERS).getVal().trim();
		}
		return membersApp;
	}

	public final void setMembersApp(final String membersAppIn) {
		membersApp = membersAppIn;
	}

	public final OtfApplications getApps() {
		return apps;
	}

	public final void setApps(final OtfApplications appsIn) {
		apps = appsIn;
	}

	public final OtfDirectories getDirs() {
		return dirs;
	}

	public final void setDirs(final OtfDirectories dirsIn) {
		dirs = dirsIn;
	}

	public void resetSettings() {
		getCachedListMaps().remSettings();
		getSettings();
	}

	public final OtfSettings getSettings() {
		OtfSettings settings = getCachedListMaps().getSettings();
		if (settings == null) {
			Map<String, OtfCustomFieldSetting> setMap = new HashMap<String, OtfCustomFieldSetting>();
			// Get settings dir
			OtfDirectory setDirectory = getDirs().getDirByName(SETTINGS);
			if (setDirectory != null) {
				// get settings group
				OtfGroup setgrp = setDirectory.getGroups().getGroupByName(
						SETTINGS);
				setgrp.setParentDirName(SETTINGS);
				settings = new OtfSettings(setgrp);
				getCachedListMaps().setSettings(settings);
			}
		}
		return settings;
	}

	public final void setSettings(OtfSettings settingsIn) {
		getCachedListMaps().setSettings(settingsIn);
	}

	public final boolean stringOK(final String chk) {
		return chk != null && chk.length() > 0;
	}

	public final List<OtfGroup> getGroupsByAppName(String appname) {
		OtfApplication app = getApps().getAppByName(appname);
		return getGroupsByApp(app);
	}

	public final List<String> getDirsByAppName(String appname) {
		List<String> dirs = new ArrayList<String>();
		for (OtfAccountStore acs : getApps().getAppByName(appname)
				.getAccountStores().values()) {
			if (acs.isDir()) {
				dirs.add(acs.getName());
			}
		}

		return dirs;
	}

	public final List<OtfGroup> getGroupsByApp(OtfApplication app) {
		List<OtfGroup> groups = new ArrayList<OtfGroup>();
		for (OtfAccountStore acs : app.getAccountStores().values()) {
			if (acs.isDir()) {
				String dirName = acs.getName();
				groups.addAll(getGroupsByDirName(dirName));
			}
		}
		return groups;

	}

	public final List<OtfGroup> getGroupsByDirName(String dirName) {
		List<OtfGroup> groups = new ArrayList<OtfGroup>();
		OtfDirectory dir = getDirs().getDirByName(dirName);
		for (OtfGroup grp : dir.getGroups().getGroups().values()) {
			grp.getId();
			grp.setParentDirName(dirName);
			groups.add(grp);
		}
		return groups;
	}

	public final Map<String, List<String>> getAppsMap() {
		Map<String, List<String>> appsMap = getCachedListMaps().getAppsMap();
		if (appsMap == null) {
			appsMap = new HashMap<String, List<String>>();
			for (OtfApplication app : getApps().getApplications().values()) {
				List<String> grpNames = new ArrayList<String>();
				List<OtfGroup> grps = getGroupsByApp(app);
				for (OtfGroup grp : grps) {
					grpNames.add(grp.getName());
				}
				Collections.sort(grpNames);
				appsMap.put(app.getName(), grpNames);
			}
			setAppsMap(appsMap);
		}

		return appsMap;
	}

	public final void setAppsMap(Map<String, List<String>> appsMap) {
		getCachedListMaps().setAppsMap(appsMap);
	}

	public final void resetAppsMap() {
		getCachedListMaps().remAppsMap();
		getAppsMap();
	}

	public final Map<String, List<String>> getDirsMap() {
		Map<String, List<String>> dirsMap = getCachedListMaps().getDirsMap();
		if (dirsMap == null) {
			dirsMap = new HashMap<String, List<String>>();
			for (String dirName : getDirs().getDirectories().keySet()) {
				List<String> grpNames = new ArrayList<String>();
				List<OtfGroup> grps = getGroupsByDirName(dirName);
				for (OtfGroup grp : grps) {
					grpNames.add(grp.getName());
				}
				Collections.sort(grpNames);
				dirsMap.put(dirName, grpNames);
			}
			setDirsMap(dirsMap);
		}

		return dirsMap;
	}

	public final void setDirsMap(Map<String, List<String>> dirsMap) {
		getCachedListMaps().setDirsMap(dirsMap);
	}

	public final void resetDirsMap() {
		getCachedListMaps().remDirsMap();
		getDirsMap();
	}

	public final List<String> getMembers() {
		// LOG.info("GET MEMBERS");
		List<String> members = getCachedListMaps().getMembersList();
		if (members == null) {
			members = new ArrayList<String>();
			// }

			// if (members.size() == 0) {
			// Get members dir
			OtfDirectory mDirectory = getMembersDir();
			if (mDirectory != null) {
				// getAll groups within
				for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
					members.add(grp.getName());
					// LOG.info("Member grp = " + grp.getName());
				}
			}
			Collections.sort(members);
			setMembers(members);
		}

		return members;
	}

	public final void setMembers(List<String> membersIn) {
		getCachedListMaps().setMembersList(membersIn);
	}

	public final void resetMembers() {
		getCachedListMaps().remMembersList();
		getMembers();
	}

	public final OtfGroup getMemberByName(final String name) {
		OtfDirectory mDirectory = getMembersDir();
		if (mDirectory != null) {
			for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
				if (grp.getName().equals(name)) {
					return grp;
				}
			}
		}
		return null;

	}

	public final OtfGroup getMemberById(final String id) {
		OtfDirectory mDirectory = getMembersDir();
		if (mDirectory != null) {
			for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
				if (grp.getIdIfSet().equals(id)) {
					return grp;
				}
			}
		}
		return null;

	}

	public final OtfGroup getGroupById(final String id) {
		for (OtfDirectory mDirectory : getDirs().getDirectories().values()) {
			if (mDirectory != null) {
				for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
					if (grp.getIdIfSet().equals(id)) {
						return grp;
					}
				}
			}
		}
		return null;

	}

	public final OtfDirectory getMembersDir() {
		return getDirs().getDirByName(getMembersApp());
	}

	public final OtfDirectory getUsersDir() {
		return getDirs().getDirByName(getUsersApp());
	}

	public final Collection<OtfAccount> getUsers(final String dirname) {
		if (dirname == null) {
			// Get members dir
			return getUsersbyDir(getUsersApp());
		}
		if (dirname.equals(ALL)) {
			return getAllUsers();
		}
		return getUsersbyDir(dirname);
	}

	public final Map<String, OtfAccount> getAdminUsersMap() {

		Map<String, OtfAccount> adminUsers = new HashMap<String, OtfAccount>();
		// First get all StormPath Admins
		if (stringOK(getHandlerAdminDir())) {
			String ucAdminDir = getHandlerAdminDir().toUpperCase();
			// LOG.info("getAdminUsers ucAdminDir");
			// getCachedListMaps().getDirsMap().keySet();
			for (String dirName : getDirs().getDirectories().keySet()) {
				if (dirName.toUpperCase().startsWith(ucAdminDir)) {
					// LOG.info("Found an admin dir " + dirName);
					Collection<OtfAccount> handleradmins = getUsersbyDir(dirName);
					for (OtfAccount acc : handleradmins) {
						adminUsers.put(acc.getName(), acc);
					}
				}
			}
		}

		// Then add any users who have a role in the relevant admin app.
		String adminA = getAdminApp();
		// LOG.info("adminApp = " + adminA);
		if (stringOK(adminA)) {
			for (OtfAccount acc : getAllUsers()) {
				if (acc.getCustData() != null) {
					List<OtfCustomField> apps = acc.getCustData().getApps();
					for (OtfCustomField app : apps) {
						OtfCustomFieldApplication oca = (OtfCustomFieldApplication) app
								.getModel();
						if (oca.getApp().equals(adminA)) {
							// LOG.info("Found an admin user " + acc.getName());
							adminUsers.put(acc.getName(), acc);
						}
					}
				}

			}
		}

		return adminUsers;
	}

	public final List<String> getAdminUsers() {
		// LOG.info("GET MEMBERS");
		List<String> adminUsers = getCachedListMaps().getAdminUsersList();
		if (adminUsers == null) {
			adminUsers = new ArrayList<String>();
			Map<String, OtfAccount> adminUserMap = getAdminUsersMap();
			if (adminUserMap != null && adminUserMap.size() > 0) {
				adminUsers.addAll(adminUserMap.keySet());
			}
			Collections.sort(adminUsers, String.CASE_INSENSITIVE_ORDER);
			setAdminUsers(adminUsers);
		}

		return adminUsers;
	}

	public final void setAdminUsers(List<String> listIn) {
		getCachedListMaps().setAdminUsersList(listIn);
	}

	public final void resetAdminUsers() {
		getCachedListMaps().remAdminUsersList();
		getAdminUsers();
	}

	public final Collection<OtfAccount> getAllUsers() {
		return getAllAccounts().values();
	}

	public final Collection<OtfAccount> getUsersbyDir(final String dirName) {
		OtfDirectory uDirectory = getDirs().getDirByName(dirName);
		if (uDirectory != null) {
			return getUsersByDir(uDirectory);
		}
		return null;
	}

	public final Collection<OtfAccount> getUsersByDir(final OtfDirectory dirIn) {
		if (dirIn != null) {
			return dirIn.getAllAccounts().values();
		}
		return null;
	}

	public final Collection<String> getDirNamesForUser(final String username) {
		ArrayList<String> dirs = new ArrayList<String>();

		for (OtfDirectory dir : getDirs().getDirectories().values()) {
			if (dir.getAccounts().getAccounts().size() > 0) {
				for (OtfAccount user : dir.getAccounts().getAccounts().values()) {
					if (user.getName().equals(username)) {
						dirs.add(dir.getName());
					}
				}
			}
		}
		return dirs;
	}

	public final Collection<OtfAccountMin> getMinUsers(final String dirName) {
		Collection<OtfAccount> users = getUsers(dirName);
		Collection<OtfAccountMin> usersMin = new ArrayList<OtfAccountMin>();
		if (users != null) {
			for (OtfAccount user : users) {
				usersMin.add((new OtfAccountMin(user)));
			}
		}
		return usersMin;
	}

	public final OtfAccount getUserAccountByName(final String name,
			final String dirName) {
		for (OtfAccount acc : getUsers(dirName)) {
			if (acc.getName().equalsIgnoreCase(name)) {
				return acc;
			}
		}
		return null;
	}

	public final OtfAccount getUserAccountById(final String id,
			final String dirName) {
		for (OtfAccount acc : getUsers(dirName)) {
			if (acc.getId().equalsIgnoreCase(id)) {
				return acc;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("UserSecurity:\n");
		sbuild.append("Directories:\n");
		sbuild.append(getDirs().toString());
		sbuild.append("Applications:\n");
		sbuild.append(getApps().toString());
		return sbuild.toString();

	}

	public final OtfCachedListsDTO getCachedListMaps() {
		return cachedListMaps;
	}

	public final void setCachedListMaps(OtfCachedListsDTO cachedListMapsIn) {
		cachedListMaps = cachedListMapsIn;
	}

	public final Collection<String> getAppNames() {
		return getAppsMap().keySet();
	}

	public final List<String> getAppsNotMembersOrUsers() {

		List<String> appsNotMembersOrUsers = getCachedListMaps()
				.getAppsNotUserMemberList();
		if (appsNotMembersOrUsers == null) {
			Collection<String> all = getAppNames();
			appsNotMembersOrUsers = new ArrayList<String>();
			if (all != null && all.size() > 0) {
				// Map<String, OtfCustomFieldSetting> settings = getSettings();
				String users = getUsersApp();
				String members = getMembersApp();
				for (String appname : all) {
					boolean remove = appname.equals(users)
							|| appname.equals(members);
					if (!remove) {
						appsNotMembersOrUsers.add(appname);
					}
				}

			}
			setAppsNotMembersOrUsers(appsNotMembersOrUsers);
		}
		return appsNotMembersOrUsers;
	}

	public final void setAppsNotMembersOrUsers(
			List<String> appsNotMembersOrUsersIn) {
		getCachedListMaps().setAppsNotUserMemberList(appsNotMembersOrUsersIn);
	}

	public final void resetAppsNotMembersOrUsers() {
		getCachedListMaps().remAppsNotUserMemberList();
		getAppsNotMembersOrUsers();
	}

	public final Map<String, OtfAccount> getAllAccounts() {
		Map<String, OtfAccount> allAccounts = getCachedListMaps()
				.getAllAccountsMap();
		if (allAccounts == null) {
			allAccounts = new HashMap<String, OtfAccount>();
			for (OtfDirectory dir : getDirs().getDirectories().values()) {
				if (dir.getAccounts().getAccounts().size() > 0) {
					for (OtfAccount user : dir.getAccounts().getAccounts()
							.values()) {
						allAccounts.put(user.getName(), user);
					}
				}
			}
			setAllAccounts(allAccounts);
		}
		return allAccounts;
	}

	public final void setAllAccounts(final Map<String, OtfAccount> allAccountsIn) {
		getCachedListMaps().setAllAccountsMap(allAccountsIn);
	}

	public final void resetAllAccounts() {
		getCachedListMaps().remAllAccountsMap();
		getAllAccounts();
	}

	public final boolean accountExists(final String accName) {
		for (String key : getAllAccounts().keySet()) {
			if (key.equalsIgnoreCase(accName)) {
				return true;
			}
		}
		return false;
	}

	public final String getAdminApp() {
		if (!stringOK(adminApp)) {
			adminApp = getSettings().getSettings()
					.get(OtfCustomFieldSetting.ADMIN).getVal().trim();
		}
		return adminApp;
	}

	public final void setAdminApp(String adminAppIn) {
		adminApp = adminAppIn;
	}

	public final String getHandlerAdminDir() {
		return handlerAdminDir;
	}

	public final void setHandlerAdminDir(String handlerAdminDirIn) {
		handlerAdminDir = handlerAdminDirIn;
	}

}
