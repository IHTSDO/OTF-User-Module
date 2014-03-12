package org.ihtsdo.otf.security.dto.query;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.query.queries.AppPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppPermGroupsQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.AppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.GroupPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.MembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppPermsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserByNameQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserMembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UsersListQueryDTO;

public class SecurityClient {

	private final static ObjectMapper mapper = new ObjectMapper();

	public static List<String> getMembers(String json) {
		try {
			MembersListQueryDTO mlq = mapper.readValue(json,
					MembersListQueryDTO.class);
			return mlq.getMembers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<OftAccountMin> getUsers(String json) {
		try {
			UsersListQueryDTO ulq = mapper.readValue(json,
					UsersListQueryDTO.class);
			return ulq.getUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getApps(String json) {
		try {
			AppsListQueryDTO mlq = mapper.readValue(json,
					AppsListQueryDTO.class);
			return mlq.getApps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static OftAccountMin getUserByName(String json) {
		try {
			UserByNameQueryDTO ulq = mapper.readValue(json,
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

	public static List<String> getUserApps(String json) {
		try {
			UserAppsListQueryDTO mlq = mapper.readValue(json,
					UserAppsListQueryDTO.class);
			return mlq.getApps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getUserMemberships(String json) {
		try {
			UserMembersListQueryDTO mlq = mapper.readValue(json,
					UserMembersListQueryDTO.class);
			return mlq.getMembers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<AppPermDTO> getUserAppPerms(String json) {
		try {
			UserAppPermsListQueryDTO mlq = mapper.readValue(json,
					UserAppPermsListQueryDTO.class);
			return mlq.getPerms();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<GroupPermDTO> getAppPermGroups(String json) {
		try {
			AppPermGroupsQueryDTO mlq = mapper.readValue(json,
					AppPermGroupsQueryDTO.class);
			return mlq.getPerms();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
