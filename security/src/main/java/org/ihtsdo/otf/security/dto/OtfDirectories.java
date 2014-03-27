package org.ihtsdo.otf.security.dto;

import java.util.HashMap;
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

}
