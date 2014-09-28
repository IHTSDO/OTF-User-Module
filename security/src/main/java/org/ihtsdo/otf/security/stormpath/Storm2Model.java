package org.ihtsdo.otf.security.stormpath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountStore;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.UserSecurityCached;
import org.ihtsdo.otf.security.xml.Xml2Model;
import org.ihtsdo.otf.security.xml.XmlStatics;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.account.Accounts;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeyList;
import com.stormpath.sdk.api.ApiKeyStatus;
import com.stormpath.sdk.application.AccountStoreMapping;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.directory.AccountStore;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.directory.DirectoryList;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.group.GroupList;
import com.stormpath.sdk.resource.ResourceException;

public class Storm2Model {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(Xml2Model.class
			.getName());

	private UserSecurity userSecurity;

	private final StormPathBaseDTO spbd;

	public Storm2Model(final StormPathBaseDTO spbdIn) {
		super();
		spbd = spbdIn;
		LOG.info("Storm2Model Tenant = " + spbd.getTenant().getName());
	}

	public final UserSecurity build() throws Exception {
		buildUserSecurity();
		return userSecurity;
	}

	public final UserSecurity buildCachedUserSecurity() {
		userSecurity = new UserSecurityCached();
		buildDirs();
		buildApps();
		return userSecurity;
	}

	public final UserSecurity buildUserSecurity() {
		userSecurity = new UserSecurityStormpath();
		return userSecurity;
	}

	public final void buildDirs() {
		DirectoryList directories = spbd.getTenant().getDirectories();
		for (Directory directory : directories) {
			OtfDirectory odir = buildDirectory(directory);
			if (odir != null) {
				userSecurity.getDirs().getDirectories()
						.put(odir.getName(), odir);
			}
		}
	}

	public final OtfDirectory getOtfDirByName(final String dirName) {
		Directory dir = getDirByName(dirName);
		if (dir != null) {
			return buildDirectory(dir);
		}

		return null;
	}

	public final Directory getDirByName(final String dirName) {
		spbd.resetTenant();
		DirectoryList directories = spbd.getTenant().getDirectories();

		for (Directory dir : directories) {
			if (dir.getName().equals(dirName)) {
				String dirHref = dir.getHref();
				Directory d2 = spbd.getResourceByHrefDirectory(dirHref);
				return d2;
			}
		}
		return null;
	}

	public final Group getGrpByName(final String grpName, final Directory dir) {
		GroupList groups = dir.getGroups();
		for (Group group : groups) {
			if (group.getName().equals(grpName)) {
				String dirHref = group.getHref();
				Group group2 = spbd.getResourceByHrefGroup(dirHref);
				return group2;
			}
		}
		return null;
	}

	public final Account getAccountByName(final String name, final Directory dir) {
		for (Account acc : dir.getAccounts()) {
			if (acc.getUsername().equals(name)) {
				return acc;
			}
		}
		return null;
	}

	public final OtfAccount getOtfAccountByUsername(final String nameIn) {
		Account acc = getAccountByUsername(nameIn);
		if (acc != null) {
			return buildAccount(acc);
		}
		return null;

	}

	public final Account getAccountByUsername(final String usernameIn) {
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			AccountList acc = application.getAccounts(Accounts.where(Accounts
					.username().eqIgnoreCase(usernameIn)));
			if (acc.iterator().hasNext()) {
				return acc.iterator().next();
			}
		}
		return null;
	}

	public final OtfAccount getOtfAccountById(final String idIn) {
		Account acc = getAccountById(idIn);
		if (acc != null) {
			return buildAccount(acc);
		}

		return null;
	}

	public final Account getAccountById(final String idIn) {
		final Account acc = spbd.getResourceByHrefAccount(idIn);
		if (acc != null) {
			return acc;
		}
		return null;
	}

	public final Application getAppByName(final String name) {
		spbd.resetTenant();
		ApplicationList apps = spbd.getTenant().getApplications();
		for (Application app : apps) {
			if (app.getName().equals(name)) {
				String href = app.getHref();
				Application app2 = spbd.getResourceByHrefApp(href);

				return app2;
			}
		}

		return null;
	}

	public final OtfDirectory buildDirectory(final Directory dirIn) {

		Directory dir = null;
		OtfDirectory oDir = null;
		try {
			dir = spbd.getResourceByHrefDirectory(dirIn.getHref());
		} catch (ResourceException re) {
			LOG.severe("Directory at this href does not exist Dir = "
					+ dirIn.getName() + " href = " + dirIn.getHref());
		}
		if (dir != null) {
			oDir = new OtfDirectory();
			oDir.setIdref(dir.getHref());
			oDir.setName(dir.getName());
			oDir.setDescription(dir.getDescription());
			oDir.setStatus(dir.getStatus().toString());
			GroupList groups = dir.getGroups();
			for (Group group : groups) {
				OtfGroup ogroup = buildGroup(group);
				ogroup.setParentDirName(dir.getName());
				oDir.getGroups().getGroups().put(ogroup.getName(), ogroup);
			}
			for (Account acc : dir.getAccounts()) {
				OtfAccount oacc = buildAccount(acc);
				oDir.getAccounts().getAccounts().put(oacc.getName(), oacc);
			}
		}
		return oDir;
	}

	public final OtfGroup buildGroup(final Group grpIn) {
		String href = grpIn.getHref();
		return buildGroup(href);
	}

	public final OtfGroup buildGroup(final String href) {
		final Group grp = spbd.getResourceByHrefGroup(href);
		OtfGroup ogrp = new OtfGroup();
		ogrp.setIdref(grp.getHref());
		ogrp.setName(grp.getName());
		ogrp.setStatus(grp.getStatus().toString());

		for (Account acc : grp.getAccounts()) {
			OtfAccount oacc = buildAccount(acc);
			ogrp.getAccounts().getAccounts().put(oacc.getName(), oacc);
		}

		Map<String, Object> cd = spbd.getResourceByHrefCustomData(grp
				.getCustomData().getHref());
		for (String key : cd.keySet()) {
			if (!OtfCustomData.getReservedWords().contains(key)) {
				String val = cd.get(key).toString();
				OtfCustomField cf = new OtfCustomField(key, val);
				ogrp.getCustData().getCustFields().put(key, cf);
			}
		}
		return ogrp;
	}

	public final OtfAccount buildAccount(final Account accIn) {
		String href = accIn.getHref();
		return buildAccount(href);
	}

	public final OtfAccount buildAccount(final String href) {
		final Account acc = spbd.getResourceByHrefAccount(href);
		OtfAccount oacc = new OtfAccount();
		oacc.setIdref(acc.getHref());
		oacc.setName(acc.getUsername());
		oacc.setEmail(acc.getEmail());
		oacc.setGivenName(acc.getGivenName());
		oacc.setMiddleName(acc.getMiddleName());
		oacc.setSurname(acc.getSurname());
		oacc.setStatus(acc.getStatus().toString());
		oacc.setParentDir(acc.getDirectory().getName());
		String cdHref = acc.getCustomData().getHref();
		Map<String, Object> cd = spbd.getResourceByHrefCustomData(cdHref);

		if (cd != null) {
			oacc.getCustData().setIdref(cdHref);
		}

		for (String key : cd.keySet()) {
			if (!OtfCustomData.getReservedWords().contains(key)) {
				String val = cd.get(key).toString();
				OtfCustomField cf = new OtfCustomField(key, val);
				oacc.getCustData().getCustFields().put(key, cf);
			}
		}

		return oacc;

	}

	public final void buildApps() {
		// Get Applications
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			OtfApplication app = buildApp(application);
			userSecurity.getApps().getApplications().put(app.getName(), app);
		}
	}

	public final OtfApplication buildApp(final Application appIn) {

		String href = appIn.getHref();
		return buildApp(href);
	}

	public final List<OtfGroup> getGroupsByAppName(final String appnameIn) {
		List<OtfGroup> groups = new ArrayList<OtfGroup>();
		// first get the app by name
		Application app = getAppByName(appnameIn);
		// then get the list of dirnames
		if (app != null) {
			for (AccountStoreMapping accountStoreMap : app
					.getAccountStoreMappings()) {
				AccountStore accountStore1 = accountStoreMap.getAccountStore();
				SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
				accountStore1.accept(spa);
				if (spa.getType().equals(
						SPAccountStoreVisitor.AccountStoreType.DIR)) {
					// Is a directory
					Directory dir = (Directory) accountStore1;
					for (Group grp : dir.getGroups()) {
						OtfGroup ogrp = buildGroup(grp);
						groups.add(ogrp);
					}
				} else {
					Group grp = (Group) accountStore1;
					OtfGroup ogrp = buildGroup(grp);
					groups.add(ogrp);
				}
			}
		}
		return groups;
	}

	public final List<String> getDirsByAppName(final String appnameIn) {
		List<String> dirnames = new ArrayList<String>();
		// first get the app by name
		Application app = getAppByName(appnameIn);
		// then get the list of dirnames
		if (app != null) {
			for (AccountStoreMapping accountStoreMap : app
					.getAccountStoreMappings()) {
				AccountStore accountStore1 = accountStoreMap.getAccountStore();
				SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
				accountStore1.accept(spa);
				if (spa.getType().equals(
						SPAccountStoreVisitor.AccountStoreType.DIR)) {
					// Is a directory
					Directory dir = (Directory) accountStore1;
					dirnames.add(dir.getName());
				}
			}
		}
		return dirnames;
	}

	public final OtfApplication buildApp(final String href) {

		final Application app = spbd.getResourceByHrefApp(href);

		OtfApplication oapp = new OtfApplication();
		oapp.setIdref(app.getHref());
		oapp.setName(app.getName());
		oapp.setDescription(app.getDescription());
		oapp.setStatus(app.getStatus().toString());
		for (AccountStoreMapping accountStoreMap : app
				.getAccountStoreMappings()) {

			OtfAccountStore oas = buildAccountStore(accountStoreMap);
			if (oas != null) {
				oapp.getAccountStores().put(oas.getName(), oas);
			}
		}

		return oapp;
	}

	public final OtfAccountStore buildAccountStore(final AccountStoreMapping asm) {

		AccountStore accountStore1 = asm.getAccountStore();
		SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
		accountStore1.accept(spa);
		if (spa.getType().equals(SPAccountStoreVisitor.AccountStoreType.DIR)) {
			// Is a directory
			Directory dir = (Directory) accountStore1;
			OtfAccountStore oAs = new OtfAccountStore();
			oAs.setIdref(dir.getHref());
			oAs.setName(dir.getName());
			oAs.setType(XmlStatics.DIR);
			oAs.setStatus(dir.getStatus().toString());
			return oAs;
		} else {
			Group grp = (Group) accountStore1;
			OtfAccountStore oAs = new OtfAccountStore();
			oAs.setIdref(grp.getHref());
			oAs.setName(grp.getName());
			oAs.setType(XmlStatics.GRP);
			oAs.setStatus(grp.getStatus().toString());
			return oAs;
		}

	}

	public final UserSecurity getUserSecurity() {
		return userSecurity;
	}

	public final void setUserSecurity(final UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
	}

	public final boolean stringOK(final String chk) {
		return chk != null && chk.length() > 0;
	}

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

	public final Account authSPAccount(final String acName, final String pw) {
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

	public final OtfAccount getAcKeys(final Account acc) {
		if (acc != null) {
			OtfAccount accIn = buildAccount(acc);
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

	public final OtfAccount getResetAcKeys(final Account acc) {
		if (acc != null) {
			OtfAccount accIn = buildAccount(acc);
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
					.equalsIgnoreCase(StormPathUserSecurityHandler.STORMPATH)) {
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

	public final Application getApplicationByUser(final String userName) {
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			AccountList acc = application.getAccounts(Accounts.where(Accounts
					.username().eqIgnoreCase(userName)));
			if (acc.iterator().hasNext()) {
				return application;
			}
		}
		return null;
	}

	public final int updateUserPassword(final String userNameIn,
			final String passwordIn, final String tokenIn) {
		Account acc = null;
		Application uApp = getUsersApplication(userNameIn);
		if (uApp != null) {
			acc = getApplicationByUser(userNameIn).verifyPasswordResetToken(
					tokenIn);
		}
		if (acc == null) {
			return -1;
		} else {
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

	public final Application getApplicationByName(final String appName) {
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			if (application.getName().equals(appName)) {
				return application;
			}
		}
		return null;
	}

	public final String requestUpdateUserPassword(final String userNameIn,
			final String emailAddrIn) {

		Application uApp = getUsersApplication(userNameIn);
		if (uApp != null) {
			Account account = uApp.sendPasswordResetEmail(emailAddrIn);
			if (account != null) {
				return "Password Mail requested";
			}
		}
		return null;
	}

	public final Application getUsersApplication(final String userName) {
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			AccountList accList = application.getAccounts();
			for (Account acc : accList) {
				if (acc.getUsername().equals(userName)) {
					return application;
				}
			}
		}
		return null;
	}

	public final List<String> getUserNames() {
		ArrayList<String> accnames = new ArrayList<String>();
		for (Account acc : getAccounts()) {
			accnames.add(acc.getUsername());
		}
		return accnames;
	}

	public final Collection<Account> getAccounts() {
		ArrayList<Account> accnames = new ArrayList<Account>();
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			AccountList accList = application.getAccounts();
			for (Account acc : accList) {
				accnames.add(acc);
			}
		}
		return accnames;
	}

	public final Collection<OtfAccount> getOtfAccounts() {
		ArrayList<OtfAccount> accnames = new ArrayList<OtfAccount>();
		for (Account acc : getAccounts()) {
			accnames.add(buildAccount(acc));
		}
		return accnames;
	}

	public final List<String> getApps() {
		ArrayList<String> accnames = new ArrayList<String>();
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			accnames.add(application.getName());
		}
		return accnames;
	}

}
