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

	public final OtfCustomData getCustData() {
		return custData;
	}

	public final void setCustData(final OtfCustomData custDataIn) {
		custData = custDataIn;
	}

}
