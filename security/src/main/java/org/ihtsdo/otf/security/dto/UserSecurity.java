package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

	public static final String SETTINGS = "OTFSettings";

	// private List<String> members;
	// private Map<String, OtfAccount> allAccounts;
	// private final Map<String, OtfCustomFieldSetting> settings = new
	// HashMap<String, OtfCustomFieldSetting>();

	public final String getDefaultpw() {
		if (!stringOK(defaultpw)) {
			defaultpw = getSettings().get(OtfCustomFieldSetting.DEFPW).getVal()
					.trim();
		}
		return defaultpw;
	}

	public final void setDefaultpw(final String defaultpwIn) {
		defaultpw = defaultpwIn;
	}

	public final String getUsersApp() {
		if (!stringOK(usersApp)) {
			usersApp = getSettings().get(OtfCustomFieldSetting.USERS).getVal()
					.trim();
		}
		return usersApp;
	}

	public final void setUsersApp(final String usersAppIn) {
		usersApp = usersAppIn;
	}

	public final String getMembersApp() {
		if (!stringOK(membersApp)) {
			membersApp = getSettings().get(OtfCustomFieldSetting.MEMBERS)
					.getVal().trim();
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

	public final Map<String, OtfCustomFieldSetting> getSettings() {
		Map<String, OtfCustomFieldSetting> settings = getCachedListMaps()
				.getSettingsMap();
		if (settings == null || settings.size() == 0) {
			settings = new HashMap<String, OtfCustomFieldSetting>();
			// Get settings dir
			OtfDirectory setDirectory = getDirs().getDirByName(SETTINGS);
			// get settings group
			OtfGroup setgrp = setDirectory.getGroups().getGroupByName(SETTINGS);
			// getSettings
			List<OtfCustomField> setList = setgrp.getCustData().getSettings();
			for (OtfCustomField cf : setList) {
				OtfCustomFieldSetting cfSet = (OtfCustomFieldSetting) cf
						.getModel();
				settings.put(cfSet.getKey(), cfSet);
			}
			setSettings(settings);
		}
		// LOG.info("Settings = \n");
		// for (String key : settings.keySet()) {
		// LOG.info("Key = " + key);
		// LOG.info("Vals = " + settings.get(key));
		// }
		return settings;
	}

	public final void setSettings(Map<String, OtfCustomFieldSetting> settingsIn) {
		getCachedListMaps().setSettingsMap(settingsIn);
	}

	public final boolean stringOK(final String chk) {
		return chk != null && chk.length() > 0;
	}

	public final List<OtfGroup> getGroupsByAppName(String appname) {
		OtfApplication app = getApps().getAppByName(appname);
		return getGroupsByApp(app);
	}

	public final List<OtfGroup> getGroupsByApp(OtfApplication app) {
		List<OtfGroup> groups = new ArrayList<OtfGroup>();
		for (OtfAccountStore acs : app.getAccountStores().values()) {
			if (acs.isDir()) {
				String dirName = acs.getName();
				OtfDirectory dir = getDirs().getDirByName(dirName);

				for (OtfGroup grp : dir.getGroups().getGroups().values()) {
					grp.getId();
					groups.add(grp);
				}
			}
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

	public final List<String> getMembers() {
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

	public final OtfDirectory getMembersDir() {
		return getDirs().getDirByName(getMembersApp());
	}

	public final Collection<OtfAccount> getUsers(final String dirname) {
		if (dirname == null) {
			// Get members dir
			return getUsersbyDir(getUsersApp());
		}
		if (dirname.equals("*")) {
			return getAllUsers();
		}

		return getUsersbyDir(dirname);

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
		setAllAccounts(null);
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

	public final OtfCachedListsDTO getCachedListMaps() {
		return cachedListMaps;
	}

	public final void setCachedListMaps(OtfCachedListsDTO cachedListMapsIn) {
		cachedListMaps = cachedListMapsIn;
	}

}
