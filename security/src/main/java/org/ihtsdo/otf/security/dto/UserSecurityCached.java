package org.ihtsdo.otf.security.dto;

import java.util.Collection;

public class UserSecurityCached extends UserSecurity {

	public UserSecurityCached() {
		super();
	}

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	// private static final Logger LOG =
	// Logger.getLogger(UserSecurityCached.class
	// .getName());

	// private OtfCachedListsDTO cachedListMaps = new OtfCachedListsDTO();
	//
	// private String usersApp;
	// private String membersApp;
	// private String adminApp;

	@Override
	public final void init() {
		// initCachedValues();
	}

	@Override
	public final void reset() {
		// resetAllCachedValues();
	}

	// public final void initCachedValues() {
	// getSettings();
	// getAllAccounts();
	// getDirsMap();
	// getAppsMap();
	//
	// if (getSettings() != null && !getSettings().getSettings().isEmpty()) {
	// getMembers();
	// getAppsNotAdmin();
	// getAdminUsers();
	// }
	//
	// }
	//
	// public final void resetAllCachedValues() {
	// resetAllAccounts();
	// resetDirsMap();
	// resetAppsMap();
	// resetMembers();
	// resetSettings();
	// resetAppsNotMembersOrUsers();
	// resetAdminUsers();
	//
	// }

	// public final String getUsersApp() {
	// if (!stringOK(usersApp)) {
	// usersApp = getSettings().getSettings()
	// .get(OtfCustomFieldSetting.USERS).getVal().trim();
	// }
	// return usersApp;
	// }
	//
	// public final void setUsersApp(final String usersAppIn) {
	// usersApp = usersAppIn;
	// }
	//
	// public final String getMembersApp() {
	// if (!stringOK(membersApp)) {
	// membersApp = getSettings().getSettings()
	// .get(OtfCustomFieldSetting.MEMBERS).getVal().trim();
	// }
	// return membersApp;
	// }
	//
	// public final void setMembersApp(final String membersAppIn) {
	// membersApp = membersAppIn;
	// }

	// public final void resetSettings() {
	// OtfCachedListsDTO.remSettings();
	// getSettings();
	// }

	// public final OtfSettings getSettings() {
	// OtfSettings settings = OtfCachedListsDTO.getSettings();
	// if (settings == null) {
	// // Get settings dir
	// OtfDirectory setDirectory = getDirs().getDirByName(SETTINGS);
	// if (setDirectory != null) {
	// // get settings group
	// OtfGroup setgrp = setDirectory.getGroups().getGroupByName(
	// SETTINGS);
	// setgrp.setParentDirName(SETTINGS);
	// settings = new OtfSettings(setgrp);
	// OtfCachedListsDTO.setSettings(settings);
	// setDefaultpw(settings.getDefPw());
	// }
	// }
	// return settings;
	// }
	//
	// public final void setSettings(final OtfSettings settingsIn) {
	// OtfCachedListsDTO.setSettings(settingsIn);
	// }

	// public final List<OtfGroup> getGroupsByAppName(final String appname) {
	// OtfApplication app = getApps().getAppByName(appname);
	// return getGroupsByApp(app);
	// }

	// public final List<String> getDirsByAppName(final String appname) {
	// List<String> dirs = new ArrayList<String>();
	// for (OtfAccountStore acs : getApps().getAppByName(appname)
	// .getAccountStores().values()) {
	// if (acs.isDir()) {
	// dirs.add(acs.getName());
	// }
	// }
	//
	// return dirs;
	// }

	// public final List<OtfGroup> getGroupsByApp(final OtfApplication app) {
	// List<OtfGroup> groups = new ArrayList<OtfGroup>();
	// for (OtfAccountStore acs : app.getAccountStores().values()) {
	// if (acs.isDir()) {
	// String dirName = acs.getName();
	// groups.addAll(getGroupsByDirName(dirName));
	// }
	// }
	// return groups;
	//
	// }

	// public final List<OtfGroup> getGroupsByDirName(final String dirName) {
	// List<OtfGroup> groups = new ArrayList<OtfGroup>();
	// OtfDirectory dir = getDirs().getDirByName(dirName);
	// for (OtfGroup grp : dir.getGroups().getGroups().values()) {
	// grp.getId();
	// grp.setParentDirName(dirName);
	// groups.add(grp);
	// }
	// return groups;
	// }

	// public final Map<String, List<String>> getAppsMap() {
	// Map<String, List<String>> appsMap = OtfCachedListsDTO.getAppsMap();
	// if (appsMap == null || appsMap.size() == 0) {
	// appsMap = new HashMap<String, List<String>>();
	// for (OtfApplication app : getApps().getApplications().values()) {
	// List<String> grpNames = new ArrayList<String>();
	// List<OtfGroup> grps = getGroupsByApp(app);
	// for (OtfGroup grp : grps) {
	// grpNames.add(grp.getName());
	// }
	// Collections.sort(grpNames);
	// appsMap.put(app.getName(), grpNames);
	// }
	//
	// // add user admin even if only dir
	// String useradminapp = getAdminApp();
	//
	// if (!appsMap.containsKey(useradminapp)) {
	// if (getDirsMap().containsKey(useradminapp)) {
	// appsMap.put(useradminapp, getDirsMap().get(useradminapp));
	// }
	// }
	// setAppsMap(appsMap);
	// }
	//
	// return appsMap;
	// }
	//
	// public final void setAppsMap(final Map<String, List<String>> appsMap) {
	// OtfCachedListsDTO.setAppsMap(appsMap);
	// }
	//
	// public final void resetAppsMap() {
	// OtfCachedListsDTO.remAppsMap();
	// getAppsMap();
	// }

	// public final Map<String, List<String>> getDirsMap() {
	// Map<String, List<String>> dirsMap = OtfCachedListsDTO.getDirsMap();
	// if (dirsMap == null || dirsMap.isEmpty()) {
	// dirsMap = new HashMap<String, List<String>>();
	// for (String dirName : getDirs().getDirectories().keySet()) {
	// List<String> grpNames = new ArrayList<String>();
	// List<OtfGroup> grps = getGroupsByDirName(dirName);
	// for (OtfGroup grp : grps) {
	// grpNames.add(grp.getName());
	// }
	// Collections.sort(grpNames);
	// dirsMap.put(dirName, grpNames);
	// }
	// setDirsMap(dirsMap);
	// }
	//
	// return dirsMap;
	// }
	//
	// public final void setDirsMap(final Map<String, List<String>> dirsMap) {
	// OtfCachedListsDTO.setDirsMap(dirsMap);
	// }
	//
	// public final void resetDirsMap() {
	// OtfCachedListsDTO.remDirsMap();
	// getDirsMap();
	// }

	// public final List<String> getMembers() {
	// List<String> members = OtfCachedListsDTO.getMembersList();
	// if (members == null) {
	// members = new ArrayList<String>();
	// OtfDirectory mDirectory = getMembersDir();
	// if (mDirectory != null) {
	// // getAll groups within
	// for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
	// members.add(grp.getName());
	// }
	// }
	// Collections.sort(members);
	// setMembers(members);
	// }
	//
	// return members;
	// }
	//
	// public final void setMembers(final List<String> membersIn) {
	// OtfCachedListsDTO.setMembersList(membersIn);
	// }
	//
	// public final void resetMembers() {
	// OtfCachedListsDTO.remMembersList();
	// getMembers();
	// }

	// public final OtfGroup getMemberByName(final String name) {
	// OtfDirectory mDirectory = getMembersDir();
	// if (mDirectory != null) {
	// for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
	// if (grp.getName().equals(name)) {
	// return grp;
	// }
	// }
	// }
	// return null;
	//
	// }

	// public final OtfGroup getMemberById(final String id) {
	// OtfDirectory mDirectory = getMembersDir();
	// if (mDirectory != null) {
	// for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
	// if (grp.getIdIfSet().equals(id)) {
	// return grp;
	// }
	// }
	// }
	// return null;
	//
	// }

	// public final OtfGroup getGroupById(final String id) {
	// for (OtfDirectory mDirectory : getDirs().getDirectories().values()) {
	// if (mDirectory != null) {
	// for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
	// if (grp.getIdIfSet().equals(id)) {
	// return grp;
	// }
	// }
	// }
	// }
	// return null;
	//
	// }

	// public final OtfDirectory getMembersDir() {
	// return getDirs().getDirByName(getMembersApp());
	// }
	//
	// public final OtfDirectory getUsersDir() {
	// return getDirs().getDirByName(getUsersApp());
	// }

	// public final OtfDirectory getDirByName(final String dirName) {
	// return getDirs().getDirByName(dirName);
	// }

	// public final Collection<OtfAccount> getUsers(final String dirname) {
	// if (dirname == null) {
	// // Get members dir
	// return getUsersbyDir(getUsersApp());
	// }
	// if (dirname.equals(ALL)) {
	// return getAllUsers();
	// }
	// return getUsersbyDir(dirname);
	// }
	//
	// public final Collection<OtfAccount> getUsers() {
	// return getAllUsers();
	// }

	// public final Map<String, OtfAccount> getAdminUsersMap() {
	//
	// Map<String, OtfAccount> adminUsers = new HashMap<String, OtfAccount>();
	// // First get all StormPath Admins
	// if (stringOK(getHandlerAdminDir())) {
	// String ucAdminDir = getHandlerAdminDir().toUpperCase();
	// for (String dirName : getDirs().getDirectories().keySet()) {
	// if (dirName.toUpperCase().startsWith(ucAdminDir)) {
	// Collection<OtfAccount> handleradmins = getUsersbyDir(dirName);
	// for (OtfAccount acc : handleradmins) {
	// adminUsers.put(acc.getName(), acc);
	// }
	// }
	// }
	// }
	//
	// // Then add any users who have a role in the relevant admin app.
	// String adminA = getAdminApp();
	// if (stringOK(adminA)) {
	// for (OtfAccount acc : getUsers()) {
	// if (acc.getCustData() != null) {
	// List<OtfCustomField> apps = acc.getCustData().getApps();
	// for (OtfCustomField app : apps) {
	// OtfCustomFieldApplication oca = (OtfCustomFieldApplication) app
	// .getModel();
	// if (oca.getApp().equals(adminA)) {
	// adminUsers.put(acc.getName(), acc);
	// }
	// }
	// }
	// }
	// }
	// // USer Admin is now just a dir.......special thing like members...
	// return adminUsers;
	// }

	// public final List<String> getAdminUsers() {
	// List<String> adminUsers = OtfCachedListsDTO.getAdminUsersList();
	// if (adminUsers == null) {
	// adminUsers = new ArrayList<String>();
	// Map<String, OtfAccount> adminUserMap = getAdminUsersMap();
	// if (adminUserMap != null && !adminUserMap.isEmpty()) {
	// adminUsers.addAll(adminUserMap.keySet());
	// }
	// Collections.sort(adminUsers, String.CASE_INSENSITIVE_ORDER);
	// setAdminUsers(adminUsers);
	// }
	//
	// return adminUsers;
	// }
	//
	// public final void setAdminUsers(final List<String> listIn) {
	// OtfCachedListsDTO.setAdminUsersList(listIn);
	// }

	// public final void resetAdminUsers() {
	// OtfCachedListsDTO.remAdminUsersList();
	// getAdminUsers();
	// }

	// public final Collection<OtfAccount> getAllUsers() {
	// return getAllAccounts().values();
	// }

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

	// public final Collection<String> getDirNamesForUser(final String username)
	// {
	// ArrayList<String> dirs = new ArrayList<String>();
	//
	// for (OtfDirectory dir : getDirs().getDirectories().values()) {
	// if (!dir.getAccounts().getAccounts().isEmpty()) {
	// for (OtfAccount user : dir.getAccounts().getAccounts().values()) {
	// if (user.getName().equals(username)) {
	// dirs.add(dir.getName());
	// }
	// }
	// }
	// }
	// return dirs;
	// }

	// public final List<String> getOtfAppNamesForUser(final String username) {
	// ArrayList<String> appsList = new ArrayList<String>();
	// Collection<String> dirsforUser = getDirNamesForUser(username);
	//
	// for (OtfApplication app : getApps().getApplications().values()) {
	// for (String key : app.getAccountStores().keySet()) {
	// if (dirsforUser.contains(key)) {
	// appsList.add(app.getName());
	// }
	// }
	// }
	//
	// return appsList;
	// }
	//
	// public final String getFirstAppForUser(final String username) {
	// List<String> appsList = getOtfAppNamesForUser(username);
	// Collections.sort(appsList);
	// if (appsList.isEmpty()) {
	// return null;
	// }
	// return appsList.get(0);
	// }

	// public final OtfAccount getUserAccountByName(final String name,
	// final String dirName) {
	// for (OtfAccount acc : getUsers(dirName)) {
	// if (acc.getName().equalsIgnoreCase(name)) {
	// return acc;
	// }
	// }
	// return null;
	// }

	// public final OtfAccount getUserAccountById(final String id,
	// final String dirName) {
	// for (OtfAccount acc : getUsers(dirName)) {
	// if (acc.getId().equalsIgnoreCase(id)) {
	// return acc;
	// }
	// }
	// return null;
	// }

	@Override
	public final String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("UserSecurity:\n");
		sbuild.append("Directories:\n");
		sbuild.append(getDirs().toString());
		sbuild.append("Applications:\n");
		sbuild.append(getApps().toString());
		return sbuild.toString();

	}

	// public final Collection<String> getAppNames() {
	// return getAppsMap().keySet();
	// }

	// public final List<String> getAppsNotAdmin() {
	// List<String> appsNotAdmin = OtfCachedListsDTO.getAppsNotAdminList();
	// if (appsNotAdmin == null || appsNotAdmin.isEmpty()) {
	// Collection<String> all = getAppNames();
	// appsNotAdmin = new ArrayList<String>();
	// if (all != null && !all.isEmpty()) {
	// String users = getUsersApp();
	// String members = getMembersApp();
	// String adminhandler = getHandlerAdminDir();
	// List<String> adminApps = new ArrayList<String>();
	// if (stringOK(users)) {
	// adminApps.add(users);
	// }
	// if (stringOK(members)) {
	// adminApps.add(members);
	// }
	// if (stringOK(adminhandler)) {
	// adminApps.add(adminhandler);
	// }
	//
	// for (String appname : all) {
	// if (!adminApps.contains(appname)) {
	// appsNotAdmin.add(appname);
	// }
	// }
	//
	// }
	//
	// setAppsNotAdmin(appsNotAdmin);
	// }
	// return appsNotAdmin;
	// }
	//
	// public final void setAppsNotAdmin(final List<String>
	// appsNotMembersOrUsersIn) {
	// OtfCachedListsDTO.setAppsNotAdminList(appsNotMembersOrUsersIn);
	// }

	// public final Map<String, OtfAccount> getAllAccounts() {
	// Map<String, OtfAccount> allAccounts = OtfCachedListsDTO
	// .getAllAccountsMap();
	//
	// if (allAccounts == null || allAccounts.size() == 0) {
	// allAccounts = new HashMap<String, OtfAccount>();
	// for (OtfDirectory dir : getDirs().getDirectories().values()) {
	// if (!dir.getAccounts().getAccounts().isEmpty()) {
	// for (OtfAccount user : dir.getAccounts().getAccounts()
	// .values()) {
	// allAccounts.put(user.getName(), user);
	// }
	// }
	// }
	// setAllAccounts(allAccounts);
	// }
	// return allAccounts;
	// }
	//
	// public final void setAllAccounts(final Map<String, OtfAccount>
	// allAccountsIn) {
	// OtfCachedListsDTO.setAllAccountsMap(allAccountsIn);
	// }

	// public final void resetAllAccounts() {
	// OtfCachedListsDTO.remAllAccountsMap();
	// getAllAccounts();
	// }

	// public final boolean accountExists(final String accName) {
	// for (String key : getAllAccounts().keySet()) {
	// if (key.equalsIgnoreCase(accName)) {
	// return true;
	// }
	// }
	// return false;
	// }

	// public final String getAdminApp() {
	// if (!stringOK(adminApp)) {
	// if (getSettings() != null) {
	// adminApp = getSettings().getSettings()
	// .get(OtfCustomFieldSetting.ADMIN).getVal().trim();
	// }
	// }
	// return adminApp;
	// }
	//
	// public final void setAdminApp(final String adminAppIn) {
	// adminApp = adminAppIn;
	// }

}
