<html>
    <head>
        <title>ihtsdo.org - admin login</title>

        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/styles-default.css" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/core.css" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/content-modules.css" />
        <link rel="icon" type="image/png" href="images/favicon.png">

        <!--[if lt IE 9]>
<script>
	document.createElement('header');
	document.createElement('nav');
	document.createElement('section');
</script>
<![endif]-->

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
                <h3>Please login to administer users</h3>
                <form name="loginForm" method="post" action=".">
                    <table>
                        <tr>
                            <td> Username : </td>
                            <td>
                                <input name="userName" type="text" /> </td>
                        </tr>
                        <tr>
                            <td> Password : </td>
                            <td>
                                <input name="passWord" type="password" /> </td>
                        </tr>
                    </table>
                    <input type="submit" value="Login" />
                </form>
                <a href="<%= request.getContextPath()%>/requestPwChange.jsp" target="_blank">Wish to change password? Click here</a>
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
