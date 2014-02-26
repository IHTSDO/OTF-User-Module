package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfApplications {

	private HashMap<String, OtfApplication> applications = new HashMap<String, OtfApplication>();

	public OtfApplications() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, OtfApplication> getApplications() {
		return applications;
	}

	public void setApplications(HashMap<String, OtfApplication> applicationsIn) {
		applications = applicationsIn;
	}

}
