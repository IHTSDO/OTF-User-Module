<html>
<head>
<%@ page import="org.ihtsdo.otf.security.web.WebStatics" %>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
</head>
<body>
	<h1 class="blue">Request a user password change Email:</h1>
            <form name="loginForm" method="post" action="<%=WebStatics.getAdminReqPwChg()%>">
                <table>
                    <tr>
                        <td> Username or email  : </td><td> <input name="<%=WebStatics.USERMAIL%>" type="text" /> </td> 
                    </tr>
                </table>
                <input type="submit" value="Please Send Email" />
            </form>   
</body>
</html>
