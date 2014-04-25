package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserAppsListQueryDTO extends AbstractSecurityQuery {

	private List<String> apps;
	private String userName;

	public UserAppsListQueryDTO() {
		super();
	}

	public UserAppsListQueryDTO(final UserSecurityHandler userSecurityIn,
			final String userNameIn) {
		super(userSecurityIn);
		userName = userNameIn;
	}

	public final List<String> getApps() {
		if (apps == null) {
			apps = new ArrayList<String>();
		}

		if (apps.size() == 0) {
			if (ush != null) {
				OtfAccount oacc = ush.getUserSecurity().getUserAccountByName(
						userName, "*");
				if (oacc != null) {
					List<OtfCustomField> mems = oacc.getCustData().getApps();
					for (OtfCustomField cf : mems) {
						String val = cf.getVals()[1];
						if (!apps.contains(val)) {
							apps.add(val);
						}
					}
				}
			}
		}
		Collections.sort(apps);
		return apps;
	}

	public final void setApps(final List<String> appsIn) {
		apps = appsIn;
	}

}
