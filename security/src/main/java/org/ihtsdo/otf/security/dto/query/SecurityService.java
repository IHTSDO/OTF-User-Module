package org.ihtsdo.otf.security.dto.query;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.UserSecurityHandler;
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
	public static final String GET_MEMBERS = "getMembers";
	public static final String GET_USERS = "getUsers";
	public static final String GET_USER_BY_NAME = "getUserByName";
	public static final String GET_USER_MEMBERSHIPS = "getUserMemberships";
	public static final String GET_USER_APP_PERMS = "getUserAppPerms";
	public static final String GET_APP_PERM_GROUPS = "getAppPermGroups";

	public static final String GET_APPS = "getApps";
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
	public SecurityService(UserSecurityHandler ushIn) {
		super();
		ush = ushIn;
	}

	public String getQueryResultFromQueryDTO(SecurityQueryDTO sqd) {

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

		case GET_USER_BY_NAME:
			if (stringOK(username) && stringOK(password)) {
				retVal = getUserByName(username, password);
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

	public String getMembers() {

		MembersListQueryDTO mlq = new MembersListQueryDTO(ush);
		String json = GetJSonFromObject(mlq);
		// LOG.info("JSON = " + json);
		return json;

	}

	public String getUsers() {

		UsersListQueryDTO ulq = new UsersListQueryDTO(ush);
		// LOG.info("ulq size = " + ulq.getUsers().size());
		String json = GetJSonFromObject(ulq);
		// LOG.info("JSON = " + json);
		return json;

	}

	public String getApps() {

		AppsListQueryDTO ulq = new AppsListQueryDTO(ush);
		// LOG.info("ulq size = " + ulq.getUsers().size());
		String json = GetJSonFromObject(ulq);
		// LOG.info("JSON = " + json);
		return json;

	}

	public String getAppUsers(String appname) {

		AppUsersListQueryDTO ulq = new AppUsersListQueryDTO(ush, appname);
		// LOG.info("ulq size = " + ulq.getUsers().size());
		String json = GetJSonFromObject(ulq);
		// LOG.info("JSON = " + json);
		return json;

	}

	public String getUserByName(String username, String password) {
		UserByNameQueryDTO ubn = new UserByNameQueryDTO(ush, username, password);
		String json = GetJSonFromObject(ubn);
		// LOG.info("JSON = " + json);
		return json;

	}

	// TODO the full account uses abstract ypes (model) which the json engine
	// does not like.

	// public String getUserByNameFull(String username) {
	// UserByNameFullQueryDTO ubn = new UserByNameFullQueryDTO(ush, username,
	// null);
	// String json = GetJSonFromObject(ubn);
	// // LOG.info("JSON = " + json);
	// return json;
	//
	// }

	public String getUserMemberships(String username) {
		UserMembersListQueryDTO uml = new UserMembersListQueryDTO(ush, username);
		String json = GetJSonFromObject(uml);
		// LOG.info("JSON = " + json);
		return json;
	}

	public String getUserApps(String username) {
		UserAppsListQueryDTO uml = new UserAppsListQueryDTO(ush, username);
		String json = GetJSonFromObject(uml);
		// LOG.info("JSON = " + json);
		return json;
	}

	public String getUserAppPerms(String username, String appName) {
		UserAppPermsListQueryDTO uml = new UserAppPermsListQueryDTO(ush,
				username, appName);
		String json = GetJSonFromObject(uml);
		// LOG.info("getUserAppPerms JSON = " + json);
		return json;
	}

	public String getUserAppPerms(String username, String appName, String member) {
		UserAppPermsListQueryDTO uml = new UserAppPermsListQueryDTO(ush,
				username, appName, member);
		String json = GetJSonFromObject(uml);
		// LOG.info("JSON = " + json);
		return json;
	}

	public String getAppPermGroups(String appName) {
		AppPermGroupsQueryDTO uml = new AppPermGroupsQueryDTO(ush, appName);
		String json = GetJSonFromObject(uml);
		// LOG.info("getAppPermGroups appname JSON = " + json);
		return json;
	}

	public String getAppPermGroups(String appName, String groupName) {
		AppPermGroupsQueryDTO uml = new AppPermGroupsQueryDTO(ush, appName,
				groupName);
		String json = GetJSonFromObject(uml);
		// LOG.info("getAppPermGroups app grp JSON = " + json);
		return json;
	}

	public String GetJSonFromObject(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Problem getting JSON from Object", e);
		}
		return null;
	}

	public static final boolean stringOK(String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

}
