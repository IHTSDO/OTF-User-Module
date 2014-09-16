package org.ihtsdo.otf.security.stormpath;

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
import com.stormpath.sdk.application.AccountStoreMapping;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
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

	public UserSecurity buildCachedUserSecurity() {
		userSecurity = new UserSecurityCached();
		buildDirs();
		buildApps();
		return userSecurity;
	}

	public UserSecurity buildUserSecurity() {
		userSecurity = new UserSecurityStormpath();
		return userSecurity;
	}

	public void buildDirs() {
		DirectoryList directories = spbd.getTenant().getDirectories();
		for (Directory directory : directories) {
			OtfDirectory odir = buildDirectory(directory);
			if (odir != null) {
				userSecurity.getDirs().getDirectories()
						.put(odir.getName(), odir);
			}
		}
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
		// LOG.info("getAccountByName name = " + name);
		for (Account acc : dir.getAccounts()) {
			// LOG.info("getAccountByName acc = " + acc.getUsername());
			if (acc.getUsername().equals(name)) {
				return acc;
			}
		}

		// try to reload the directory and check again
		//
		// LOG.info("Was null reloading directory");
		// String dirHref = dir.getHref();
		// LOG.info("dirHref = " + dirHref);
		// Directory d2 = spbd.getResourceByHrefDirectory(dirHref);
		//
		// for (Account acc : d2.getAccounts()) {
		// LOG.info("XXXXX --- getAccountByName acc = " + acc.getUsername());
		// if (acc.getUsername().equals(name)) {
		// return acc;
		// }
		// }

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

	public OtfDirectory buildDirectory(final Directory dirIn) {

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

	public OtfGroup buildGroup(final Group grpIn) {
		final Group grp = spbd.getResourceByHrefGroup(grpIn.getHref());
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

	public OtfAccount buildAccount(final Account accIn) {
		final Account acc = spbd.getResourceByHrefAccount(accIn.getHref());

		OtfAccount oacc = new OtfAccount();
		oacc.setIdref(acc.getHref());
		oacc.setName(acc.getUsername());
		oacc.setEmail(acc.getEmail());
		oacc.setGivenName(acc.getGivenName());
		oacc.setMiddleName(acc.getMiddleName());
		oacc.setSurname(acc.getSurname());
		oacc.setStatus(acc.getStatus().toString());
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

	public void buildApps() {
		// Get Applications
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			OtfApplication app = buildApp(application);
			userSecurity.getApps().getApplications().put(app.getName(), app);
		}
	}

	public OtfApplication buildApp(final Application appIn) {

		final Application app = spbd.getResourceByHrefApp(appIn.getHref());

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

	public OtfAccountStore buildAccountStore(final AccountStoreMapping asm) {

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

	public final void setUserSecurity(UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
	}

}
