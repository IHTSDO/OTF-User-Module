package org.ihtsdo.otf.security.dto;

import java.util.List;
import java.util.Map;

import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;
import org.ihtsdo.otf.security.objectcache.ObjectCache;

public class OtfCachedListsDTO {

	// Keys for getting lists/Denormalized maps from cache.
	public static final String APPS_MAP = "Apps_Map";
	public static final String SETTINGS_MAP = "Settings_Map";
	public static final String MEMBERS_LIST = "Members_List";
	public static final String ACCOUNTS_LIST = "Accounts_List";
	public static final String APPS_NOT_USER_MEM_LIST = "AppNotUserMember_List";

	// private Map<String, OtfCustomFieldSetting> settings = new HashMap<String,
	// OtfCustomFieldSetting>();
	// private List<String> appsNotUserMemberList;

	// private Map<String, OtfAccount> allAccounts;
	// private Map<String, List<String>> appsMap;

	public final Map<String, OtfCustomFieldSetting> getSettingsMap() {
		@SuppressWarnings("unchecked")
		Map<String, OtfCustomFieldSetting> settings = (Map<String, OtfCustomFieldSetting>) ObjectCache.INSTANCE
				.get(SETTINGS_MAP);
		return settings;
	}

	public final void setSettingsMap(
			Map<String, OtfCustomFieldSetting> settingsIn) {
		ObjectCache.INSTANCE.put(SETTINGS_MAP, settingsIn);
	}

	public final List<String> getMembersList() {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) ObjectCache.INSTANCE
				.get(MEMBERS_LIST);
		return list;
	}

	public final void setMembersList(List<String> membersIn) {
		ObjectCache.INSTANCE.put(MEMBERS_LIST, membersIn);
	}

	public final Map<String, OtfAccount> getAllAccountsMap() {
		@SuppressWarnings("unchecked")
		Map<String, OtfAccount> allAccounts = (Map<String, OtfAccount>) ObjectCache.INSTANCE
				.get(ACCOUNTS_LIST);
		return allAccounts;
	}

	public final void setAllAccountsMap(Map<String, OtfAccount> allAccountsIn) {
		ObjectCache.INSTANCE.put(ACCOUNTS_LIST, allAccountsIn);
	}

	public final Map<String, List<String>> getAppsMap() {
		@SuppressWarnings("unchecked")
		Map<String, List<String>> appsMap = (Map<String, List<String>>) ObjectCache.INSTANCE
				.get(APPS_MAP);
		return appsMap;
	}

	public final void setAppsMap(Map<String, List<String>> appsMapIn) {
		ObjectCache.INSTANCE.put(APPS_MAP, appsMapIn);
	}

	public final List<String> getAppsNotUserMemberList() {
		@SuppressWarnings("unchecked")
		List<String> appsNotUserMemberList = (List<String>) ObjectCache.INSTANCE
				.get(APPS_NOT_USER_MEM_LIST);
		return appsNotUserMemberList;
	}

	public final void setAppsNotUserMemberList(
			List<String> appsNotUserMemberListIn) {
		ObjectCache.INSTANCE.put(APPS_NOT_USER_MEM_LIST,
				appsNotUserMemberListIn);
	}

}
