package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtfCustomData {

	public static final List<String> RESERVED_WORDS = new ArrayList<String>();
	private final HashMap<String, OtfCustomField> custFields = new HashMap<String, OtfCustomField>();

	public HashMap<String, OtfCustomField> getCustFields() {
		return custFields;
	}

	public static List<String> getReservedWords() {
		if (RESERVED_WORDS.size() == 0) {
			RESERVED_WORDS.add("href");
			RESERVED_WORDS.add("createdAt");
			RESERVED_WORDS.add("modifiedAt");
			RESERVED_WORDS.add("meta");
			RESERVED_WORDS.add("spMeta");
			RESERVED_WORDS.add("spmeta");
			RESERVED_WORDS.add("ionmeta");
			RESERVED_WORDS.add("ionMeta");
		}
		return RESERVED_WORDS;
	}
}
