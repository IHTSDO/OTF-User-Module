package org.ihtsdo.otf.security.dto;

import java.util.HashMap;
import java.util.Map;

public class OtfApplications {

	private Map<String, OtfApplication> applications = new HashMap<String, OtfApplication>();

	public OtfApplications() {
		super();
	}

	public final Map<String, OtfApplication> getApplications() {
		return applications;
	}

	public final void setApplications(
			final Map<String, OtfApplication> applicationsIn) {
		applications = applicationsIn;
	}

	public final OtfApplication getAppByName(final String name) {
		return applications.get(name);
	}

}
