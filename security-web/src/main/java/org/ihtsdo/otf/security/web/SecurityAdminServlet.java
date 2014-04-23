package org.ihtsdo.otf.security.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ihtsdo.otf.security.dto.OftAccountMin;
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
	public static final String DEF_REDIR = "/tree.jsp";

	private String treetype;

	public final String getMembersTreeHtml() {
		return getList("Members", SecurityService.MEMBERS, getMembers());
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

	public final String getUsersTreeHtml() {
		return getList("Users", SecurityService.USERS, getUsers());
	}

	public final String getAppsTreeHtml() {
		return getList("Applications", SecurityService.APPS, getApps());
	}

	public final String getList(String title, String baseUrl, List<String> vals) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<h5>").append(title).append("</h5>");
		sbuild.append("<ul>");

		for (String val : vals) {
			sbuild.append("<li><a title=\"").append(val).append("\" href=\"")
					.append(baseUrl).append("/").append(val)
					.append("</a></li>");
		}
		sbuild.append("</ul>");
		return sbuild.toString();
	}

	public final String getTreeHtml() {
		if (treeHtml == null || isLoadTree()) {
			switch (getTreetype()) {
			case SecurityService.MEMBERS:
				treeHtml = getMembersTreeHtml();
			case SecurityService.USERS:
				treeHtml = getUsersTreeHtml();
			case SecurityService.APPS:
				treeHtml = getAppsTreeHtml();
			default:
				treeHtml = "<div class=\"error\">no type set</div>";
			}
		}
		return treeHtml;
	}

	public final void setTreeHtml(String treeHtmlIn) {
		treeHtml = treeHtmlIn;
	}

	@Override
	protected void handlePostRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		setHr(requestIn);
		System.out.println("handlePostRequest");
		LOG.info("handlePostRequest");

	}

	@Override
	protected void handleGetRequest(HttpServletRequest requestIn,
			HttpServletResponse responseIn) throws ServletException,
			IOException {
		setHr(requestIn);
		System.out.println("handleGetRequest");
		LOG.info("handleGetRequest");

		hr.getSession().setAttribute(TREE, getTreeHtml());

		// response.sendRedirect("index.jsp");

		// setRedirect("index-admin.jsp");
		// final RequestDispatcher reqd = sc.getServletContext()
		// .getRequestDispatcher(redirect);
		// reqd.forward(requestIn, responseIn);

	}

	public final String getTreetype() {
		if (treetype == null) {
			treetype = "";
		}
		return treetype;
	}

	public final void setTreetype(String treetypeIn) {
		LOG.info("setTreetype to " + treetypeIn);
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

}
