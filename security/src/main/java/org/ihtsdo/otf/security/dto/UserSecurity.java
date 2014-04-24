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
					// LOG.info("adding memeber : " + grp.getName());
				}
			}
			Collections.sort(members);
		}

		return members;
	}

	public final OtfDirectory getMembersDir() {
		return getDirs().getDirByName(getMembersApp());
	}

	public final Collection<OtfAccount> getUsers() {
		// Get members dir
		OtfDirectory uDirectory = getDirs().getDirByName(getUsersApp());
		if (uDirectory != null) {
			return uDirectory.getAllAccounts().values();

		}
		return null;
	}

	public final Collection<OftAccountMin> getMinUsers() {
		Collection<OtfAccount> users = getUsers();
		Collection<OftAccountMin> usersMin = new ArrayList<OftAccountMin>();
		if (users != null) {
			for (OtfAccount user : users) {
				usersMin.add((new OftAccountMin(user)));
			}
		}
		return usersMin;
	}

	public final OtfAccount getUserAccountByName(final String name) {
		for (OtfAccount acc : getUsers()) {
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

}
