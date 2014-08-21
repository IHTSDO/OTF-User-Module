OTF User Security Module

Note for those simply interested in getting something working fast:
i.e. Quick and dirty - get running w/o explanation read QuickAndDirty.txt

1) Introduction
2) Building
3) Example files
4) Moving data between XML and StormPath
5) Running the web apps 
6) URLS - What you can ask for and the URL to do it.
7) Authentication - tokens
8) Nginx - https set up

(1) Introduction

The OTF User Security Module is designed to do 2 distinct tasks:
(A) Authentication - You are who you say you are. 
(B) Authorization - You can do this.

It consists of 3 modules:
i) Security - the basic security code. includes 2 implementations: 
1 for XML and 1 for Stormpath

ii) Security-web - The web UI/App for interacting with the security system.

This is split into 2 parts: 
A) The /query/ code which responds to REST queries and authentication 
posts with json encoded text.
B) The /admin/ code which allows a user to administer the records.

iii) The security-web-example code which creates a web application for looking 
at the queries you can run as examples.

The Structure which contains this information looks like:

1 directories object which contains 1..n directory objects.
1 applications object which contains 1..n application objects.

An application object contain 1..n Account Store Objects. 
Usually there is only one Account Store per application, it is of type=dir
and points to a directory object.

A directory object contains 0..1 groups object and 0..1 accounts object
A groups object contains 0..n group objects which are in essence roles 
i.e. some group an account can be part of/belong to.
An accounts object contains 0..n account objects which is the user information.

Group and Account objects contain string:string key:value/maps/tables 
called customData

A record/line within a customData is called a customField.
A customField consists of a string key and string value. 
The value is then encoded using a full colon (:) as a separator to give an array 
of connected values.
If encoded the value looks like:
CD_TYPE_APP:Release:Manager:UK
Where the 1st value in the array is the type and then the other values are 
specific to that type e.g. a MEMBER type has a single value which is the member:
CD_TYPE_MEMBER:INTL

All objects have a Status of enabled and disabled. 
There is no deletion as other systems might have used key values 
(for instance a user name who created a record) as records in their 
own data.

A quick way to look at the structure is to examine the :
/OTF-User-Module/security/TextFiles/Example.xml
file.

2) Building

The build system uses Maven.

There are 2 ways to build:

2.1) Use the root pom.xml : /OTF-User-Module/pom.xml

This builds and packages the security and security-web modules into a copy of 
tomcat contained within a debian based linux (e.g. Ubuntu, debian etc) deb file.

If looking to build and deploy onto a new debian linux instance (e.g. a clean 
digital ocean image) this is the one to use,

First you have to install supervisord (supervisord.org)

'sudo apt-get install supervisor'
then 
'dpkg -i otf-user-module-security-web-0.1.2-SNAPSHOT-all.deb'
if you forget to install supervisor first then:
'apt-get -f install'
Will fix it.
The deb sets up the following:

A) The jar (which includes tomcat) gets installed to: 

/opt/otf-user-module-security-web

B) The conf file for supervisor is installed to:

/etc/supervisor/conf.d/otf-user-module-security-web.conf

C) The Conf file points to the properties file:
/etc/opt/otf-user-module-security-web/config.properties
Which you have to adjust for your own use

as detailed below.

D) Logging is written to /var/log/supervisor


The app is stopped by :

sudo supervisorctl stop otf-user-module-security-web

To start:

sudo supervisorctl start otf-user-module-security-web

To restart:

sudo supervisorctl restart otf-user-module-security-web




2.2) Build as individual jar/war files using: 
pom-orig.xml for security and security-web
pom.xml for security-web-example

You can then copy the created war file(s) into a web container of your choice 
(the tested target has been tomcat though it has worked fine in Jetty).

pom-orig.xml for security-web contains the tomcat7-maven-plugin so if you set 
that up you can autodeploy into tomcat using the tomcat7:redeploy maven task.


3) Example Files

An Example XML file containing every required to run the system:

/OTF-User-Module/security/TextFiles/Example_orig.xml

This can be used as is if running as an XML only (test) implementation or via
the Data moving code to insert a model into Stormpath.

The are 2 example properties files for running the web app:

/OTF-User-Module/security/TextFiles/ExampleSecuritySettings.props

Showing all the settings &

/OTF-User-Module/security/TextFiles/MinimalExampleSecuritySettings.props

Showing the minimal settings

There is also an example Data moving properties file which moves example data 
from Example.xml into Stormpath and then writes out the Stormpath Model into a 
separate XML file.

/OTF-User-Module/security/TextFiles/MoveXml2StormPath.txt

4) Moving data between XML and StormPath

Using the security<version>.jar produced by running 

/OTF-User-Module/security/pom-orig.xml

Run org.ihtsdo.otf.security.util.ModelMover

With the argument: 

settings=<YOUR LOCAL FILEPATH TO A RELEVANT PROPERTIES FILE>

e.g. pointing at:
/OTF-User-Module/security/TextFiles/MoveXml2StormPath.txt

Within that properties files you must have

For StormPath:
either :
keyPath - a path to a properties file containing the stormpath apiKey & secret
or
apiKey.id & apiKey.secret as key/values

For the XML :

xmlFnIn - If transferring information from XML this is th model file to load
xmlFnOut - If wishing to write out the Stormpath model as XML this is the file 
to write out to.

Optional Args:

Rebuild=true - strip all information from Stormpath and rebuild it. 
NB does not include STORMPATH ADMINISTRATORS information.
log=true log what is going on.

In the example below the system will:

i) Load from xmlFnIn, 
ii) Rebuild Stormpath
iii) Insert the model using Stormpath key and secret contained within keyPath
iv) Write out the model from Stormpath to xmlFnOut
v) Log these actions.

xmlFnIn=./OTF-User-Module/security/TextFiles/Example.xml
xmlFnOut=./OTF-User-Module/security/TextFiles/Example-out.xml
rebuild=true
log=true
keyPath=C:/Users/adamf/stormpath/apiKey.properties
#apiKey.id=
#apiKey.secret=

5) Running the web apps

5.1) Build the security-web war & if required the security-web-example war & put 
them into the webapps (or equivalent) directory of your java web app server.

5.2) The security web-example war can be set to point to the security web url anywhere using a setting in the web.xml, a commandline (-D) setting or an environment variable set to secWebBaseUrl e.g. 
-DsecWebBaseUrl=https://usermanagement.ihtsdotools.org/security-web

If running in the same container as the security-web app the UI experience can be improved by wrapping the results in a page however that requires allowing crossContext by (in Tomcat) editing the context.xml and setting crossContext = "true" .

5.3) The security-web Settings:

These are laid out in 
 /OTF-User-Module/security/TextFiles/ExampleSecuritySettings.props
 
 The 2 root settings  are:
 
 REQUIRED: UserSecurityHandler
 Sets the UserSecurity Handler to use. The 2 options at present are :
 A) XmlUserSecurity
 B) StormPathUserSecurity
 
 OPTIONAL: save
 If you want to be able to save your model to an XML file (for example you might 
 want to keep it under version control e.g. git) then save points to a this 
 file. 
 
 The String set via UserSecurityHandler is then used as the key to lookup the 
 specific handler e.g. if :
 UserSecurityHandler=XmlUserSecurity
 then the class key will look like XmlUserSecurity.class
 
 REQUIRED: class
 The name of the java class which implements UserSecurityHandler

After Class every setting is handler specific 

XmlUserSecurity:
REQUIRED: configFile
The XML file to use

StormPathUserSecurity
REQUIRED: EITHER
keyPath a file path to a properties file containing apiKey.id and apiKey.secret
OR:
apiKey.id and apiKey.secret
 
See blow for examples: 

 # Security Service Properties File

# UserSecurityHandler
UserSecurityHandler=XmlUserSecurity
#UserSecurityHandler=StormPathUserSecurity

# XML User Security Handler
UserSecurityHandler-XML=XmlUserSecurity
XmlUserSecurity.class=org.ihtsdo.otf.security.xml.XmlUserSecurity
XmlUserSecurity.configFile=D:/Data/git/OTF-User-Module/security/TextFiles/Example.xml

# StormPath Security Handler
StormPathUserSecurity.class=org.ihtsdo.otf.security.stormpath.StormPathUserSecurity

StormPathUserSecurity.keyPath=C:/Users/adamf/stormpath/apiKey.properties
#StormPathUserSecurity.keyPath=/opt/api/apiKey.properties

#StormPathUserSecurity.apiKey.id=SOME LONG ID
#StormPathUserSecurity.apiKey.secret=SOME LONG SECRET
	
	
# Where/What file you want any save actrions to be written to.
save=D:/Data/git/OTF-User-Module/security/TextFiles/Example-save.xml
 
 5.4) Where to put settings.
 
 The code looks in the following places and in the following order for properties:
 Properties found later will over write properties found earlier.
 Look at org.ihtsdo.otf.security.util.PropertiesLoader in the security package 
 for details.
 
1) ServletContext initParams
2) ServletConfig initParams (mostly from web.xml within the war).
3) A properties file indicated in Environment variables (System.getenv())
4) A properties file indicated in System Properties (System.getProperties())
5) Environment Variables whose key is contained in the list below.
6) System Properties whose key is contained in the list below.
 
 
Properties Key values: 
 
SecurityServiceProps  - A filepath to a properties file used in (3) and (4)
UserSecurityHandler - The User Security Handler to use. 
save - the file you may want to back up /write out your model to.

Example:

Setting SecurityServiceProps to:
./MinimalExampleSecuritySettings.props

If using an environment variable then consult your operating system 
documentation for a how to guide.

If using a System Property then call the java runtime with a -D argument.
This can be done within tomcat by creating a shell/bat file called setenv and
within it put:

SET CATALINA_OPTS=-DSecurityServiceProps=./MinimalExampleSecuritySettings.props

If running as a self contained jar via our deb build process then just call 
the jar with :
java -jar THEJARNAME.jar --DSecurityServiceProps=./MinimalExampleSecuritySettings.props

6) URLS:

If you are running the security-web-example web app then this is a repeat of:
/security-web-example/home.jsp

All URLS start with /security-web/query so /reload
Will be /security-web/query/reload e.g. 
http://localhost:8080/security-web/query/reload
Text starting with ## is a variable you are meant to supply e.g.:
/security-web/query/users/##user mean that if looking up a user called bob the 
url would be:

/security-web/query/users/bob

Action:	          					Rest URL:

--GENERAL--
Reload Model from persistence:		/reload

--Lists of All--
A list of all Members:				/members
A list of all Users:				/users
A list of all Applications:			/apps

--Specific user--
Details for a specific User:		/users/##user
Applications for a specific User:	/users/##user/app
Roles within a specific application 
for a specific user:      			/users/##user/apps/##app
Roles within a specific application 
for a specific user with a specific
membership:							/users/##user/apps/##app/members/##member
Memberships for a specific user: 	/users/##user/members

--Specific Application--
All users for a specific app:		/apps/##app/users
All permissions for a specific app:	/apps/##app/perms
All permissions for a specific app
By Group:							/apps/##app/perms/##group

7) Authentication - tokens

Given the central place taken by the user authentication many applications will
be needing to pass users over to another application. For example the refset 
application might use the browser part run separately for viewing concept 
detail.

This requires that the password is not typed in repeatedly but that a token is 
passed instead.

Stormpath is in the midst of putting in OAuth token handling in part to allow 
for external authentication (e.g. if using a google account).

As a temporary measure the application will generate a UUID upon login which 
will be changed periodically e.g. after 12 hours of use.

As such when a user logs in the user details are returned as JSON which looks 
like:

{"user":{
"name":"Bob",
"status":"ENABLED",
"email":"bob@test.com",
"givenName":"Bob",
"middleName":"",
"surname":"Bobbin",
"token":"1054fb93-2de6-47f4-ba0a-18f95ef2c3b1"
}}

Note the last field entitled "token".

This is the UUID and can then be used in place of the password.
Using the UUID will be quicker as the app caches that whereas it goes off to stormpath to validate the username + password
The app using the service must be prepared for the uuid to change as it is set to have a 16 hour time to live.

In order to login you must POST the following information to: 
/security-web/query/

queryName=getUserByNameAuth
username=
password=

where username & the password are the username and password (or token) being 
used.

(8) Nginx - https set up

We use Nginx to provide a proxy https service so that tomcat does not need to be configured etc.

(8.1) Install Nginx:
Command:
sudo apt-get install nginx  

(8.2) Cert and SSL Key installation:
Place the certificate in /etc/ssl/certs
Place the ssl private key in /etc/ssl/private
Make sure the private key is owned by root:root, with a mode of 0600

(8.3) Configuring Nginx

Go to the nginx sites-available folder ( usually: /etc/nginx/sites-available)
open or create the default file

Make it look like:

server {
  server_name www.mydomain.com;
  listen 80;
  rewrite ^ https://$host$request_uri permanent;
}

server {
  server_name www.mydomain.com;
  listen 443 ssl;
  ssl on;

  ssl_certificate     /etc/ssl/certs/server.crt;
  ssl_certificate_key /etc/ssl/private/server.key;

  location / {
      proxy_pass http://localhost:8081;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Proto "https";
	  proxy_set_header X-Url-Scheme $scheme;
      proxy_redirect off;
  }
}

(8.4) Check the config

                         MAKE SURE:
						 
You change/Check 

(A) www.mydomain.com to the name of YOUR machine
(B) proxy_pass http://localhost:8081; to the port your web app is using
(C) ssl_certificate     /etc/ssl/certs/server.crt;  points to the directory and file(name) or the certificate file
(D) ssl_certificate_key /etc/ssl/private/server.key; points to the directory and file(name) or the certificate key file

(8.5) Reload nginx with the new config.

Command: 
service nginx reload