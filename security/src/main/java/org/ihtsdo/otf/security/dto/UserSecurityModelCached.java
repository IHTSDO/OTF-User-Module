package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityModel;

public class UserSecurityModelCached extends AbstractUserSecurityModel {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(UserSecurityModelCached.class.getName());

	private UserSecurityCached localModel;

	@Override
	public final void init() {
		initCachedValues();
	}

	@Override
	public final void reset() {
		resetAllCachedValues();
	}

	public final void initCachedValues() {

		getSettings();
		getAllAccounts();
		getDirsMap();
		getAppsMap();

		if (getSettings() != null && !getSettings().getSettings().isEmpty()) {
			getMembers();
			getAppsNotAdmin();
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

	@Override
	public final UserSecurity getModel() {
		if (model == null) {
			model = new UserSecurityCached();
		}
		// LOG.info("Model = " + model);
		return model;
	}

	@Override
	public final UserSecurity getFullModel() {
		return getModel();
	}

	private UserSecurityCached getLocalModel() {
		// LOG.info("getLocalModel localModel = " + localModel);
		if (localModel == null) {
			if (getModel() != null
					&& (getModel() instanceof UserSecurityCached)) {
				localModel = (UserSecurityCached) model;
			}
		}
		return localModel;
	}

	@Override
	public final void buildFullModel() {
		buildModel();

	}

	@Override
	public void buildModel() {
		// Not necessary with Cached as always full model
	}

	@Override
	public final OtfAccount getUserAccountById(final String idIn) {
		for (OtfAccount acc : getUsers()) {
			if (acc.getId().equalsIgnoreCase(idIn)) {
				return acc;
			}
		}
		return null;
	}

	@Override
	public final List<String> getAdminUsers() {
		List<String> adminUsers = OtfCachedListsDTO.getAdminUsersList();
		if (adminUsers == null) {
			adminUsers = super.getAdminUsers();
			setAdminUsers(adminUsers);
		}
		return adminUsers;
	}

	public final void setAdminUsers(final List<String> listIn) {
		OtfCachedListsDTO.setAdminUsersList(listIn);
	}

	public final void resetAdminUsers() {
		OtfCachedListsDTO.remAdminUsersList();
		getAdminUsers();
	}

	// @Override
	// public final String getDirNameForUser(final String accNameIn) {
	// Collection<String> dirnames = getDirNamesForUser(accNameIn);
	// if (dirnames != null && dirnames.size() > 0) {
	// return dirnames.iterator().next();
	// }
	// return null;
	//
	// }

	// @Override
	// public final String getAppNameForUser(final String accNameIn) {
	// return getFirstAppForUser(accNameIn);
	// }

	public final String getFirstAppForUser(final String username) {
		List<String> appsList = getOtfAppNamesForUser(username);
		Collections.sort(appsList);
		if (appsList.isEmpty()) {
			return null;
		}
		return appsList.get(0);
	}

	public final List<String> getOtfAppNamesForUser(final String username) {
		ArrayList<String> appsList = new ArrayList<String>();
		Collection<String> dirsforUser = getDirNamesForUser(username);

		for (OtfApplication app : getLocalModel().getApps().getApplications()
				.values()) {
			for (String key : app.getAccountStores().keySet()) {
				if (dirsforUser.contains(key)) {
					appsList.add(app.getName());
				}
			}
		}
		return appsList;
	}

	public final Collection<String> getDirNamesForUser(final String username) {
		ArrayList<String> dirs = new ArrayList<String>();

		for (OtfDirectory dir : getLocalModel().getDirs().getDirectories()
				.values()) {
			if (!dir.getAccounts().getAccounts().isEmpty()) {
				for (OtfAccount user : dir.getAccounts().getAccounts().values()) {
					if (user.getName().equals(username)) {
						dirs.add(dir.getName());
					}
				}
			}
		}
		return dirs;
	}

	@Override
	public final OtfDirectory getMembersDir() {
		return getLocalModel().getDirs().getDirByName(getMembersApp());
	}

	@Override
	public final OtfDirectory getUsersDir() {
		return getLocalModel().getDirs().getDirByName(getUsersApp());
	}

	@Override
	public final List<String> getMembers() {
		List<String> members = OtfCachedListsDTO.getMembersList();
		if (members == null) {
			members = super.getMembers();
		}
		return members;
	}

	public final void resetMembers() {
		OtfCachedListsDTO.remMembersList();
		getMembers();
	}

	@Override
	public final OtfSettings getSettings() {
		OtfSettings settings = OtfCachedListsDTO.getSettings();
		if (settings == null) {
			// Get settings dir
			OtfDirectory setDirectory = getLocalModel().getDirs().getDirByName(
					UserSecurity.SETTINGS);
			settings = getSettings(setDirectory);
		}
		return settings;
	}

	@Override
	public final OtfGroup getGroupById(final String idIn) {
		for (OtfDirectory mDirectory : getLocalModel().getDirs()
				.getDirectories().values()) {
			if (mDirectory != null) {
				for (OtfGroup grp : mDirectory.getGroups().getGroups().values()) {
					if (grp.getIdIfSet().equals(idIn)) {
						return grp;
					}
				}
			}
		}
		return null;
	}

	@Override
	public final List<String> getApps() {
		List<String> apps = OtfCachedListsDTO.getAppNamesList();
		if (apps == null || apps.size() == 0) {
			apps = new ArrayList<String>(getAppsMap().keySet());
			OtfCachedListsDTO.setAppNamesList(apps);
		}

		return apps;
	}

	@Override
	public final Map<String, List<String>> getAppsMap() {
		Map<String, List<String>> appsMap = OtfCachedListsDTO.getAppsMap();
		if (appsMap == null || appsMap.size() == 0) {
			appsMap = new HashMap<String, List<String>>();
			for (OtfApplication app : getLocalModel().getApps()
					.getApplications().values()) {
				List<String> grpNames = new ArrayList<String>();
				List<OtfGroup> grps = getGroupsByApp(app);
				for (OtfGroup grp : grps) {
					grpNames.add(grp.getName());
				}
				Collections.sort(grpNames);
				appsMap.put(app.getName(), grpNames);
			}

			// add user admin even if only dir
			String useradminapp = getAdminApp();

			if (!appsMap.containsKey(useradminapp)) {
				if (getDirsMap().containsKey(useradminapp)) {
					appsMap.put(useradminapp, getDirsMap().get(useradminapp));
				}
			}
			setAppsMap(appsMap);
		}

		return appsMap;
	}

	public final void setAppsMap(final Map<String, List<String>> appsMap) {
		OtfCachedListsDTO.setAppsMap(appsMap);
	}

	public final void resetAppsMap() {
		OtfCachedListsDTO.remAppsMap();
		getAppsMap();
	}

	@Override
	public final Map<String, List<String>> getDirsMap() {
		Map<String, List<String>> dirsMap = OtfCachedListsDTO.getDirsMap();
		if (dirsMap == null || dirsMap.isEmpty()) {
			dirsMap = new HashMap<String, List<String>>();
			for (String dirName : getLocalModel().getDirs().getDirectories()
					.keySet()) {
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

	public final void setDirsMap(final Map<String, List<String>> dirsMap) {
		OtfCachedListsDTO.setDirsMap(dirsMap);
	}

	public final void resetDirsMap() {
		OtfCachedListsDTO.remDirsMap();
		getDirsMap();
	}

	@Override
	public final List<OtfGroup> getGroupsByAppName(final String appnameIn) {
		OtfApplication app = getLocalModel().getApps().getAppByName(appnameIn);
		return getGroupsByApp(app);
	}

	public final List<OtfGroup> getGroupsByApp(final OtfApplication app) {
		List<OtfGroup> groups = new ArrayList<OtfGroup>();
		for (OtfAccountStore acs : app.getAccountStores().values()) {
			if (acs.isDir()) {
				String dirName = acs.getName();
				groups.addAll(getGroupsByDirName(dirName));
			}
		}
		return groups;
	}

	@Override
	public final List<OtfGroup> getGroupsByDirName(final String dirNameIn) {
		OtfDirectory dir = getLocalModel().getDirs().getDirByName(dirNameIn);
		return getOtfGroupsByOtfDir(dir);
	}

	@Override
	public final OtfAccount getUserAccountByName(final String nameIn) {
		for (OtfAccount acc : getUsers()) {
			if (acc.getName().equalsIgnoreCase(nameIn)) {
				return acc;
			}
		}
		return null;
	}

	@Override
	public final List<String> getDirsByAppName(final String appnameIn) {
		List<String> dirs = new ArrayList<String>();
		OtfApplication oapp = getLocalModel().getApps().getAppByName(appnameIn);
		if (oapp != null) {
			Map<String, OtfAccountStore> acsmap = oapp.getAccountStores();
			if (acsmap != null && acsmap.size() > 0) {
				for (OtfAccountStore acs : acsmap.values()) {
					if (acs.isDir()) {
						dirs.add(acs.getName());
					}
				}
			}
		}

		return dirs;
	}

	@Override
	public final OtfDirectory getDirByName(final String dirNameIn) {
		return getLocalModel().getDirs().getDirByName(dirNameIn);
	}

	@Override
	public final List<String> getAppsNotAdmin() {
		List<String> appsNotAdmin = OtfCachedListsDTO.getAppsNotAdminList();
		if (appsNotAdmin == null || appsNotAdmin.isEmpty()) {
			appsNotAdmin = super.getAppsNotAdmin();
		}
		return appsNotAdmin;
	}

	public final void resetAppsNotMembersOrUsers() {
		OtfCachedListsDTO.remAppsNotAdminList();
		getAppsNotAdmin();
	}

	@Override
	public final Map<String, OtfAccount> getAllAccounts() {
		Map<String, OtfAccount> allAccounts = OtfCachedListsDTO
				.getAllAccountsMap();
		if (allAccounts == null || allAccounts.size() == 0) {
			allAccounts = new HashMap<String, OtfAccount>();
			for (OtfDirectory dir : getModel().getDirs().getDirectories()
					.values()) {
				if (!dir.getAccounts().getAccounts().isEmpty()) {
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
		OtfCachedListsDTO.setAllAccountsMap(allAccountsIn);
	}

	@Override
	public OtfApplication getAppbyName(String appNameIn) {
		for (OtfApplication app : getLocalModel().getApps().getApplications()
				.values()) {
			if (app.getName().equals(appNameIn)) {
				return app;
			}
		}
		return null;
	}

	// from model

	@Override
	public final boolean appExists(final String appname) {
		return getModel().getApps().appExists(appname);
	}

	@Override
	public final boolean dirExists(final String appname) {
		return getModel().getDirs().dirExists(appname);
	}

	@Override
	public final Collection<OtfApplication> getOtfApps() {
		return getModel().getApps().getApplications().values();
	}

	@Override
	public final Collection<OtfDirectory> getOtfDirs() {
		return getModel().getDirs().getDirectories().values();
	}

	@Override
	public final OtfApplication getAppById(final String idIn) {
		return getModel().getApps().getAppById(idIn);
	}

	@Override
	public final OtfDirectory getDirById(final String idIn) {
		return getModel().getDirs().getDirById(idIn);
	}

	@Override
	public final void setUsersToken(final String userNameIn,
			final String tokenIn) {
		OtfAccount oacc2 = getUserAccountByName(userNameIn);
		if (oacc2 != null) {
			oacc2.setAuthToken(tokenIn);
		}

	}

}
