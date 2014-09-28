package org.ihtsdo.otf.security.stormpath;

import java.util.Collection;
import java.util.List;
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
	public final void init() {
		LOG.info("Init tenant = " + spbd.getTenant().getName());
	}

	@Override
	public final void reset() {
		spbd.resetTenant();
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
		OtfDirectory setDir = getStorm2Mod().getOtfDirByName(
				UserSecurity.SETTINGS);
		if (setDir != null) {
			return getSettings(setDir);
		}
		return null;
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
	public final List<String> getUserNames() {
		return getStorm2Mod().getUserNames();
	}

	@Override
	public final List<String> getApps() {
		return getStorm2Mod().getApps();
	}

	// @Override
	// public final Map<String, OtfAccount> getAllAccounts() {
	// // TODO Auto-generated method stub
	// return null;
	// }

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
