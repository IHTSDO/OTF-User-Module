package org.ihtsdo.otf.security.dto;

import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldMember;
import org.ihtsdo.otf.security.dto.query.SecurityService;

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
		getCustData().setHiddenRows(getHiddenRows());
		getCustData().addHiddenRows();
		// getHiddenRows().addAll(getCustData().getHiddenRows());
	}

	@Override
	@JsonIgnore
	public String getHtmlForm(String formName) {

		StringBuilder sbuild = new StringBuilder();
		// sbuild.append(super.getHtmlForm(formName));
		// add the hidden fields
		OtfCustomFieldMember cfm = new OtfCustomFieldMember();
		OtfCustomFieldApplication cfa = new OtfCustomFieldApplication();
		//
		getCustData().setModels(null);
		getCustData().getModels().add(cfm);
		getCustData().getModels().add(cfa);

		sbuild.append(cfa.getHiddenDivRoleOptions());
		// sbuild.append(getHiddenDiv(
		// getHtmlRemBtnAction(getCDTable(cfa.getLabelValuesMap()),
		// getCustData().getCssClass()), cfa.getNewFormId()));
		//
		// sbuild.append(getHiddenDiv(
		// getHtmlRemBtnAction(getCDTable(cfm.getLabelValuesMap()),
		// getCustData().getCssClass()), cfm.getNewFormId()));

		sbuild.append(super.getHtmlForm(formName,
				getCustData().getHtmlForm(formName)));

		return sbuild.toString();
	}

	@Override
	@JsonIgnore
	public String getInputKey() {
		return SecurityService.USERS;
	}

	@Override
	public void processParams() {
		// LOG.info("ACCOUNT: ");
		// printParams();
		resetErrors();
		validateParams();
		if (getCustData() != null) {
			custData.setParams(getParams());
			custData.processParams();
		}
		if (isNew()) {
			// update the vals as object can be dumped
			setValsFromParams();
		} else {
			// If no errors then update
			if (errors.size() == 0) {
				// LOG.info("Before " + this.toString());
				setValsFromParams();
				// LOG.info("After " + this.toString());
			}
		}
	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();

	}

	@Override
	public void validateParams() {
		super.validateParams();
	}

}
