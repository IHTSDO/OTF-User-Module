package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldBasic;
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
	private String grpDesc = null;
	private String grptype = null;
	// The name of the parent directory
	private String parentDirName;

	public static final String TYPE_MEMBER = "group_member";
	public static final String TYPE_SETTING = "group_setting";
	public static final String TYPE_NORMAL = "group_group";

	public static final String PARENT_DIR_NAME = "parentDir";

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
		sbuild.append("Parent Dir = ").append(getParentDirName()).append("\n");
		sbuild.append("GrpType =  = ").append(getGrptype()).append("\n");
		sbuild.append(custData.toString());
		return sbuild.toString();
	}

	@Override
	public void processParams() {
		// LOG.info("GROUP: ");
		printParams();
		resetErrors();
		validateParams();
		// If no errors then update
		if (errors.size() == 0) {
			LOG.info("Before " + this.toString());
			setValsFromParams();
			LOG.info("After " + this.toString());

		}

	}

	@Override
	public void validateParams() {
		resetErrors();
		super.validateParams();
		OtfCustomFieldBasic cfb = new OtfCustomFieldBasic();
		// Check if changed
		String nameIn = getNotNullParam(NAME_NAME);
		// Only check if changed
		if (!nameIn.equals(getName())) {
			List<String> names = new ArrayList<String>();
			LOG.info("GrpType = " + getGrptype());
			if (getGrptype().equals(TYPE_MEMBER)) {
				names = cfb.getMembers();
				LOG.info("members names size = " + names.size());
			}
			if (getGrptype().equals(TYPE_NORMAL)) {
				LOG.info("getParentDirName = " + getParentDirName());
				names = cfb.getRolesByDir(getParentDirName());
				LOG.info("roles names size = " + names.size());
			}
			checkWebFieldInList(nameIn, NAME_NAME, names, false,
					"Name must be unique");
		}

		LOG.info("OtfGroup validate num errs = " + getErrors().size());

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
		getHiddenRows().add(
				getHtmlInputHidden(PARENT_DIR_NAME, getParentDirName()));
	}

	public final boolean isShowCustData() {
		return showCustData;
	}

	public final void setShowCustData(boolean showCustDataIn) {
		showCustData = showCustDataIn;
	}

	public final String getGrpDesc() {
		if (grpDesc == null) {
			getGrptype();
		}
		// LOG.info("getGrpDesc " + grpDesc);
		return grpDesc;
	}

	public final void setGrpDesc(String grpDescIn) {
		// LOG.info("setGrpDesc " + grpDescIn);
		grpDesc = grpDescIn;
	}

	@Override
	@JsonIgnore
	public String getInputKey() {
		return getGrptype();
	}

	public final String getParentDirName() {
		if (parentDirName == null) {
			parentDirName = "";
		}
		return parentDirName;
	}

	public final void setParentDirName(String parentDirNameIn) {
		parentDirName = parentDirNameIn;
	}

	public final String getGrptype() {
		if (grptype == null) {
			setGrptype(TYPE_NORMAL);
		}
		return grptype;
	}

	public final void setGrptype(String grptypeIn) {
		grptype = grptypeIn;
		// LOG.info("setGrptype grptype = " + grptype);
		switch (grptype) {
		case TYPE_MEMBER:
			setShowCustData(false);
			setGrpDesc("Member");
			break;
		case TYPE_SETTING:
			setGrpDesc("Settings");
			break;
		default:
			grptype = TYPE_NORMAL;
			setGrpDesc("Role/Group");
		}
	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();
	}

}
