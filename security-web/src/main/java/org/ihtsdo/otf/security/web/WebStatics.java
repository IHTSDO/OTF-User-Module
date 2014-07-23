package org.ihtsdo.otf.security.web;

public class WebStatics {

	public static final String QUERY_NAME = "queryName";
	public static final String QUERY_JSON = "jsonQuery";
	public static final String REDIRECT = "redirect";
	public static final String CON_PATH = "context";
	public static final String JSON = "json";
	public static final String RELOAD = "reload";
	public static final String BASEURL = "BASEURL";
	public static final String SAVE = "save";

	public static final String USERNAME = "userName";
	public static final String PASSWD = "passWord";
	public static final String AUTH_TOKEN = "AUTH_TOKEN";
	public static final String USERMAIL = "userMail";
	public static final String SP_TOKEN = "sptoken";

	public static final String CLASS = "class";
	public static final String USER_SECURITY_HANDLER = "UserSecurityHandler";
	public static final String SETTINGS_PROPS = "SecurityServiceProps";

	public static final String TREE = "TreeHTML";
	public static final String FORM = "FormHTML";

	public static final String DEF_REDIR_URL = "/tree.jsp";
	public static final String NEW_FORM_URL = "/newForm/";
	public static final String ADMIN_URL = "./admin/";
	public static final String CHG_PW_REG_URL = "RequestPwChange";
	public static final String CHG_PW_URL = "PwChange";

	public static final String CHG_PW_REG_REC_JSP = "passwordRequestRec.jsp";
	public static final String CHG_PW_JSP = "changePw.jsp";
	public static final String CHG_PW_REG_REC_NOTOK_JSP = "TokenWrong.jsp";
	public static final String CHG_PW_REG_REC_OK_JSP = "pwChangeOk.jsp";

	public static final String getAdminReqPwChg() {
		return ADMIN_URL + CHG_PW_REG_URL;
	}

	public static final String getAdminPwChg() {
		return ADMIN_URL + CHG_PW_URL;
	}

}
