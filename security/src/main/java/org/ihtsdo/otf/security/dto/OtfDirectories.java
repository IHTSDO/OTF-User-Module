package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfDirectories {

	private HashMap<String, OtfDirectory> directories = new HashMap<String, OtfDirectory>();

	public OtfDirectories() {
		super();

	}

	public final HashMap<String, OtfDirectory> getDirectories() {
		return directories;
	}

	public final void setDirectories(
			final HashMap<String, OtfDirectory> directoriesIn) {
		directories = directoriesIn;
	}

	public final OtfDirectory getDirByName(final String name) {
		return directories.get(name);
	}

}
