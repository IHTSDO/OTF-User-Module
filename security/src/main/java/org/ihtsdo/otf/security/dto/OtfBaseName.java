package org.ihtsdo.otf.security.dto;

import java.util.UUID;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseName {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseName.class
			.getName());

	protected String name;
	private Status status;
	@JsonIgnore
	private String idref;
	private String id;

	// public static String ID_UUID_PREFIX = "UUID_";
	// public static final String URL_REF_SEP = "://";

	public String getName() {
		return name;
	}

	public final void setName(final String nameIn) {
		name = nameIn;
	}

	public final Status getStatus() {
		if (status == null) {
			status = Status.ENABLED;
		}
		return status;
	}

	// public void setStatus(Status statusIn) {
	// status = statusIn;
	// }

	public final void setStatus(final String statusIn) {
		if (statusIn != null && statusIn.length() > 0) {
			try {
				status = Status.valueOf(statusIn);
			} catch (IllegalArgumentException iae) {
				// Ignore as will be set to enabled by default
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Name:").append(name).append(", idRef:")
				.append(getIdref()).append(", id:").append(getId())
				.append(", Status:").append(status);
		return sbuild.toString();
	}

	public final String getIdref() {
		if (idref == null) {
			idref = "";
		}
		return idref;
	}

	public final void setIdref(String idrefIn) {
		idref = idrefIn;
	}

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

}
