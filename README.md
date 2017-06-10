# CoyoteWeb
Small, embeddable web server using the Loader toolkit.

# Goals
This is an example of implementing a lightweight web application using the [Coyote 
Loader](https://github.com/sdcote/loader) toolkit. It is intended to be a use case 
project to further explore the features, capabilities and gaps in the Coyote Loader 
project. In going through a few practical use cases, it is expected that the 
features of the Coyote Loader project can be tested, refined and updated to provide 
the intended capabilities.

The primary use case is standing up a HTTP server in Heroku to serve static content 
with some dynamic processing. This is a common front-end implementation for a 12-
factor application, something the Loader project is designed to enable.

A secondary use case is to run a light-weight HTTP server locally to aid in the 
development of HTML, JavaScript, and CSS. The server can be actively serving content 
from the project directory while files are being edited. You don't need an Apache 
HTTP server or other servlet container like Jetty.

This project also serves to illustrate the intended manner of using the Loader 
project to achieve certain goals. For example, the Authentication, Authorization and 
Auditing capabilities are used in this project and can be modeled by other projects 
to achieve the same level of security in them.

# Contents
The majority of the functionality of this JAR is derived from the WebServer class in 
the Coyote Loader project. WebServer is a specialized loader which extends the 
`AbstractLoader` and includes the `HTTPDResponder`.  As `Loader`, the 
`coyote.loader.BootStrap` class is called from the command line, reads the 
configuration file, loads the `WebServer` class and passes the configuration to it.
From there the `BoostStrap` class calls `start()` on the `WebServer`. 

Just like any `Loader`, the web server is controlled by a JSON configuration file. 
This file controls what components are loaded. Further, it support the mapping of 
request paths to `Responder` components. This means you can write any class you 
want to service the HTTP request for that URL path. Several are included to handle 
basic requests and to show-off some of the features of the Loader toolkit.[

# Quickstart
All that is required to spin up a web server is:

    java -jar CoyoteWeb-X.X.jar config.json

The `config.json` is a JSON formatted file which contains the configuration for 
the server. Examples can be found in the test directories.

Although the server can be called programmatically, it may be better to use the HTTPD
server in the Coyote Loader project as WebServer is designed to be a Loader which 
contains the HTTPD server and may add more overhead than you might want.  
