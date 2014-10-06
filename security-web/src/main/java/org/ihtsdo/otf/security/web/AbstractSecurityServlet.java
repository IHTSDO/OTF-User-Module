package org.ihtsdo.otf.security.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.query.SecurityService;
import org.ihtsdo.otf.security.objectcache.ObjectCacheClassHandler;
import org.ihtsdo.otf.security.util.PropertiesLoader;
import org.ihtsdo.otf.security.util.UuidConverter;
import org.ihtsdo.otf.security.xml.XmlUserSecurityHandler;

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

	protected String redirect;

	protected ServletConfig sc;

	protected Properties paramsProps;

	protected String[] urlNodes;

	private String baseUrl;

	private String savePath = null;
	private Boolean canSave = null;

	public static final String NO_PERM = "NO_Admin_Edit_Perm";

	@Override
	public void init(final ServletConfig config) throws ServletException {

		try {
			initParameters(config);
			getSecServ();
		} catch (Exception E) {
			LOG.log(Level.SEVERE, "Exception in init", E);
		}
	}

	public final List<String> getInitParamNames() {
		List<String> pnames = new ArrayList<String>();
		pnames.add(WebStatics.SETTINGS_PROPS);
		pnames.add(WebStatics.USER_SECURITY_HANDLER);
		pnames.add(WebStatics.SAVE);
		return pnames;
	}

	private void initParameters(final ServletConfig scIn) throws Exception {
		sc = scIn;
		ServletContext scon = sc.getServletContext();
		Enumeration<String> contextParams = scon.getInitParameterNames();

		// Add context params first so that init params over write if present

		Properties contextProps = new Properties();

		while (contextParams.hasMoreElements()) {
			String name = contextParams.nextElement();
			String val = scon.getInitParameter(name);
			contextProps.setProperty(name, val);
		}
		Enumeration<String> initParams = sc.getInitParameterNames();

		while (initParams.hasMoreElements()) {
			String name = initParams.nextElement();
			String val = sc.getInitParameter(name);
			contextProps.setProperty(name, val);
		}

		PropertiesLoader pl = new PropertiesLoader(getInitParamNames(),
				WebStatics.SETTINGS_PROPS, contextProps);

		setParamsProps(pl.getSettings());

	}

	private Properties getCmdEnvProps() {

		Properties setProps = null;
		String setProp = null;

		Properties sysProps = System.getProperties();
		for (Object key : sysProps.keySet()) {
			if (key.toString().equals(WebStatics.SETTINGS_PROPS)) {
				setProp = sysProps.getProperty(key.toString());
			}
		}
		if (setProp == null) {
			Map<String, String> env = System.getenv();
			for (String envName : env.keySet()) {
				if (envName.equals(WebStatics.SETTINGS_PROPS)) {
					setProp = env.get(envName);
				}
			}
		}
		if (setProp != null) {
			try {
				setProps = new Properties();
				setProps.load(new FileInputStream(setProp));
			} catch (IOException e) {
				LOG.log(Level.SEVERE,
						"Tried to load parameters properties files from \n"
								+ setProp, e);
			}
		}
		return setProps;
	}

	protected String authUser(final HttpServletRequest request) {

		// See if UN and Token are set in the session
		String uname = (String) request.getSession().getAttribute(
				WebStatics.USERNAME);
		String token = (String) request.getSession().getAttribute(
				WebStatics.AUTH_TOKEN);

		// if un & tok OK & set in the Session assume OK

		if (stringOK(uname) && stringOK(token)) {
			return uname;
		}

		// Get the UN + pw strings
		if (!stringOK(uname)) {
			uname = getNamedParam(WebStatics.USERNAME, request);
		}
		String password = getNamedParam(WebStatics.PASSWD, request);
		// auth users

		// test to see if token

		// HERE

		return authUser(uname, password, token, request);

	}

	private String authUser(final String uname, String password,
			final String tokenIn, final HttpServletRequest request) {
		if (stringOK(uname)) {
			// First see if user is admin
			Boolean perm = getUsh().getUserSecurityModel().getAdminUsers()
					.contains(uname);
			if (!perm) {
				return NO_PERM;
			}

			OtfAccount oacc = getUsh().authAccount(uname, password, tokenIn);
			if (oacc != null) {
				String token = oacc.getAuthToken();
				if (!stringOK(token)) {
					token = UuidConverter.format(UUID.randomUUID());
					oacc.setAuthToken(token);
				}

				getUsh().getUserSecurityModel().setUsersToken(uname, token);

				request.getSession().setAttribute(WebStatics.USERNAME, uname);
				request.getSession().setAttribute(WebStatics.AUTH_TOKEN, token);
				password = null;
				return uname;
			}
		}
		return null;
	}

	@Override
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		setUrlNodes(request);
		handlePostRequest(request, response);

	}

	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
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
		String pathI = getNotNullPath(request);
		if (pathI.startsWith("/")) {
			return pathI.substring(1);
		}
		return pathI;
	}

	protected String getNotNullPath(final HttpServletRequest request) {
		String pathI = request.getPathInfo();
		if (pathI == null) {
			return "/";
		}

		return pathI;

	}

	protected final String getDecString(String toDec) {
		String dec = null;
		try {
			dec = URLDecoder.decode(toDec, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			dec = toDec.replaceAll("%20", "");
		}
		return dec;
	}

	protected final String getContextUrl(final HttpServletRequest request) {
		String urlS = request.getRequestURI();
		String pathI = getNotNullPath(request);

		String decPath;
		String decUrl;

		decPath = getDecString(pathI);
		decUrl = getDecString(urlS);

		String context = "";
		if (decPath == null || decPath.length() == 0 || decPath.equals("/")) {
			if (decUrl.endsWith("/")) {
				return decUrl;
			} else {
				return decUrl + "/";
			}
		} else {
			int chop = decUrl.indexOf(decPath);
			if (chop > 0) {
				context = urlS.substring(0, chop);
			}
		}

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
			if (!paramName.equals(WebStatics.CON_PATH)
					&& !paramName.equals(WebStatics.REDIRECT)
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
			String ushName = getParamsProps().getProperty(
					WebStatics.USER_SECURITY_HANDLER);
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

			String clname = props.getProperty(WebStatics.CLASS);
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
		getCanSave();
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
		if (stringOK(urlS)) {
			urlNodes = urlS.split("/");
		}

		if (urlNodes == null || !stringOK(urlS)) {
			urlNodes = new String[1];
			urlNodes[0] = "";
		}
		return urlNodes;
	}

	private void logUrlNodes() {
		if (urlNodes == null) {
			LOG.info("logUrlNodes urlNodes is null");
		} else {
			LOG.info("logUrlNodes urlNodes length = " + urlNodes.length);
			for (String url : urlNodes) {
				LOG.info("url = " + url);
			}
		}

	}

	public final String[] getUrlNodes() {
		return urlNodes;
	}

	public final void setUrlNodes(final String[] urlNodesIn) {
		urlNodes = urlNodesIn;
	}

	public final String getBaseUrl() {

		if (baseUrl == null) {
			baseUrl = getParamsProps().getProperty(WebStatics.BASEURL);
			LOG.info("getBaseUrl baseUrl = " + baseUrl);
		}
		return baseUrl;
	}

	public final void setBaseUrl(final String baseUrlIn) {
		baseUrl = baseUrlIn;
	}

	private boolean canSaveFile() {
		if (stringOK(getSavePath())) {
			File fi = new File(getSavePath());
			if (fi.exists()) {
				if (fi.isFile()) {
					return fi.canWrite();
				}
			} else {
				File parent = fi.getParentFile();
				if (parent != null && parent.exists()) {
					// assume dir
					return parent.canWrite();
				}
			}
		}
		return false;
	}

	public final Boolean getCanSave() {

		if (canSave == null) {
			canSave = new Boolean(canSaveFile());
			if (canSave) {
				hr.getSession().setAttribute(WebStatics.SAVE, "true");
			}
			if (!canSave) {
				hr.getSession().removeAttribute(WebStatics.SAVE);
			}
		}

		return canSave;
	}

	public final void setCanSave(Boolean canSaveIn) {
		canSave = canSaveIn;
	}

	public final String getSavePath() {
		if (savePath == null) {
			savePath = getParamsProps().getProperty(WebStatics.SAVE);
		}
		return savePath;
	}

	public final void setSavePath(String savePathIn) {
		savePath = savePathIn;
	}

	public void save() {
		if (getCanSave()) {
			XmlUserSecurityHandler xmlUsOut = new XmlUserSecurityHandler();
			xmlUsOut.setConfigFN(getSavePath());
			xmlUsOut.getUserSecurityModel().setModel(
					(getUsh().getUserSecurityModel().getFullModel()));
			try {
				xmlUsOut.saveUserSecurity();
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Error trying to write model to file", e);
			}
		}
	}
}
