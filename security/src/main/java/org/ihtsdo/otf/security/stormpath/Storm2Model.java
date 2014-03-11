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

	public Storm2Model(StormPathBaseDTO spbdIn) {
		super();
		spbd = spbdIn;
	}

	public final UserSecurity build() throws Exception {
		buildUserSecurity();
		return userSecurity;
	}

	private void buildUserSecurity() {
		userSecurity = new UserSecurity();
		buildDirs();
		buildApps();

	}

	private void buildDirs() {
		DirectoryList directories = spbd.getTenant().getDirectories();
		for (Directory directory : directories) {
			OtfDirectory odir = buildDirectory(directory);
			userSecurity.getDirs().getDirectories().put(odir.getName(), odir);
		}
	}

	private OtfDirectory buildDirectory(Directory dir) {
		OtfDirectory oDir = new OtfDirectory();
		oDir.setName(dir.getName());
		oDir.setDescription(dir.getDescription());
		oDir.setStatus(dir.getStatus().toString());

		GroupList groups = dir.getGroups();

		for (Group group : groups) {
			OtfGroup ogroup = buildGroup(group);
			oDir.getGroups().getGroups().put(ogroup.getName(), ogroup);
		}
		for (Account acc : dir.getAccounts()) {
			// sLOG.info("Account =" + acc);
			OtfAccount oacc = buildAccount(acc);
			oDir.getAccounts().getAccounts().put(oacc.getName(), oacc);
		}
		return oDir;
	}

	private OtfGroup buildGroup(Group grp) {
		OtfGroup ogrp = new OtfGroup();

		ogrp.setName(grp.getName());
		ogrp.setStatus(grp.getStatus().toString());

		for (Account acc : grp.getAccounts()) {
			OtfAccount oacc = buildAccount(acc);
			ogrp.getAccounts().getAccounts().put(oacc.getName(), oacc);
		}

		Map<String, Object> cd = spbd.getCustomData(grp.getCustomData()
				.getHref());
		for (String key : cd.keySet()) {
			if (!OtfCustomData.getReservedWords().contains(key)) {
				String val = cd.get(key).toString();
				OtfCustomField cf = new OtfCustomField(key, val);
				ogrp.getCustData().getCustFields().put(key, cf);
			}
		}
		return ogrp;
	}

	private OtfAccount buildAccount(Account acc) {
		// boolean log = acc.getUsername().equalsIgnoreCase("bob");
		OtfAccount oacc = new OtfAccount();
		oacc.setName(acc.getUsername());
		oacc.setEmail(acc.getEmail());
		oacc.setGivenName(acc.getGivenName());
		oacc.setMiddleName(acc.getMiddleName());
		oacc.setSurname(acc.getSurname());
		oacc.setStatus(acc.getStatus().toString());
		Map<String, Object> cd = spbd.getCustomData(acc.getCustomData()
				.getHref());
		// if (log) {
		// LOG.info("Account: " + acc);
		// LOG.info("buildAccount1 cd size " + cd.size());
		// LOG.info("href = " + acc.getCustomData().getHref());
		// }
		for (String key : cd.keySet()) {
			if (!OtfCustomData.getReservedWords().contains(key)) {

				String val = cd.get(key).toString();
				// if (log) {
				// LOG.info("buildAccount val = " + val);
				// }
				OtfCustomField cf = new OtfCustomField(key, val);
				oacc.getCustData().getCustFields().put(key, cf);
			}
		}

		return oacc;

	}

	private void buildApps() {
		// Get Applications
		ApplicationList applications = spbd.getTenant().getApplications();
		for (Application application : applications) {
			OtfApplication app = buildApp(application);
			userSecurity.getApps().getApplications().put(app.getName(), app);
		}
	}

	private OtfApplication buildApp(final Application app) {
		OtfApplication oapp = new OtfApplication();
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

	private OtfAccountStore buildAccountStore(final AccountStoreMapping asm) {

		AccountStore accountStore1 = asm.getAccountStore();

		// LOG.info("accountStore1 = " + accountStore1);
		SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
		accountStore1.accept(spa);
		// LOG.info("SPA TYEP = " + spa.getType());
		if (spa.getType().equals(SPAccountStoreVisitor.AccountStoreType.DIR)) {
			// Is a directory
			Directory dir = (Directory) accountStore1;
			// LOG.info("DIR NAME =" + dir.getName());
			OtfAccountStore oAs = new OtfAccountStore();
			oAs.setName(dir.getName());
			oAs.setType(XmlStatics.DIR);
			return oAs;
		} else {
			Group grp = (Group) accountStore1;
			// LOG.info("DIR NAME =" + dir.getName());
			OtfAccountStore oAs = new OtfAccountStore();
			oAs.setName(grp.getName());
			oAs.setType(XmlStatics.GRP);
			return oAs;
		}

	}

	private OtfDirectory buildDir(Directory dir) {
		OtfDirectory oDir = new OtfDirectory();

		return oDir;

	}

}
