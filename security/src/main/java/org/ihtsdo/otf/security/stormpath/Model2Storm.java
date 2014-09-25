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
	private UserSecurity userSecurity;
	private final StormPathBaseDTO spbd;

	public Model2Storm(final StormPathBaseDTO spbdIn) {
		super();
		spbd = spbdIn;

	}

	public final void sendToStormPath(final UserSecurity userSecurityIn) {
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
		String UCSP = StormPathUserSecurityHandler.STORMPATH.toUpperCase();
		// Clear apps not SP
		ApplicationList apps = spbd.getTenant().getApplications();
		for (Application app : apps) {
			String UCName = app.getName().toUpperCase();
			boolean canDel = !UCName.contains(UCSP);
			if (canDel) {
				app.delete();
			}
		}

		// Clear Dirs not SP
		DirectoryList dirs = spbd.getTenant().getDirectories();
		for (Directory dir : dirs) {
			String UCName = dir.getName().toUpperCase();
			boolean canDel = !UCName.contains(UCSP);
			if (canDel) {
				dir.delete();
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

	public void buildDirectory(final OtfDirectory oDir, Directory dir) {
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

	private void buildGroups(final OtfDirectory oDir, final Directory dir) {

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

	public void buildGroup(final OtfGroup ogrp, Group grp, final Directory dir) {
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
				grp = null;
			}
		}

		if (ogrp != null && grp != null) {
			// add accounts
			buildAccounts(ogrp, grp);
			// add customFields
			if (!ogrp.getCustData().getCustFields().isEmpty()) {
				CustomData cd = spbd.getResourceByHrefCustomData(grp
						.getCustomData().getHref());
				buildCustomData(ogrp.getCustData().getCustFields(), cd);
				cd.save();
			}
		}

	}

	private void buildAccounts(final OtfAccountStore oDir,
			final AccountStore accSt) {
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

	public void buildAccount(final OtfAccount oacc, Account acc,
			final AccountStore accSt) {
		boolean log = false;

		if (oacc == null && acc == null) {
			LOG.severe("buildAccount all is null");
			return;
		}
		if (oacc != null && acc != null) {
			// update
			acc.setUsername(oacc.getName());
			acc.setGivenName(oacc.getGivenName());
			acc.setMiddleName(oacc.getMiddleName());
			acc.setSurname(oacc.getSurname());
			acc.setEmail(oacc.getEmail());
			if (!oacc.getCustData().getCustFields().isEmpty()) {
				CustomData cd = spbd.getResourceByHrefCustomData(acc
						.getCustomData().getHref());
				buildCustomData(oacc.getCustData().getCustFields(), cd);
				cd.save();
			}
			acc.save();
		} else {
			if (acc == null) {
				// Add new
				acc = spbd.getClient().instantiate(Account.class);
				acc.setUsername(oacc.getName());
				acc.setGivenName(oacc.getGivenName());
				acc.setMiddleName(oacc.getMiddleName());
				acc.setSurname(oacc.getSurname());
				acc.setEmail(oacc.getEmail());
				userSecurity.getDefaultpw();
				acc.setPassword(userSecurity.getDefaultpw());
				if (!oacc.getCustData().getCustFields().isEmpty()) {
					CustomData cd = acc.getCustomData();
					buildCustomData(oacc.getCustData().getCustFields(), cd);
					if (log) {
						LOG.info("Acc1 = " + acc);
					}
				}

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
		}
	}

	private void buildCustomData(final Map<String, OtfCustomField> custFields,
			final CustomData customData) {
		if (custFields != null && customData != null) {
			for (String key : custFields.keySet()) {
				String value = custFields.get(key).getValFromVals();
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
		}
	}

	private void buildApps() {

		// spbd.resetTenant

		ApplicationList apps = spbd.getTenant().getApplications();

		for (OtfApplication oApp : userSecurity.getApps().getApplications()
				.values()) {
			boolean found = false;
			// refresh spbd in order to pick up new directories

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

	public void buildApp(final OtfApplication oApp, Application app) {
		spbd.resetTenant();
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

				// TODO: NOTE not creating groups. This is on purpose as we do
				// not require groups and it makes the coding and logic simpler.

				// Get list of accountStores
				int accStores = oApp.getAccountStores().size();

				LOG.info("App called " + oApp.getName() + " has " + accStores
						+ " accstores set");

				if (accStores == 0) {
					// Build App with default DIR
					spbd.getTenant().createApplication(
							Applications.newCreateRequestFor(app)
									.createDirectory().build());
				}

				if (accStores > 0) {
					DirectoryList dl = spbd.getTenant().getDirectories();
					LOG.info("dl has next = " + dl.iterator().hasNext());
					// if (!dl.iterator().hasNext()) {
					// String href = spbd.getTenant().getHref();
					// Tenant ten = spbd.getResourceByHrefTenant(href);
					// DirectoryList dl2 = ten.getDirectories();
					// LOG.info("dl2 has next  = = " + dl.iterator().hasNext());
					// }
					spbd.getTenant().createApplication(
							Applications.newCreateRequestFor(app).build());
					boolean isDefStore = true;
					int i = 0;
					for (OtfAccountStore oAst : oApp.getAccountStores()
							.values()) {
						String name = oAst.getName();
						LOG.info("Acc store name = " + name);

						for (Directory dir : dl) {
							LOG.info("dir name = " + dir.getName());
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
				}
			}
			if (oApp == null) {
				app = null;
			}
		}

	}

	public final UserSecurity getUserSecurity() {
		return userSecurity;
	}

	public final void setUserSecurity(final UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
	}

	public final StormPathBaseDTO getSpbd() {
		return spbd;
	}

}
