<%@ include file="include.jsp" %>


<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>"/>
</head>
<body>

<h2>Please Log in</h2>

<form name="loginform" action="query" method="post">
<input type="hidden" name="queryName" value="getUserByName"/>
    <table align="left" border="0" cellspacing="0" cellpadding="3">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username" maxlength="30"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" maxlength="30"></td>
        </tr>

        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="Login"></td>
        </tr>
    </table>
</form>

</body>
</html>
