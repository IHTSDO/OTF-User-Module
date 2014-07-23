package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class AppUsersListQueryDTO extends AbstractSecurityQuery {

	private List<String> appUsers = new ArrayList<String>();
	private String appname;

	public AppUsersListQueryDTO() {
		super();
	}

	public AppUsersListQueryDTO(final UserSecurityHandler userSecurityIn,
			final String appnameIn) {
		super(userSecurityIn);
		appname = appnameIn;
	}

	public final List<String> getAppUsers() {

		if (appUsers == null) {
			appUsers = new ArrayList<String>();
		}

		if (!appUsers.isEmpty()) {
			if (ush != null) {
				for (OtfAccount ac : ush.getUserSecurity().getUsers("*")) {
					if (!ac.getCustData().getAppsByAppName(appname).isEmpty()) {
						String name = ac.getName();
						if (!appUsers.contains(name)) {
							appUsers.add(ac.getName());
						}
					}
				}
			}
		}
		Collections.sort(appUsers);
		return appUsers;
	}

	public final void setAppUsers(final List<String> appUsersIn) {
		appUsers = appUsersIn;
	}
}
