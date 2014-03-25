package org.ihtsdo.otf.security.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.query.SecurityQueryDTO;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.objectcache.ObjectCacheClassHandler;

public class SecurityServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2152026919182141827L;

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(SecurityServlet.class
			.getName());

	private HttpServletRequest hr;

	private SecurityService secServ;
	private UserSecurityHandler ush;
	public static final String QUERY_NAME = "queryName";
	public static final String QUERY_JSON = "jsonQuery";
	public static final String REDIRECT = "redirect";
	public static final String CON_PATH = "context";
	public static final String JSON = "json";
	public static final String RELOAD = "reload";

	public static final String CLASS = "class";
	public static final String USER_SECURITY_HANDLER = "UserSecurityHandler";

	private String redirect;

	private ServletConfig sc;

	@Override
	public void init(final ServletConfig config) throws ServletException {

		try {
			initParameters(config);
		} catch (Exception E) {
			LOG.log(Level.SEVERE, "Exception in init", E);
		}
	}

	private void initParameters(final ServletConfig scIn) throws Exception {
		sc = scIn;
	}

	@Override
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// LOG.info("doPost called");
		// logParameters(request);
		handleRequest(request, response);

	}

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// LOG.info("doGet called");
		doPost(request, response);

	}

	private void handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		hr = request;

		final boolean reload = stringOK(getNamedParam(RELOAD, hr));

		if (reload) {
			reloadUsh();
			response.sendRedirect("index.jsp");
		}

		if (!reload) {
			// setRedirect(getNamedParam(REDIRECT, hr));
			redirect = getNamedParam(REDIRECT, hr);
			String json = handleQuery(hr);
			if (!stringOK(json)) {
				json = "NO RESPONSE";
			}
			if (stringOK(redirect)) {
				hr.setAttribute(JSON, json);
				final String context = getNamedParam(CON_PATH, hr);
				if (stringOK(context)) {
					LOG.info("Context = " + context);
					final RequestDispatcher reqd = sc.getServletContext()
							.getContext(context).getRequestDispatcher(redirect);
					reqd.forward(request, response);
				} else {
					final RequestDispatcher reqd = sc.getServletContext()
							.getRequestDispatcher(redirect);
					reqd.forward(request, response);
				}

				// response.sendRedirect(redirect);
			} else {
				response.setContentType("application/json");
				hr.getSession().setAttribute(JSON, null);
				final PrintWriter out = response.getWriter();
				out.write(json);
			}
		}

	}

	private final String handleQuery(final HttpServletRequest request) {
		final String queryName = getNamedParam(QUERY_NAME, request);
		if (queryName != null && queryName.length() > 0) {

			final Map<String, String> args = getFiltParamsAsHM(request);

			final SecurityQueryDTO sqd = new SecurityQueryDTO(queryName, args);
			// LOG.info("SQD \n" + getSecServ().GetJSonFromObject(sqd));
			return getJSonFromSqd(sqd);
		}

		// else try for json as a param
		final String jsonQ = getNamedParam(QUERY_JSON, request);
		if (jsonQ != null && jsonQ.length() > 0) {
			// LOG.info("jsonQ = " + jsonQ);
			final SecurityQueryDTO sqd = getSqdFromJSON(jsonQ);
			return getJSonFromSqd(sqd);
		}
		// finally see if there is json in the body
		final String jsonB = getContentAsString(request);
		if (jsonB != null && jsonB.length() > 0) {
			// LOG.info("jsonB = " + jsonB);
			final SecurityQueryDTO sqd = getSqdFromJSON(jsonB);
			return getJSonFromSqd(sqd);
		}

		return null;
	}

	private final String getJSonFromSqd(final SecurityQueryDTO sqd) {
		if (sqd != null) {
			// LOG.info("sqd = " + sqd);
			final String rval = getSecServ().getQueryResultFromQueryDTO(sqd);

			if (sqd.getQueryName().equals(SecurityService.GET_USER_BY_NAME)
					&& stringOK(rval)) {
				hr.getSession().setAttribute(SecurityService.USER_NAME,
						sqd.getArgs().get(SecurityService.USER_NAME));
				// rem password
				sqd.getArgs().put(SecurityService.PASSWORD, "*******");
			}

			// hr.setAttribute(SecurityService.USER_NAME, hr.getSession()
			// .getAttribute(SecurityService.USER_NAME));

			if (stringOK(redirect)) {
				final String qasJ = getSecServ().getJSonFromObject(sqd);
				hr.setAttribute(QUERY_JSON, qasJ);
			}
			if (!stringOK(redirect)) {
				hr.setAttribute(QUERY_JSON, null);
			}
			return rval;
		}

		return null;
	}

	private final SecurityQueryDTO getSqdFromJSON(final String json) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final SecurityQueryDTO sqd = mapper.readValue(json,
					SecurityQueryDTO.class);
			return sqd;
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Exception in getSqdFromJSON", e);
		}
		return null;
	}

	private final String getContentAsString(final HttpServletRequest request) {

		String content = "";
		try {
			final BufferedReader br = request.getReader();
			String nextLine = "";

			final StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
			}

			content = sb.toString();
		} catch (Exception E) {
			LOG.log(Level.SEVERE, "Error in getContentAsString ", E);

		}
		return content;

	}

	private final String getNamedParam(final String param_name,
			final HttpServletRequest hr) {
		String value = null;

		final Enumeration<String> paramNames = hr.getParameterNames();
		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			if (paramName.indexOf(param_name) > -1) {
				final String[] paramValues = hr.getParameterValues(paramName);
				if (paramValues.length == 1) {
					value = paramValues[0];
				}
			}
		}

		return value;
	}

	// private final HashMap<String, String> getParamsAsHM(
	// final HttpServletRequest hr) {
	//
	// HashMap<String, String> result = new HashMap<String, String>();
	// Enumeration<String> paramNames = hr.getParameterNames();
	// while (paramNames.hasMoreElements()) {
	// String paramName = paramNames.nextElement();
	// String[] paramValues = hr.getParameterValues(paramName);
	// if (paramValues.length == 1) {
	// String paramValue = paramValues[0];
	// result.put(paramName, paramValue);
	// }
	// }
	// return result;
	// }

	private final Map<String, String> getFiltParamsAsHM(
			final HttpServletRequest hr) {

		HashMap<String, String> result = new HashMap<String, String>();
		Enumeration<String> paramNames = hr.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (!paramName.equals(CON_PATH) && !paramName.equals(REDIRECT)
					&& !paramName.equals("submit")) {
				String[] paramValues = hr.getParameterValues(paramName);
				if (paramValues.length == 1) {
					String paramValue = paramValues[0];
					result.put(paramName, paramValue);
				}
			}
		}
		return result;
	}

	private void logParameters(final HttpServletRequest hr) {
		Enumeration<String> paramNames = hr.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = hr.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				LOG.info("logParameters paramName = " + paramName
						+ " paramValue = " + paramValue);
			}
		}

	}

	private void logParameters(final String Message, final HttpServletRequest hr) {
		LOG.info("Starting logParam **** Message =" + Message);
		logParameters(hr);
		LOG.info("Ending logParam **** Message =" + Message);

	}

	private final SecurityService getSecServ() {
		if (secServ == null && getUsh() != null) {
			secServ = new SecurityService(getUsh());
		}
		return secServ;
	}

	private void setSecServ(SecurityService secServIn) {
		secServ = secServIn;
	}

	private final UserSecurityHandler getUsh() {
		if (ush == null) {

			String ushName = sc.getInitParameter(USER_SECURITY_HANDLER);

			Properties props = new Properties();
			String key = new StringBuilder().append(ushName).append(".")
					.toString();
			int keyL = key.length();

			Enumeration<String> ipn = sc.getInitParameterNames();
			while (ipn.hasMoreElements()) {
				String name = ipn.nextElement();
				if (name.startsWith(key)) {
					String value = sc.getInitParameter(name);
					String pkey = name.substring(keyL);
					props.setProperty(pkey, value);
				}
			}
			String clname = props.getProperty(CLASS);
			if (clname != null && clname.length() > 0) {
				Object obj = ObjectCacheClassHandler.getInstClass(clname);
				if (obj == null) {
					LOG.log(Level.SEVERE,
							"Handler Could not be created using = " + clname);
				} else {
					if (obj instanceof UserSecurityHandler) {
						ush = (UserSecurityHandler) obj;
						try {
							ush.init(props);
						} catch (Exception e) {
							LOG.log(Level.SEVERE,
									"Could not initialize the UserSecurityHandler",
									e);
						}
					}
				}
			}
		}
		return ush;
	}

	public void setUsh(final UserSecurityHandler ushIn) {
		ush = ushIn;
	}

	private void reloadUsh() {
		getUsh().reload();
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirectIn) {

		// If http assume full url is set
		// if (redirectIn.startsWith("http")) {
		// redirect = redirectIn;
		// } else {
		// // Assume on the same instance but different web app
		// String rq = hr.getRequestURL().toString();
		// String loccp = sc.getServletContext().getContextPath();
		// int in = rq.indexOf(loccp);
		// String baseUrl = rq.substring(0, in);
		// LOG.info("baseUrl = " + baseUrl);
		//
		// LOG.info("hr url = " + hr.getRequestURL());
		// if (redirectIn.startsWith("/")) {
		// redirect = new StringBuilder().append(baseUrl)
		// .append(redirectIn).toString();
		// } else {
		// redirect = new StringBuilder().append(baseUrl).append('/')
		// .append(redirectIn).toString();
		// }
		// }
		
		redirect = redirectIn;
	
		LOG.info("setRedirect redirect = " + redirect);
	}

}
