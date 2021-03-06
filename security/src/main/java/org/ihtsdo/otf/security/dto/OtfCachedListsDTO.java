package org.ihtsdo.otf.security.dto;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.objectcache.ObjectCache;

public class OtfCachedListsDTO {
	private static final Logger LOG = Logger.getLogger(OtfCachedListsDTO.class
			.getName());
	// Keys for getting lists/Denormalized maps from cache.
	public static final String APPS_MAP = "Apps_Map";
	public static final String DIRS_MAP = "Dirs_Map";
	public static final String SETTINGS_MAP = "Settings_Map";
	public static final String SETTINGS = "Settings";
	public static final String MEMBERS_LIST = "Members_List";
	public static final String ACCOUNTS_LIST = "Accounts_List";
	public static final String APP_NAMES = "App_Names";
	public static final String APPS_NOT_ADMIN = "AppNotAdmin_List";

	public static final String APPS_WITH_ACCOUNTS = "AppWithAcc_List";
	public static final String DIRS_WITH_ACCOUNTS = "DirWithAcc_List";
	public static final String DIRS_WITH_GROUPS = "DirWithGrp_List";

	public static final String ADMINUSERS_LIST = "AdminUsers_List";

	public static final String ADMIN_CONTEXT_URL = "OtfAdminServletContextUrl";

	private static long lastmod = -1;
	/** 5 minutes ttl **/
	private static long ttl = 300000;

	public static final void setSettings(OtfSettings settingsIn) {
		ObjectCache.INSTANCE.put(SETTINGS, settingsIn);
	}

	public static final OtfSettings getSettings() {
		OtfSettings settings = (OtfSettings) ObjectCache.INSTANCE.get(SETTINGS);
		return settings;
	}

	public static final void remSettings() {
		ObjectCache.INSTANCE.remove(SETTINGS);
	}

	public static final List<String> getAppNamesList() {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) ObjectCache.INSTANCE.get(APP_NAMES);
		return list;
	}

	public static final void setAppNamesList(List<String> membersIn) {
		ObjectCache.INSTANCE.put(APP_NAMES, membersIn);
	}

	public static final void remAppNamesList() {
		ObjectCache.INSTANCE.remove(APP_NAMES);
	}

	public static final List<String> getMembersList() {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) ObjectCache.INSTANCE
				.get(MEMBERS_LIST);
		return list;
	}

	public static final void setMembersList(List<String> membersIn) {
		ObjectCache.INSTANCE.put(MEMBERS_LIST, membersIn);
	}

	public static final void remMembersList() {
		ObjectCache.INSTANCE.remove(MEMBERS_LIST);
	}

	public static final List<String> getAdminUsersList() {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) ObjectCache.INSTANCE
				.get(ADMINUSERS_LIST);
		return list;
	}

	public static final void setAdminUsersList(List<String> listIn) {
		ObjectCache.INSTANCE.put(ADMINUSERS_LIST, listIn);
	}

	public static final void remAdminUsersList() {
		ObjectCache.INSTANCE.remove(ADMINUSERS_LIST);
	}

	public static final Map<String, OtfAccount> getAllAccountsMap() {
		@SuppressWarnings("unchecked")
		Map<String, OtfAccount> allAccounts = (Map<String, OtfAccount>) ObjectCache.INSTANCE
				.get(ACCOUNTS_LIST);
		return allAccounts;
	}

	public static final void setAllAccountsMap(
			final Map<String, OtfAccount> allAccountsIn) {
		ObjectCache.INSTANCE.put(ACCOUNTS_LIST, allAccountsIn);
	}

	public static final void remAllAccountsMap() {
		ObjectCache.INSTANCE.remove(ACCOUNTS_LIST);
	}

	public static final Map<String, List<String>> getAppsMap() {
		@SuppressWarnings("unchecked")
		Map<String, List<String>> appsMap = (Map<String, List<String>>) ObjectCache.INSTANCE
				.get(APPS_MAP);
		return appsMap;
	}

	public static final void setAppsMap(Map<String, List<String>> appsMapIn) {
		ObjectCache.INSTANCE.put(APPS_MAP, appsMapIn);
	}

	public static final void remAppsMap() {
		ObjectCache.INSTANCE.remove(APPS_MAP);
	}

	public static final Map<String, List<String>> getDirsMap() {
		@SuppressWarnings("unchecked")
		Map<String, List<String>> dirsMap = (Map<String, List<String>>) ObjectCache.INSTANCE
				.get(DIRS_MAP);
		return dirsMap;
	}

	public static final void setDirsMap(Map<String, List<String>> dirsMapIn) {
		ObjectCache.INSTANCE.put(DIRS_MAP, dirsMapIn);
	}

	public static final void remDirsMap() {
		ObjectCache.INSTANCE.remove(DIRS_MAP);
	}

	public static final List<String> getAppsNotAdminList() {
		@SuppressWarnings("unchecked")
		List<String> appsNotUserMemberList = (List<String>) ObjectCache.INSTANCE
				.get(APPS_NOT_ADMIN);
		return appsNotUserMemberList;
	}

	public static final void setAppsNotAdminList(
			List<String> appsNotUserMemberListIn) {
		ObjectCache.INSTANCE.put(APPS_NOT_ADMIN, appsNotUserMemberListIn);
	}

	public static final void remAppsNotAdminList() {
		ObjectCache.INSTANCE.remove(APPS_NOT_ADMIN);
	}

	public static final String getAdminServletContextUrl() {
		return (String) ObjectCache.INSTANCE.get(ADMIN_CONTEXT_URL);
	}

	public static final void setAdminServletContextUrl(
			final String adminServletContextUrlIn) {
		ObjectCache.INSTANCE.put(ADMIN_CONTEXT_URL, adminServletContextUrlIn);
	}

	public static final void remAdminServletContextUrl() {
		ObjectCache.INSTANCE.remove(ADMIN_CONTEXT_URL);
	}

	public static final long getLastmod() {
		return lastmod;
	}

	public static final void setLastmod(long lastmodIn) {
		lastmod = lastmodIn;
	}

	public static boolean updatecache() {
		long now = System.currentTimeMillis();
		if ((lastmod + ttl) > now) {
			return false;
		} else {
			setLastmod(now);
			return true;
		}

	}

}
