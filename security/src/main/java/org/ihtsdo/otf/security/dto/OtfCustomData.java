package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldBasic;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldMember;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldModel;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldPerm;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;

//TODO Pull the html out to specific html class
public class OtfCustomData extends OtfBaseId {

	public OtfCustomData(CustomParentType parentTypeIn) {
		super();
		parentType = parentTypeIn;
	}

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfCustomData.class
			.getName());

	public static final List<String> RESERVED_WORDS = new ArrayList<String>();
	private final Map<String, OtfCustomField> custFields = new HashMap<String, OtfCustomField>();

	private CustomParentType parentType;
	public static String cssClass = "CustomDataTable";
	private List<OtfCustomFieldModel> models = new ArrayList<OtfCustomFieldModel>();

	public final CustomParentType getParentType() {
		return parentType;
	}

	public final void setParentType(CustomParentType parentTypeIn) {
		parentType = parentTypeIn;
	}

	/**
	 * Used to indicate the type of the parent and thus what cust field types
	 * can belong within.
	 */
	public static enum CustomParentType {
		/** NO VALUE. */
		DEFAULT,
		/** An Account */
		ACCOUNT,
		/** A Group. */
		GROUP
	}

	public final Map<String, OtfCustomField> getCustFields() {
		return custFields;
	}

	public static List<String> getReservedWords() {
		if (RESERVED_WORDS.size() == 0) {
			RESERVED_WORDS.add("href");
			RESERVED_WORDS.add("createdAt");
			RESERVED_WORDS.add("modifiedAt");
			RESERVED_WORDS.add("meta");
			RESERVED_WORDS.add("spMeta");
			RESERVED_WORDS.add("spmeta");
			RESERVED_WORDS.add("ionmeta");
			RESERVED_WORDS.add("ionMeta");
		}
		return RESERVED_WORDS;
	}

	public final List<OtfCustomField> getFieldsByType(
			final OtfCustomField.CustomType type) {
		List<OtfCustomField> fields = new ArrayList<OtfCustomField>();
		for (OtfCustomField cf : custFields.values()) {
			if (cf.getType().equals(type)) {
				fields.add(cf);
			}
		}
		return fields;
	}

	public final List<OtfCustomField> getDefaults() {
		return getFieldsByType(OtfCustomField.CustomType.CD_TYPE);
	}

	public final List<OtfCustomField> getMembers() {
		return getFieldsByType(OtfCustomField.CustomType.CD_TYPE_MEMBER);
	}

	public final List<OtfCustomField> getPerms() {
		return getFieldsByType(OtfCustomField.CustomType.CD_TYPE_PERM);
	}

	public final List<OtfCustomField> getApps() {
		return getFieldsByType(OtfCustomField.CustomType.CD_TYPE_APP);
	}

	public final List<OtfCustomField> getSettings() {
		return getFieldsByType(OtfCustomField.CustomType.CD_TYPE_SETTING);
	}

	public final List<OtfCustomField> getAppsByAppName(final String appName) {

		List<OtfCustomField> allApps = getApps();

		List<OtfCustomField> allAppsByName = new ArrayList<OtfCustomField>();

		for (OtfCustomField cf : allApps) {
			if (cf.getModel().getVals()[1].equals(appName)) {
				allAppsByName.add(cf);
			}
		}

		return allAppsByName;
	}

	public final boolean isaMemberOf(final String member) {

		for (OtfCustomField cf : getMembers()) {
			if (cf.getVals()[1].equalsIgnoreCase(member)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("CustomFields:\n");
		List<String> keys = new ArrayList<String>(custFields.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(custFields.get(key).toString()).append("\n");
		}

		return sbuild.toString();

	}

	@Override
	public void addTableRows() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHiddenRows() {
		// TODO Auto-generated method stub

	}

	@Override
	@JsonIgnore
	public String getTableTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@JsonIgnore
	protected String getHtmlForm(String formName) {
		StringBuilder sbuild = new StringBuilder();
		for (OtfCustomFieldModel model : getModels()) {
			sbuild.append(getHtmlForm(model));
		}
		return sbuild.toString();
	}

	public String getHtmlForm(OtfCustomFieldModel model) {

		switch (model.getType()) {
		case CD_TYPE_MEMBER:
			return getMembersForm();
		case CD_TYPE_PERM:
			return getPermsForm();
		case CD_TYPE_APP:
			return getAppsForm();
		default:
			return getDefaultForm();
		}
	}

	private String getDefaultForm() {
		return getCdForm(new OtfCustomFieldBasic(), custFields.values());
	}

	private String getMembersForm() {
		return getCdForm(new OtfCustomFieldMember(), getMembers());
	}

	private String getAppsForm() {

		// StringBuilder sbuild = new StringBuilder();
		// OtfCustomFieldApplication cfa = new OtfCustomFieldApplication();
		// // sbuild.append(cfa.getHiddenDivRoleOptions());
		// sbuild.append(getCdForm(cfa, getApps()));
		// return sbuild.toString();
		return getCdForm(new OtfCustomFieldApplication(), getApps());
	}

	private String getPermsForm() {
		return getCdForm(new OtfCustomFieldPerm(), getPerms());
	}

	private String getSettingsForm() {
		return getCdForm(new OtfCustomFieldSetting(), getSettings());
	}

	private String getCdForm(OtfCustomFieldModel cfType,
			Collection<OtfCustomField> cfields) {
		StringBuilder sbuild = new StringBuilder();
		// getLabelValuesMap()

		// String emptyRow = getCDTable(cfType.getLabelValuesMap());
		// if (emptyRow == null) {
		// LOG.info("empty row = null");
		// emptyRow = "";
		// }

		// String incRemBtn = getHtmlRemBtnAction(emptyRow, cssClass);
		// LOG.info("getCdForm incRemBtn = " + incRemBtn);
		// String result = StringEscapeUtils.escapeHtml4(incRemBtn);
		// Add heading
		sbuild.append(getSubFormHead(cfType.getCollectionTitle()));

		String id = UUID.randomUUID().toString();

		// sbuild.append(getHiddenDiv(
		// getHtmlRemBtnAction(getCDTable(cfType.getLabelValuesMap()),
		// cssClass), cfType.getNewFormId()));

		// getJavaScriptAddRow(String formID, String rowToAppend)
		String js = getJavaScriptAddRow(id, cfType.getNewFormURLWithContext());
		// LOG.info("getCdForm js = " + js);

		// getHtmlAddRowBtn(String btnTitle, String onClickAction)

		sbuild.append(getHtmlAddRowBtn(cfType.getNewTitle(), js));

		// sbuild.append(getHtmlFormHeadId(id, cfType.getNewTitle(),
		// getAction()))
		// .append("\n");

		sbuild.append(getHtmlDivHeadId(id, cfType.getNewTitle())).append("\n");

		for (OtfCustomField cf : cfields) {
			String row = getCDTable(cf.getModel().getLabelValuesMap());
			// LOG.info("getCdForm row = " + row);
			sbuild.append(getHtmlRemBtnAction(row, cssClass));
		}

		// Add AddRow Button
		// sbuild.append(HTML_FORM_CLOSE);
		sbuild.append(HTML_DIV_CLOSE);
		return getFormDiv(sbuild.toString());
	}

	public final String getCssClass() {
		return cssClass;
	}

	public final void setCssClass(String cssClassIn) {
		cssClass = cssClassIn;
	}

	public final List<OtfCustomFieldModel> getModels() {
		if (models == null) {
			models = new ArrayList<OtfCustomFieldModel>();
		}
		return models;
	}

	public final void setModels(List<OtfCustomFieldModel> modelsIn) {
		models = modelsIn;
	}

	@Override
	public void setValsFromParams() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processParams() {

		for (String key : params.keySet()) {
			// if string startsWith(OtfCustomField.CustomType.CD_TYPE
			if (key.startsWith(OtfCustomField.CustomType.CD_TYPE.toString())) {

			}
		}

	}

	@Override
	public void validateParams() {
		// TODO Auto-generated method stub

	}
}
