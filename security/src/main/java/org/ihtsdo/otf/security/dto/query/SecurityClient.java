package org.ihtsdo.otf.security.dto.query;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.query.queries.AppPermDTO;
import org.ihtsdo.otf.security.dto.query.queries.MembersListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.UserAppPermsListQueryDTO;
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

	public String getUserAppPerms(String username, String appName,
			String membership) {

		return null;
	}

	public String getAppPermGroups(String json) {
		return null;
	}

	public String getAppPermGroupPerms(String json) {
		return null;
	}

}
