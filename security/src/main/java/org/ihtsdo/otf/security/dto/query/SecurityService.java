package org.ihtsdo.otf.security.dto.query;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.query.queries.AppPermGroupsQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppUsersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.MembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppPermsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserByNameQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserMembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UsersListQueryDTO;

public class SecurityService {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(SecurityService.class
			.getName());
	private final UserSecurityHandler ush;
	private final ObjectMapper mapper = new ObjectMapper();

	// QueryNames
	public static final String GET_USER_BY_NAME_AUTH = "getUserByNameAuth";
	// Single Arg
	public static final String GET_MEMBERS = "getMembers";
	public static final String GET_USERS = "getUsers";
	public static final String GET_APPS = "getApps";
	// Screens
	public static final String MEMBERS = "members";
	public static final String USERS = "users";
	public static final String APPS = "apps";
	public static final String PERMS = "perms";
	public static final String SETTINGS = "settings";
	public static final String DIR = "dir";

	public static final String GET_USER_BY_NAME = "getUserByName";
	public static final String GET_USER_MEMBERSHIPS = "getUserMemberships";
	public static final String GET_USER_APP_PERMS = "getUserAppPerms";
	public static final String GET_APP_PERM_GROUPS = "getAppPermGroups";

	public static final String GET_USER_APPS = "getUserApps";
	public static final String GET_APP_USERS = "getAppUsers";

	// Leaving this one for now as a problem wrt json and abstract types
	public static final String GET_FULL_USER = "getFullUser";

	// ArgNames:
	public static final String APP_NAME = "appName";
	public static final String GRP_NAME = "groupName";
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";
	public static final String MEMBER = "member";

	// For use when acting as a server
	public SecurityService(final UserSecurityHandler ushIn) {
		super();
		ush = ushIn;
	}

	public final String getQueryResultFromQueryDTO(final SecurityQueryDTO sqd) {

		String retVal = null;
		String username = sqd.getArgs().get(USER_NAME);
		String password = sqd.getArgs().get(PASSWORD);
		String appName = sqd.getArgs().get(APP_NAME);
		String groupName = sqd.getArgs().get(GRP_NAME);
		String member = sqd.getArgs().get(MEMBER);

		switch (sqd.getQueryName()) {
		case GET_MEMBERS:
			retVal = getMembers();
			break;
		case GET_USERS:
			retVal = getUsers();
			break;

		case GET_APP_USERS:
			if (stringOK(appName)) {
				retVal = getAppUsers(appName);
			}
			break;
		case GET_APPS:
			retVal = getApps();
			break;

		case GET_USER_BY_NAME_AUTH:
			if (stringOK(username) && stringOK(password)) {
				retVal = getUserByName(username, password);
			}
			break;
		case GET_USER_BY_NAME:
			if (stringOK(username)) {
				retVal = getUserByName(username);
			}
			break;

		case GET_USER_MEMBERSHIPS:
			if (stringOK(username)) {
				retVal = getUserMemberships(username);
			}
			break;

		case GET_USER_APPS:
			if (stringOK(username)) {
				retVal = getUserApps(username);
			}
			break;
		case GET_USER_APP_PERMS:
			if (stringOK(username) && stringOK(appName) && stringOK(member)) {
				retVal = getUserAppPerms(username, appName, member);
			}
			if (stringOK(username) && stringOK(appName) && !stringOK(member)) {
				retVal = getUserAppPerms(username, appName);
			}
			break;
		case GET_APP_PERM_GROUPS:
			if (stringOK(appName) && stringOK(groupName)) {
				retVal = getAppPermGroups(appName, groupName);
			}
			if (stringOK(appName) && !stringOK(groupName)) {
				retVal = getAppPermGroups(appName);
			}
			break;
		default:
			retVal = null;
		}

		return retVal;

	}

	public final String getMembers() {

		MembersListQueryDTO mlq = new MembersListQueryDTO(ush);
		String json = getJSonFromObject(mlq);
		return json;

	}

	public final String getUsers() {
		UsersListQueryDTO ulq = new UsersListQueryDTO(ush);
		String json = getJSonFromObject(ulq);
		return json;

	}

	public final String getApps() {

		AppsListQueryDTO ulq = new AppsListQueryDTO(ush);
		String json = getJSonFromObject(ulq);
		return json;

	}

	public final String getAppUsers(final String appname) {

		AppUsersListQueryDTO ulq = new AppUsersListQueryDTO(ush, appname);
		String json = getJSonFromObject(ulq);
		return json;

	}

	public final String getUserByName(final String username,
			final String password) {
		UserByNameQueryDTO ubn = new UserByNameQueryDTO(ush, username, password);
		if (ubn.getUser() == null) {
			return null;
		}

		String json = getJSonFromObject(ubn);

		ubn.getUser().setAuth(false);
		return json;

	}

	public final String getUserByName(final String username) {

		OtfAccount oacc = ush.getUserSecurityModel().getUserAccountByName(
				username);

		if (oacc != null) {
			OtfAccountMin user = new OtfAccountMin(oacc);
			String json = getJSonFromObject(user);
			return json;
		}

		return null;

	}

	public final String getUserMemberships(final String username) {
		UserMembersListQueryDTO uml = new UserMembersListQueryDTO(ush, username);
		String json = getJSonFromObject(uml);
		return json;
	}

	public final String getUserApps(final String username) {
		UserAppsListQueryDTO uml = new UserAppsListQueryDTO(ush, username);
		String json = getJSonFromObject(uml);
		return json;
	}

	public final String getUserAppPerms(final String username,
			final String appName) {
		UserAppPermsListQueryDTO uml = new UserAppPermsListQueryDTO(ush,
				username, appName);
		String json = getJSonFromObject(uml);
		return json;
	}

	public final String getUserAppPerms(final String username,
			final String appName, final String member) {
		UserAppPermsListQueryDTO uml = new UserAppPermsListQueryDTO(ush,
				username, appName, member);
		String json = getJSonFromObject(uml);
		return json;
	}

	public final String getAppPermGroups(final String appName) {
		AppPermGroupsQueryDTO uml = new AppPermGroupsQueryDTO(ush, appName);
		String json = getJSonFromObject(uml);
		return json;
	}

	public final String getAppPermGroups(final String appName,
			final String groupName) {
		AppPermGroupsQueryDTO uml = new AppPermGroupsQueryDTO(ush, appName,
				groupName);
		String json = getJSonFromObject(uml);
		return json;
	}

	public final String getJSonFromObject(final Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Problem getting JSON from Object", e);
		}
		return null;
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

}
