package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	private final HashMap<String, OtfCustomFieldSetting> settings = new HashMap<String, OtfCustomFieldSetting>();

	public static final String SETTINGS = "OTFSettings";

	private List<String> members;

	public String getDefaultpw() {
		if (!stringOK(defaultpw)) {
			defaultpw = getSettings().get(OtfCustomFieldSetting.DEFPW).getVal()
					.trim();
		}
		return defaultpw;
	}

	public void setDefaultpw(String defaultpwIn) {
		defaultpw = defaultpwIn;
	}

	public String getUsersApp() {
		if (!stringOK(usersApp)) {
			usersApp = getSettings().get(OtfCustomFieldSetting.USERS).getVal()
					.trim();
		}
		return usersApp;
	}

	public void setUsersApp(String usersAppIn) {
		usersApp = usersAppIn;
	}

	public String getMembersApp() {
		if (!stringOK(membersApp)) {
			membersApp = getSettings().get(OtfCustomFieldSetting.MEMBERS)
					.getVal().trim();
		}
		return membersApp;
	}

	public void setMembersApp(String membersAppIn) {
		membersApp = membersAppIn;
	}

	public OtfApplications getApps() {
		return apps;
	}

	public void setApps(OtfApplications appsIn) {
		apps = appsIn;
	}

	public OtfDirectories getDirs() {
		return dirs;
	}

	public void setDirs(OtfDirectories dirsIn) {
		dirs = dirsIn;
	}

	public HashMap<String, OtfCustomFieldSetting> getSettings() {
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

	public boolean stringOK(String chk) {
		return chk != null && chk.length() > 0;
	}

	public List<String> getMembers() {

		if (members == null) {
			members = new ArrayList<String>();
		}

		if (members.size() == 0) {
			// Get members dir
			OtfDirectory mDirectory = getDirs().getDirByName(getMembersApp());
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

	public Collection<OtfAccount> getUsers() {
		// Get members dir
		OtfDirectory uDirectory = getDirs().getDirByName(getUsersApp());
		if (uDirectory != null) {
			return uDirectory.getAllAccounts().values();

		}
		return null;
	}

	public OtfAccount getUserAccountByName(String name) {
		for (OtfAccount acc : getUsers()) {
			if (acc.getName().equalsIgnoreCase(name)) {
				return acc;
			}
		}
		return null;
	}

}
