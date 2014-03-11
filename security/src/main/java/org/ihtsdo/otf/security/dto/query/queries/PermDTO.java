package org.ihtsdo.otf.security.dto.query.queries;

public class PermDTO {
	public PermDTO(String keyIn, String valueIn) {
		super();
		key = keyIn;
		value = valueIn;
	}

	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String keyIn) {
		key = keyIn;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String valueIn) {
		value = valueIn;
	}

}
