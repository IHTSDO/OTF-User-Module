
<%@ include file="include.jsp"%>

<html>
<head>
<link type="text/css" rel="stylesheet"
	href="<c:url value="./style.css"/>" />
	
<title>Query Result</title>
</head>
<body>

	<h1>IHTSDO OTF User Management Web Example Query Result</h1>
	<%
	String json = (String)request.getAttribute("json");
	String query = (String)request.getAttribute("jsonQuery");
	String user = (String)request.getAttribute("username");
	
	//System.out.println("Status = "+response.getStatus());
	%>

	
	 <p>
	 <a href="<%=request.getContextPath()%>/home.jsp">Back To Home</a> 
	 </p>
	<h2>Result:</h2>
	
	<table class="sample">
        <tr>
            <td>Query:</td>
            <td><%=query %></td>
        </tr>
        <tr>
            <td>Result:</td>
            <td><textarea cols="50" rows="10"><%=json %></textarea> </td>
        </tr>
    </table>
</body>
</html>
