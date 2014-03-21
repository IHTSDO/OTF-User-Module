package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfApplications {

	private HashMap<String, OtfApplication> applications = new HashMap<String, OtfApplication>();

	public OtfApplications() {
		super();
		// TODO Auto-generated constructor stub
	}

	public final HashMap<String, OtfApplication> getApplications() {
		return applications;
	}

	public final void setApplications(
			final HashMap<String, OtfApplication> applicationsIn) {
		applications = applicationsIn;
	}

	public final OtfApplication getAppByName(final String name) {
		return applications.get(name);
	}

}
