package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtfDirectories {

	private Map<String, OtfDirectory> directories = new HashMap<String, OtfDirectory>();

	public OtfDirectories() {
		super();

	}

	public final Map<String, OtfDirectory> getDirectories() {
		return directories;
	}

	public final void setDirectories(
			final Map<String, OtfDirectory> directoriesIn) {
		directories = directoriesIn;
	}

	public final OtfDirectory getDirByName(final String name) {
		return directories.get(name);
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		List<String> keys = new ArrayList<String>(directories.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(directories.get(key).toString()).append("\n");
		}

		return sbuild.toString();
	}

}
