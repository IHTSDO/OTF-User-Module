package org.ihtsdo.otf.security.stormpath;

import java.io.IOException;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.UserSecurity;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.resource.ResourceException;

public class StormPathUserSecurity implements UserSecurityHandler {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(StormPathUserSecurity.class.getName());

	private Application usersApplication;
	private String usersAppName = "OTF Users";

	private UserSecurity userSecurity;

	private StormPathBaseDTO spbd;

	public static final String STORMPATH = "Stormpath";

	Storm2Model storm2Mod;
	Model2Storm mod2Storm;

	public StormPathUserSecurity() {
		super();
	}

	public final void clearSP() throws Exception {
		if (spbd == null) {
			spbd = new StormPathBaseDTO();
			spbd.load();
		}
		if (mod2Storm == null) {
			mod2Storm = new Model2Storm(spbd);
		}
		mod2Storm.sendToStormPath(null);

	}

	public final void buildUserSecurity() throws Exception {
		if (spbd == null) {
			spbd = new StormPathBaseDTO();
			spbd.load();
		}
		if (storm2Mod == null) {
			storm2Mod = new Storm2Model(spbd);
		}
		userSecurity = storm2Mod.build();
	}

	public final void sendUserSecuritytoStormPath(UserSecurity userSecurityIn)
			throws Exception {
		if (userSecurityIn != null) {
			userSecurity = userSecurityIn;
		}
		if (spbd == null) {
			spbd = new StormPathBaseDTO();
			spbd.load();
		}
		if (mod2Storm == null) {
			mod2Storm = new Model2Storm(spbd);
		}
		mod2Storm.sendToStormPath(userSecurity);
	}

	public void load(String keyPathIn) {
		spbd = new StormPathBaseDTO();
		spbd.setKeyPath(keyPathIn);
		spbd.load();
	}

	@Override
	public UserSecurity getUserSecurity() {
		return userSecurity;
	}

	@Override
	public void setUserSecurity(UserSecurity userSecurityIn) {
		userSecurity = userSecurityIn;
	}

	@Override
	public void saveUserSecurity() throws IOException {
	}

	public Application getUsersApplication() {
		if (usersApplication == null) {
			String userAppName = getUserSecurity().getUsersApp();
			ApplicationList applications = spbd.getTenant().getApplications();
			for (Application application : applications) {
				if (application.getName().equals(userAppName)) {
					setUsersApplication(application);
					return usersApplication;
				}
			}
		}
		return usersApplication;
	}

	public void setUsersApplication(Application usersApplicationIn) {
		usersApplication = usersApplicationIn;
	}

	public String getUsersAppName() {
		return usersAppName;
	}

	public void setUsersAppName(String usersAppNameIn) {
		usersAppName = usersAppNameIn;
	}

	public StormPathBaseDTO getSpbd() {
		return spbd;
	}

	public void setSpbd(StormPathBaseDTO spbdIn) {
		spbd = spbdIn;
	}

	@Override
	public OtfAccount authAccount(String acNameIn, String pwIn) {
		Account acc = authSPAccount(acNameIn, pwIn);

		// CustomData cd = spdb.

		if (acc != null) {
			return getUserSecurity().getUserAccountByName(acNameIn);
		}
		return null;
	}

	private Account authSPAccount(String acName, String pw) {
		// Create an authentication request using the credentials
		AuthenticationRequest request = new UsernamePasswordRequest(acName, pw);
		// Now let's authenticate the account with the application:
		try {
			return getUsersApplication().authenticateAccount(request)
					.getAccount();
		} catch (ResourceException name) {
			// ...catch the error and print it to the syslog if it wasn't.
			LOG.severe("Auth error: " + name.getDeveloperMessage());
			return null;
		} finally {
			// Clear the request data to prevent later memory access
			request.clear();
		}
	}

}
