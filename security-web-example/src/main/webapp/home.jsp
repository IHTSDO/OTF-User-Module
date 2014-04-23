
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
<%@ page import="org.ihtsdo.otf.security.example.ExampleDto"%>
<title>IHTSDO OTF User Management Web Example</title>
</head>
<body>

	<h1>IHTSDO OTF User Management Web Example</h1>
	<%
		ExampleDto edto = new ExampleDto(request);
	%>

	<table class="sampleNoBorder">
		<tr VALIGN="top">
			<td>
				<form name="setParamsform" action="./home.jsp" method="post">
					<table class="sampleNoBorder">
						<th>Set the Parameters</th>
						<tr>
							<td><%=ExampleDto.USER%>:</td>
							<td><input type="text" class="w100" name="<%=ExampleDto.USER%>"
								value="<%=edto.getUser()%>" ></td>
						</tr>
						<tr>
							<td><%=ExampleDto.APP%>:</td>
							<td><input type="text" class="w100" name="<%=ExampleDto.APP%>"
								value="<%=edto.getApp()%>"></td>
						</tr>
						<tr>
							<td><%=ExampleDto.GROUP%>:</td>
							<td><input type="text" class="w100" name="<%=ExampleDto.GROUP%>"
								value="<%=edto.getGrp()%>"></td>
						</tr>
						<tr>
							<td><%=ExampleDto.MEMBER%>:</td>
							<td><input type="text" class="w100" name="<%=ExampleDto.MEMBER%>"
								value="<%=edto.getMemb()%>"></td>
						</tr>

						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
			<td>
				<form name="checkUserform" action="<%=edto.getAction()%>"
					method="post">
					<input type="hidden" name="queryName" value="getUserByNameAuth" />
					<input type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<th>Authenticate a user</th>
						<tr>
							<td>User:</td>
							<td><input type="text" class="w100" name="username"
								value="<%=edto.getUser()%>"></td>
						</tr>
						<tr>
							<td>Password:</td>
							<td><input type="password" class="w100" name="password"></td>
						</tr>

						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>

	<table class="sample">
		<tr>
			<td><span class="docSubTitle">Action:</span></td>
			<td><span class="docSubTitle">As Rest URL: </span></td>
			<td><span class="docSubTitle">As Parameters: </span></td>
		</tr>

		<tr class="green">
			<td><span class="docSubSubTitle">Reload Model:</span></td>
			<td><%=edto.getRestLink("reload")%></td>
			<td align="center">
				<form name="Reloadform" action="<%=edto.getAction()%>" method="get"
					class="centered">
					<input type="hidden" name="reload" value="true" /> <input
						type="submit" class="w100" name="submit" value="Submit" />
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">Members:</span></td>
			<td><%=edto.getRestLink("members")%></td>
			<td>
				<form name="listMembersform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getMembers" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<input type="submit" class="w100" name="submit" value="Submit" />
				</form>
			</td>
		</tr>
		<tr class="green">
			<td colspan="3" align="left"><span class="docSubTitleRed">Users:</span></td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">Users:</span></td>
			<td><%=edto.getRestLink("users")%></td>
			<td>
				<form name="listUsersform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getUsers" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<input type="submit" class="w100" name="submit" value="Submit" />
				</form>
			</td>
		</tr>

		<tr class="green">
			<td><span class="docSubSubTitle">User Details:</span></td>
			<td><%=edto.getRestLink("users/##user")%></td>
			<td>
				<form name="UserByName" action="<%=edto.getAction()%>" method="get"
					class="centered">
					<input type="hidden" name="queryName" value="getUserByName" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>User:</td>
							<td><input type="text" class="w100" name="username"
								value="<%=edto.getUser()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">User Apps:</span></td>
			<td><%=edto.getRestLink("users/##user/apps")%></td>
			<td>
				<form name="UserAppform" action="<%=edto.getAction()%>" method="get"
					class="centered">
					<input type="hidden" name="queryName" value="getUserApps" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>User:</td>
							<td><input type="text" class="w100" name="username"
								value="<%=edto.getUser()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">User App Groups:</span></td>
			<td><%=edto.getRestLink("users/##user/apps/##app")%></td>
			<td>
				<form name="UserAppPermform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getUserAppPerms" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>User:</td>
							<td><input type="text" class="w100" name="username"
								value="<%=edto.getUser()%>"></td>
						</tr>
						<tr>
							<td>Application:</td>
							<td><input type="text" class="w100" name="appName"
								value="<%=edto.getApp()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>

		<tr class="green">
			<td><span class="docSubSubTitle">User App Groups By
					Member:</span></td>
			<td><%=edto
					.getRestLink("users/##user/apps/##app/members/##member")%></td>
			<td>
				<form name="UserAppPermform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getUserAppPerms" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>User:</td>
							<td><input type="text" class="w100" name="username"
								value="<%=edto.getUser()%>"></td>
						</tr>
						<tr>
							<td>Application:</td>
							<td><input type="text" class="w100" name="appName"
								value="<%=edto.getApp()%>"></td>
						</tr>
						<tr>
							<td>Member:</td>
							<td><input type="text" class="w100" name="member"
								value="<%=edto.getMemb()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">User Memberships:</span></td>
			<td><%=edto.getRestLink("users/##user/members")%></td>
			<td>
				<form name="UserMemform" action="<%=edto.getAction()%>" method="get"
					class="centered">
					<input type="hidden" name="queryName" value="getUserMemberships" />
					<input type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>User:</td>
							<td><input type="text" class="w100" name="username"
								value="<%=edto.getUser()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>

		<tr class="green">
			<td colspan="3" align="left"><span class="docSubTitleRed">Applications:</span></td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">Apps:</span></td>
			<td><%=edto.getRestLink("apps")%></td>
			<td>
				<form name="listAppsform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getApps" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<input type="submit" class="w100" name="submit" value="Submit" />
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">App Users:</span></td>
			<td><%=edto.getRestLink("apps/##app/users")%></td>
			<td>
				<form name="AppUsersform" action="/security-web" method="get"
					class="centered">
					<input type="hidden" name="queryName" value="getAppUsers" /> <input
						type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="/security-web-example" />
					<table class="sampleNoBorder">
						<tr>
							<td>Application:</td>
							<td><input type="text" class="w100" name="appName"
								value="<%=edto.getApp()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">App Permission Groups:</span></td>
			<td><%=edto.getRestLink("apps/##app/perms")%></td>
			<td>
				<form name="AppPermGroupform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getAppPermGroups" />
					<input type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>Application:</td>
							<td><input type="text" class="w100" name="appName"
								value="<%=edto.getApp()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr class="green">
			<td><span class="docSubSubTitle">App Permission Groups by Group:</span></td>
			<td><%=edto.getRestLink("apps/##app/perms/##group")%></td>
			<td>
				<form name="AppPermGroupform" action="<%=edto.getAction()%>"
					method="get" class="centered">
					<input type="hidden" name="queryName" value="getAppPermGroups" />
					<input type="hidden" name="redirect" value="/result.jsp" /> <input
						type="hidden" name="context" value="<%=edto.getContextPath()%>" />
					<table class="sampleNoBorder">
						<tr>
							<td>Application:</td>
							<td><input type="text" class="w100" name="appName"
								value="<%=edto.getApp()%>"></td>
						</tr>
						<tr>
							<td>Group:</td>
							<td><input type="text" class="w100" name="groupName"
								value="<%=edto.getGrp()%>"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit" class="w100"
								name="submit" value="Submit"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>



	</table>

</body>
</html>
