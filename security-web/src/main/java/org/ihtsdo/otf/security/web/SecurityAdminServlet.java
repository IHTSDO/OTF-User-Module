package org.ihtsdo.otf.security.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ihtsdo.otf.security.AbstractUserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfBaseId;
import org.ihtsdo.otf.security.dto.OtfBaseWeb;
import org.ihtsdo.otf.security.dto.OtfBasicWeb;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldApplication;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldMember;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldPerm;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.dto.query.queries.UsersListQueryDTO;

public class SecurityAdminServlet extends AbstractSecurityServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861449973287813591L;

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(SecurityAdminServlet.class.getName());

	private String treeHtml;
	public static final String TREE = "TreeHTML";
	public static final String FORM = "FormHTML";
	public static final String DEF_REDIR = "/tree.jsp";

	public static final String NEW_FORM = "/newForm/";

	// public static Map<String, List<String>> apps;
	// public static List<String> members;

	private String treetype;

	@Override
	protected void handlePostRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		boolean userok = checkCred(requestIn, responseIn);
		setHr(requestIn);
		if (userok) {
			if (getNamedParam(PASSWD, requestIn) != null) {
				handleGetRequest(requestIn, responseIn);
			} else {
				// logParameters(requestIn);
				OtfBaseWeb obw = handlePostAction(requestIn, responseIn);
				LOG.info("obw3 = " + obw + " num errs = "
						+ obw.getErrors().size());
				if (obw.getErrors().size() == 0) {
					// update remotely using obw
					String ok = updateFromWebObject(obw);
					// LOG.info("ok =" + ok);

					setTreetype(null);

					if (ok == null) {
						ok = AbstractUserSecurityHandler.REMOTE_COMMIT_NOT_OK;
					}

					if (ok.equals(AbstractUserSecurityHandler.REMOTE_COMMIT_OK)) {
						LOG.info("YAYYYY " + ok);
					}
					if (ok.equals(AbstractUserSecurityHandler.REMOTE_COMMIT_NOT_OK)) {
						// try again? reload entire model from remote? revert?
						// Capture
						// orig as JSON?
						LOG.info("BOOOO " + ok);
					}
					loadScreen(requestIn, responseIn, null);
				} else {
					obw.setAction(getDecString(getHr().getRequestURI()));
					loadScreen(requestIn, responseIn, obw);
				}
			}
		}
		// TODO - make sure values carry through
		// e.g. change name of mapping app find all refs and change that.
	}

	public String updateFromWebObject(OtfBaseWeb webObject) {

		// update remote

		// get new object from remote

		// update model to match

		// LOG.info("updateFromWebObject webObject type = "
		// + webObject.getClass().getName());

		if (webObject instanceof OtfSettings) {
			// LOG.info("is OtfSettings");
			OtfSettings otfObj = (OtfSettings) webObject;
			String retval = getUsh().addUpdateGroup(otfObj.getGrp());
			getUsh().getUserSecurity().resetSettings();
			getUsh().getUserSecurity().resetAppsNotMembersOrUsers();
			return retval;
		}
		if (webObject instanceof OtfAccount) {
			// LOG.info("is OtfAccount");
			OtfAccount otfObj = (OtfAccount) webObject;
			String retval = getUsh().addUpdateAccount(otfObj, null);
			getUsh().getUserSecurity().resetAllAccounts();
			getUsh().getUserSecurity().resetAdminUsers();
			return retval;
		}
		if (webObject instanceof OtfApplication) {
			// LOG.info("is OtfApplication");
			OtfApplication otfObj = (OtfApplication) webObject;
			String retval = getUsh().addUpdateApp(otfObj);
			getUsh().getUserSecurity().resetAppsMap();
			getUsh().getUserSecurity().resetAppsNotMembersOrUsers();
			getUsh().getUserSecurity().resetDirsMap();
			getUsh().getUserSecurity().resetAdminUsers();
			return retval;
		}
		if (webObject instanceof OtfGroup) {
			// LOG.info("is OtfGroup");
			OtfGroup otfObj = (OtfGroup) webObject;
			// LOG.info("Parent dir = " + otfObj.getParentDirName());
			String retval = getUsh().addUpdateGroup(otfObj);
			getUsh().getUserSecurity().resetAppsMap();
			getUsh().getUserSecurity().resetAppsNotMembersOrUsers();
			getUsh().getUserSecurity().resetDirsMap();
			getUsh().getUserSecurity().resetMembers();
			getUsh().getUserSecurity().resetAdminUsers();
			return retval;
		} else
			return AbstractUserSecurityHandler.REMOTE_COMMIT_NOT_OK;

	}

	@Override
	protected void handleGetRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		boolean userok = checkCred(requestIn, responseIn);
		// LOG.info("userok = " + userok);
		setHr(requestIn);
		if (userok) {
			String decPath = getDecString(getNotNullPath(requestIn));
			// LOG.info("decPath = " + decPath);
			if (decPath.startsWith(NEW_FORM)) {
				// getUrlNodes();
				// LOG.info("New Form val = " + getUrlNodes()[1]);
				String retval = getNewForm();
				// LOG.info("New Form retval = " + retval);

				responseIn.setContentType("application/html");
				final PrintWriter out = responseIn.getWriter();
				out.write(retval);

			} else {

				if (decPath.endsWith(RELOAD)) {
					reloadUsh();
				}
				if (decPath.endsWith(SAVE)) {
					save();
				}

				loadScreen(requestIn, responseIn, null);
			}
		}
	}

	private boolean checkCred(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws IOException,
			ServletException {
		String usern = authUser(requestIn);
		// LOG.info("Check Cred uname = " + usern);
		if (usern != null) {
			// check perm - is the uname in the list of admin users
			Boolean perm = getUsh().getUserSecurity().getAdminUsers()
					.contains(usern);
			// if not perm send to sorry page
			if (!perm) {
				requestIn.getSession().removeAttribute(USERNAME);
				requestIn.getSession().removeAttribute(AUTH_TOKEN);
				setRedirect("/NoAdmin.jsp");
				final RequestDispatcher reqd = sc.getServletContext()
						.getRequestDispatcher(redirect);
				reqd.forward(requestIn, responseIn);
				return false;
			}
		}
		if (usern == null) {
			setRedirect("/login.jsp");
			final RequestDispatcher reqd = sc.getServletContext()
					.getRequestDispatcher(redirect);
			reqd.forward(requestIn, responseIn);
			return false;
		}

		return true;

	}

	private void loadScreen(HttpServletRequest requestIn,
			HttpServletResponse responseIn, OtfBaseWeb obw)
			throws ServletException, IOException {
		String curl = getContextUrl(requestIn);
		getUsh().getUserSecurity().getCachedListMaps()
				.setAdminServletContextUrl(curl);
		hr.getSession().setAttribute(BASEURL, curl);
		// LOG.info("loadScreen curl = " + curl + "session id = "
		// + hr.getSession().getId());
		boolean loadObw = false;
		if (obw != null) {
			if (obw instanceof OtfBaseId) {
				OtfBaseId obi = (OtfBaseId) obw;
				// LOG.info("loadScreen obw isnew = = " + obi.isNew());
				// LOG.info("loadScreen obw errs = " + obw.getErrors().size());
				loadObw = obi.isNew() && obw.getErrors().size() > 0;
			}

		}
		// LOG.info("loadObw = " + loadObw);
		if (loadObw) {
			hr.getSession().setAttribute(FORM, obw.getRHS());
		} else {
			hr.getSession().setAttribute(FORM, getForm());
		}
		hr.getSession().setAttribute(TREE, getTreeHtml(curl));

		// if (obw == null) {
		// hr.getSession().setAttribute(FORM, getForm());
		// } else {
		// LOG.info("using obw errs size = " + obw.getErrors().size());
		// hr.getSession().setAttribute(FORM, obw.getRHS());
		// }
		setRedirect("/index-admin.jsp");
		final RequestDispatcher reqd = sc.getServletContext()
				.getRequestDispatcher(redirect);
		reqd.forward(requestIn, responseIn);
	}

	public final String getMembersTreeHtml(final String path) {
		return getList("Members", path + SecurityService.MEMBERS, getUsh()
				.getUserSecurity().getMembers());
	}

	public final List<String> getUsers() {
		UsersListQueryDTO ulq = new UsersListQueryDTO(getUsh());
		List<String> usersL = new ArrayList<String>();
		for (OtfAccountMin acc : ulq.getUsers()) {
			usersL.add(acc.getName());
		}
		Collections.sort(usersL);

		return usersL;
	}

	public final OtfSettings getSettings() {
		return getUsh().getUserSecurity().getSettings();
	}

	public final String getUsersTreeHtml(final String path) {
		return getList("Users", path + SecurityService.USERS, getUsers());
	}

	public final String getAppsTreeHtml(final String path) {
		return getList("Applications", path + SecurityService.APPS, getUsh()
				.getUserSecurity().getAppsNotMembersOrUsers());
	}

	public final String getList(String title, String baseUrl, List<String> vals) {
		StringBuilder sbuild = new StringBuilder();
		Collections.sort(vals, String.CASE_INSENSITIVE_ORDER);
		sbuild.append(OtfBaseWeb.getForm(title));
		sbuild.append("<ul>");
		for (String val : vals) {
			sbuild.append("<li><a title=\"").append(val).append("\" href=\"")
					.append(baseUrl).append("/").append(val).append("\">")
					.append(val).append("</a></li>");
		}
		sbuild.append("</ul>");
		return sbuild.toString();
	}

	protected OtfBaseWeb handlePostAction(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {

		String inputKey = getNamedParam(OtfBaseWeb.INPUT_KEY_NAME, requestIn);
		String id = getNamedParam(OtfBaseId.ID_KEY, requestIn);
		// LOG.info("InputKey = " + inputKey);
		// LOG.info("handlePostAction id = " + id);
		// logParameters(requestIn);

		OtfBaseWeb obw = getWebObjectFromId(inputKey, id);
		// LOG.info("obw = " + obw + " num errs = " + obw.getErrors().size());
		handleParams(requestIn, responseIn, obw);
		// LOG.info("obw2 = " + obw + " num errs = " + obw.getErrors().size());
		return obw;

	}

	private OtfBaseWeb getWebObjectFromId(String inputKey, String id) {

		if (!stringOK(inputKey)) {
			return null;
		}
		boolean exists = stringOK(id);
		// LOG.info("getWebObjectFromId is = " + id);
		switch (inputKey) {

		case SecurityService.USERS:
			// LOG.info("ACCOUNT");
			if (exists) {
				return getUsh().getUserSecurity().getUserAccountById(id, "*");
			} else {
				return new OtfAccount();
			}
		case SecurityService.APPS:
			// LOG.info("APP");
			if (exists) {
				return getUsh().getUserSecurity().getApps().getAppById(id);
			} else {
				return new OtfApplication();
			}
		case SecurityService.SETTINGS:
			// LOG.info("SETTING");
			return getSettings();
		case OtfGroup.TYPE_NORMAL:
			// LOG.info("GROUP : NORMAL");
			if (exists) {
				// LOG.info("Group");
				return getUsh().getUserSecurity().getGroupById(id);
			} else {
				return new OtfGroup();
			}
		case OtfGroup.TYPE_MEMBER:
			// LOG.info("GROUP : MEMBER");
			if (exists) {
				return getUsh().getUserSecurity().getMemberById(id);
			} else {
				return new OtfGroup();
			}

		case OtfGroup.TYPE_SETTING:
			// LOG.info("GROUP : SETTING");
			return getSettings();

		default:
			LOG.info(" no handler found for input Key = " + inputKey);
			return null;
		}
	}

	protected void handleParams(HttpServletRequest requestIn,
			HttpServletResponse responseIn, OtfBaseWeb webObject)
			throws ServletException, IOException {

		Map<String, String> paramsMap = getFiltParamsAsHM(requestIn);
		webObject.setParams(paramsMap);
		webObject.processParams();

		// LOG.info("Errs size = " + webObject.getErrors().size());

		// if (webObject.getErrors().size() == 0) {
		// // update
		// updateFromWebObject(webObject);
		//
		// }
		if (webObject.getErrors().size() > 0) {
			webObject.logErrors();
		}

	}

	public final String getTreeHtml(final String path) {
		// LOG.info("getTreeHtml path = " + path);
		if (isLoadTree()) {
			switch (getTreetype()) {
			case SecurityService.MEMBERS:
				treeHtml = getMembersTreeHtml(path);
				break;
			case SecurityService.USERS:
				treeHtml = getUsersTreeHtml(path);
				break;
			case SecurityService.APPS:
				treeHtml = getAppsTreeHtml(path);
				break;
			case SecurityService.SETTINGS:
				treeHtml = "";
				break;
			default:
				treeHtml = "<div class=\"error\">no type set</div>";
			}
		}
		return treeHtml;
	}

	public final void setTreeHtml(String treeHtmlIn) {
		treeHtml = treeHtmlIn;
	}

	public final String getTreetype() {
		if (treetype == null) {
			treetype = "";
		}
		return treetype;
	}

	public final void setTreetype(String treetypeIn) {
		treetype = treetypeIn;
	}

	public final boolean isLoadTree() {
		String type = getUrlNodes()[0];
		// LOG.info("isLoadTree type = " + type + " treetype = " + treetype);
		if (stringOK(treetype) && treetype.equals(type)) {
			return false;
		} else {
			setTreetype(type);
			return true;
		}
	}

	public final String getNewForm() {
		try {
			OtfCustomField.CustomType cf = OtfCustomField.CustomType
					.valueOf(getUrlNodes()[1]);
			switch (cf) {
			case CD_TYPE_PERM:
				return new OtfBasicWeb().getNewCfModelElement(
						new OtfCustomFieldPerm(), OtfCustomData.cssClass);
			case CD_TYPE_MEMBER:
				return new OtfBasicWeb().getNewCfModelElement(
						new OtfCustomFieldMember(), OtfCustomData.cssClass);
			case CD_TYPE_APP:
				return new OtfBasicWeb()
						.getNewCfModelElement(new OtfCustomFieldApplication(),
								OtfCustomData.cssClass);
			default:
				return "";
			}
		} catch (java.lang.IllegalArgumentException iae) {
			return "";
		}
	}

	public final String getForm() {
		switch (getUrlNodes()[0]) {
		case SecurityService.MEMBERS:
			return getMemberForm();
		case SecurityService.USERS:
			return getUserForm();
		case SecurityService.APPS:
			return getAppForm();
		case SecurityService.SETTINGS:
			return getSettingsForm();
		default:
			return getEmptyForm();
		}
	}

	public final String getEmptyForm() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(OtfBaseWeb.getForm("No record selected"));
		return sbuild.toString();
	}

	public final String getSettingsForm() {
		OtfSettings otfSett = getSettings();
		otfSett.setAction(getDecString(getHr().getRequestURI()));
		return otfSett.getRHS();
	}

	public final String getUserForm() {
		OtfAccount oacc = null;

		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurity().getUserAccountByName(val, "*");
			if (oacc != null) {
				oacc.getId();
				// return oacc.getRHS();
			}
		}
		if (oacc == null) {
			oacc = new OtfAccount();
		}
		oacc.setAction(getDecString(getHr().getRequestURI()));
		// make sure the lists have been built.
		getUsh().getUserSecurity().getCachedListMaps().getMembersList();
		getUsh().getUserSecurity().getCachedListMaps().getAppsMap();
		return oacc.getRHS();

	}

	public final String getMemberForm() {

		OtfGroup member = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			member = getUsh().getUserSecurity().getMemberByName(val);
			if (member != null) {
				member.getId();
				// return member.getRHS();
			}
		}
		if (member == null) {
			member = new OtfGroup();
		}

		member.setAction(getDecString(getHr().getRequestURI()));
		member.setGrptype(OtfGroup.TYPE_MEMBER);
		member.setParentDirName(getUsh().getUserSecurity().getMembersDir()
				.getName());
		// LOG.info("member errs = " + member.getErrors().size());
		return member.getRHS();
	}

	public final String getAppForm() {
		OtfApplication oacc = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurity().getApps().getAppByName(val);
			if (oacc != null) {
				oacc.getId();
				// oacc.setAction(getContextUrl(getHr()));

				// return oacc.getRHS();
			}
		}
		if (oacc == null) {
			oacc = new OtfApplication();
		}
		oacc.setAction(getDecString(getHr().getRequestURI()));
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(oacc.getRHS());

		// get the groups via the accountStores
		if (stringOK(oacc.getName())) {
			List<OtfGroup> grps = getUsh().getUserSecurity()
					.getGroupsByAppName(oacc.getName());

			String action = getDecString(getHr().getRequestURI());
			// LOG.info("grp action = " + action);
			for (OtfGroup grp : grps) {
				grp.setAction(action);
				grp.getId();
			}

			// add a new grp
			OtfGroup newGrp = new OtfGroup();
			newGrp.setAction(action);

			// Set it's dir parent to the same as the
			String dirname = getUsh().getUserSecurity()
					.getDirsByAppName(oacc.getName()).iterator().next();
			newGrp.setParentDirName(dirname);
			grps.add(newGrp);

			Collections.sort(grps);
			sbuild.append(OtfBaseWeb.getRepeatingSubForms("Roles/Groups", grps));
		}
		return sbuild.toString();
	}

	public final Map<String, List<String>> getApps() {
		return getUsh().getUserSecurity().getAppsMap();
	}

}
