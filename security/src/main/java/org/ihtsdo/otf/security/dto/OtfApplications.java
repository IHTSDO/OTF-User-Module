package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

	public boolean appExists(String appName) {
		for (String key : applications.keySet()) {
			if (key.equalsIgnoreCase(appName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		List<String> keys = new ArrayList<String>(applications.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(applications.get(key).toString()).append("\n");
		}
		return sbuild.toString();

	}

}
