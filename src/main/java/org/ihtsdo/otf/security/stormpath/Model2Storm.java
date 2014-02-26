package org.ihtsdo.otf.security.stormpath;

import java.util.HashMap;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountStore;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfBaseAccountStore;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.application.AccountStoreMapping;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.ApplicationStatus;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.directory.AccountStore;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.directory.DirectoryList;
import com.stormpath.sdk.directory.DirectoryStatus;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.group.GroupList;
import com.stormpath.sdk.group.GroupStatus;

public class Model2Storm {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(Model2Storm.class
			.getName());
	UserSecurity userSecurity;
	private final StormPathBaseDTO spbd;

	public Model2Storm(StormPathBaseDTO spbdIn) {
		super();
		spbd = spbdIn;

	}

	public void sendToStormPath(UserSecurity userSecurityIn) {
		if (userSecurityIn != null) {
			userSecurity = userSecurityIn;
			buildDirs();
			buildApps();
		}
		if (userSecurityIn == null) {
			clear();
		}
	}

	private void clear() {
		// StormPathUserSecurity.STORMPATH
		String UCSP = StormPathUserSecurity.STORMPATH.toUpperCase();
		// Clear apps not SP
		ApplicationList apps = spbd.getTenant().getApplications();
		for (Application app : apps) {
			String UCName = app.getName().toUpperCase();
			boolean canDel = !UCName.contains(UCSP);
			if (canDel) {
				LOG.info("Can del app " + app.getName());
				app.delete();
			}
			if (!canDel) {
				LOG.info("Can NOT del app " + app.getName());
			}

		}

		// Clear Dirs not SP
		DirectoryList dirs = spbd.getTenant().getDirectories();
		for (Directory dir : dirs) {
			String UCName = dir.getName().toUpperCase();
			boolean canDel = !UCName.contains(UCSP);
			if (canDel) {
				LOG.info("Can del Dir " + dir.getName());
				dir.delete();
			}
			if (!canDel) {
				LOG.info("Can NOT del Dir " + dir.getName());
			}

		}

	}

	private void buildDirs() {
		DirectoryList directories = spbd.getTenant().getDirectories();
		for (OtfDirectory oDir : userSecurity.getDirs().getDirectories()
				.values()) {
			boolean found = false;
			for (Directory directory : directories) {
				if (directory.getName().equals(oDir.getName())) {
					buildDirectory(oDir, directory);
					found = true;
				}
			}
			if (!found) {
				buildDirectory(oDir, null);
			}
		}
	}

	private void buildDirectory(OtfDirectory oDir, Directory dir) {
		// TODO: catch both being null;

		if (oDir == null && dir == null) {
			LOG.severe("buildDirectory all is null");
			return;
		}

		if (oDir != null && dir != null) {
			// update dir
			dir.setName(oDir.getName());
			dir.setDescription(oDir.getDescription());
			dir.setStatus(DirectoryStatus.valueOf(oDir.getStatus().toString()));
			dir.save();
		} else {
			if (dir == null) {
				// Add new

				dir = spbd.getClient().getDataStore()
						.instantiate(Directory.class);
				dir.setName(oDir.getName());
				dir.setDescription(oDir.getDescription());

				spbd.getTenant().createDirectory(dir);

			}
			if (oDir == null) {
				// delete existing? NOT STORMPATH
				dir = null;

			}
		}

		if (oDir != null && dir != null) {
			// add groups and accounts
			buildGroups(oDir, dir);
			buildAccounts(oDir, dir);
		}

	}

	private void buildGroups(OtfDirectory oDir, Directory dir) {

		GroupList grps = dir.getGroups();
		for (OtfGroup ogrp : oDir.getGroups().getGroups().values()) {
			boolean found = false;
			for (Group grp : grps) {
				if (grp.getName().equals(ogrp.getName())) {
					buildGroup(ogrp, grp, dir);
					found = true;
				}
			}
			if (!found) {
				buildGroup(ogrp, null, dir);
			}
		}

	}

	private void buildGroup(OtfGroup ogrp, Group grp, Directory dir) {
		if (ogrp == null && grp == null) {
			LOG.severe("buildGroup all is null");
			return;
		}
		if (ogrp != null && grp != null) {
			// update dir
			grp.setName(ogrp.getName());
			grp.setDescription(ogrp.getDescription());
			grp.setStatus(GroupStatus.valueOf(ogrp.getStatus().toString()));
			grp.save();
		} else {
			if (grp == null) {
				// Add new

				grp = spbd.getClient().instantiate(Group.class);
				grp.setName(ogrp.getName());
				grp.setDescription(ogrp.getDescription());
				dir.createGroup(grp);

			}
			if (ogrp == null) {
				// delete existing? NOT STORMPATH
				grp = null;
			}
		}

		if (ogrp != null && grp != null) {
			// add accounts
			buildAccounts(ogrp, grp);
			// add customFields
			buildCustomData(ogrp.getCustFields(), grp.getCustomData());
		}

	}

	private void buildAccounts(OtfBaseAccountStore oDir, AccountStore accSt) {
		AccountList accs = null;
		SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
		accSt.accept(spa);
		if (spa.getType().equals(SPAccountStoreVisitor.AccountStoreType.DIR)) {
			Directory dir = (Directory) accSt;
			accs = dir.getAccounts();
		}
		if (spa.getType().equals(SPAccountStoreVisitor.AccountStoreType.GROUP)) {
			Group grp = (Group) accSt;
			accs = grp.getAccounts();
		}

		if (accs != null) {
			for (OtfAccount oacc : oDir.getAccounts().getAccounts().values()) {
				boolean found = false;
				for (Account acc : accs) {
					if (acc.getUsername().equals(oacc.getName())) {
						buildAccount(oacc, acc, accSt);
						found = true;
					}
				}
				if (!found) {
					buildAccount(oacc, null, accSt);
				}

			}
		}
	}

	private void buildAccount(OtfAccount oacc, Account acc, AccountStore accSt) {
		if (oacc == null && acc == null) {
			LOG.severe("buildAccount all is null");
			return;
		}
		if (oacc != null && acc != null) {
			// update dir
			acc.setUsername(oacc.getName());
			acc.setEmail(oacc.getEmail());
			acc.save();
		} else {
			if (acc == null) {
				// Add new
				// TODO: Check to see if account must be added
				// to directory prior to being added to Group;
				acc = spbd.getClient().instantiate(Account.class);
				acc.setUsername(oacc.getName());
				acc.setGivenName(oacc.getGivenName());
				acc.setMiddleName(oacc.getMiddleName());
				acc.setSurname(oacc.getSurname());
				acc.setEmail(oacc.getEmail());
				acc.setPassword(userSecurity.getDefaultpw());

				SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
				accSt.accept(spa);
				if (spa.getType().equals(
						SPAccountStoreVisitor.AccountStoreType.DIR)) {
					Directory dir = (Directory) accSt;
					dir.createAccount(acc);
				}
				if (spa.getType().equals(
						SPAccountStoreVisitor.AccountStoreType.GROUP)) {
					Group grp = (Group) accSt;
					grp.addAccount(acc);

				}

			}
			if (oacc == null) {
				// delete existing? NOT STORMPATH
				acc = null;
			}
		}

		if (oacc != null && acc != null) {
			// add customFields
			buildCustomData(oacc.getCustFields(), acc.getCustomData());

		}

	}

	private void buildCustomData(HashMap<String, OtfCustomField> custFields,
			CustomData customData) {
		if (custFields == null && customData == null) {
			LOG.severe("buildCustomData all is null");
			return;
		}
		if (custFields != null && customData != null) {
			for (String key : custFields.keySet()) {
				Object value = custFields.get(key);
				// roll through keys in cust field
				if (!OtfCustomData.getReservedWords().contains(key)) {
					customData.put(key, value);
				}
			}
			for (String key : customData.keySet()) {
				// roll through keys checking not reserved.
				if (!OtfCustomData.getReservedWords().contains(key)) {
					if (!custFields.containsKey(key)) {
						customData.remove(key);
					}
				}
			}
			customData.save();
		}
	}

	private void buildApps() {

		ApplicationList apps = spbd.getTenant().getApplications();
		for (OtfApplication oApp : userSecurity.getApps().getApplications()
				.values()) {
			boolean found = false;
			// refresh spbd in order to pick up new directories
			spbd.load();
			for (Application app : apps) {
				if (app.getName().equals(oApp.getName())) {
					buildApp(oApp, app);
					found = true;
				}
			}
			if (!found) {
				buildApp(oApp, null);
			}
		}
	}

	private void buildApp(OtfApplication oApp, Application app) {
		if (oApp == null && app == null) {
			LOG.severe("buildApp all is null");
			return;
		}
		if (oApp != null && app != null) {
			// update dir
			app.setName(oApp.getName());
			app.setDescription(oApp.getDescription());
			app.setStatus(ApplicationStatus
					.valueOf(oApp.getStatus().toString()));
			app.save();
		} else {
			if (app == null) {
				// Add new

				app = spbd.getClient().instantiate(Application.class);
				app.setName(oApp.getName());
				app.setDescription(oApp.getDescription());
				// Get list of accountStores

				// Build App with default DIR
				// spbd.getTenant().createApplication(
				// Applications.newCreateRequestFor(app).createDirectory()
				// .build());

				DirectoryList dl = spbd.getTenant().getDirectories();

				spbd.getTenant().createApplication(
						Applications.newCreateRequestFor(app).build());
				boolean isDefStore = true;
				int i = 0;
				for (OtfAccountStore oAst : oApp.getAccountStores().values()) {
					String name = oAst.getName();

					for (Directory dir : dl) {
						if (dir.getName().equals(name)) {
							AccountStoreMapping accountStoreMapping = spbd
									.getClient().instantiate(
											AccountStoreMapping.class);
							accountStoreMapping.setAccountStore(dir);
							accountStoreMapping.setApplication(app);
							accountStoreMapping
									.setDefaultAccountStore(isDefStore);
							accountStoreMapping
									.setDefaultGroupStore(isDefStore);
							accountStoreMapping.setListIndex(i);
							app.createAccountStoreMapping(accountStoreMapping);
							isDefStore = false;
							i++;
						}
					}
				}
				//

			}
			if (oApp == null) {
				// delete existing? NOT STORMPATH
				app = null;
			}
		}

	}

	public UserSecurity getUserSecurity() {
		return userSecurity;
	}

	public void setUserSecurity(UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
	}

	public StormPathBaseDTO getSpbd() {
		return spbd;
	}

}
