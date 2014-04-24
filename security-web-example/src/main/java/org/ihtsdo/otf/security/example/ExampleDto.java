package org.ihtsdo.otf.security.example;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ExampleDto {

	public static final String USER = "User";
	public static final String APP = "Application";
	public static final String GROUP = "Group";
	public static final String MEMBER = "Member";
	public static final String ACTION = "Action";

	// template field names
	public static final String USER_T = "##user";
	public static final String APP_T = "##app";
	public static final String GRP_T = "##group";
	public static final String MEM_T = "##member";

	private String action;
	private String user;
	private String app;
	private String grp;
	private String memb;

	private HttpServletRequest request;
	private HttpSession session;

	private String contextPath;

	private String urlNorm = "/security-web/query/";
	private String urlDev = "/security-web-dev/query/";

	public ExampleDto(HttpServletRequest requestIn) {
		super();
		request = requestIn;
		// init();
	}

	private void init() {
		// System.out.println("Init called");
		// final Enumeration<String> paramNames = request.getSession()
		// .getAttributeNames();
		// while (paramNames.hasMoreElements()) {
		// final String paramName = paramNames.nextElement();
		// System.out.println("Session name = " + paramName);
		// }

	}

	private String getLink(String linkUrl) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<a class=\"pseudoLabel\" href=\"");
		sbuild.append(linkUrl);
		sbuild.append("\"target=\"_blank\">");
		sbuild.append(linkUrl);
		sbuild.append("</a>");
		return sbuild.toString();
	}

	private String getLabel(String linkUrl) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<span class=\"pseudoLabel\" >");
		sbuild.append(linkUrl);
		sbuild.append("</span>");
		return sbuild.toString();
	}

	public String getRestLink(String templateRestUrl) {
		String rUrl = getLocalContextRestURL(templateRestUrl);
		if (rUrl.contains("##")) {
			return getLabel(rUrl);
		} else {
			return getLink(rUrl);
		}
	}

	private String getLocalContextRestURL(String templateRestUrl) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getAction()).append(getRestURL(templateRestUrl));
		return sbuild.toString();

	}

	private String getRestURL(String templateRestUrl) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(templateRestUrl);
		if (templateRestUrl.contains(USER_T)) {
			String lval = sbuild.toString();
			sbuild.setLength(0);
			sbuild.append(replaceUser(lval));
		}
		if (templateRestUrl.contains(APP_T)) {
			String lval = sbuild.toString();
			sbuild.setLength(0);
			sbuild.append(replaceApp(lval));
		}
		if (templateRestUrl.contains(GRP_T)) {
			String lval = sbuild.toString();
			sbuild.setLength(0);
			sbuild.append(replaceGrp(lval));
		}
		if (templateRestUrl.contains(MEM_T)) {
			String lval = sbuild.toString();
			sbuild.setLength(0);
			sbuild.append(replaceMember(lval));
		}
		return sbuild.toString();
	}

	private String replaceUser(String url) {
		if (stringOK(getUser())) {
			return url.replaceAll(USER_T, getUser());
		}
		return url;
	}

	private String replaceApp(String url) {
		if (stringOK(getApp())) {
			return url.replaceAll(APP_T, getApp());
		}
		return url;
	}

	private String replaceGrp(String url) {
		if (stringOK(getGrp())) {
			return url.replaceAll(GRP_T, getGrp());
		}
		return url;
	}

	private String replaceMember(String url) {
		if (stringOK(getMemb())) {
			return url.replaceAll(MEM_T, getMemb());
		}
		return url;
	}

	public final String getAction() {
		if (!stringOK(action)) {
			action = getSessAttAsString(ACTION);
			if (!stringOK(action)) {
				String contextPath = request.getContextPath();
				// System.out.println("getAction contextPath = " + contextPath);
				// action = "/security-web";
				if (contextPath.endsWith("dev")) {
					setAction(getUrlDev());
				} else {
					setAction(getUrlNorm());
				}
			}
		}

		return action;
	}

	public final void setAction(String actionIn) {
		action = actionIn;
		request.getSession().setAttribute(ACTION, action);
	}

	private String getSessAttAsString(String key) {
		Object o = request.getSession().getAttribute(key);
		if (o != null) {
			return o.toString();
		} else
			return null;
	}

	public final String getUser() {
		if (!stringOK(user)) {
			String param = getNamedParam(USER);
			if (param != null) {
				setUser(param);
			}
		}
		if (!stringOK(user)) {
			user = getSessAttAsString(USER);
		}
		if (user == null) {
			user = "";
		}
		return user;
	}

	public final void setUser(String usrIn) {
		user = usrIn;
		request.getSession().setAttribute(USER, user);
	}

	public final String getApp() {
		if (!stringOK(app)) {
			String param = getNamedParam(APP);
			if (param != null) {
				setApp(param);
			}
		}
		if (!stringOK(app)) {
			app = getSessAttAsString(APP);
		}
		if (app == null) {
			app = "";
		}
		return app;
	}

	public final void setApp(String appIn) {
		app = appIn;
		request.getSession().setAttribute(APP, app);
	}

	public final String getGrp() {
		if (!stringOK(grp)) {
			String param = getNamedParam(GROUP);
			if (param != null) {
				setGrp(param);
			}
		}
		if (!stringOK(grp)) {
			grp = getSessAttAsString(GROUP);
		}
		if (grp == null) {
			grp = "";
		}
		return grp;
	}

	public final void setGrp(String grpIn) {
		grp = grpIn;
		request.getSession().setAttribute(GROUP, grp);
	}

	public final String getMemb() {
		if (!stringOK(memb)) {
			String param = getNamedParam(MEMBER);
			if (param != null) {
				setMemb(param);
			}
		}
		if (!stringOK(memb)) {
			memb = getSessAttAsString(MEMBER);
		}
		if (memb == null) {
			memb = "";
		}
		return memb;
	}

	public final void setMemb(String membIn) {
		memb = membIn;
		request.getSession().setAttribute(MEMBER, memb);
	}

	public final HttpServletRequest getRequest() {
		return request;
	}

	public final void setRequest(HttpServletRequest requestIn) {
		request = requestIn;
	}

	private final String getNamedParam(final String param_name) {
		final Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			if (paramName.indexOf(param_name) > -1) {
				final String[] paramValues = request
						.getParameterValues(paramName);
				if (paramValues.length == 1) {
					return paramValues[0];
				}
			}
		}
		return null;
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

	public final HttpSession getSession() {
		if (session == null) {
			session = request.getSession();
		}
		return session;
	}

	public final void setSession(HttpSession sessionIn) {
		session = sessionIn;
	}

	public final String getContextPath() {
		if (contextPath == null) {
			contextPath = request.getContextPath();
		}
		return contextPath;
	}

	public final void setContextPath(String contextpathIn) {
		contextPath = contextpathIn;
	}

	public final String getUrlNorm() {
		return urlNorm;
	}

	public final void setUrlNorm(String urlNormIn) {
		urlNorm = urlNormIn;
	}

	public final String getUrlDev() {
		return urlDev;
	}

	public final void setUrlDev(String urlDevIn) {
		urlDev = urlDevIn;
	}

}
