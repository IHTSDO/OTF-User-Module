package org.ihtsdo.otf.security.dto;

import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldPerm;

public class OtfGroup extends OtfBaseAccountStore {

	private OtfCustomData custData = new OtfCustomData(
			OtfCustomData.CustomParentType.GROUP);
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(OtfGroup.class.getName());

	private boolean showCustData = true;
	private String grpDesc = "Role/Group";

	public OtfGroup() {
		super();
	}

	@Override
	@JsonIgnore
	public String getHtmlForm(String formName) {
		StringBuilder sbuild = new StringBuilder();

		if (!isNew() && showCustData) {
			getCustData().setModels(null);
			getCustData().getModels().add(new OtfCustomFieldPerm());
			// sbuild.append(getCustData().getHtmlForm(formName));
			sbuild.append(super.getHtmlForm(formName, getCustData()
					.getHtmlForm(formName)));
		} else {
			sbuild.append(super.getHtmlForm(formName));
		}
		return sbuild.toString();
	}

	@Override
	public void addTableRows() {
		super.addTableRows();
	}

	public final OtfCustomData getCustData() {
		// LOG.info("group getCustData href = " + custData.getIdref());
		// if (!stringOK(custData.getIdref())) {
		// new Exception().printStackTrace();
		// }
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
		StringBuilder sbuild = new StringBuilder();
		if (isNew()) {
			sbuild.append("Add New ");
		} else {
			sbuild.append("Update ");
		}
		sbuild.append(getGrpDesc());
		sbuild.append(": ");

		if (!isNew()) {
			sbuild.append(getName());
		}
		return sbuild.toString();
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();

	}

	public final boolean isShowCustData() {
		return showCustData;
	}

	public final void setShowCustData(boolean showCustDataIn) {
		showCustData = showCustDataIn;
	}

	public final String getGrpDesc() {
		return grpDesc;
	}

	public final void setGrpDesc(String grpDescIn) {
		grpDesc = grpDescIn;
	}

}
