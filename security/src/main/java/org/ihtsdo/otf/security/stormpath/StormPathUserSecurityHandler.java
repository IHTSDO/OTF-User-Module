package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.UserSecurityModel;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.UserSecurityModelCached;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;

public class StormPathUserSecurityHandler extends AbstractUserSecurityHandler {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(StormPathUserSecurityHandler.class.getName());

	private String usersAppName = "OTF Users";

	private StormPathBaseDTO spbd;

	public static final String STORMPATH = "Stormpath";

	private Properties props;

	private boolean cacheModel = true;

	private Storm2Model storm2Mod;
	private Model2Storm mod2Storm;

	public StormPathUserSecurityHandler() {
		super();
	}

	public StormPathUserSecurityHandler(final Properties propsIn) {
		super();
		try {
			init(propsIn);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	@Override
	public final void init(final Properties propsIn) throws Exception {
		setProps(propsIn);
		buildUserSecurity();
	}

	public final void clearSP() throws Exception {
		if (spbd == null) {
			spbd = new StormPathBaseDTO(props);
			spbd.load();
		}
		getMod2Storm().sendToStormPath(null);
	}

	@Override
	public final void buildUserSecurity() throws Exception {
		if (spbd == null) {
			spbd = new StormPathBaseDTO(props);
			spbd.load();

		}
		if (isCacheModel()) {
			UserSecurity us = getStorm2Mod().buildCachedUserSecurity();
			getUserSecurityModel(us, STORMPATH);
		}
		if (!isCacheModel()) {
			UserSecurity us = new UserSecurityStormpath();
			getUserSecurityModel(us, STORMPATH);
		}

	}

	public final void sendUserSecuritytoStormPath(
			final UserSecurity userSecurityIn) throws Exception {
		if (userSecurityIn != null) {
			getUserSecurityModel().setModel(userSecurityIn);
		}
		if (spbd == null) {
			spbd = new StormPathBaseDTO(props);
			spbd.load();
		}
		// Make sure settings inited by called defpw
		getUserSecurityModel().getSettings().getDefPw();
		getMod2Storm().sendToStormPath(getUserSecurityModel().getModel());
	}

	@Override
	public void saveUserSecurity() throws Exception {
	}

	public final String getUsersAppName() {
		if (!stringOK(usersAppName)) {
			usersAppName = getUserSecurityModel().getSettings().getUsers();
		}
		return usersAppName;
	}

	public final void setUsersAppName(final String usersAppNameIn) {
		usersAppName = usersAppNameIn;
	}

	public final StormPathBaseDTO getSpbd() {
		return spbd;
	}

	public final void setSpbd(final StormPathBaseDTO spbdIn) {
		spbd = spbdIn;
	}

	@Override
	public final OtfAccount authAccountLocal(final String acNameIn,
			final String pwIn, final String tokenIn) {
		return getStorm2Mod().authAccountLocal(acNameIn, pwIn, tokenIn);
	}

	public final Properties getProps() {
		return props;
	}

	public final void setProps(final Properties propsIn) {
		props = propsIn;
	}

	@Override
	public final void localReload() {
		spbd = null;
		storm2Mod = null;
		mod2Storm = null;
	}

	@Override
	public final String addUpdateMemberLocal(final OtfGroup grpIn,
			final OtfDirectory mDirectoryIn, final boolean isNewIn) {
		Directory mdir = getStorm2Mod().getDirByName(mDirectoryIn.getName());
		if (mdir == null) {
			return DIR_NOT_FOUND;
		}
		if (isNewIn) {
			getMod2Storm().buildGroup(grpIn, null, mdir);
		} else {
			Group remoteG = getStorm2Mod().getGrpByName(grpIn.getName(), mdir);
			getMod2Storm().buildGroup(grpIn, remoteG, mdir);
		}
		return REMOTE_COMMIT_OK;
	}

	@Override
	public final String addUpdateAppLocal(final OtfApplication appIn,
			final boolean isNewIn) {
		// Remember to create a new dir? Before adding app
		if (isNewIn) {
			getMod2Storm().buildApp(appIn, null);
		} else {
			Application app = getStorm2Mod().getAppByName(appIn.getName());
			getMod2Storm().buildApp(appIn, app);
		}
		return REMOTE_COMMIT_OK;
	}

	@Override
	public final String addUpdateAccountLocal(final OtfAccount accIn,
			final String parentDirNameIn, final boolean isNewIn) {
		Directory mdir = getStorm2Mod().getDirByName(parentDirNameIn);
		if (mdir == null) {
			return DIR_NOT_FOUND;
		}
		if (!stringOK(accIn.getGivenName())) {
			accIn.setGivenName(accIn.getName());
		}
		if (!stringOK(accIn.getSurname())) {
			accIn.setSurname("SurnameNotSet");
		}

		if (isNewIn) {
			getMod2Storm().buildAccount(accIn, null, mdir);
			if (parentDirNameIn != null) {
				OtfDirectory parentIn = getUserSecurityModel().getDirByName(
						parentDirNameIn);
				parentIn.getAccounts().getAccounts()
						.put(accIn.getName(), accIn);
			}
		} else {
			Account acc = getStorm2Mod()
					.getAccountByName(accIn.getName(), mdir);
			getMod2Storm().buildAccount(accIn, acc, mdir);
		}
		return REMOTE_COMMIT_OK;
	}

	@Override
	public final String addUpdateGroupLocal(final OtfGroup grpIn,
			final OtfDirectory mDirectoryIn, final boolean isNewIn) {
		Directory dir = getStorm2Mod().getDirByName(mDirectoryIn.getName());
		if (dir == null) {
			return DIR_NOT_FOUND;
		}
		if (isNewIn) {
			getMod2Storm().buildGroup(grpIn, null, dir);
		} else {
			Group grp = getStorm2Mod().getGrpByName(grpIn.getName(), dir);
			getMod2Storm().buildGroup(grpIn, grp, dir);
		}
		return REMOTE_COMMIT_OK;
	}

	@Override
	public final String addUpdateDirLocal(final OtfDirectory dirIn,
			final boolean isNewIn) {

		if (isNewIn) {
			getMod2Storm().buildDirectory(dirIn, null);
		} else {
			Directory dir = getStorm2Mod().getDirByName(dirIn.getName());
			getMod2Storm().buildDirectory(dirIn, dir);
		}
		return REMOTE_COMMIT_OK;
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

	public final Model2Storm getMod2Storm() {
		if (mod2Storm == null) {
			mod2Storm = new Model2Storm(spbd);
			mod2Storm.setUserSecurity(getUserSecurityModel().getModel());
		}
		return mod2Storm;
	}

	public final void setMod2Storm(final Model2Storm mod2StormIn) {
		mod2Storm = mod2StormIn;
	}

	@Override
	public final String requestUpdateUserPassword(final String userNameIn,
			final String emailAddrIn) {
		return getStorm2Mod()
				.requestUpdateUserPassword(userNameIn, emailAddrIn);
	}

	@Override
	public final int updateUserPassword(final String userNameIn,
			final String passwordIn, final String tokenIn) {
		return getStorm2Mod().updateUserPassword(userNameIn, passwordIn,
				tokenIn);
	}

	@Override
	public final UserSecurityModel getLocalUserSecurityModel() {
		if (isCacheModel()) {
			return new UserSecurityModelCached();
		} else {
			return new StormpathUserSecurityModel(getSpbd());
		}
	}

	public final boolean isCacheModel() {
		return cacheModel;
	}

	public final void setCacheModel(final boolean cacheModelIn) {
		cacheModel = cacheModelIn;
	}

	// public final Application getUsersApplication(String username) {
	// return getStorm2Mod().getApplicationByUser(username);
	// }

	// public final Account getAccountByUsername(String username) {

	// // See if the account is in the accList locally
	// if (OtfCachedListsDTO.getAllAccountsMap() != null) {
	// OtfAccount oacc = OtfCachedListsDTO.getAllAccountsMap().get(
	// username);
	// if (oacc != null) {
	// // get the href
	// String href = oacc.getIdref();
	// if (stringOK(href)) {
	// final Account acc = spbd.getResourceByHrefAccount(href);
	// if (acc != null) {
	// return acc;
	// }
	// }
	// }
	// }
	// // else....get from remote via apps
	// // Possibly use href is account is untrustworthy.
	// return getRemoteAccountByUser(username);
	// }

	// public final Application getApplicationByName(final String appName) {
	// ApplicationList applications = spbd.getTenant().getApplications();
	// for (Application application : applications) {
	// if (application.getName().equals(appName)) {
	// return application;
	// }
	// }
	// return null;
	// }

	// public final Account getRemoteAccountByUser(final String userName) {
	// ApplicationList applications = spbd.getTenant().getApplications();
	// for (Application application : applications) {
	// AccountList acc = application.getAccounts(Accounts.where(Accounts
	// .username().eqIgnoreCase(userName)));
	// if (acc.iterator().hasNext()) {
	// return acc.iterator().next();
	// }
	// }
	// return null;
	// }

	// public final Application getApplicationByUser(final String userName) {
	// ApplicationList applications = spbd.getTenant().getApplications();
	// for (Application application : applications) {
	// AccountList acc = application.getAccounts(Accounts.where(Accounts
	// .username().eqIgnoreCase(userName)));
	// if (acc.iterator().hasNext()) {
	// return application;
	// }
	// }
	//
	// return null;
	// }

	// public boolean userOrAdminApp(String appname) {
	// if (appname.equalsIgnoreCase(StormPathUserSecurityHandler.STORMPATH)) {
	// return true;
	// }
	// return appname.equalsIgnoreCase(getUsersAppName());
	// }

	// Get the f1st NOT SP app in case users need to be added to it.

}
