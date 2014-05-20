package org.ihtsdo.otf.security.dto;

import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldMember;

public class OtfAccount extends OtfAccountMin {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfAccount.class
			.getName());

	private OtfCustomData custData = new OtfCustomData(
			OtfCustomData.CustomParentType.ACCOUNT);

	private final OtfCachedListsDTO ocache = new OtfCachedListsDTO();

	public OtfAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public final OtfCustomData getCustData() {
		// LOG.info("OtfAccount getCustData href = " + custData.getIdref());
		return custData;
	}

	public final void setCustData(final OtfCustomData custDataIn) {
		custData = custDataIn;
	}

	@Override
	public String toString() {

		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Account:\n").append(super.toString()).append("\n");
		sbuild.append(custData.toString());
		return sbuild.toString();

	}

	@Override
	public void processParams(Map<String, String> paramsIn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableRows() {
		super.addTableRows();
	}

	@Override
	public String getTableTitle() {
		return super.getTableTitle();
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();

	}

	@Override
	@JsonIgnore
	public String getHtmlForm(String formName) {

		StringBuilder sbuild = new StringBuilder();
		// sbuild.append(super.getHtmlForm(formName));
		// add the hidden fields

		//
		getCustData().setModels(null);
		getCustData().getModels().add(new OtfCustomFieldMember());
		getCustData().getModels().add(new OtfCustomFieldApplication());

		sbuild.append(super.getHtmlForm(formName,
				getCustData().getHtmlForm(formName)));

		return sbuild.toString();
	}

}
