<html>
<%System.out.println("Index-admin jsp loaded");%>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
<title>IHTSDO OTF User Management Admin</title>
</head>
<body>
 <!--  <script src="jquery.js"></script> -->
<script>

</script>
	<h1 class="blue">IHTSDO OTF User Management Admin</h1>
<div class="topPanel">	
	<a href="userAdmin/users">  Users   </a> <a href="userAdmin/apps">  Apps   </a><a href="userAdmin/members">  Members   </a>
</div>	
	<div class="container">
		<div class="row">
			<div id="leftCol" class="columnLeft"><%= session.getAttribute("TreeHTML") %></div>
			<!--  <div id="rightCol" class="columnRight"><jsp:include page="content.jsp" /></div> -->
		</div>
	</div>
</body>
</html>
