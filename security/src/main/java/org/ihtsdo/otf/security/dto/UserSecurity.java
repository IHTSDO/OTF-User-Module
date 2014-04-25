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

	private OtfDirectories dirs = new OtfDirectories();
	private OtfApplications apps = new OtfApplications();
	private String defaultpw;
	private String usersApp;
	private String membersApp;
	private final Map<String, OtfCustomFieldSetting> settings = new HashMap<String, OtfCustomFieldSetting>();

	public static final String SETTINGS = "OTFSettings";

	private List<String> members;

	private Map<String, OtfAccount> allAccounts;

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
		if (settings.size() == 0) {
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
		}
		// LOG.info("Settings = \n");
		// for (String key : settings.keySet()) {
		// LOG.info("Key = " + key);
		// LOG.info("Vals = " + settings.get(key));
		//
		// }
		return settings;
	}

	public final boolean stringOK(final String chk) {
		return chk != null && chk.length() > 0;
	}

	public final List<String> getMembers() {

		if (members == null) {
			members = new ArrayList<String>();
		}

		if (members.size() == 0) {
			// Get members dir
			OtfDirectory mDirectory = getMembersDir();
			if (mDirectory != null) {
				// getAll groups within
				for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
					members.add(grp.getName());
				}
			}
			Collections.sort(members);
		}

		return members;
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

	public final Collection<OftAccountMin> getMinUsers(final String dirName) {
		Collection<OtfAccount> users = getUsers(dirName);
		Collection<OftAccountMin> usersMin = new ArrayList<OftAccountMin>();
		if (users != null) {
			for (OtfAccount user : users) {
				usersMin.add((new OftAccountMin(user)));
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

		}
		return allAccounts;
	}

	public final void setAllAccounts(final Map<String, OtfAccount> allAccountsIn) {
		allAccounts = allAccountsIn;
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

}
