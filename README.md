OTF-User-Module
===============

User Module for the OTF

http://198.211.118.116:8080/security-web-example

All non Stormpath admin users have a password of: Sn0m3dDefPass
Best to use Bob as he has the most things set up.
Best to use Mapping as the App as that has the most things set up.

Git: https://github.com/IHTSDO/OTF-User-Module.git

Master branch

For structure etc look at:
/OTF-User-Module/security/TextFiles\Example.xml

Main code is found @:

/OTF-User-Module/security/src/main/java/org/ihtsdo/otf/security

Servlet is at:
/OTF-User-Module/security-web/src/main/java/org/ihtsdo/otf/security/web/SecurityServlet.java

Main example JSP is at:

/OTF-User-Module/security-web-example/src/main/webapp/home.jsp

The example shows the json incoming + the returned json.

The Reload model does just that and will give you and indication of how long it takes to load the model. 
The "no response" is meant to happen and shows when it has been reloaded but that given it was not a query, that is the response you would expect as it was not a query but a reload operation.

Have a play.

 If there are queries which are obviously missing then shout out.

