package org.ihtsdo.otf.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfCachedListsDTO;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;

public abstract class AbstractUserSecurityModel implements UserSecurityModel {

	private static final Logger LOG = Logger
			.getLogger(AbstractUserSecurityModel.class.getName());

	protected UserSecurity model;
	private HandlerAdmin handlerAdmin;
	private String usersApp;
	private String membersApp;
	private String adminApp;

	@Override
	public abstract UserSecurity getModel();

	@Override
	public void setModel(UserSecurity userSecurityIn) {
		model = userSecurityIn;
	}

	@Override
	public abstract UserSecurity getFullModel();

	@Override
	public abstract void buildFullModel();

	@Override
	public abstract void buildModel();

	@Override
	public abstract OtfAccount getUserAccountByName(final String name);

	@Override
	public final String getAdminApp() {
		if (!stringOK(adminApp)) {
			if (getSettings() != null) {
				adminApp = getSettings().getSettings()
						.get(OtfCustomFieldSetting.ADMIN).getVal().trim();
			}
		}
		return adminApp;
	}

	@Override
	public final void setAdminApp(final String adminAppIn) {
		adminApp = adminAppIn;
	}

	@Override
	public final String getUsersApp() {
		if (!stringOK(usersApp)) {
			usersApp = getSettings().getSettings()
					.get(OtfCustomFieldSetting.USERS).getVal().trim();
		}
		return usersApp;
	}

	@Override
	public final void setUsersApp(final String usersAppIn) {
		usersApp = usersAppIn;
	}

	@Override
	public final String getMembersApp() {
		if (!stringOK(membersApp)) {
			membersApp = getSettings().getSettings()
					.get(OtfCustomFieldSetting.MEMBERS).getVal().trim();
		}
		return membersApp;
	}

	@Override
	public final void setMembersApp(final String membersAppIn) {
		membersApp = membersAppIn;
	}

	public final boolean stringOK(final String chk) {
		return chk != null && chk.length() > 0;
	}

	@Override
	public final Collection<OtfAccountMin> getUsersMin() {
		Collection<OtfAccount> users = getUsers();
		Collection<OtfAccountMin> usersMin = new ArrayList<OtfAccountMin>();
		if (users != null) {
			for (OtfAccount user : users) {
				usersMin.add((new OtfAccountMin(user)));
			}
		}
		return usersMin;
	}

	@Override
	public final void setHandlerAdmin(final HandlerAdmin handlerAdminIn) {
		handlerAdmin = handlerAdminIn;
	}

	@Override
	public final HandlerAdmin getHandlerAdmin() {
		if (handlerAdmin == null) {
			handlerAdmin = new HandlerAdmin();
		}
		return handlerAdmin;
	}

	@Override
	public List<String> getAppsNotAdmin() {
		List<String> appsNotAdminList = new ArrayList<String>();
		Collection<String> all = getApps();
		if (all != null && !all.isEmpty()) {
			String users = getUsersApp();
			String members = getMembersApp();
			String adminhandler = getHandlerAdmin().getAppName();
			List<String> adminApps = new ArrayList<String>();
			if (stringOK(users)) {
				adminApps.add(users);
			}
			if (stringOK(members)) {
				adminApps.add(members);
			}
			if (stringOK(adminhandler)) {
				adminApps.add(adminhandler);
			}

			for (String appname : all) {
				if (!adminApps.contains(appname)) {
					appsNotAdminList.add(appname);
				}
			}

		}
		OtfCachedListsDTO.setAppsNotAdminList(appsNotAdminList);
		return appsNotAdminList;
	}

	public final Map<String, OtfAccount> getAdminUsersMap() {
		Map<String, OtfAccount> adminUsers = new HashMap<String, OtfAccount>();
		String adminA = getAdminApp();
		String admindir = getHandlerAdmin().getDirName();

		boolean adminAok = stringOK(adminA);
		boolean admindirok = stringOK(admindir);

		for (OtfAccount acc : getUsers()) {
			// see if parent dir =
			if (admindirok && acc.getParentDir().equalsIgnoreCase(admindir)) {
				adminUsers.put(acc.getName(), acc);
			}
			if (adminAok) {
				if (acc.getCustData() != null) {
					List<OtfCustomField> apps = acc.getCustData().getApps();
					for (OtfCustomField app : apps) {
						OtfCustomFieldApplication oca = (OtfCustomFieldApplication) app
								.getModel();
						if (oca.getApp().equals(adminA)) {
							adminUsers.put(acc.getName(), acc);
						}
					}
				}
			}

		}
		return adminUsers;

	}

	@Override
	public List<String> getAdminUsers() {
		List<String> adminUsers = new ArrayList<String>();
		Map<String, OtfAccount> adminUserMap = getAdminUsersMap();
		if (adminUserMap != null && !adminUserMap.isEmpty()) {
			adminUsers.addAll(adminUserMap.keySet());
		}
		Collections.sort(adminUsers, String.CASE_INSENSITIVE_ORDER);
		return adminUsers;
	}

	@Override
	public final boolean accountExists(final String accName) {
		for (String name : getUserNames()) {
			if (name.equalsIgnoreCase(accName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final String getUsersDirName() {
		return getUsersApp();
	}

	public final OtfSettings getSettings(final OtfDirectory setDirectoryIn) {

		if (setDirectoryIn != null) {
			// get settings group
			OtfGroup setgrp = setDirectoryIn.getGroups().getGroupByName(
					UserSecurity.SETTINGS);
			setgrp.setParentDirName(UserSecurity.SETTINGS);
			OtfSettings settings = new OtfSettings(setgrp);

			getModel().setDefaultpw(settings.getDefPw());
			getModel().getDefaultpw();
			OtfCachedListsDTO.setSettings(settings);
			LOG.info("Get settings defpw set to " + getModel().getDefaultpw());
			return settings;
		}
		return null;
	}

	@Override
	public final void resetSettings() {
		OtfCachedListsDTO.remSettings();
		getSettings();
	}

	@Override
	public List<String> getMembers() {
		List<String> members = OtfCachedListsDTO.getMembersList();
		if (members == null || members.size() == 0
				|| OtfCachedListsDTO.updatecache()) {
			members = new ArrayList<String>();
			OtfDirectory mDirectory = getMembersDir();
			if (mDirectory != null) {
				// getAll groups within
				for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
					members.add(grp.getName());
				}
			}
			Collections.sort(members);
			OtfCachedListsDTO.setMembersList(members);
		}
		return members;
	}

	@Override
	public final OtfGroup getMemberByName(final String memberNameIn) {
		OtfDirectory mDirectory = getMembersDir();
		if (mDirectory != null) {
			for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
				if (grp.getName().equals(memberNameIn)) {
					return grp;
				}
			}
		}
		return null;
	}

	@Override
	public final OtfGroup getMemberById(final String idIn) {
		OtfDirectory mDirectory = getMembersDir();
		if (mDirectory != null) {
			for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
				if (grp.getIdIfSet().equals(idIn)) {
					return grp;
				}
			}
		}
		return null;
	}

	public final List<OtfGroup> getOtfGroupsByOtfDir(final OtfDirectory odirIn) {
		List<OtfGroup> groups = new ArrayList<OtfGroup>();
		String dname = odirIn.getName();
		for (OtfGroup grp : odirIn.getGroups().getGroups().values()) {
			grp.getId();
			grp.setParentDirName(dname);
			groups.add(grp);
		}
		return groups;
	}

	@Override
	public final void resetAllAccounts() {
		OtfCachedListsDTO.remAllAccountsMap();
		getAllAccounts();
	}

	@Override
	public final Collection<OtfAccount> getUsers() {
		return getAllAccounts().values();
	}

	@Override
	public final List<String> getUserNames() {
		return new ArrayList<String>(getAllAccounts().keySet());
	}

}
