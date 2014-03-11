package org.ihtsdo.otf.security.dto.query;

import java.io.IOException;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.query.queries.MembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppPermsListQueryDTO;
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

	// For use when acting as a server
	public SecurityService(UserSecurityHandler ushIn) {
		super();
		ush = ushIn;
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

	public String getUserByName(String username, String password) {
		UserByNameQueryDTO ubn = new UserByNameQueryDTO(ush, username, password);
		String json = GetJSonFromObject(ubn);
		// LOG.info("JSON = " + json);
		return json;

	}

	public String getUserMemberships(String username) {
		UserMembersListQueryDTO uml = new UserMembersListQueryDTO(ush, username);
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
		UserMembersListQueryDTO uml = new UserMembersListQueryDTO(ush, appName);
		String json = GetJSonFromObject(uml);
		LOG.info("JSON = " + json);
		return json;
	}

	// public String getAppPermGroupPerms(String appName, String groupName) {
	// UserMembersListQueryDTO uml = new UserMembersListQueryDTO(ush, appName,
	// groupName);
	// String json = GetJSonFromObject(uml);
	// LOG.info("JSON = " + json);
	// return json;
	// }

	private String GetJSonFromObject(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

}
