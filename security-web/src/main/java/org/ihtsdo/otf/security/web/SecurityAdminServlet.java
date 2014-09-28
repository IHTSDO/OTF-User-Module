package org.ihtsdo.otf.security.web;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.ihtsdo.otf.security.dto.OtfCachedListsDTO;
import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfDirectory;
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

	private String treetype;

	@Override
	protected void handlePostRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		setHr(requestIn);
		String decPath = getDecString(getNotNullPath(requestIn));
		if (decPath.endsWith(WebStatics.CHG_PW_REG_URL)) {
			String userdetails = getNamedParam(WebStatics.USERMAIL, requestIn);
			if (stringOK(userdetails)) {
				String reqVal = requestResetUserPassword(userdetails);
				final RequestDispatcher reqd = sc.getServletContext()
						.getRequestDispatcher(
								"/" + WebStatics.CHG_PW_REG_REC_JSP);
				reqd.forward(requestIn, responseIn);
				return;
			}
		}

		if (decPath.endsWith(WebStatics.CHG_PW_URL)) {
			String username = getNamedParam(WebStatics.USERNAME, requestIn);
			String password = getNamedParam(WebStatics.PASSWD, requestIn);
			String token = getNamedParam(WebStatics.SP_TOKEN, requestIn);
			String redirUrl = resetUserPassword(username, password, token);
			// redirect
			final RequestDispatcher reqd = sc.getServletContext()
					.getRequestDispatcher("/" + redirUrl);
			reqd.forward(requestIn, responseIn);
			return;
		}

		boolean userok = checkCred(requestIn, responseIn);
		if (userok) {
			if (getNamedParam(WebStatics.PASSWD, requestIn) != null) {
				handleGetRequest(requestIn, responseIn);
			} else {
				OtfBaseWeb obw = handlePostAction(requestIn, responseIn);
				if (obw == null) {
					loadScreen(requestIn, responseIn, null);
					return;
				}
				if (obw.getErrors().isEmpty()) {
					// update remotely using obw
					String ok = updateFromWebObject(obw);
					setTreetype(null);

					if (ok == null) {
						ok = AbstractUserSecurityHandler.REMOTE_COMMIT_NOT_OK;
					}

					loadScreen(requestIn, responseIn, null);
				} else {
					obw.setAction(getDecString(getHr().getRequestURI()));
					loadScreen(requestIn, responseIn, obw);
				}
			}
		}
	}

	public String updateFromWebObject(OtfBaseWeb webObject) {

		// update remote

		// get new object from remote

		// update model to match

		if (webObject instanceof OtfSettings) {
			OtfSettings otfObj = (OtfSettings) webObject;
			String retval = getUsh().addUpdateGroup(otfObj.getGrp());
			getUsh().getUserSecurityModel().getModel().reset();
			return retval;
		}
		if (webObject instanceof OtfAccount) {
			OtfAccount otfObj = (OtfAccount) webObject;
			String retval = getUsh().addUpdateAccount(otfObj);
			getUsh().getUserSecurityModel().getModel().reset();
			return retval;
		}
		if (webObject instanceof OtfApplication) {
			OtfApplication otfObj = (OtfApplication) webObject;
			String retval = getUsh().addUpdateApp(otfObj);
			getUsh().getUserSecurityModel().getModel().reset();
			return retval;
		}
		if (webObject instanceof OtfDirectory) {
			OtfDirectory otfObj = (OtfDirectory) webObject;
			String retval = getUsh().addUpdateDir(otfObj);
			getUsh().getUserSecurityModel().getModel().reset();
			return retval;
		}
		if (webObject instanceof OtfGroup) {
			OtfGroup otfObj = (OtfGroup) webObject;
			String retval = getUsh().addUpdateGroup(otfObj);
			getUsh().getUserSecurityModel().getModel().reset();
			return retval;
		} else {
			return AbstractUserSecurityHandler.REMOTE_COMMIT_NOT_OK;
		}

	}

	@Override
	protected void handleGetRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		setHr(requestIn);
		String decPath = getDecString(getNotNullPath(requestIn));
		if (decPath.endsWith(WebStatics.CHG_PW_URL)) {

			// Check token is real.

			String sptoken = getNamedParam(WebStatics.SP_TOKEN, requestIn);
			String uname = getNamedParam(WebStatics.USERNAME, requestIn);
			String redirUrl = resetUserPassword(uname, null, sptoken);
			// redirect

			final RequestDispatcher reqd = sc.getServletContext()
					.getRequestDispatcher("/" + redirUrl);
			reqd.forward(requestIn, responseIn);
			return;
		}

		boolean userok = checkCred(requestIn, responseIn);
		if (userok) {
			if (decPath.startsWith(WebStatics.NEW_FORM_URL)) {
				String retval = getNewForm();
				responseIn.setContentType("application/html");
				final PrintWriter out = responseIn.getWriter();
				out.write(retval);

			} else {

				if (decPath.endsWith(WebStatics.RELOAD)) {
					reloadUsh();
				}
				if (decPath.endsWith(WebStatics.SAVE)) {
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
		if (usern != null) {
			// check perm - is the uname in the list of admin users
			Boolean perm = getUsh().getUserSecurityModel().getAdminUsers()
					.contains(usern);
			// if not perm send to sorry page
			if (!perm) {
				requestIn.getSession().removeAttribute(WebStatics.USERNAME);
				requestIn.getSession().removeAttribute(WebStatics.AUTH_TOKEN);
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
		OtfCachedListsDTO.setAdminServletContextUrl(curl);
		hr.getSession().setAttribute(WebStatics.BASEURL, curl);
		boolean loadObw = false;
		if (obw != null) {
			if (obw instanceof OtfBaseId) {
				OtfBaseId obi = (OtfBaseId) obw;
				loadObw = obi.isNew() && !obw.getErrors().isEmpty();
			}

		}
		if (loadObw) {
			hr.getSession().setAttribute(WebStatics.FORM, obw.getRHS());
		} else {
			hr.getSession().setAttribute(WebStatics.FORM, getForm());
		}
		hr.getSession().setAttribute(WebStatics.TREE, getTreeHtml(curl));
		setRedirect("/index-admin.jsp");
		final RequestDispatcher reqd = sc.getServletContext()
				.getRequestDispatcher(redirect);
		reqd.forward(requestIn, responseIn);
	}

	public final List<String> getUsers() {
		List<String> users = getUsh().getUserSecurityModel().getUserNames();
		Collections.sort(users);
		return users;
	}

	public final List<String> getApps() {
		List<String> users = getUsh().getUserSecurityModel().getApps();
		Collections.sort(users);
		return users;
	}

	public final List<String> getMembers() {
		List<String> users = getUsh().getUserSecurityModel().getMembers();
		Collections.sort(users);
		return users;
	}

	public final String getMembersTreeHtml(final String path) {
		return getList("Members", path + SecurityService.MEMBERS, getMembers());
	}

	public final String getUsersTreeHtml(final String path) {
		return getList("Users", path + SecurityService.USERS, getUsers());
	}

	public final String getAppsTreeHtml(final String path) {
		return getList("Applications", path + SecurityService.APPS, getUsh()
				.getUserSecurityModel().getAppsNotAdmin());
	}

	public final OtfSettings getSettings() {
		return getUsh().getUserSecurityModel().getSettings();
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
		OtfBaseWeb obw = getWebObjectFromId(inputKey, id);
		if (obw == null) {
			LOG.severe("No Web Object found for inputKey : " + inputKey
					+ " id = " + id);
		}
		if (obw != null) {
			handleParams(requestIn, responseIn, obw);
		}
		return obw;

	}

	private OtfBaseWeb getWebObjectFromId(String inputKey, String id) {
		if (!stringOK(inputKey)) {
			return null;
		}
		boolean exists = stringOK(id);
		switch (inputKey) {

		case SecurityService.USERS:
			if (exists) {
				return getUsh().getUserSecurityModel().getUserAccountById(id);
			} else {
				return new OtfAccount();
			}
		case SecurityService.APPS:
			if (exists) {
				return getUsh().getUserSecurityModel().getModel().getApps()
						.getAppById(id);
			} else {
				return new OtfApplication();
			}
		case SecurityService.DIR:
			if (exists) {
				return getUsh().getUserSecurityModel().getModel().getDirs()
						.getDirById(id);
			} else {
				return new OtfDirectory();
			}
		case SecurityService.SETTINGS:
			return getSettings();
		case OtfGroup.TYPE_NORMAL:
			if (exists) {
				return getUsh().getUserSecurityModel().getGroupById(id);
			} else {
				return new OtfGroup();
			}
		case OtfGroup.TYPE_MEMBER:
			if (exists) {
				return getUsh().getUserSecurityModel().getMemberById(id);
			} else {
				return new OtfGroup();
			}

		case OtfGroup.TYPE_SETTING:
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
		if (!webObject.getErrors().isEmpty()) {
			webObject.logErrors();
		}

	}

	public final String getTreeHtml(final String path) {
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
			oacc = getUsh().getUserSecurityModel().getUserAccountByName(val);
			if (oacc != null) {
				oacc.getId();
			}
		}
		if (oacc == null) {
			oacc = new OtfAccount();
		}
		oacc.setAction(getDecString(getHr().getRequestURI()));
		// make sure the lists have been built.
		// getUsh().getUserSecurityModel().getModel().getCachedListMaps()
		// .getMembersList();
		// getUsh().getUserSecurityModel().getModel().getCachedListMaps()
		// .getAppsMap();
		return oacc.getRHS();

	}

	public final String getMemberForm() {

		OtfGroup member = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			member = getUsh().getUserSecurityModel().getMemberByName(val);
			if (member != null) {
				member.getId();
			}
		}
		if (member == null) {
			member = new OtfGroup();
		}

		member.setAction(getDecString(getHr().getRequestURI()));
		member.setGrptype(OtfGroup.TYPE_MEMBER);
		member.setParentDirName(getUsh().getUserSecurityModel().getMembersDir()
				.getName());
		return member.getRHS();
	}

	public final String getAppForm() {
		OtfApplication oacc = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurityModel().getModel().getApps()
					.getAppByName(val);
			if (oacc != null) {
				oacc.getId();
			}
			if (oacc == null) {
				// try dir
				if (getUsh().getUserSecurityModel().getModel().getDirs()
						.getDirByName(val) != null) {
					return getDirForm();
				}
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
			List<OtfGroup> grps = getUsh().getUserSecurityModel()
					.getGroupsByAppName(oacc.getName());

			String action = getDecString(getHr().getRequestURI());
			for (OtfGroup grp : grps) {
				grp.setAction(action);
				grp.getId();
			}

			// add a new grp
			OtfGroup newGrp = new OtfGroup();
			newGrp.setAction(action);

			// Set it's dir parent to the same as the app name
			String dirname = getUsh().getUserSecurityModel()
					.getDirsByAppName(oacc.getName()).iterator().next();
			newGrp.setParentDirName(dirname);
			grps.add(newGrp);

			Collections.sort(grps);
			sbuild.append(OtfBaseWeb.getRepeatingSubForms("Roles/Groups", grps));
		}
		return sbuild.toString();
	}

	public final String getDirForm() {
		OtfDirectory oacc = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurityModel().getModel().getDirs()
					.getDirByName(val);
			if (oacc != null) {
				oacc.getId();
			}
		}
		if (oacc == null) {
			oacc = new OtfDirectory();
		}
		oacc.setAction(getDecString(getHr().getRequestURI()));
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(oacc.getRHS());

		// get the groups via the accountStores
		if (stringOK(oacc.getName())) {
			List<OtfGroup> grps = getUsh().getUserSecurityModel()
					.getGroupsByDirName(oacc.getName());

			String action = getDecString(getHr().getRequestURI());
			for (OtfGroup grp : grps) {
				grp.setAction(action);
				grp.getId();
			}

			// add a new grp
			OtfGroup newGrp = new OtfGroup();
			newGrp.setAction(action);
			newGrp.setParentDirName(oacc.getName());
			grps.add(newGrp);

			Collections.sort(grps);
			sbuild.append(OtfBaseWeb.getRepeatingSubForms("Roles/Groups", grps));
		}
		return sbuild.toString();
	}

	// public final Map<String, List<String>> getApps() {
	// return getUsh().getUserSecurityModel().getAppsMap();
	// }

	public final String requestResetUserPassword(final String userdetail) {
		// First find if there is an account with that name or email
		String email = null;
		String username = null;
		// Find account
		UsersListQueryDTO ulq = new UsersListQueryDTO(getUsh());

		for (OtfAccountMin acc : ulq.getUsers()) {
			if (acc.getName().equals(userdetail)) {
				email = acc.getEmail();
				username = acc.getName();
				acc.setAuthToken(null);
			}
		}

		if (!stringOK(email)) {
			// try all email registered
			for (OtfAccountMin acc : ulq.getUsers()) {
				if (acc.getEmail().equals(userdetail)) {
					email = acc.getEmail();
					username = acc.getName();
					acc.setAuthToken(null);
				}
			}
		}

		if (!stringOK(email)) {
			return null;
		} else {
			return getUsh().requestUpdateUserPassword(username, email);
		}
	}

	public final String resetUserPassword(final String username,
			final String password, final String token) {
		UsersListQueryDTO ulq = new UsersListQueryDTO(getUsh());
		for (OtfAccountMin acc : ulq.getUsers()) {
			if (acc.getName().equals(username)) {
				acc.setAuthToken(null);
				int retval = getUsh().updateUserPassword(username, password,
						token);

				if (retval == 0) {
					// return set pw page
					return WebStatics.CHG_PW_JSP;
				}
				if (retval == 1) {
					// return password updated page
					return WebStatics.CHG_PW_REG_REC_OK_JSP;
				}
				if (retval < 0) {
					// return password updated page
					return WebStatics.CHG_PW_REG_REC_NOTOK_JSP;
				}
			}
		}
		return null;
	}
}
