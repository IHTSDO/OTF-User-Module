<html>
<head>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
</head>
<body>
	<h1 class="blue">Please login to administer users</h1>
            <form name="loginForm" method="post" action=".">
                <table>
                    <tr>
                        <td> Username  : </td><td> <input name="userName" type="text" /> </td> 
                    </tr>
                    <tr>
                        <td> Password  : </td><td> <input name="passWord"  type="password" /> </td> 
                    </tr>
                </table>
                <input type="submit" value="Login" />
            </form>

<a class="pseudoLabel" href="<%= request.getContextPath()%>/requestPwChange.jsp" target="_blank">Forgot password? Click here</a>
        
</body>
</html>
