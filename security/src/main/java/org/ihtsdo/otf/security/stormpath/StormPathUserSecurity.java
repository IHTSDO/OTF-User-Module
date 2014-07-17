package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;
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
		UserSecurity us = getStorm2Mod().build();
		us.setHandlerAdminDir(STORMPATH);
		setUserSecurity(us);
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

		getMod2Storm().sendToStormPath(getUserSecurity());
	}

	@Override
	public void saveUserSecurity() throws Exception {
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
	public final OtfAccount authAccountLocal(final String acNameIn,
			String pwIn, OtfAccount accIn) {
		Account acc = authSPAccount(acNameIn, pwIn);
		pwIn = null;
		if (acc != null) {
			return accIn;
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

	@Override
	public void localReload() {
		spbd = null;
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
			final OtfDirectory parentIn, final boolean isNewIn) {
		Directory mdir = getStorm2Mod().getDirByName(parentIn.getName());
		if (mdir == null) {
			return DIR_NOT_FOUND;
		}
		if (isNewIn) {
			getMod2Storm().buildAccount(accIn, null, mdir);
		} else {
			Account acc = getStorm2Mod()
					.getAccountByName(accIn.getName(), mdir);
			getMod2Storm().buildAccount(accIn, acc, mdir);
		}
		return REMOTE_COMMIT_OK;
	}

	@Override
	public String addUpdateGroupLocal(OtfGroup grpIn,
			OtfDirectory mDirectoryIn, boolean isNewIn) {
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
	public String addUpdateDirLocal(OtfDirectory dirIn, boolean isNewIn) {

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

	public final void setStorm2Mod(Storm2Model storm2ModIn) {
		storm2Mod = storm2ModIn;
	}

	public final Model2Storm getMod2Storm() {
		if (mod2Storm == null) {
			mod2Storm = new Model2Storm(spbd);
			mod2Storm.setUserSecurity(getUserSecurity());
		}
		return mod2Storm;
	}

	public final void setMod2Storm(Model2Storm mod2StormIn) {
		mod2Storm = mod2StormIn;
	}

}
