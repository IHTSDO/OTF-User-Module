package org.ihtsdo.otf.security.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.dto.query.queries.AppsListQueryDTO;
import org.ihtsdo.otf.security.dto.query.queries.MembersListQueryDTO;
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

	private String treetype;

	@Override
	protected void handlePostRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		setHr(requestIn);
		logParameters(requestIn);
	}

	@Override
	protected void handleGetRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		setHr(requestIn);
		// System.out.println("handleGetRequest");
		String curl = getContextUrl(requestIn);
		hr.getSession().setAttribute(BASEURL, curl);
		// LOG.info("handleGetRequest curl = " + curl);
		hr.getSession().setAttribute(TREE, getTreeHtml(curl));
		hr.getSession().setAttribute(FORM, getForm());
		setRedirect("/index-admin.jsp");
		final RequestDispatcher reqd = sc.getServletContext()
				.getRequestDispatcher(redirect);
		reqd.forward(requestIn, responseIn);

	}

	public final String getMembersTreeHtml(final String path) {
		return getList("Members", path + SecurityService.MEMBERS, getMembers());
	}

	public final List<String> getMembers() {
		MembersListQueryDTO mlq = new MembersListQueryDTO(getUsh());
		return mlq.getMembers();
	}

	public final List<String> getUsers() {
		UsersListQueryDTO ulq = new UsersListQueryDTO(getUsh());
		List<String> usersL = new ArrayList<String>();
		for (OftAccountMin acc : ulq.getUsers()) {
			usersL.add(acc.getName());
		}
		Collections.sort(usersL);

		return usersL;
	}

	public final List<String> getApps() {
		AppsListQueryDTO ulq = new AppsListQueryDTO(getUsh());
		return ulq.getApps();
	}

	public final String getUsersTreeHtml(final String path) {
		return getList("Users", path + SecurityService.USERS, getUsers());
	}

	public final String getAppsTreeHtml(final String path) {
		return getList("Applications", path + SecurityService.APPS, getApps());
	}

	public final String getList(String title, String baseUrl, List<String> vals) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<h1 class=\"blue\">").append(title).append("</h1>");
		sbuild.append("<ul>");

		for (String val : vals) {
			sbuild.append("<li><a title=\"").append(val).append("\" href=\"")
					.append(baseUrl).append("/").append(val).append("\">")
					.append(val).append("</a></li>");
		}
		sbuild.append("</ul>");
		return sbuild.toString();
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

	public final String getForm() {
		switch (getUrlNodes()[0]) {
		case SecurityService.MEMBERS:
			return getMemberForm();
		case SecurityService.USERS:
			return getUserForm();
		case SecurityService.APPS:
			return getAppForm();
		default:
			return getEmptyForm();
		}
	}

	public final String getEmptyForm() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<h1 class=\"blue\">No record selected</h1>");
		return sbuild.toString();
	}

	public final String getUserForm() {
		OtfAccount oacc = null;

		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurity().getUserAccountByName(val, "*");
			if (oacc != null) {
				oacc.getId();
				oacc.setAction(getContextUrl(getHr()));
				return oacc.getRHS();
			}
		}
		if (oacc == null) {
			oacc = new OtfAccount();
		}
		return oacc.getRHS();

	}

	public final String getMemberForm() {

		OtfGroup member = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			member = getUsh().getUserSecurity().getMemberByName(val);
			if (member != null) {
				member.getId();
				member.setAction(getContextUrl(getHr()));
				return member.getRHS();
			}
		}
		if (member == null) {
			member = new OtfGroup();
		}
		return member.getRHS();
	}

	public final String getAppForm() {
		OtfApplication oacc = null;
		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurity().getApps().getAppByName(val);
			if (oacc != null) {
				oacc.getId();
				oacc.setAction(getContextUrl(getHr()));
				return oacc.getRHS();
			}
		}
		if (oacc == null) {
			oacc = new OtfApplication();
		}
		return oacc.getRHS();
	}

}
