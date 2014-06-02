package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldParamDTO {

	private String paramKey;
	private String cd_type;
	private String localVal;
	private String id;
	private String val;
	private OtfCustomField.CustomType type;

	public OtfCustomFieldParamDTO(String paramKeyIn, String valIn) {
		super();
		setParamKey(paramKeyIn);
		val = valIn;
	}

	public final String getParamKey() {
		return paramKey;
	}

	public final void setParamKey(String paramKeyIn) {
		paramKey = paramKeyIn;
	}

	public final String getCd_type() {
		return cd_type;
	}

	public final void setCd_type(String cd_typeIn) {
		cd_type = cd_typeIn;
		setType(OtfCustomField.CustomType.getCustomTypeByString(cd_type));
	}

	public final String getLocalVal() {
		return localVal;
	}

	public final void setLocalVal(String localValIn) {
		localVal = localValIn;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String idIn) {
		id = idIn;
	}

	public final String getVal() {
		return val;
	}

	public final void setVal(String valIn) {
		val = valIn;
	}

	public final OtfCustomField.CustomType getType() {
		return type;
	}

	public final void setType(OtfCustomField.CustomType typeIn) {
		type = typeIn;
	}

}
