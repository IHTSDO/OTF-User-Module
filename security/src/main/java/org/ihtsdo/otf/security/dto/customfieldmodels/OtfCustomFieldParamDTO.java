package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldParamDTO {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(OtfCustomFieldParamDTO.class.getName());

	private String paramKey;
	private String cdType;
	private String localKey;
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
		paramKeyToArray();
	}

	public final String getCdType() {
		return cdType;
	}

	public final void setCdType(String cdTypeIn) {
		cdType = cdTypeIn;
		setType(OtfCustomField.CustomType.getCustomTypeByString(cdType));
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

	public final String getLocalKey() {
		return localKey;
	}

	public final void setLocalKey(String localKeyIn) {
		localKey = localKeyIn;
	}

	public final OtfCustomField.CustomType getType() {
		return type;
	}

	public final void setType(OtfCustomField.CustomType typeIn) {
		type = typeIn;
	}

	public final String[] paramKeyToArray() {
		String[] keyAr = paramKey.split(OtfCustomFieldModel.SEP);
		setCdType(keyAr[0]);
		setLocalKey(keyAr[1]);
		setId(keyAr[2]);

		return keyAr;
	}

}
