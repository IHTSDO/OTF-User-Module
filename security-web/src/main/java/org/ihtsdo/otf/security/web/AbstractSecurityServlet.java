package org.ihtsdo.otf.security.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.objectcache.ObjectCacheClassHandler;

/**
 * 
 * @author adamf
 */
public abstract class AbstractSecurityServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5785539099003961809L;

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(AbstractSecurityServlet.class.getName());

	protected HttpServletRequest hr;

	private SecurityService secServ;
	private UserSecurityHandler ush;
	public static final String QUERY_NAME = "queryName";
	public static final String QUERY_JSON = "jsonQuery";
	public static final String REDIRECT = "redirect";
	public static final String CON_PATH = "context";
	public static final String JSON = "json";
	public static final String RELOAD = "reload";
	public static final String BASEURL = "BASEURL";

	public static final String CLASS = "class";
	public static final String USER_SECURITY_HANDLER = "UserSecurityHandler";

	protected String redirect;

	protected ServletConfig sc;

	protected Properties paramsProps;

	protected String[] urlNodes;

	private String baseUrl;

	@Override
	public void init(final ServletConfig config) throws ServletException {

		try {
			initParameters(config);
			getSecServ();
		} catch (Exception E) {
			LOG.log(Level.SEVERE, "Exception in init", E);
		}
	}

	private void initParameters(final ServletConfig scIn) throws Exception {
		sc = scIn;
		ServletContext scon = sc.getServletContext();
		Enumeration<String> contextParams = scon.getInitParameterNames();

		// Add context params first so that init params over write if present

		while (contextParams.hasMoreElements()) {
			String name = contextParams.nextElement();
			String val = scon.getInitParameter(name);
			// LOG.info("Adding context param name = " + name + " val = " +
			// val);
			getParamsProps().setProperty(name, val);
		}
		Enumeration<String> initParams = sc.getInitParameterNames();

		while (initParams.hasMoreElements()) {
			String name = initParams.nextElement();
			String val = sc.getInitParameter(name);
			// LOG.info("Adding init param name = " + name + " val = " + val);
			getParamsProps().setProperty(name, val);
		}
	}

	@Override
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// LOG.info("doPost called path info = " + request.getPathInfo());
		// logParameters(request);
		setUrlNodes(request);
		handlePostRequest(request, response);

	}

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// LOG.info("doGet called path info = " + request.getPathInfo());
		setUrlNodes(request);
		handleGetRequest(request, response);

	}

	protected abstract void handlePostRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException;

	protected abstract void handleGetRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException;

	protected final String getContextFreeUrl(final HttpServletRequest request) {
		// String urlS = request.getRequestURI();
		// String contextP = request.getContextPath();
		String pathI = request.getPathInfo();

		// LOG.info("urlS = " + urlS);
		// LOG.info("contextP = " + contextP);
		// LOG.info("pathI = " + pathI);

		// String noContext = urlS.substring(contextP.length());
		if (pathI.startsWith("/")) {
			return pathI.substring(1);
		}
		return pathI;
	}

	protected final String getContextUrl(final HttpServletRequest request) {
		String urlS = request.getRequestURI();
		String pathI = request.getPathInfo();

		String decPath;
		String decUrl;
		// rem %20
		try {
			decPath = URLDecoder.decode(pathI, "UTF-8");
			decUrl = URLDecoder.decode(urlS, "UTF-8");
		} catch (UnsupportedEncodingException e) {

			// try the basic
			decPath = pathI.replaceAll("%20", "");
			decUrl = urlS.replaceAll("%20", "");
		}

		// LOG.info("getContextUrl pathI = " + pathI);
		// LOG.info("getContextUrl decPath = " + decPath);
		// LOG.info("getContextUrl urlS = " + urlS);
		// LOG.info("getContextUrl decUrl = " + decUrl);

		String context = "";
		if (decPath == null || decPath.length() == 0 || decPath.equals("/")) {
			return context;
		} else {
			int chop = decUrl.indexOf(decPath);
			if (chop > 0) {
				context = urlS.substring(0, chop);
			}
		}
		LOG.info("getContextUrl context = " + context);

		if (context.endsWith("/")) {
			return context;
		} else {
			return context + "/";
		}

	}

	protected final String getContentAsString(final HttpServletRequest request) {

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

	protected final String getNamedParam(final String param_name,
			final HttpServletRequest hr) {
		final Enumeration<String> paramNames = hr.getParameterNames();
		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			if (paramName.indexOf(param_name) > -1) {
				final String[] paramValues = hr.getParameterValues(paramName);
				if (paramValues.length == 1) {
					return paramValues[0];
				}
			}
		}
		return null;
	}

	protected final Map<String, String> getFiltParamsAsHM(
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

	protected void logParameters(final HttpServletRequest hr) {
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

	protected void logParameters(final String Message,
			final HttpServletRequest hr) {
		LOG.info("Starting logParam **** Message =" + Message);
		logParameters(hr);
		LOG.info("Ending logParam **** Message =" + Message);

	}

	public final SecurityService getSecServ() {
		if (secServ == null && getUsh() != null) {
			secServ = new SecurityService(getUsh());
		}
		return secServ;
	}

	public void setSecServ(SecurityService secServIn) {
		secServ = secServIn;
	}

	public final UserSecurityHandler getUsh() {
		if (ush == null) {

			String ushName = getParamsProps()
					.getProperty(USER_SECURITY_HANDLER);

			Properties props = new Properties();
			String key = new StringBuilder().append(ushName).append(".")
					.toString();
			int keyL = key.length();

			for (String name : getParamsProps().stringPropertyNames()) {
				if (name.startsWith(key)) {
					String value = getParamsProps().getProperty(name);
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

	protected void setUsh(final UserSecurityHandler ushIn) {
		ush = ushIn;
	}

	protected void reloadUsh() {
		getUsh().reload();
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirectIn) {
		redirect = redirectIn;
		// LOG.info("setRedirect redirect = " + redirect);
	}

	public final Properties getParamsProps() {
		if (paramsProps == null) {
			paramsProps = new Properties();
		}
		return paramsProps;
	}

	public final void setParamsProps(Properties paramsPropsIn) {
		paramsProps = paramsPropsIn;
	}

	public final HttpServletRequest getHr() {
		return hr;
	}

	public final void setHr(HttpServletRequest hrIn) {
		hr = hrIn;
	}

	public final ServletConfig getSc() {
		return sc;
	}

	public final void setSc(ServletConfig scIn) {
		sc = scIn;
	}

	public final String[] setUrlNodes(final HttpServletRequest request) {
		String noContext = getContextFreeUrl(request);
		return setUrlNodes(noContext);
	}

	public final String[] setUrlNodes(final String urlS) {
		urlNodes = urlS.split("/");
		// for (String s : urlNodes) {
		// LOG.info("setUrlNodes val = " + s);
		// }
		return urlNodes;
	}

	public final String[] getUrlNodes() {
		return urlNodes;
	}

	public final void setUrlNodes(final String[] urlNodesIn) {
		urlNodes = urlNodesIn;
	}

	public final String getBaseUrl() {

		if (baseUrl == null) {
			baseUrl = getParamsProps().getProperty(BASEURL);
			// LOG.info("getBaseUrl baseUrl = " + baseUrl);
		}
		return baseUrl;
	}

	public final void setBaseUrl(final String baseUrlIn) {
		baseUrl = baseUrlIn;
	}

}
