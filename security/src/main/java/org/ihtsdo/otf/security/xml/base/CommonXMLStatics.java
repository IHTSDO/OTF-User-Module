package org.ihtsdo.otf.security.xml.base;



public class CommonXMLStatics {
	
	/** The Default Encoding. Set to UTF-8. Possibly set in web.xml? */
	public static String DEFAULTENC = "UTF-8";
	
	/** Action parameter to set the XMLFilename */
	public static String ACTION_PARAM_XMLFILENAME = "action.xml.filename";

	/**
	 * The secret UUID attrib added to an XML doc in the absence of any other
	 * in order to guarantee a unique XPath
	 */
	public static String SECRETUUID = "b0b";
	//public static String SECRETUUID = "rumplestiltskin";
	/** Action parameter to set the Relative or http path */
	public static String ACTION_PARAM_NODELOCATE = "action.node.locator";
	
	/** ObjectCache Lookup prefix O is a 0 on purpose just to make a collison less likely*/
	public static String CONCEPT_PRE = "C0ncept-";
	
	/** ObjectCache Lookup for the UUID-INT hashmap */
	public static String UUID_INT_HT = "UUID_INT_HTXYZ123";
	
	/** Int Suffix */
	public static String INTSUFFIX = "_int";
	/** UUID Suffix */
	public static String UUIDSUFFIX = "_UUID";
	/** Date Suffix */
	public static String DATESUFFIX = "_date";
	/** DestRels array list Lookup */
	public static String DESTRELS_AR = "destRels_AR";
	/** SrcRels array list Lookup */
	public static String SRCRELS_AR = "srcRels_AR";
	
	

	/** XML Element names/values **/
	/** 
	 * Attribute names end with _ATT
	 * Element Names end with _ENAME
	 */
	/** Common: **/

	/** Native ID **/
	public static String NATIVE_ATT = "id_int";
	/** Status **/
	public static String STATUS_ATT = "status";
	/** Path **/
	public static String PATH_ATT = "path";
	/** Position **/
	public static String POSITION_ATT = "position";
	/** Version **/
	public static String VERSION_ATT = "version";
	/** Number of Versions **/
	public static String NUMVERSION_ATT = "numVersions";
	/** Text Part **/
	public static String TEXT_ENAME = "text";
	/** TEXT ATT **/
	public static String TEXT_ATT = "text";
	/** typeId ATT **/
	public static String TYPE_ID_ATT = "typeId";
	

	
	/** Concepts **/
	public static String CONCEPTS_ENAME = "concepts";
	public static String REL_CONCEPTS_ENAME = "relatedconcepts";
	public static String REL_TYPES_ENAME = "reltypes";
	
	
	
	/** I_GetConceptData **/
	/** Concept **/
	public static String CONCEPT_ENAME = "concept";
	/** Descriptions **/
	public static String DESCRIPTIONS_ENAME = "descriptions";
	/** SrcRels **/
	public static String SRCRELS_ENAME = "srcRels";
	/** DestRels **/
	public static String DESTRELS_ENAME = "destRels";
	/** Images **/
	public static String IMAGES_ENAME = "images";
	/** Extensions **/
	public static String EXTENSIONS_ENAME = "extensions";
	
	
	/** I_ConceptAttributePart **/
	/** Att Part **/
	public static String CAP_ENAME = "attPart";
	/** Defined **/
	public static String DEFINED_ATT = "defined";
	
	/** I_ConceptAttributeVersioned **/
	/** Attribute **/
	public static String CAV_ENAME = "attribute";
	
	/** I_DescriptionPart **/
	/** Description Part **/
	public static String DESCP_ENAME = "descriptionPart";
	/** lang **/
	public static String LANG_ATT = "lang";
	/** initCaseSig **/
	public static String INITCASESIG_ATT = "initCaseSig";

	
	/** I_DescriptionVersioned **/
	/** Description **/
	public static String DESC_ENAME = "description";
	/** descId **/
	public static String DESC_ID_ATT = "descId";
	/** conceptId **/
	public static String CONCEPT_ID_ATT = "conceptId";
	
	/** I_IdPart **/
	/** Id **/
	public static String ID_ENAME = "id";
	/** id **/
	public static String ID_ATT = "id";
	
	/** I_IdVersioned **/
	/** Ids **/
	public static String IDS_ENAME = "ids";
	
	/** I_ImagePart **/
	/** ImagePart **/
	public static String IMG_PART_ENAME = "imagePart";
	
	/** I_ImageVersioned **/
	/** Image **/
	public static String IMG_ENAME = "image";
	/** ImageBytes **/
	public static String IMGBYTES_ENAME = "imageBytes";
	/** format **/
	public static String FORMAT_ATT = "format";
	
	/** I_RelPart **/
	/** RelPart **/
	public static String REL_PART_ENAME = "relPart";
	/** characteristicId **/
	public static String CHAR_ID_ATT = "characteristicId";
	/** refinabilityId **/
	public static String REFIN_ID_ATT = "refinabilityId";
	/** characteristicId **/
	public static String GROUP_ATT = "group";
	
	/** I_RelVersioned **/
	/** SrcRel **/
	public static String SRC_REL_ENAME = "srcRel";
	/** DestRel **/
	public static String DEST_REL_ENAME = "destRel";
	/** relId **/
	public static String REL_ID_ATT = "relId";
	/** componentOneId **/
	public static String C1_ID_ATT = "c1Id";
	/** componentTwoId **/
	public static String C2_ID_ATT = "c2Id";
	/** componentThreeId **/
	public static String C3_ID_ATT = "c3Id";
	/** nId **/
	public static String N_ID_ATT = "nId";
	
	/** I_ExtendByRefPart **/
	/** RefSetPart **/
	public static String REFSET_PART_ENAME = "refSetPart";
	/** characteristicId **/
	public static String CLASS_NAME_ATT = "className";
	
	/** I_ExtendByRef **/
	/** Extension **/
	public static String EXT_ENAME = "extension";
	/** RefSet **/
	public static String REFSET_ENAME = "refSet";
	/** RefSetConcept **/
	public static String REFSETCON_ENAME = "refSetConcept";
	/** TypeConcept **/
	public static String TYPECON_ENAME = "typeConcept";
	/** Versions **/
	public static String VERSIONS_ENAME = "versions";
	/** memberId **/
	public static String MEMBER_ID_ATT = "memberId";
	/** refSetId **/
	public static String REFSET_ID_ATT = "refSetId";
	/** componentId **/
	public static String COMPONENT_ID_ATT = "componentId";

	

	/** I_ExtendByRefPart Implementations **/
	/** RefSetValue **/
	public static String REFSET_VAL_ENAME = "refSetValue";
	/** value **/
	public static String VAL_ATT = "value";
	
	/** ThinExtByRefPartCrossmapForRel **/
	/** refineFlagId **/
	public static String REFIN_FLAG_ID_ATT = "refineFlagId";
	/** additionalCodeId **/
	public static String ADD_CODE_ID_ATT = "additionalCodeId";
	/** elementNo **/
	public static String ELEM_NO_ATT = "elementNo";
	/** blockNo **/
	public static String BLOCK_NO_ATT = "blockNo";
	
	/** ThinExtByRefPartLanguage **/
	/** acceptabilityId **/
	public static String ACCEPT_ID_ATT = "acceptabilityId";
	/** correctnessId **/
	public static String CORRECT_ID_ATT = "correctnessId";
	/** degreeOfSynonymyId **/
	public static String DEG_SYN_ID_ATT = "degreeOfSynonymyId";
	
	/** ThinExtByRefPartMeasurement **/
	/** unitsOfMeasureId **/
	public static String UOM_ID_ATT = "unitsOfMeasureId";

	/** ThinExtByRefPartTemplateForRel **/
	/** valueTypeId **/
	public static String VAL_TYPE_ID_ATT = "valueTypeId";
	/** semanticStatusId **/
	public static String SEM_STATUS_ID_ATT = "semanticStatusId";
	/** attributeDisplayStatusId **/
	public static String ATT_DISP_ID_ATT = "attributeDisplayStatusId";
	/** characteristicStatusId **/
	public static String CHAR_STATUS_ID_ATT = "characteristicStatusId";
	/** cardinality **/
	public static String CARD_ATT = "cardinality";
	/** browseAttributeOrder **/
	public static String BROWSE_ORDER_ATT = "browseAttributeOrder";
	/** browseValueOrder **/
	public static String BROWSE_VAL_ORDER_ATT = "browseValueOrder";
	/** notesScreenOrder **/
	public static String NOTE_SCREEN_ORDER_ATT = "notesScreenOrder";
	
	
	/** RefSetType Attribute **/
	
	public static String REFSET_TYPE_ATT = "refsetType";
	/** RefSetClassNames **/
	
	/*public static String CLASS_NAME_TE_BOOL = "ThinExtByRefPartBoolean";
	public static String CLASS_NAME_TE_CON = "ThinExtByRefPartConcept";
	public static String CLASS_NAME_TE_CONCON = "ThinExtByRefPartConceptConcept";
	public static String CLASS_NAME_TE_CONCONSTR = "ThinExtByRefPartConceptConceptString";
	public static String CLASS_NAME_TE_CONSTR = "ThinExtByRefPartConceptString";
	public static String CLASS_NAME_TE_CROSS = "ThinExtByRefPartCrossmapForRel";
	public static String CLASS_NAME_TE_INT = "ThinExtByRefPartInteger";
	public static String CLASS_NAME_TE_LANG = "ThinExtByRefPartLanguage";
	public static String CLASS_NAME_TE_MEAS = "ThinExtByRefPartMeasurement";
	public static String CLASS_NAME_TE_STR = "ThinExtByRefPartString";
	public static String CLASS_NAME_TE_TEMPL = "ThinExtByRefPartTemplateForRel";*/

	
	/** RefsetType Names **/
	
	public static String REFSET_TYPE_BOOL = "Bool";
	public static String REFSET_TYPE_CID = "CID";
	public static String REFSET_TYPE_CIDCID = "CIDCID";
	public static String REFSET_TYPE_CIDCIDCID = "CIDCIDCID";
	public static String REFSET_TYPE_CIDCIDSTR = "CIDCIDString";
	public static String REFSET_TYPE_CIDFLOAT = "CIDFloat";
	public static String REFSET_TYPE_CIDINT = "CIDInt";
	public static String REFSET_TYPE_CIDLONG = "CIDLong";
	public static String REFSET_TYPE_CIDSTR = "CIDString";
	public static String REFSET_TYPE_INT = "Int";
	public static String REFSET_TYPE_LONG = "Long";
	public static String REFSET_TYPE_STR = "String";
	
	/*public static String REFSET_TYPE_CROSS = "CrossmapForRel";
	public static String REFSET_TYPE_LANG = "Language";
	public static String REFSET_TYPE_MEAS = "Measurement";
	public static String REFSET_TYPE_TEMPL = "TemplateForRel";*/
	
	
	
	//XPATH Statements
	
	//String rootXP = "//*[name()='"+BackingFileProps.INSTANCES +"']/@"+BackingFileProps.ROOT;
	/** Start Select Elem by name **/
	public static String XPATH_START_E_BY_NAME = "//*[name()='";
	/** End Select Elem by name **/
	public static String XPATH_END_SELECT = "']";
	/** Start Select All Attr by name **/
	public static String XPATH_START_ALL_ATT_BY_NAME = "//@*[name()='";
	/** Start Select an Attr by name **/
	public static String XPATH_START_ATT_BY_NAME = "/@*[name()='";
	
	/** Start Select by Attr val **/
	public static String XPATH_START_ATT_BY_VAL = "[@";
	/** Equals + singlequote **/
	public static String XPATH_EQUAL_SINGLEQUOT = "='";
	
	
	
	//*[name()='id'][@id_int='-2147483561']/@*[name()='value']
	
	
}
