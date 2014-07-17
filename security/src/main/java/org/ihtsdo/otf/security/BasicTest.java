package org.ihtsdo.otf.security;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.stormpath.SPAccountStoreVisitor;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.application.AccountStoreMapping;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationCriteria;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.ClientBuilder;
import com.stormpath.sdk.directory.AccountStore;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.directory.DirectoryList;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.group.GroupList;
import com.stormpath.sdk.resource.ResourceException;
import com.stormpath.sdk.tenant.Tenant;

public class BasicTest {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(BasicTest.class
			.getName());

	private static Client client;
	private Tenant tenant;
	private String keyPath = "C:/Users/adamf/stormpath/apiKey.properties";

	private Account testUser;

	private final String testUname = "B0bAutoTest";
	private final String testAppname = "AutoTestApp";

	private Application application;

	public static void main(final String[] args) {
		BasicTest bt = new BasicTest();
		bt.init();

	}

	private void init() {

		client = new ClientBuilder().setApiKeyFileLocation(getKeyPath())
				.build();
		tenant = client.getCurrentTenant();
		test();
	}

	private void test() {
		getTestApplication();
		getTestUser();
		getDirs();

	}

	private void createApplication(final String appName) {
		application = client.instantiate(Application.class);
		application.setName(appName); // must be unique among your other apps
		application = client.getCurrentTenant().createApplication(
				Applications.newCreateRequestFor(application).createDirectory()
						.build());
	}

	private Account createAccount(final String uname, final String pw) {
		// Create the account object
		Account account = client.instantiate(Account.class);

		// Set the account properties
		account.setGivenName("Bob");
		account.setSurname("Bobbin");
		account.setUsername(uname); // optional, defaults to email if unset
		account.setEmail("afl@ihtsdo.org");
		account.setPassword(pw);
		CustomData cd = account.getCustomData();
		cd.put("rank", "Captain");

		// Create the account using the existing Application object
		return application.createAccount(account);
	}

	private Account authAccount(final String acName, final String pw) {
		// Create an authentication request using the credentials
		AuthenticationRequest request = new UsernamePasswordRequest(acName, pw);

		// Now let's authenticate the account with the application:
		try {
			return application.authenticateAccount(request).getAccount();
		} catch (ResourceException name) {
			// ...catch the error and print it to the syslog if it wasn't.
			LOG.severe("Auth error: " + name.getDeveloperMessage());
			return null;
		} finally {
			// Clear the request data to prevent later memory access
			request.clear();
		}
	}

	public final List<String> getApps() {
		ArrayList<String> appsList = new ArrayList<String>();

		ApplicationList applications = tenant.getApplications();
		for (Application app : applications) {
			appsList.add(app.getName());
			LOG.info("Application =" + app);

			for (AccountStoreMapping accountStoreMap : app
					.getAccountStoreMappings()) {
				int i = 0;
				AccountStore accountStore1 = accountStoreMap.getAccountStore();
				LOG.info("accountStore1 = " + accountStore1);
				SPAccountStoreVisitor spa = new SPAccountStoreVisitor();
				accountStore1.accept(spa);
				LOG.info("SPA TYEP = " + spa.getType());
				if (spa.getType().equals(
						SPAccountStoreVisitor.AccountStoreType.DIR)) {
					// Is a directory
					Directory dir = (Directory) accountStore1;
					LOG.info("DIR NAME =" + dir.getName());
				}
			}
			AccountStore accountStore = application.getDefaultAccountStore();
			LOG.info("accountStore = " + accountStore);
			if (accountStore != null) {
				accountStore.accept(new SPAccountStoreVisitor());
			}

		}
		return appsList;
	}

	public final List<String> getDirs() {
		ArrayList<String> dirsList = new ArrayList<String>();

		DirectoryList directories = tenant.getDirectories();
		for (Directory directory : directories) {
			dirsList.add(directory.getName());
			LOG.info("Directory " + directory);
			getGroups(directory);

		}
		return dirsList;
	}

	public final List<String> getGroups(final Directory directory) {
		ArrayList<String> groupsList = new ArrayList<String>();
		GroupList groups = directory.getGroups();
		for (Group group : groups) {
			groupsList.add(group.getName());
			LOG.info("group " + group);

		}
		return groupsList;
	}

	public final List<String> getAppAccounts(final Application application) {
		ArrayList<String> acList = new ArrayList<String>();

		AccountList acs = application.getAccounts();
		for (Account ac : acs) {
			acList.add(ac.getUsername());
			LOG.info("Account = " + ac);
		}
		return acList;
	}

	public final String getKeyPath() {
		return keyPath;
	}

	public final void setKeyPath(final String keyPath) {
		this.keyPath = keyPath;
	}

	public final Application getTestApplication() {
		LOG.info("getTestApplication");
		List<String> apps = getApps();
		if (apps.contains(testAppname)) {
			LOG.info(testAppname + " found");
			ApplicationCriteria criteria = Applications.where(Applications
					.name().eqIgnoreCase(testAppname));
			ApplicationList appList = tenant.getApplications(criteria);
			application = appList.iterator().next();
		}

		else {
			LOG.info(testAppname + " not found so creating");
			createApplication(testAppname);
		}

		LOG.info("getTestApplication app = " + application.toString());

		return application;
	}

	public final Application getApplication() {
		return application;
	}

	public final void setApplication(final Application application) {
		this.application = application;
	}

	public final Account getTestUser() {

		if (testUser == null) {
			testUser = authAccount(testUname, testUname);
			if (testUser == null) {
				LOG.info("getTestUser testUser not found so creating");
				testUser = createAccount(testUname, testUname);

			}
		}
		LOG.info("getTestUser testUser = " + testUser.toString());
		return testUser;
	}

	public final void setTestUser(final Account testUser) {
		this.testUser = testUser;
	}

}
