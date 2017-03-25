# CoyoteWeb
Small, embeddable web server using the Loader toolkit.

# Goals
This is an example of implementing a lightweight web application using the Coyote 
Loader toolkit. It is intended to be a use case project to further explore the 
features, capabilities and gaps in the Coyote Loader project. In going through a few 
practical use cases, it is expected that the features of the Coyote Loader project 
can be tested, refined and updated to provide the intended capabilities.

This project also serves to illustrate the intended manner of using the Loader 
project to achieve certain goals. For example, the Authentication, Authorization and 
Auditing capabilities are used in this project and can be modeled by other projects 
to achieve the same level of security in them   

# Quickstart
All that is required to spin up a web server is:
	java -jar CoyoteWeb-X.X.jar config.json

The `config.json` is a JSON formatted file which contains the configuration for 
the server. Examples can be found in the test directories.

Although the server can be called programmatically, it may be better to use the HTTP
server in the Coyote Loader project. 
