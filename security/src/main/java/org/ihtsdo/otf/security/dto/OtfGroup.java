package org.ihtsdo.otf.security.dto;

import java.util.Map;

public class OtfGroup extends OtfBaseAccountStore {

	private OtfCustomData custData = new OtfCustomData();

	public OtfGroup() {
		super();
	}

	@Override
	public String getHtmlForm() {
		return super.getHtmlForm();
	}

	@Override
	public void addTableRows() {
		super.addTableRows();
	}

	public final OtfCustomData getCustData() {
		return custData;
	}

	public final void setCustData(final OtfCustomData custDataIn) {
		custData = custDataIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Group:\n");
		sbuild.append(super.toString());
		sbuild.append(custData.toString());
		return sbuild.toString();

	}

	@Override
	public void processParams(Map<String, String> paramsIn) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTableTitle() {
		if (isNew()) {
			return "Add New:";
		} else {
			return "Update:";
		}
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();

	}

}
