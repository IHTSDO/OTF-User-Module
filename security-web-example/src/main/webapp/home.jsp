
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">

<title>IHTSDO OTF User Management Web Example</title>
</head>
<body>

	<h1>IHTSDO OTF User Management Web Example</h1>
	<%
	String contextPath = request.getContextPath();
	System.out.println("contextPath = "+contextPath);
	
	String action ="/security-web/query";
	if(contextPath.endsWith("dev")){
		action ="/security-web-dev/query";
	}
	%>
	<p><form name="checkUserform" action="<%=action%>" method="post">
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
					name="submit" value="Authenticate User"></td>
        </tr>
    </table>
</form></p>


<p>

    <table class="sample">
        <tr>
            <td> 	<form name="Reloadform" action="<%=action%>" method="post">
<input type="hidden" name="reload" value="true" />
<input type="submit" name="submit" value="Reload Model" />
</form></td>
        </tr>
        <tr>
            <td><form name="listUsersform" action="<%=action%>" method="post">
<input type="hidden" name="queryName" value="getUsers" />
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
<input type="submit" name="submit" value="List Users" />
</form></td>
        </tr>

        <tr>
            <td><form name="listMambersform" action="<%=action%>" method="post">
<input type="hidden" name="queryName" value="getMembers" />
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
<input type="submit" name="submit" value="List Members" />
</form></td>
        </tr>
                <tr>
            <td><form name="listAppsform" action="<%=action%>" method="post">
<input type="hidden" name="queryName" value="getApps" />
<input type="hidden" name="redirect" value="/result.jsp" />
<input type="hidden" name="context" value="<%=contextPath%>" />
<input type="submit" name="submit" value="List Apps" />
</form></td>
        </tr>
    </table>


<p><form name="UserMemform" action="<%=action%>" method="post">
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

<p><form name="UserAppform" action="<%=action%>" method="post">
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

<p><form name="UserAppPermform" action="<%=action%>" method="post">
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

<p><form name="AppPermGroupform" action="<%=action%>" method="post">
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

<p><form name="AppUsersform" action="<%=action%>" method="post">
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

</body>
</html>
