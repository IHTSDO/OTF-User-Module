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

	public static String ID_KEY = "id";

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
		return !stringOK(id);
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
		if (id == null || id.isEmpty()) {
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
		getHiddenRows().put(INPUT_KEY_NAME,
				getHtmlInputHidden(INPUT_KEY_NAME, getInputKey()));
		// add id if set
		getHiddenRows().put(ID_KEY, getHtmlInputHidden(ID_KEY, getIdIfSet()));
	}

	public String getInputKey() {
		return getClass().getName();
	}

	@Override
	public void setValsFromParams() {
		setId(getNotNullParam(ID_KEY));
	}
}
