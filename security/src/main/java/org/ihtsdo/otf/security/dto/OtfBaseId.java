package org.ihtsdo.otf.security.dto;

import java.util.UUID;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseId extends OtfBaseWeb {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseName.class
			.getName());

	@JsonIgnore
	private String idref;
	private String id;

	public final String getIdref() {
		if (idref == null) {
			idref = "";
		}
		return idref;
	}

	public final void setIdref(String idrefIn) {
		idref = idrefIn;
	}

	@JsonIgnore
	public boolean isNew() {
		return id == null;
	}

	@JsonIgnore
	public final String getIdIfSet() {
		if (isNew()) {
			return "";
		} else {
			return getId();
		}
	}

	@JsonIgnore
	public final String getId() {
		if (id == null) {
			id = new StringBuilder().append(UUID.randomUUID().toString())
					.toString();
		}
		return id;
	}

	public final void setId(String idIn) {
		id = idIn;
	}

	@Override
	public void addHiddenRows() {
		// first add the name of the class
		getHiddenRows().add(
				getHtmlInputHidden(INPUT_KEY_NAME, getClass().getName()));
		// add id if set
		getHiddenRows().add(getHtmlInputHidden("id", getIdIfSet()));
	}
}
