package org.ihtsdo.otf.security.dto;

public class OftAccountMin extends OtfBaseName {
	public OftAccountMin() {
		super();
	}

	public OftAccountMin(OftAccountMin orig) {
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
	public String getName() {
		if (name == null || name.length() == 0) {
			name = email;
		}
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String emailIn) {
		email = emailIn;
	}

	// public HashMap<String, OtfCustomField> getCustFields() {
	// return custFields;
	// }

	public String getGivenName() {
		if (givenName == null || givenName.length() == 0) {
			givenName = "NoGivenNameSet";
		}
		return givenName;
	}

	public void setGivenName(String givenNameIn) {
		givenName = givenNameIn;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleNameIn) {
		middleName = middleNameIn;
	}

	public String getSurname() {
		if (surname == null || surname.length() == 0) {
			surname = getName();
		}
		return surname;
	}

	public void setSurname(String surnameIn) {
		surname = surnameIn;
	}

}
