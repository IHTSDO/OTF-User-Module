package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldPerm extends OtfCustomFieldKeyVal {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfCustomFieldPerm.class
			.getName());

	public static final String PERM_NAME = "PermissionName";
	public static final String PERM_VAL = "PermissionValue";

	public OtfCustomFieldPerm() {
		super();
	}

	public OtfCustomFieldPerm(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);
	}

	@Override
	public final String getCollectionTitle() {
		// TODO Auto-generated method stub
		return "Permissions";
	}

	@Override
	public final String getNewTitle() {
		// TODO Auto-generated method stub
		return "Add New Permission";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		String id = getId();
		retval.put(
				"Name:",
				getObw().getHtmlInputText(getUniqueControlName(PERM_NAME, id),
						getKeyVal()));
		retval.put(
				"Value:",
				getObw().getHtmlInputText(getUniqueControlName(PERM_VAL, id),
						getVal()));
		return retval;
	}

	@Override
	public void valFromParamDTO(OtfCustomFieldParamDTO ocfpIn) {
		switch (ocfpIn.getLocalKey()) {
		case PERM_NAME:
			setKeyVal(ocfpIn.getVal());
			break;
		case PERM_VAL:
			setVal(ocfpIn.getVal());
			break;
		default:
			LOG.info("valFromParamDTO localKey not found and = "
					+ ocfpIn.getLocalKey());
		}
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.CD_TYPE_PERM;
		return type;
	}
}
