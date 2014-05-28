<html>
<%
String context = session.getAttribute("BASEURL").toString();;
%>
<head>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
<title>IHTSDO OTF User Management Admin</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<script src="<%= request.getContextPath()%>/jquery-2.1.0.js" type="text/javascript"></script> 

<script>

function replaceRole(select,hiddenRoleId,cssClass,selectName){	
	var selVal = select.options[select.selectedIndex].text;
//	alert(selVal);
	var selId =selVal+hiddenRoleId;
	var hiddenRole = document.getElementById(selId);
	var pdiv = $(select).closest(cssClass);
	var roleSelect = $(pdiv).find(selectName);	
	roleSelect.html(hiddenRole.innerHTML);		
}

function appendElement(urlIn,parentId){
	
	 //create the ajax request
	$.get( urlIn, function( data ) {
		  $( parentId ).html( data );
		  //alert( "Load was performed." );
		});
	
	
}

$(window).load(function(){
	$(".base_web_text_input_error").mouseover(function() {
		//alert( "Load was performed. this = "+$(this).attr("class") );
	    $(this).parent().children(".errortooltip").show();
	}).mouseout(function() {
	    $(this).parent().children(".errortooltip").hide();
	});

	});
</script> 

	<h1 class="blue">IHTSDO OTF User Management Admin</h1>
<div class="container">	
	<div class="row">
	<div class="topRow"><a href="<%=context%>users">Users</a></div>
	<div class="topRow"><a href="<%=context%>apps">Apps</a></div>
	<div class="topRow"><a href="<%=context%>members">Members</a></div>
	<div class="topRow"><a href="<%=context%>settings">Settings</a></div>
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
