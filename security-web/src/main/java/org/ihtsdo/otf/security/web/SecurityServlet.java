package org.ihtsdo.otf.security.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.ihtsdo.otf.security.dto.query.SecurityQueryDTO;
import org.ihtsdo.otf.security.dto.query.SecurityService;

/**
 * 
 * @author adamf
 * 
 *         <h1>A Servlet to handle the needs of the IHTSDO User Management</h1>
 * 
 *         <h4>GET: It will handle queries sent via an HTTP GET via either a
 *         queryname parameter + other parameters or via REST URLs</h4>
 * 
 *         <h5>REST URLS:</h5> <div style="font-weight: bold;">
 *         <ul>
 *         <li>
 *         Reload Model:
 *         <ul>
 *         <li>/reload <br/>
 *         RETURNS: Nothing. Reloads the model from the backing persistence
 *         store</li>
 *         </ul>
 *         <li>
 *         <li>
 *         Members:
 *         <ul>
 *         <li>/members <br/>
 *         RETURNS: A List of All members</li>
 *         </ul>
 *         <li>
 *         Users:
 *         <ul>
 *         <li>/users <br/>
 *         RETURNS: A List of All Users</li>
 *         <li>/users/##user <br/>
 *         RETURNS: Details for that user</li>
 *         <li>/users/##user/members <br/>
 *         RETURNS: A List of the memberships for that user</li>
 *         <li>/users/##user/apps <br/>
 *         RETURNS: A List of the Applications for that user.</li>
 *         <li>/users/##user/apps/##app <br/>
 *         RETURNS: A List of the Application permissions for that user with
 *         that app</li>
 *         <li>/users/##user/apps/##app/members/##member<br/>
 *         RETURNS: A List of the Application permissions for that user with
 *         that application and that membership</li>
 *         </ul>
 *         <li>
 *         Apps:
 *         <ul>
 *         <li>/apps <br/>
 *         RETURNS: A List all Applications</li>
 *         <li>/apps}/##app/users <br/>
 *         RETURNS: List all the users for that application</li>
 *         <li>/apps/##app/perms <br/>
 *         RETURNS: A List of permissions for the application</li>
 *         <li>/apps/##app/perms/##group <br/>
 *         RETURNS: A List of permissions for the application with that
 *         group/role</li>
 *         </ul>
 *         </ul>
 *         </div>
 * 
 *         <h5>Parameters</h5>
 * 
 */
public class SecurityServlet extends AbstractSecurityServlet {

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

	@Override
	protected void handlePostRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		LOG.info("handlePostRequest");
		System.out.println("handlePostRequest");
		setHr(request);

		// AUTHUSER
		handleGetRequest(request, response);

	}

	@Override
	protected void handleGetRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		LOG.info("handleGetRequest");
		System.out.println("handleGetRequest");
		setHr(request);

		String urlS = request.getRequestURI();
		// LOG.info("urls = " + urlS);
		// String contextP = request.getContextPath();
		// LOG.info("context = " + contextP);

		// See if Rest of QueryName
		// boolean isRest = false;
		// final String queryName = getNamedParam(QUERY_NAME, request);
		// if (queryName != null && queryName.length() > 0) {
		// isRest = true;
		// }

		boolean reload = stringOK(getNamedParam(RELOAD, hr));
		if (!reload) {
			// try rest
			reload = urlS.endsWith(RELOAD);
		}

		if (reload) {
			reloadUsh();
			response.sendRedirect("index.jsp");
		}

		if (!reload) {
			// setRedirect(getNamedParam(REDIRECT, hr));
			redirect = getNamedParam(REDIRECT, hr);
			// LOG.info("redirect = " + redirect);
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
		boolean isQuery = stringOK(queryName);

		if (isQuery) {
			final Map<String, String> args = getFiltParamsAsHM(request);
			final SecurityQueryDTO sqd = new SecurityQueryDTO(queryName, args);
			// LOG.info("SQD \n" + getSecServ().GetJSonFromObject(sqd));
			String rval = getJSonFromSqd(sqd);
			if (stringOK(rval)) {
				return rval;
			}
		}

		// try rest url

		final SecurityQueryDTO sqdRest = getSqdFromRequest(request);
		if (sqdRest != null) {
			String rval = getJSonFromSqd(sqdRest);
			if (stringOK(rval)) {
				return rval;
			}
		}

		// else try for json as a param
		final String jsonQ = getNamedParam(QUERY_JSON, request);
		if (jsonQ != null && jsonQ.length() > 0) {
			// LOG.info("jsonQ = " + jsonQ);
			final SecurityQueryDTO sqd = getSqdFromJSON(jsonQ);
			String rval = getJSonFromSqd(sqd);
			if (stringOK(rval)) {
				return rval;
			}
		}
		// finally see if there is json in the body
		final String jsonB = getContentAsString(request);
		if (jsonB != null && jsonB.length() > 0) {
			// LOG.info("jsonB = " + jsonB);
			final SecurityQueryDTO sqd = getSqdFromJSON(jsonB);
			String rval = getJSonFromSqd(sqd);
			if (stringOK(rval)) {
				return rval;
			}
		}

		return null;
	}

	private final SecurityQueryDTO getSqdFromRequest(
			final HttpServletRequest request) {
		String noContext = getContextFreeUrl(request);
		return getSqdFromRestUrl(noContext);

	}

	private final SecurityQueryDTO getSqdFromRestUrl(final String urlS) {

		if (urlNodes == null || urlNodes.length == 0) {
			return null;
		}

		if (urlNodes.length > 0) {
			switch (urlNodes[0]) {
			case SecurityService.MEMBERS:
				return getSqdFromUrlMembers(urlNodes);
			case SecurityService.USERS:
				return getSqdFromUrlUsers(urlNodes);
			case SecurityService.APPS:
				return getSqdFromUrlApps(urlNodes);
			default:
				return null;
			}
		}
		return null;

	}

	private final SecurityQueryDTO getSqdFromUrlUsers(String[] nodes) {

		final Map<String, String> args = new HashMap<String, String>();
		if (nodes.length == 1) {
			return new SecurityQueryDTO(SecurityService.GET_USERS, args);
		}
		if (nodes.length == 2) {
			args.put(SecurityService.USER_NAME, nodes[1]);
			return new SecurityQueryDTO(SecurityService.GET_USER_BY_NAME, args);
		}
		if (nodes.length == 3) {
			String str2 = nodes[2];
			args.put(SecurityService.USER_NAME, nodes[1]);
			if (str2.equals(SecurityService.MEMBERS)) {
				return new SecurityQueryDTO(
						SecurityService.GET_USER_MEMBERSHIPS, args);
			}
			if (str2.equals(SecurityService.APPS)) {
				return new SecurityQueryDTO(SecurityService.GET_USER_APPS, args);
			}

			// Look at listing all apps for a User

		}
		if (nodes.length == 4) {
			String str2 = nodes[2];
			String str3 = nodes[3];
			args.put(SecurityService.USER_NAME, nodes[1]);
			if (str2.equals(SecurityService.APPS)) {
				args.put(SecurityService.APP_NAME, str3);
				return new SecurityQueryDTO(SecurityService.GET_USER_APP_PERMS,
						args);
			}
		}

		if (nodes.length == 6) {
			String str2 = nodes[2];
			String str3 = nodes[3];
			String str4 = nodes[4];
			String str5 = nodes[5];

			args.put(SecurityService.USER_NAME, nodes[1]);
			if (str2.equals(SecurityService.APPS)) {
				args.put(SecurityService.APP_NAME, str3);
				if (str4.equals(SecurityService.MEMBERS)) {
					args.put(SecurityService.MEMBER, str5);
					return new SecurityQueryDTO(
							SecurityService.GET_USER_APP_PERMS, args);
				}
			}
		}

		return null;
	}

	private final SecurityQueryDTO getSqdFromUrlApps(String[] nodes) {
		final Map<String, String> args = new HashMap<String, String>();

		if (nodes.length == 1) {
			return new SecurityQueryDTO(SecurityService.GET_APPS, args);
		}

		if (nodes.length == 3) {
			String str2 = nodes[2];
			args.put(SecurityService.APP_NAME, nodes[1]);
			if (str2.equals(SecurityService.USERS)) {
				return new SecurityQueryDTO(SecurityService.GET_APP_USERS, args);
			}
			if (str2.equals(SecurityService.PERMS)) {
				return new SecurityQueryDTO(
						SecurityService.GET_APP_PERM_GROUPS, args);
			}
		}

		if (nodes.length == 4) {
			String str2 = nodes[2];
			String str3 = nodes[3];
			args.put(SecurityService.APP_NAME, nodes[1]);
			if (str2.equals(SecurityService.PERMS)) {
				args.put(SecurityService.GRP_NAME, str3);
				return new SecurityQueryDTO(
						SecurityService.GET_APP_PERM_GROUPS, args);
			}
		}

		return null;
	}

	private final SecurityQueryDTO getSqdFromUrlMembers(String[] nodes) {
		final Map<String, String> args = new HashMap<String, String>();
		return new SecurityQueryDTO(SecurityService.GET_MEMBERS, args);
	}

	private final String getJSonFromSqd(final SecurityQueryDTO sqd) {
		if (sqd != null) {
			// LOG.info("sqd = " + sqd);
			final String rval = getSecServ().getQueryResultFromQueryDTO(sqd);

			if (sqd.getQueryName()
					.equals(SecurityService.GET_USER_BY_NAME_AUTH)
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

}
