package org.ihtsdo.otf.security.stormpath;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.UserSecurity;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.resource.ResourceException;

public class StormPathUserSecurity extends AbstractUserSecurityHandler {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(StormPathUserSecurity.class.getName());

	private Application usersApplication;
	private String usersAppName = "OTF Users";

	private StormPathBaseDTO spbd;

	public static final String STORMPATH = "Stormpath";

	private Properties props;

	private Storm2Model storm2Mod;
	private Model2Storm mod2Storm;

	public StormPathUserSecurity() {
		super();
	}

	public StormPathUserSecurity(final Properties propsIn) {
		super();
		try {
			init(propsIn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if (mod2Storm == null) {
			mod2Storm = new Model2Storm(spbd);
		}
		mod2Storm.sendToStormPath(null);

	}

	@Override
	public final void buildUserSecurity() throws Exception {
		if (spbd == null) {
			spbd = new StormPathBaseDTO(props);
			spbd.load();
		}
		if (storm2Mod == null) {
			storm2Mod = new Storm2Model(spbd);
		}
		setUserSecurity(storm2Mod.build());
	}

	public final void sendUserSecuritytoStormPath(
			final UserSecurity userSecurityIn) throws Exception {
		if (userSecurityIn != null) {
			setUserSecurity(userSecurityIn);
		}
		if (spbd == null) {
			spbd = new StormPathBaseDTO(props);
			spbd.load();
		}
		if (mod2Storm == null) {
			mod2Storm = new Model2Storm(spbd);
		}
		mod2Storm.sendToStormPath(getUserSecurity());
	}

	@Override
	public void saveUserSecurity() throws IOException {
	}

	public final Application getUsersApplication() {
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

	public final void setUsersApplication(final Application usersApplicationIn) {
		usersApplication = usersApplicationIn;
	}

	public final String getUsersAppName() {
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
	public final OtfAccount authAccount(final String acNameIn, final String pwIn) {
		Account acc = authSPAccount(acNameIn, pwIn);

		// CustomData cd = spdb.

		if (acc != null) {
			return getUserSecurity().getUserAccountByName(acNameIn);
		}
		return null;
	}

	private Account authSPAccount(final String acName, final String pw) {
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

	public final Properties getProps() {
		return props;
	}

	public final void setProps(final Properties propsIn) {
		props = propsIn;
	}

}
