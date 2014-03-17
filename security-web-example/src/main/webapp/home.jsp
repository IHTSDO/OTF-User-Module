
<%@ include file="include.jsp"%>

<html>
<head>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/style.css"/>" />
<title>IHTSDO OTF User Management Web Example</title>
</head>
<body>

	<h1>IHTSDO OTF User Management Web Example</h1>
	<%
	String welcome = "Hullo please login";
	String user = (String)session.getAttribute("username");
	if(user != null){
		welcome = "Hello "+user;
	}
	
	String contextPath = request.getContextPath();
	//System.out.println("contextPath = "+contextPath);
	%>

	<p><%=welcome%></p>
	<!-- <p><a href="<c:url value="/login.jsp"/>">Log in</a> </p> -->
	<p><form name="loginform" action="/security-web/query" method="post">
		<input type="hidden" name="queryName" value="getUserByName" />
		<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
    <table class="sample">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username" maxlength="30"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password"
					maxlength="30"></td>
        </tr>

        <tr>
            <td colspan="2" align="right"><input type="submit"
					name="submit" value="Login"></td>
        </tr>
    </table>
</form></p>


<p>

    <table class="sample">
        <tr>
            <td> 	<form name="Reloadform" action="/security-web/query" method="post">
<input type="hidden" name="reload" value="true" />
<input type="submit" name="submit" value="Reload Model" />
</form></td>
        </tr>
        <tr>
            <td><form name="listUsersform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getUsers" />
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
<input type="submit" name="submit" value="List Users" />
</form></td>
        </tr>

        <tr>
            <td><form name="listMambersform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getMembers" />
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
<input type="submit" name="submit" value="List Members" />
</form></td>
        </tr>
                <tr>
            <td><form name="listAppsform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getApps" />
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
<input type="submit" name="submit" value="List Apps" />
</form></td>
        </tr>
    </table>


<p><form name="UserMemform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getUserMemberships"/>
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
    <table class="sample">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="List User Memberships"></td>
        </tr>
    </table>
</form></p>

<p><form name="UserAppform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getUserApps"/>
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
    <table class="sample">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="List User Apps"></td>
        </tr>
    </table>
</form></p>

<p><form name="UserAppPermform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getUserAppPerms"/>
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
    <table class="sample">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username" maxlength="30"></td>
        </tr>
         <tr>
            <td>Application Name:</td>
            <td><input type="text" name="appName" maxlength="30"></td>
        </tr>
                <tr>
            <td>Member (optional):</td>
            <td><input type="text" name="member" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="List User Application Permissions"></td>
        </tr>
    </table>
</form></p>

<p><form name="AppPermGroupform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getAppPermGroups"/>
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
    <table class="sample">
         <tr>
            <td>Application Name:</td>
            <td><input type="text" name="appName" maxlength="30"></td>
        </tr>
                <tr>
            <td>Group name (optional):</td>
            <td><input type="text" name="groupName" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="List Application Permission Groups"></td>
        </tr>
    </table>
</form></p>

<p><form name="AppUsersform" action="/security-web/query" method="post">
<input type="hidden" name="queryName" value="getAppUsers"/>
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
    <table class="sample">
         <tr>
            <td>Application Name:</td>
            <td><input type="text" name="appName" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="List Application Users"></td>
        </tr>
    </table>
</form></p>


getAppUsers
</body>
</html>
