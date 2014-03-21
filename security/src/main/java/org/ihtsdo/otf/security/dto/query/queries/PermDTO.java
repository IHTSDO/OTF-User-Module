package org.ihtsdo.otf.security.dto.query.queries;

public class PermDTO {
	private String key;
	private String value;

	public PermDTO() {
		super();

	}

	public PermDTO(final String keyIn, final String valueIn) {
		super();
		key = keyIn;
		value = valueIn;
	}

	public final String getKey() {
		return key;
	}

	public final void setKey(final String keyIn) {
		key = keyIn;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(final String valueIn) {
		value = valueIn;
	}

}
