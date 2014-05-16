package org.ihtsdo.otf.security.dto.query;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.query.queries.AppPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppPermGroupsQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppUsersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.GroupPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.MembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppPermsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserByNameQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserMembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UsersListQueryDTO;

public class SecurityClient {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static List<String> getMembers(final String json) {
		try {
			MembersListQueryDTO mlq = MAPPER.readValue(json,
					MembersListQueryDTO.class);
			return mlq.getMembers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<OtfAccountMin> getUsers(final String json) {
		try {
			UsersListQueryDTO ulq = MAPPER.readValue(json,
					UsersListQueryDTO.class);
			return ulq.getUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getApps(final String json) {
		try {
			AppsListQueryDTO mlq = MAPPER.readValue(json,
					AppsListQueryDTO.class);
			return mlq.getApps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static OtfAccountMin getUserByName(final String json) {
		try {
			UserByNameQueryDTO ulq = MAPPER.readValue(json,
					UserByNameQueryDTO.class);
			return ulq.getUser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// public static OftAccountMin getUserByNameFull(String json) {
	// try {
	// UserByNameFullQueryDTO ulq = mapper.readValue(json,
	// UserByNameFullQueryDTO.class);
	// return ulq.getUser();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static List<String> getAppUsers(final String json) {
		try {
			AppUsersListQueryDTO mlq = MAPPER.readValue(json,
					AppUsersListQueryDTO.class);
			return mlq.getAppUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getUserApps(final String json) {
		try {
			UserAppsListQueryDTO mlq = MAPPER.readValue(json,
					UserAppsListQueryDTO.class);
			return mlq.getApps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getUserMemberships(final String json) {
		try {
			UserMembersListQueryDTO mlq = MAPPER.readValue(json,
					UserMembersListQueryDTO.class);
			return mlq.getMembers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<AppPermDTO> getUserAppPerms(final String json) {
		try {
			UserAppPermsListQueryDTO mlq = MAPPER.readValue(json,
					UserAppPermsListQueryDTO.class);
			return mlq.getPerms();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<GroupPermDTO> getAppPermGroups(final String json) {
		try {
			AppPermGroupsQueryDTO mlq = MAPPER.readValue(json,
					AppPermGroupsQueryDTO.class);
			return mlq.getPerms();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
