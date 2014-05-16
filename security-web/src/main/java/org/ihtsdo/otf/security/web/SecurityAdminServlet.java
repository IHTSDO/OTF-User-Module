package org.ihtsdo.otf.security.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfBaseWeb;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;
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

	// public static Map<String, List<String>> apps;
	// public static List<String> members;

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

	public final List<String> getAppsNotMembersOrUsers() {
		Collection<String> all = getAppNames();
		List<String> notMemUser = new ArrayList<String>();
		if (all != null && all.size() > 0) {
			Map<String, OtfCustomFieldSetting> settings = getSettings();
			String users = "";
			String members = "";
			OtfCustomFieldSetting user = settings.get(SecurityService.USERS);
			if (user != null) {
				users = user.getVal();
			}
			OtfCustomFieldSetting mem = settings.get(SecurityService.MEMBERS);
			if (mem != null) {
				members = mem.getVal();
			}

			for (String appname : all) {
				boolean remove = appname.equals(users)
						|| appname.equals(members);
				if (!remove) {
					notMemUser.add(appname);
				}
			}

		}

		return notMemUser;

	}

	public final Map<String, OtfCustomFieldSetting> getSettings() {
		return getUsh().getUserSecurity().getSettings();
	}

	public final String getUsersTreeHtml(final String path) {
		return getList("Users", path + SecurityService.USERS, getUsers());
	}

	public final String getAppsTreeHtml(final String path) {
		return getList("Applications", path + SecurityService.APPS,
				getAppsNotMembersOrUsers());
	}

	public final String getList(String title, String baseUrl, List<String> vals) {
		StringBuilder sbuild = new StringBuilder();
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
		OtfSettings otfSett = new OtfSettings(getSettings());
		return otfSett.getRHS();
	}

	public final String getUserForm() {
		OtfAccount oacc = null;

		if (getUrlNodes().length > 1) {
			String val = getUrlNodes()[1];
			oacc = getUsh().getUserSecurity().getUserAccountByName(val, "*");
			if (oacc != null) {
				oacc.getId();
				oacc.setAction(getContextUrl(getHr()));
				// return oacc.getRHS();
			}
		}
		if (oacc == null) {
			oacc = new OtfAccount();
		}

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
				member.setAction(getContextUrl(getHr()));
				// return member.getRHS();
			}
		}
		if (member == null) {
			member = new OtfGroup();
		}
		member.setShowCustData(false);
		member.setGrpDesc("Member");
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
				// return oacc.getRHS();
			}
		}
		if (oacc == null) {
			oacc = new OtfApplication();
		}

		StringBuilder sbuild = new StringBuilder();
		sbuild.append(oacc.getRHS());

		// get the groups via the accountStores
		if (stringOK(oacc.getName())) {
			List<OtfGroup> grps = getUsh().getUserSecurity()
					.getGroupsByAppName(oacc.getName());

			// add a new grp
			OtfGroup newGrp = new OtfGroup();
			grps.add(newGrp);

			Collections.sort(grps);
			sbuild.append(OtfBaseWeb.getRepeatingSubForms("Roles/Groups", grps));
		}
		return sbuild.toString();
	}

	// public static final List<String> getMembers() {
	// return getUsh().getUserSecurity().getMembers();
	// }
	//
	// public static final void setMembers(List<String> membersIn) {
	// members = membersIn;
	// }

	public final Map<String, List<String>> getApps() {
		return getUsh().getUserSecurity().getAppsMap();
	}

	public final Collection<String> getAppNames() {
		return getApps().keySet();
	}

	// public final List<String> getAppsL() {
	// AppsListQueryDTO ulq = new AppsListQueryDTO(getUsh());
	// return ulq.getApps();
	// }

	// public final List<String> getMembers() {
	// MembersListQueryDTO mlq = new MembersListQueryDTO(getUsh());
	// return mlq.getMembers();
	// }

	// public String getRepeatingSubForms(String title,
	// Collection<? extends OtfBaseName> items) {
	// StringBuilder sbuild = new StringBuilder();
	// sbuild.append(OtfBaseWeb.getSubForm(title));
	// // getTableSubview
	// for (OtfBaseName obw : items) {
	// sbuild.append(obw.getRHS());
	// }
	//
	// return sbuild.toString();
	//
	// }

}
