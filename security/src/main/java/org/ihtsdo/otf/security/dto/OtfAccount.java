package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfAccount extends OtfBaseName {
	public OtfAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	private final HashMap<String, OtfCustomField> custFields = new HashMap<String, OtfCustomField>();

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

	public HashMap<String, OtfCustomField> getCustFields() {
		return custFields;
	}

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
