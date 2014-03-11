package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseName {

	protected String name;
	private Status status;

	public String getName() {
		return name;
	}

	public void setName(String nameIn) {
		name = nameIn;
	}

	public Status getStatus() {
		if (status == null) {
			status = Status.ENABLED;
		}
		return status;
	}

	// public void setStatus(Status statusIn) {
	// status = statusIn;
	// }

	public void setStatus(String statusIn) {
		if (statusIn != null && statusIn.length() > 0) {
			try {
				status = Status.valueOf(statusIn);
			} catch (IllegalArgumentException iae) {
				// Ignore as will be set to enabled by default
			}
		}
	}

}
