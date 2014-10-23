<html>

    <head>
        <title>ihtsdo.org - request password reset</title>

        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/styles-default.css" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/core.css" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/content-modules.css" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/gcse.css" />
        <link rel="icon" type="image/png" href="images/favicon.png">

        <!--[if lt IE 9]>
<script>
	document.createElement('header');
	document.createElement('nav');
	document.createElement('section');
</script>
<![endif]-->

        <script>
            var myCallback = function() {
            	if (document.readyState == 'complete') {
            		google.search.cse.element.render({
            			div: "searchBox",
            			tag: 'search'
            		});
            	} else {
            		google.setOnLoadCallback(function() {
            			google.search.cse.element.render({
            				div: "searchBox",
            				tag: 'search'
            			});
            		}, true);
            	}
            };

            window.__gcse = {
            	parsetags: 'explicit',
            	callback: myCallback
            };
            (function() {
            	var cx = '000682608166411809097:7u30gcur6l4';
            	var gcse = document.createElement('script');
            	gcse.type = 'text/javascript';
            	gcse.async = true;
            	gcse.src = (document.location.protocol == 'https' ? 'https:' : 'http:') +
            		'//www.google.com/cse/cse.js?cx=' + cx;
            	var s = document.getElementsByTagName('script')[0];
            	s.parentNode.insertBefore(gcse, s);
            })();
        </script>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
        <%@ page import="org.ihtsdo.otf.security.web.WebStatics" %>
    </head>

    <body>
        <div id="pageWrap" class="contain">
            <header>
                <nav class="top contain" style="vertical-align:middle">
                    <ul class="contain" style="vertical-align:middle">
                        <li><a href="http://www.ihtsdo.org/news-articles">News</a>
                        </li>
                        <li><a href="https://ihtsdo.freshdesk.com/support/home">FAQ</a>
                        </li>
                        <li><a href="http://www.ihtsdo.org/members/">Members</a>
                        </li>
                        <li style="vertical-align:middle"><a href="http://www.ihtsdo.org/contact-us/">Contact Us</a>
                        </li>
                        <li>
                            <span style="float:right;width:275px"><div id="searchBox"></div></span>
                        </li>
                    </ul>
                </nav>
                <h1 class="logo"><a href="/">ihtsdo Leading healthcare terminology, worldwide</a></h1>
                <nav class="main contain">
                    <ul class="contain">
                        <li class="selected"><a href="http://www.ihtsdo.org/">Home</a>
                        </li>

                        <li class=""><a href="http://www.ihtsdo.org/about-ihtsdo">IHTSDO</a>
                        </li>

                        <li class=""><a href="http://www.ihtsdo.org/snomed-ct">SNOMED CT</a>
                        </li>

                        <li class=""><a href="http://www.ihtsdo.org/participate">Participate</a>
                        </li>

                    </ul>
                </nav>
            </header>
            <section class="mainContent contain">
                <h3>Request a user password change Email:</h3>
                <form name="loginForm" method="post" action="<%=WebStatics.getAdminReqPwChg()%>">
                    <table>
                        <tr>
                            <td> Username or email : </td>
                            <td>
                                <input name="<%=WebStatics.USERMAIL%>" type="text" /> </td>
                        </tr>
                    </table>
                    <input type="submit" value="Please Send Email" />
                </form>
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
