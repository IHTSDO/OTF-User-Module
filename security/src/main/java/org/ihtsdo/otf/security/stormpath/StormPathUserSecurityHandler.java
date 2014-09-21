package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.UserSecurityModel;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfCachedListsDTO;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.UserSecurityModelCached;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.account.Accounts;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeyList;
import com.stormpath.sdk.api.ApiKeyStatus;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.resource.ResourceException;

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
			// Don't build unless calling full build.
			// Then use the cached User Security impl
			// UserSecurity us = new UserSecurityCached();
			UserSecurity us = getStorm2Mod().buildCachedUserSecurity();
			us.setHandlerAdminDir(STORMPATH);
			getUserSecurityModel().setModel(us);
		}
		if (!isCacheModel()) {
			UserSecurity us = new UserSecurityStormpath();
			us.setHandlerAdminDir(STORMPATH);
			getUserSecurityModel().setModel(us);
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

		getMod2Storm().sendToStormPath(getUserSecurityModel().getModel());
	}

	@Override
	public void saveUserSecurity() throws Exception {
	}

	public final Application getFirstApplicationByUserName(final String userName) {

		String appname = getUserSecurityModel().getAppNameForUser(userName);
		if (stringOK(appname)) {
			return getApplicationByName(appname);
		}
		return null;
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

	public final OtfAccount getAcKeys(Account acc) {
		if (acc != null) {
			OtfAccount accIn = getStorm2Mod().buildAccount(acc);
			ApiKeyList apList = acc.getApiKeys();
			for (ApiKey apiKey : apList) {
				if (apiKey.getStatus().equals(ApiKeyStatus.ENABLED)) {
					accIn.setAuthToken(apiKey.getId());
				} else {
					accIn.addAuthToken(apiKey.getId());
				}
			}
			return accIn;
		}

		return null;
	}

	public final OtfAccount getResetAcKeys(Account acc) {
		if (acc != null) {
			OtfAccount accIn = getStorm2Mod().buildAccount(acc);
			ApiKeyList apList = acc.getApiKeys();
			int apikeyCount = 0;
			for (ApiKey ak : apList) {
				apikeyCount++;
			}

			if (apikeyCount == 0) {
				// add an apikey
				ApiKey apiKey = acc.createApiKey();
				accIn.setAuthToken(apiKey.getId());
			}
			// Stormpath admins are responsible for updating etc their own keys.
			// Consider having an "App user" whose key never changes/is manually
			// changed.
			if (!getApplicationByUser(acc.getUsername()).getName()
					.equalsIgnoreCase(STORMPATH)) {
				if (apikeyCount == 1) {
					// add an apikey
					ApiKey apiKey = acc.createApiKey();
					accIn.setAuthToken(apiKey.getId());
					// set old apikey to
					ApiKey apiKeyOld = apList.iterator().next();
					apiKeyOld.setStatus(ApiKeyStatus.DISABLED);
					apiKeyOld.save();
					accIn.addAuthToken(apiKey.getId());
				}
				if (apikeyCount > 1) {
					// Roll through keys
					for (ApiKey ak : apList) {
						// if status = disabled > delete
						if (ak.getStatus() == ApiKeyStatus.DISABLED) {
							ak.delete();
						}
						// if status = enabled status > disabled
						if (ak.getStatus() == ApiKeyStatus.ENABLED) {
							ak.setStatus(ApiKeyStatus.DISABLED);
							ak.save();
							accIn.addAuthToken(ak.getId());
						}
					}
					// add an new apikey
					ApiKey apiKey = acc.createApiKey();
					accIn.setAuthToken(apiKey.getId());
				}
			}
			return accIn;
		}
		return null;

	}

	@Override
	public final OtfAccount authAccountLocal(final String acNameIn,
			String pwIn, final String tokenIn) {
		if (stringOK(acNameIn)) {
			// see if the pw is a token
			if (stringOK(tokenIn)) {
				Account accTok = getAccountByUsername(acNameIn);
				if (accTok != null) {
					OtfAccount oacc = getAcKeys(accTok);
					if (oacc.checkAuthToken(tokenIn)) {
						return oacc;
					}
				}
			}
			if (stringOK(pwIn)) {
				Account acc = authSPAccount(acNameIn, pwIn);
				// The moment pwIn finshed with set to null
				pwIn = null;
				return getResetAcKeys(acc);
			}
		}
		return null;
	}

	private Account authSPAccount(final String acName, final String pw) {
		// Create an authentication request using the credentials
		AuthenticationRequest request = new UsernamePasswordRequest(acName, pw);
		Application userApp = getUsersApplication(acName);
		if (userApp == null) {
			LOG.severe("Auth error: User " + acName + " not found. ");
			return null;
		}
		// Now let's authenticate the account with the application:
		try {
			Account userAcc = userApp.authenticateAccount(request).getAccount();
			return userAcc;
		} catch (ResourceException name) {
			// ...catch the error and print it to the syslog if it wasn't.
			LOG.severe("Auth error: " + name.getDeveloperMessage());
			return null;
		} finally {
			// Clear the request data to prevent later memory access
			request.clear();
		}
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

		Application uApp = getFirstApplicationByUserName(userNameIn);
		if (uApp != null) {
			Account account = uApp.sendPasswordResetEmail(emailAddrIn);
			if (account != null) {
				return "Password Mail requested";
			}
		}

		return null;
	}

	@Override
	public final int updateUserPassword(final String userNameIn,
			final String passwordIn, final String tokenIn) {
		Account acc = null;
		Application uApp = getFirstApplicationByUserName(userNameIn);
		if (uApp != null) {
			acc = getUsersApplication(userNameIn).verifyPasswordResetToken(
					tokenIn);
		}

		if (acc == null) {
			return -1;
		}

		else {
			// check username & acc username agree
			boolean namesMatch = userNameIn.equals(acc.getUsername());
			if (!namesMatch) {
				return -2;
			}
			if (namesMatch) {
				if (stringOK(passwordIn)) {
					acc.setPassword(passwordIn);
					acc.save();
					return 1;
				} else {
					return 0;
				}

			}

		}

		return -3;
	}

	@Override
	public UserSecurityModel getLocalUserSecurityModel() {
		if (isCacheModel()) {
			return new UserSecurityModelCached();
		} else {
			return new StormpathUserSecurityModel();
		}
	}

	public final boolean isCacheModel() {
		return cacheModel;
	}

	public final void setCacheModel(boolean cacheModelIn) {
		cacheModel = cacheModelIn;
	}

	public final Application getUsersApplication(String username) {
		return getApplicationByUser(username);

	}

	public final Account getAccountByUsername(String username) {

		// See if the account is in the accList locally
		if (OtfCachedListsDTO.getAllAccountsMap() != null) {
			OtfAccount oacc = OtfCachedListsDTO.getAllAccountsMap().get(
					username);
			if (oacc != null) {
				// get the href
				String href = oacc.getIdref();
				if (stringOK(href)) {
					final Account acc = spbd.getResourceByHrefAccount(href);
					if (acc != null) {
						return acc;
					}
				}
			}
		}
		// else....get from remote via apps
		// Possibly use href is account is untrustworthy.
		return getRemoteAccountByUser(username);
	}

	public final Application getApplicationByName(final String appName) {
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			if (application.getName().equals(appName)) {
				return application;
			}
		}
		return null;
	}

	public final Account getRemoteAccountByUser(final String userName) {
		ApplicationList applications = spbd.getTenant().getApplications();
		// First try users or admin app as most likely
		for (Application application : applications) {
			if (userOrAdminApp(application.getName())) {
				AccountList acc = application.getAccounts(Accounts
						.where(Accounts.username().eqIgnoreCase(userName)));
				if (acc.iterator().hasNext()) {
					return acc.iterator().next();
				}
			}
		}
		// then try others
		for (Application application : applications) {
			if (!userOrAdminApp(application.getName())) {
				AccountList acc = application.getAccounts(Accounts
						.where(Accounts.username().eqIgnoreCase(userName)));
				if (acc.iterator().hasNext()) {
					return acc.iterator().next();
				}
			}
		}
		return null;
	}

	public final Application getApplicationByUser(final String userName) {
		ApplicationList applications = spbd.getTenant().getApplications();
		// First try users or admin app as most likely
		for (Application application : applications) {
			if (userOrAdminApp(application.getName())) {
				AccountList acc = application.getAccounts(Accounts
						.where(Accounts.username().eqIgnoreCase(userName)));
				if (acc.iterator().hasNext()) {
					return application;
				}
			}
		}
		// then try others
		for (Application application : applications) {
			if (!userOrAdminApp(application.getName())) {
				AccountList acc = application.getAccounts(Accounts
						.where(Accounts.username().eqIgnoreCase(userName)));
				if (acc.iterator().hasNext()) {
					return application;
				}
			}
		}
		return null;
	}

	public boolean userOrAdminApp(String appname) {
		if (appname.equalsIgnoreCase(StormPathUserSecurityHandler.STORMPATH)) {
			return true;
		}
		return appname.equalsIgnoreCase(getUsersAppName());
	}

}
