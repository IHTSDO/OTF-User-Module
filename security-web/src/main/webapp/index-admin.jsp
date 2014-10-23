<html>
		<%
		String context = session.getAttribute("BASEURL").toString();
		//System.out.println("context = "+context);
		String username = session.getAttribute("userName").toString();
		String authToken = session.getAttribute("AUTH_TOKEN").toString();
		Object save = session.getAttribute("save");
		boolean canSave = save != null && save.toString().length() > 0;
		//System.out.println("username = "+username +" Authtoken = "+authToken);
		%>
        <head>
            <title>IHTSDO OTF User Management Admin</title>

            <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/styles-default.css" />
            <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/core.css" />
            <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/content-modules.css" />
            <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
            <link rel="icon" type="image/png" href="images/favicon.png">
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        </head>

        <body>
            <script src="<%= request.getContextPath()%>/jquery-2.1.0.js" type="text/javascript"></script>

            <script>
                function replaceRole(select, hiddenRoleId, cssClass, selectName) {
                	var selVal = select.options[select.selectedIndex].text;
                	//	alert(selVal);
                	var selId = selVal + hiddenRoleId;
                	var hiddenRole = document.getElementById(selId);
                	var pdiv = $(select).closest(cssClass);
                	var roleSelect = $(pdiv).find(selectName);
                	roleSelect.html(hiddenRole.innerHTML);
                }

                function appendElement(urlIn, parentId) {

                	//create the ajax request
                	$.get(urlIn, function(data) {
                		$(parentId).html(data);
                		//alert( "Load was performed." );
                	});


                }

                $(window).load(function() {
                	$(".base_web_text_input_error").mouseover(function() {
                		//alert( "Load was performed. this = "+$(this).attr("class") );
                		$(this).parent().children(".errortooltip").show();
                	}).mouseout(function() {
                		$(this).parent().children(".errortooltip").hide();
                	});

                });
            </script>

            <div id="pageWrap" class="contain">
                <header>
                    <nav class="top contain" style="vertical-align:middle">
                        <ul class="contain" style="vertical-align:middle">
                            <li><a href="/news-articles">News</a>
                            </li>
                            <li><a href="https://ihtsdo.freshdesk.com/support/home">FAQ</a>
                            </li>
                            <li><a href="/members/">Members</a>
                            </li>
                            <li style="vertical-align:middle"><a href="/contact-us/">Contact Us</a>
                            </li>
                        </ul>
                    </nav>
                    <h1 class="logo"><a href="/">ihtsdo Leading healthcare terminology, worldwide</a></h1>
                    <nav class="main contain">
                    </nav>
                </header>
                <section class="mainContent contain">
                    <h1 class="blue">IHTSDO OTF User Management Admin</h1>
                    <div class="container">
                        <div class="row">
                            <div class="topRow"><a href="<%=context%>users">Users</a>
                            </div>
                            <div class="topRow"><a href="<%=context%>apps">Apps</a>
                            </div>
                            <div class="topRow"><a href="<%=context%>members">Members</a>
                            </div>
                            <div class="topRow"><a href="<%=context%>settings">Settings</a>
                            </div>
                            <div class="topRow"><a href="<%=context%>reload">Reload</a>
                            </div>
                            <%if(canSave){%>
                                <div class="topRow"><a href="<%=context%>save">Save</a>
                                </div>
                                <%}%>

                        </div>
                    </div>
                    <div class="container">
                        <div class="row">
                            <div id="leftCol" class="columnLeft">
                                <%=s ession.getAttribute( "TreeHTML") %>
                            </div>
                            <div id="rightCol" class="columnRight">
                                <%=s ession.getAttribute( "FormHTML") %>
                            </div>
                        </div>
                    </div>
				</section>
				<footer class="contain">
					<p class="companyName"><span>International Health Terminology Standards Development Organisation</span>
					</p>
					<ul class="social contain">
						<li class="text">Follow us:</li>
						<li class="linkedIn"><a href="https://www.linkedin.com/company/ihtsdo" target="_blank">LinkedIn</a>
						</li>
						<li class="twitter"><a href="https://twitter.com/SnomedCT" target="_blank">twitter</a>
						</li>
						<li class="googlePlus"><a href="http://google.com/+IhtsdoOrg" target="_blank">google+</a>
						</li>
					</ul>
					<ul class="footerLinks contain">
						<li><a href="http://www.ihtsdo.org/news-articles">News</a>
						</li>
						<li><a href="https://ihtsdo.freshdesk.com/support/home">FAQ</a>
						</li>
						<li><a href="http://www.ihtsdo.org/members/">Members</a>
						</li>
						<li><a href="http://www.ihtsdo.org/contact-us/">Contact Us</a>
						</li>
						<li><span>&copy; Copyright IHTSDO 2014</span>
						</li>
					</ul>
				</footer>

				</div>
			</body>
</html>
