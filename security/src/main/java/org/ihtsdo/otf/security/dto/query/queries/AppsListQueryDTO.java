package org.ihtsdo.otf.security.dto.query.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class AppsListQueryDTO extends AbstractSecurityQuery {

	List<String> apps;

	public AppsListQueryDTO() {
		super();
	}

	public AppsListQueryDTO(UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public List<String> getApps() {
		if (apps == null || apps.size() == 0) {
			if (ush != null) {
				apps = new ArrayList<String>();
				for (OtfApplication app : ush.getUserSecurity().getApps()
						.getApplications().values()) {
					String val = app.getName();
					if (!apps.contains(val)) {
						apps.add(app.getName());
					}
				}
			}
		}
		Collections.sort(apps);
		return apps;
	}

	public void setApps(List<String> appsIn) {
		apps = appsIn;
	}

}
