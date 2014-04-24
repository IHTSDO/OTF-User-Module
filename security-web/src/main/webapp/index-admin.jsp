<html>
<%
String context = session.getAttribute("BASEURL").toString();;
%>
<head>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
<title>IHTSDO OTF User Management Admin</title>
</head>
<body>
 <!--  <script src="jquery.js"></script> -->
<script>

</script>
	<h1 class="blue">IHTSDO OTF User Management Admin</h1>
<div class="container">	
	<div class="row">
	<div class="topRow"><a href="<%=context%>users">Users</a></div>
	<div class="topRow"><a href="<%=context%>apps">Apps</a></div>
	<div class="topRow"><a href="<%=context%>members">Members</a></div>
	</div>
</div>	
	<div class="container">
		<div class="row">
			<div id="leftCol" class="columnLeft"><%= session.getAttribute("TreeHTML") %></div>
			<div id="rightCol" class="columnRight"><%= session.getAttribute("FormHTML") %></div>
		</div>
	</div>
</body>
</html>
