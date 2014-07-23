<html>
<head>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
<%@ page import="org.ihtsdo.otf.security.web.WebStatics" %>
<%
			String sptoken = request.getParameter(WebStatics.SP_TOKEN);
			String uname = request.getParameter(WebStatics.USERNAME);%>
</head>
<body>
	<h1 class="blue">Change Password For <%=uname%></h1>
            <form name="loginForm" method="post" action="<%=WebStatics.getAdminPwChg()%>">
                <table>
                    <tr>
                        <td> Password  : </td><td> <input name="<%=WebStatics.PASSWD%>"  type="password" /> </td> 
                    </tr>
                </table>
                <input type="submit" value="Change Password" />
                <input type="hidden" name="<%=WebStatics.SP_TOKEN%>" value="<%=sptoken%>">
                <input type="hidden" name="<%=WebStatics.USERNAME%>" value="<%=uname%>">
                
            </form>

        
</body>
</html>
