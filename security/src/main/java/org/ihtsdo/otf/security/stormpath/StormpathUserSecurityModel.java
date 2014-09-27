package org.ihtsdo.otf.security.stormpath;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityModel;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.UserSecurityCached;
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
	public void init() {
	}

	@Override
	public void reset() {
	}

	@Override
	public final UserSecurity getModel() {
		if (model == null) {
			model = new UserSecurityStormpath();
		}
		return null;
	}

	@Override
	public final UserSecurity getFullModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildFullModel() {
		// TODO Auto-generated method stub

	}

	private UserSecurityStormpath getLocalModel() {
		if (localModel == null) {
			if (getModel() != null
					&& (getModel() instanceof UserSecurityCached)) {
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
	public final Collection<OtfAccount> getUsers() {
		return getStorm2Mod().getOtfAccounts();
	}

	// @Override
	// public final List<String> getAdminUsers() {
	// String ucAdminDir = null;
	// if (stringOK(getHandlerAdminDir())) {
	// ucAdminDir = getHandlerAdminDir().toUpperCase();
	// }
	// //
	// String adminA = getSettings().getAdmin();
	// // String adminA = "test";
	//
	// // add get dir to OtfAccount & use that
	//
	// // move to
	//
	// if (stringOK(adminA)) {
	// for (OtfAccount acc : getUsers()) {
	// // acc.get
	//
	// }
	// }
	//
	// return null;
	// }

	@Override
	public final String getDirNameForUser(final String accNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String getAppNameForUser(final String accNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String getUsersDirName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfDirectory getMembersDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfDirectory getUsersDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfSettings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<String> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfGroup getMemberByName(final String accNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfGroup getGroupById(final String idIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfGroup getMemberById(final String idIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Map<String, List<String>> getAppsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<OtfGroup> getGroupsByAppName(final String appnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfAccount getUserAccountByName(final String nameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<String> getDirsByAppName(final String appnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final OtfDirectory getDirByName(final String dirNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<String> getAppsNotAdmin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<OtfGroup> getGroupsByDirName(final String dirnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<String> getUserNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final List<String> getApps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Map<String, OtfAccount> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
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

}
