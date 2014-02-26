package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfGroup extends OtfBaseAccountStore {
	public OtfGroup() {
		super();
	}

	private final HashMap<String, OtfCustomField> custFields = new HashMap<String, OtfCustomField>();

	public HashMap<String, OtfCustomField> getCustFields() {
		return custFields;
	}

	// private OtfCustomData custData;

	// public OtfCustomData getCustData() {
	// return custData;
	// }
	//
	// public void setCustData(OtfCustomData custDataIn) {
	// custData = custDataIn;
	// }

}
