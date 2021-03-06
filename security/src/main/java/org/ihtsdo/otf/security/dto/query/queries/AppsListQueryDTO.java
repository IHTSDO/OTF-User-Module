package org.ihtsdo.otf.security.dto.query.queries;

import java.util.Collections;
import java.util.List;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class AppsListQueryDTO extends AbstractSecurityQuery {

	private List<String> apps;

	public AppsListQueryDTO() {
		super();
	}

	public AppsListQueryDTO(final UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public final List<String> getApps() {
		if (apps == null || apps.isEmpty()) {
			if (ush != null) {
				apps = ush.getUserSecurityModel().getApps();
			}
		}
		Collections.sort(apps);
		return apps;
	}

	public final void setApps(final List<String> appsIn) {
		apps = appsIn;
	}

}
