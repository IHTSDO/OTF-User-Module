package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class AppPermGroupsQueryDTO extends AbstractSecurityQuery {

	String appName;
	String groupName;
	List<GroupPermDTO> perms;

	public AppPermGroupsQueryDTO() {
		super();
	}

	public AppPermGroupsQueryDTO(UserSecurityHandler ushIn, String appNameIn) {
		super(ushIn);
		appName = appNameIn;
	}

	public AppPermGroupsQueryDTO(UserSecurityHandler ushIn, String appNameIn,
			String groupNameIn) {
		super(ushIn);
		groupName = groupNameIn;
		appName = appNameIn;
	}

	public List<GroupPermDTO> getPerms() {
		if (perms == null) {
			perms = new ArrayList<GroupPermDTO>();
		}

		if (perms.size() == 0) {
			if (ush != null) {
				for (OtfApplication app : ush.getUserSecurity().getApps()
						.getApplications().values()) {
					// app.getAccountStores().
				}

			}
		}
		return perms;
	}

	public void setPerms(List<GroupPermDTO> permsIn) {
		perms = permsIn;
	}

}
