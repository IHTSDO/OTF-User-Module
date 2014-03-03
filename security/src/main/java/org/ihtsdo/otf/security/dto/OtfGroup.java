package org.ihtsdo.otf.security.dto;

public class OtfGroup extends OtfBaseAccountStore {

	private OtfCustomData custData = new OtfCustomData();

	public OtfGroup() {
		super();
	}

	// private final HashMap<String, OtfCustomField> custFields = new
	// HashMap<String, OtfCustomField>();

	// public HashMap<String, OtfCustomField> getCustFields() {
	// return custFields;
	// }

	public OtfCustomData getCustData() {
		return custData;
	}

	public void setCustData(OtfCustomData custDataIn) {
		custData = custDataIn;
	}

}
