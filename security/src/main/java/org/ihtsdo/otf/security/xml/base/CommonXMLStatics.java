package org.ihtsdo.otf.security.xml.base;

public class CommonXMLStatics {

	/** The Default Encoding. Set to UTF-8. Possibly set in web.xml? */
	public static final String DEFAULTENC = "UTF-8";

	/** Action parameter to set the XMLFilename */
	public static final String ACTION_PARAM_XMLFILENAME = "action.xml.filename";

	/**
	 * The secret UUID attrib added to an XML doc in the absence of any other in
	 * order to guarantee a unique XPath
	 */
	public static final String SECRETUUID = "b0b";
	// public static final String SECRETUUID = "rumplestiltskin";
	/** Action parameter to set the Relative or http path */
	public static final String ACTION_PARAM_NODELOCATE = "action.node.locator";

	/**
	 * ObjectCache Lookup prefix O is a 0 on purpose just to make a collison
	 * less likely
	 */
	public static final String CONCEPT_PRE = "C0ncept-";

	/** ObjectCache Lookup for the UUID-INT map */
	public static final String UUID_INT_HT = "UUID_INT_HTXYZ123";

	/** Int Suffix */
	public static final String INTSUFFIX = "_int";
	/** UUID Suffix */
	public static final String UUIDSUFFIX = "_UUID";
	/** Date Suffix */
	public static final String DATESUFFIX = "_date";
	/** DestRels array list Lookup */
	public static final String DESTRELS_AR = "destRels_AR";
	/** SrcRels array list Lookup */
	public static final String SRCRELS_AR = "srcRels_AR";

	/** XML Element names/values **/
	/**
	 * Attribute names end with _ATT Element Names end with _ENAME
	 */
	/** Common: **/

	/** Native ID **/
	public static final String NATIVE_ATT = "id_int";
	/** Status **/
	public static final String STATUS_ATT = "status";
	/** Path **/
	public static final String PATH_ATT = "path";
	/** Position **/
	public static final String POSITION_ATT = "position";
	/** Version **/
	public static final String VERSION_ATT = "version";
	/** Number of Versions **/
	public static final String NUMVERSION_ATT = "numVersions";
	/** Text Part **/
	public static final String TEXT_ENAME = "text";
	/** TEXT ATT **/
	public static final String TEXT_ATT = "text";
	/** typeId ATT **/
	public static final String TYPE_ID_ATT = "typeId";

	/** Concepts **/
	public static final String CONCEPTS_ENAME = "concepts";
	public static final String REL_CONCEPTS_ENAME = "relatedconcepts";
	public static final String REL_TYPES_ENAME = "reltypes";

	/** I_GetConceptData **/
	/** Concept **/
	public static final String CONCEPT_ENAME = "concept";
	/** Descriptions **/
	public static final String DESCRIPTIONS_ENAME = "descriptions";
	/** SrcRels **/
	public static final String SRCRELS_ENAME = "srcRels";
	/** DestRels **/
	public static final String DESTRELS_ENAME = "destRels";
	/** Images **/
	public static final String IMAGES_ENAME = "images";
	/** Extensions **/
	public static final String EXTENSIONS_ENAME = "extensions";

	/** I_ConceptAttributePart **/
	/** Att Part **/
	public static final String CAP_ENAME = "attPart";
	/** Defined **/
	public static final String DEFINED_ATT = "defined";

	/** I_ConceptAttributeVersioned **/
	/** Attribute **/
	public static final String CAV_ENAME = "attribute";

	/** I_DescriptionPart **/
	/** Description Part **/
	public static final String DESCP_ENAME = "descriptionPart";
	/** lang **/
	public static final String LANG_ATT = "lang";
	/** initCaseSig **/
	public static final String INITCASESIG_ATT = "initCaseSig";

	/** I_DescriptionVersioned **/
	/** Description **/
	public static final String DESC_ENAME = "description";
	/** descId **/
	public static final String DESC_ID_ATT = "descId";
	/** conceptId **/
	public static final String CONCEPT_ID_ATT = "conceptId";

	/** I_IdPart **/
	/** Id **/
	public static final String ID_ENAME = "id";
	/** id **/
	public static final String ID_ATT = "id";

	/** I_IdVersioned **/
	/** Ids **/
	public static final String IDS_ENAME = "ids";

	/** I_ImagePart **/
	/** ImagePart **/
	public static final String IMG_PART_ENAME = "imagePart";

	/** I_ImageVersioned **/
	/** Image **/
	public static final String IMG_ENAME = "image";
	/** ImageBytes **/
	public static final String IMGBYTES_ENAME = "imageBytes";
	/** format **/
	public static final String FORMAT_ATT = "format";

	/** I_RelPart **/
	/** RelPart **/
	public static final String REL_PART_ENAME = "relPart";
	/** characteristicId **/
	public static final String CHAR_ID_ATT = "characteristicId";
	/** refinabilityId **/
	public static final String REFIN_ID_ATT = "refinabilityId";
	/** characteristicId **/
	public static final String GROUP_ATT = "group";

	/** I_RelVersioned **/
	/** SrcRel **/
	public static final String SRC_REL_ENAME = "srcRel";
	/** DestRel **/
	public static final String DEST_REL_ENAME = "destRel";
	/** relId **/
	public static final String REL_ID_ATT = "relId";
	/** componentOneId **/
	public static final String C1_ID_ATT = "c1Id";
	/** componentTwoId **/
	public static final String C2_ID_ATT = "c2Id";
	/** componentThreeId **/
	public static final String C3_ID_ATT = "c3Id";
	/** nId **/
	public static final String N_ID_ATT = "nId";

	/** I_ExtendByRefPart **/
	/** RefSetPart **/
	public static final String REFSET_PART_ENAME = "refSetPart";
	/** characteristicId **/
	public static final String CLASS_NAME_ATT = "className";

	/** I_ExtendByRef **/
	/** Extension **/
	public static final String EXT_ENAME = "extension";
	/** RefSet **/
	public static final String REFSET_ENAME = "refSet";
	/** RefSetConcept **/
	public static final String REFSETCON_ENAME = "refSetConcept";
	/** TypeConcept **/
	public static final String TYPECON_ENAME = "typeConcept";
	/** Versions **/
	public static final String VERSIONS_ENAME = "versions";
	/** memberId **/
	public static final String MEMBER_ID_ATT = "memberId";
	/** refSetId **/
	public static final String REFSET_ID_ATT = "refSetId";
	/** componentId **/
	public static final String COMPONENT_ID_ATT = "componentId";

	/** I_ExtendByRefPart Implementations **/
	/** RefSetValue **/
	public static final String REFSET_VAL_ENAME = "refSetValue";
	/** value **/
	public static final String VAL_ATT = "value";

	/** ThinExtByRefPartCrossmapForRel **/
	/** refineFlagId **/
	public static final String REFIN_FLAG_ID_ATT = "refineFlagId";
	/** additionalCodeId **/
	public static final String ADD_CODE_ID_ATT = "additionalCodeId";
	/** elementNo **/
	public static final String ELEM_NO_ATT = "elementNo";
	/** blockNo **/
	public static final String BLOCK_NO_ATT = "blockNo";

	/** ThinExtByRefPartLanguage **/
	/** acceptabilityId **/
	public static final String ACCEPT_ID_ATT = "acceptabilityId";
	/** correctnessId **/
	public static final String CORRECT_ID_ATT = "correctnessId";
	/** degreeOfSynonymyId **/
	public static final String DEG_SYN_ID_ATT = "degreeOfSynonymyId";

	/** ThinExtByRefPartMeasurement **/
	/** unitsOfMeasureId **/
	public static final String UOM_ID_ATT = "unitsOfMeasureId";

	/** ThinExtByRefPartTemplateForRel **/
	/** valueTypeId **/
	public static final String VAL_TYPE_ID_ATT = "valueTypeId";
	/** semanticStatusId **/
	public static final String SEM_STATUS_ID_ATT = "semanticStatusId";
	/** attributeDisplayStatusId **/
	public static final String ATT_DISP_ID_ATT = "attributeDisplayStatusId";
	/** characteristicStatusId **/
	public static final String CHAR_STATUS_ID_ATT = "characteristicStatusId";
	/** cardinality **/
	public static final String CARD_ATT = "cardinality";
	/** browseAttributeOrder **/
	public static final String BROWSE_ORDER_ATT = "browseAttributeOrder";
	/** browseValueOrder **/
	public static final String BROWSE_VAL_ORDER_ATT = "browseValueOrder";
	/** notesScreenOrder **/
	public static final String NOTE_SCREEN_ORDER_ATT = "notesScreenOrder";

	/** RefSetType Attribute **/

	public static final String REFSET_TYPE_ATT = "refsetType";
	/** RefSetClassNames **/

	/*
	 * public static final String CLASS_NAME_TE_BOOL =
	 * "ThinExtByRefPartBoolean"; public static final String CLASS_NAME_TE_CON =
	 * "ThinExtByRefPartConcept"; public static final String
	 * CLASS_NAME_TE_CONCON = "ThinExtByRefPartConceptConcept"; public static
	 * final String CLASS_NAME_TE_CONCONSTR =
	 * "ThinExtByRefPartConceptConceptString"; public static String
	 * CLASS_NAME_TE_CONSTR = "ThinExtByRefPartConceptString"; public static
	 * final String CLASS_NAME_TE_CROSS = "ThinExtByRefPartCrossmapForRel";
	 * public static final String CLASS_NAME_TE_INT = "ThinExtByRefPartInteger";
	 * public static final String CLASS_NAME_TE_LANG =
	 * "ThinExtByRefPartLanguage"; public static final String CLASS_NAME_TE_MEAS
	 * = "ThinExtByRefPartMeasurement"; public static final String
	 * CLASS_NAME_TE_STR = "ThinExtByRefPartString"; public static final String
	 * CLASS_NAME_TE_TEMPL = "ThinExtByRefPartTemplateForRel";
	 */

	/** RefsetType Names **/

	public static final String REFSET_TYPE_BOOL = "Bool";
	public static final String REFSET_TYPE_CID = "CID";
	public static final String REFSET_TYPE_CIDCID = "CIDCID";
	public static final String REFSET_TYPE_CIDCIDCID = "CIDCIDCID";
	public static final String REFSET_TYPE_CIDCIDSTR = "CIDCIDString";
	public static final String REFSET_TYPE_CIDFLOAT = "CIDFloat";
	public static final String REFSET_TYPE_CIDINT = "CIDInt";
	public static final String REFSET_TYPE_CIDLONG = "CIDLong";
	public static final String REFSET_TYPE_CIDSTR = "CIDString";
	public static final String REFSET_TYPE_INT = "Int";
	public static final String REFSET_TYPE_LONG = "Long";
	public static final String REFSET_TYPE_STR = "String";

	/*
	 * public static final String REFSET_TYPE_CROSS = "CrossmapForRel"; public
	 * static String REFSET_TYPE_LANG = "Language"; public static final String
	 * REFSET_TYPE_MEAS = "Measurement"; public static final String
	 * REFSET_TYPE_TEMPL = "TemplateForRel";
	 */

	// XPATH Statements

	// String rootXP = "//*[name()='"+BackingFileProps.INSTANCES
	// +"']/@"+BackingFileProps.ROOT;
	/** Start Select Elem by name **/
	public static final String XPATH_START_E_BY_NAME = "//*[name()='";
	/** End Select Elem by name **/
	public static final String XPATH_END_SELECT = "']";
	/** Start Select All Attr by name **/
	public static final String XPATH_START_ALL_ATT_BY_NAME = "//@*[name()='";
	/** Start Select an Attr by name **/
	public static final String XPATH_START_ATT_BY_NAME = "/@*[name()='";

	/** Start Select by Attr val **/
	public static final String XPATH_START_ATT_BY_VAL = "[@";
	/** Equals + singlequote **/
	public static final String XPATH_EQUAL_SINGLEQUOT = "='";

	// *[name()='id'][@id_int='-2147483561']/@*[name()='value']

}
