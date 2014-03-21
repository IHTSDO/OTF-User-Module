package org.ihtsdo.otf.security.dto;

public class OftAccountMin extends OtfBaseName {
	public OftAccountMin() {
		super();
	}

	public OftAccountMin(final OftAccountMin orig) {
		super();
		name = orig.getName();
		email = orig.getEmail();
		givenName = orig.getGivenName();
		middleName = orig.getMiddleName();
		surname = orig.getSurname();
		setStatus(orig.getStatus().toString());
	}

	private String email;
	private String givenName;
	private String middleName;
	private String surname;

	@Override
	public final String getName() {
		if (name == null || name.length() == 0) {
			name = email;
		}
		return name;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(final String emailIn) {
		email = emailIn;
	}

	// public HashMap<String, OtfCustomField> getCustFields() {
	// return custFields;
	// }

	public final String getGivenName() {
		if (givenName == null || givenName.length() == 0) {
			givenName = "NoGivenNameSet";
		}
		return givenName;
	}

	public final void setGivenName(final String givenNameIn) {
		givenName = givenNameIn;
	}

	public final String getMiddleName() {
		return middleName;
	}

	public final void setMiddleName(final String middleNameIn) {
		middleName = middleNameIn;
	}

	public final String getSurname() {
		if (surname == null || surname.length() == 0) {
			surname = getName();
		}
		return surname;
	}

	public final void setSurname(final String surnameIn) {
		surname = surnameIn;
	}

}
