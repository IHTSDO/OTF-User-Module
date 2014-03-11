package org.ihtsdo.otf.security.dto;

public class OtfAccount extends OftAccountMin {
	public OtfAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	private OtfCustomData custData = new OtfCustomData();

	public OtfCustomData getCustData() {
		return custData;
	}

	public void setCustData(OtfCustomData custDataIn) {
		custData = custDataIn;
	}
}
