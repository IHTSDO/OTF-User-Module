package org.ihtsdo.otf.security.stormpath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityModel;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfCachedListsDTO;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.UserSecurityModelCached;

public class StormpathUserSecurityModel extends AbstractUserSecurityModel {

	private static final Logger LOG = Logger
			.getLogger(UserSecurityModelCached.class.getName());

	private UserSecurityStormpath localModel;
	private final StormPathBaseDTO spbd;
	private Storm2Model storm2Mod;

	public StormpathUserSecurityModel(final UserSecurityStormpath localModelIn,
			final StormPathBaseDTO spbdIn) {
		super();
		localModel = localModelIn;
		spbd = spbdIn;
	}

	public StormpathUserSecurityModel(final StormPathBaseDTO spbdIn) {
		super();
		spbd = spbdIn;
	}

	@Override
	public final void init() {
		LOG.info("Init tenant = " + spbd.getTenant().getName());
	}

	@Override
	public final void reset() {
		spbd.resetTenant();
		setStorm2Mod(null);
		getStorm2Mod();
	}

	@Override
	public final UserSecurity getModel() {
		if (model == null) {
			model = new UserSecurityStormpath();
		}
		return model;
	}

	@Override
	public final UserSecurity getFullModel() {
		return getModel();
	}

	@Override
	public void buildFullModel() {

	}

	private UserSecurityStormpath getLocalModel() {
		if (localModel == null) {
			if (getModel() != null
					&& (getModel() instanceof UserSecurityStormpath)) {
				localModel = (UserSecurityStormpath) model;
			}
		}
		return localModel;
	}

	@Override
	public void buildModel() {
		// TODO Auto-generated method stub
	}

	@Override
	public final OtfAccount getUserAccountById(final String idIn) {
		return getStorm2Mod().getOtfAccountById(idIn);
	}

	@Override
	public final OtfDirectory getMembersDir() {
		return getStorm2Mod().getOtfDirByName(getMembersApp());
	}

	@Override
	public final OtfDirectory getUsersDir() {
		return getStorm2Mod().getOtfDirByName(getUsersApp());
	}

	@Override
	public final OtfSettings getSettings() {

		OtfSettings settings = OtfCachedListsDTO.getSettings();
		if (settings == null) {
			OtfDirectory setDir = getStorm2Mod().getOtfDirByName(
					UserSecurity.SETTINGS);
			if (setDir != null) {
				settings = getSettings(setDir);
				OtfCachedListsDTO.setSettings(settings);
				return settings;
			}
		}
		return settings;
	}

	@Override
	public final OtfGroup getGroupById(final String idIn) {
		return getStorm2Mod().buildGroup(idIn);
	}

	@Override
	public final List<OtfGroup> getGroupsByAppName(final String appnameIn) {
		return getStorm2Mod().getGroupsByAppName(appnameIn);
	}

	@Override
	public final List<String> getDirsByAppName(final String appnameIn) {
		return getStorm2Mod().getDirsByAppName(appnameIn);
	}

	@Override
	public final OtfAccount getUserAccountByName(final String nameIn) {
		return getStorm2Mod().getOtfAccountByUsername(nameIn);
	}

	@Override
	public final OtfDirectory getDirByName(final String dirNameIn) {
		return getStorm2Mod().getOtfDirByName(dirNameIn);
	}

	@Override
	public final List<OtfGroup> getGroupsByDirName(final String dirnameIn) {
		OtfDirectory odir = getDirByName(dirnameIn);
		return getOtfGroupsByOtfDir(odir);
	}

	@Override
	public final List<String> getApps() {
		// List<String> apps = getStorm2Mod().getApps();
		List<String> apps = new ArrayList<String>(getAppsMap().keySet());
		OtfCachedListsDTO.setAppNamesList(apps);
		return apps;
	}

	public final StormPathBaseDTO getSpbd() {
		return spbd;
	}

	public final Storm2Model getStorm2Mod() {
		if (storm2Mod == null) {
			storm2Mod = new Storm2Model(spbd);
		}
		return storm2Mod;
	}

	public final void setStorm2Mod(final Storm2Model storm2ModIn) {
		storm2Mod = storm2ModIn;
	}

	@Override
	public final OtfApplication getAppbyName(final String appNameIn) {
		return getStorm2Mod().getOtfAppbyName(appNameIn);
	}

	@Override
	public final boolean appExists(final String appnameIn) {
		return getStorm2Mod().appExists(appnameIn);
	}

	@Override
	public final boolean dirExists(final String dirnameIn) {
		return getStorm2Mod().dirExists(dirnameIn);
	}

	@Override
	public final Collection<OtfApplication> getOtfApps() {
		return getStorm2Mod().getOtfApps();
	}

	@Override
	public final Collection<OtfDirectory> getOtfDirs() {
		return getStorm2Mod().getOtfDirList();
	}

	@Override
	public final OtfApplication getAppById(final String idIn) {
		return getStorm2Mod().buildApp(idIn);
	}

	@Override
	public final OtfDirectory getDirById(final String idIn) {
		return getStorm2Mod().buildDirectory(idIn);
	}

	@Override
	public final Map<String, List<String>> getAppsMap() {

		Map<String, List<String>> appsMap = OtfCachedListsDTO.getAppsMap();
		if (appsMap == null || appsMap.size() == 0
				|| OtfCachedListsDTO.updatecache()) {
			reset();
			appsMap = getStorm2Mod().getAppsMap();

			// add user admin even if only dir
			String useradminapp = getAdminApp();

			if (!appsMap.containsKey(useradminapp)) {
				if (getDirsMap().containsKey(useradminapp)) {
					appsMap.put(useradminapp, getDirsMap().get(useradminapp));
				}
			}

			OtfCachedListsDTO.setAppsMap(appsMap);
		}
		return appsMap;
	}

	@Override
	public final Map<String, List<String>> getDirsMap() {
		Map<String, List<String>> dirsMap = OtfCachedListsDTO.getDirsMap();
		if (dirsMap == null || dirsMap.size() == 0
				|| OtfCachedListsDTO.updatecache()) {
			reset();
			dirsMap = getStorm2Mod().getDirsMap();
			OtfCachedListsDTO.setDirsMap(dirsMap);
		}
		return dirsMap;
	}

	@Override
	public void setUsersToken(String userNameIn, String tokenIn) {
		// Don't do anything as everything is remote.

	}

	@Override
	public final Map<String, OtfAccount> getAllAccounts() {
		Map<String, OtfAccount> allAccounts = OtfCachedListsDTO
				.getAllAccountsMap();

		if (allAccounts == null || allAccounts.size() == 0
				|| OtfCachedListsDTO.updatecache()) {
			reset();
			allAccounts = new HashMap<String, OtfAccount>();
			for (OtfAccount oacc : getStorm2Mod().getOtfAccounts()) {
				allAccounts.put(oacc.getName(), oacc);
			}
			OtfCachedListsDTO.setAllAccountsMap(allAccounts);
		}
		return allAccounts;
	}
}
