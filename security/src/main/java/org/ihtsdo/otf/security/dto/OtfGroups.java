package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtfGroups {
	private final Map<String, OtfGroup> groups = new HashMap<String, OtfGroup>();

	public OtfGroups() {
		super();
	}

	public final Map<String, OtfGroup> getGroups() {
		return groups;
	}

	public final OtfGroup getGroupByName(final String name) {
		return groups.get(name);
	}

	public boolean groupExists(String grpName) {
		for (String key : groups.keySet()) {
			if (key.equalsIgnoreCase(grpName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Groups:\n");
		List<String> keys = new ArrayList<String>(groups.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(groups.get(key).toString()).append("\n");
		}
		return sbuild.toString();
	}

}
