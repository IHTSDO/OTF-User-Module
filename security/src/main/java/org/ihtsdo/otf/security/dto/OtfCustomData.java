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
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldParamDTO;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldPerm;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;

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
	public static final String CD_KEY_ID_LIST = "Cd_Key_id_list";
	public static final String CD_KEY_ID_LIST_SEP = ",";

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

	}

	@Override
	public void addHiddenRows() {
		getHiddenRows().put(CD_KEY_ID_LIST,
				getHtmlInputHidden(CD_KEY_ID_LIST, getCDKeyList()));
	}

	private String getCDKeyList() {
		StringBuilder sbuild = new StringBuilder();
		for (String key : custFields.keySet()) {
			sbuild.append(key).append(CD_KEY_ID_LIST_SEP);
		}
		return sbuild.toString();
	}

	@Override
	@JsonIgnore
	public String getTableTitle() {

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
		// Add heading
		sbuild.append(getSubFormHead(cfType.getCollectionTitle()));

		String id = UUID.randomUUID().toString();
		String js = getJavaScriptAddRow(id, cfType.getNewFormURLWithContext());

		sbuild.append(getHtmlAddRowBtn(cfType.getNewTitle(), js));

		sbuild.append(getHtmlDivHeadId(id, cfType.getNewTitle())).append("\n");

		for (OtfCustomField cf : cfields) {
			String row = getCDTable(cf.getModel().getLabelValuesMap());
			sbuild.append(getHtmlRemBtnAction(row, cssClass));
		}

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
	}

	private List<String> getInitalCdIdList(String fullVal) {
		List<String> ids = new ArrayList<String>();

		String[] vals = fullVal.split(CD_KEY_ID_LIST_SEP);
		for (String val : vals) {
			if (stringOK(val)) {
				ids.add(val);
			}
		}

		return ids;
	}

	@Override
	public void processParams() {
		List<OtfCustomFieldParamDTO> cfpList = new ArrayList<OtfCustomFieldParamDTO>();
		List<String> initialIds = new ArrayList<String>();
		List<String> newIds = new ArrayList<String>();

		for (String key : params.keySet()) {
			if (key.startsWith(OtfCustomField.CustomType.CD_TYPE.toString())) {
				String val = params.get(key);
				OtfCustomFieldParamDTO cfp = new OtfCustomFieldParamDTO(key,
						val);
				cfpList.add(cfp);
			}
			if (key.equals(CD_KEY_ID_LIST)) {
				initialIds = getInitalCdIdList(params.get(key));
			}
			// iterate seeing if cd id is in cust fields
			for (OtfCustomFieldParamDTO cfp : cfpList) {
				// See if the OftCustomField exists in getCustFields
				String cfpId = cfp.getId();
				newIds.add(cfpId);
				if (getCustFields().containsKey(cfpId)) {
					OtfCustomField oft = getCustFields().get(cfpId);
					oft.setModelvalFromParamDTO(cfp);
				} else {
					// new
					OtfCustomField oft = new OtfCustomField();
					oft.setKey(cfpId);
					oft.setModelvalFromParamDTO(cfp);
					getCustFields().put(cfpId, oft);
				}
			}

		}
		// Remove
		// if id not in list.....
		for (String initId : initialIds) {
			if (!newIds.contains(initId)) {
				getCustFields().remove(initId);
			}
		}

		// iterate custfields updating from model
		for (OtfCustomField ocf : getCustFields().values()) {
			ocf.setValueFromModel();
		}
	}

	@Override
	public void validateParams() {

	}
}
