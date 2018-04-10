# a2si-capacity-service-client
Capacity Service Client - simple Jar project that can be shared between projects.

Contains the capacity Service client that can initiate http calls to the Capacity Service Rest API.
The module uses profiles to define whether the client is a real rest client, i.e. actually making rest calls
over HTTP, or is a stub client that returns dummy data, or data stored via the stub client. The stub client
uses an in-memory Java collection to store the data.

A simple jar is built using Maven.

The code is not designed to run as a standalone piece of code and is expected to be used in other projects 
that are runtime applications. For this reason, these is no configuration for different profiles in this
module, it is the modules that use it that will provide this information. 
N.B. Because of this, the number of profiles for the rest and stub clients is large.

## Getting started
First, download the code from GitHub. This can be done using the desktop git tool, an IDE which supports git or by downloading the code as a zip file which you can then extract.

Next, install the dev tools and dependencies....

##Installation of Development Tools and Dependencies
Install Git for Windows:
Install official git release: https://git-scm.com/download/win

Or install GitHub Desktop which also includes a GUI interface for git management: https://desktop.github.com/

###Install Java Development Kit 8:
http://www.oracle.com/technetwork/java/javase/downloads/

###Install Maven 3:
https://maven.apache.org/download.cgi

###Environment Variables
Ensure that the system environment variables for Java and Maven are set correctly, as described below...

M2_HOME should point to the install directory of your local Maven install folder, e.g.
M2_HOME C:\Maven\apache-maven-3.3.9

JAVA_HOME should point to the install directory of your local Java JDK install folder, e.g.

JAVA_HOME C:\Program Files\Java\jdk1.8.0_121

PATH should contain the bin directory of both M2_HOME and JAVA_HOME, e.g.

```
...;%JAVA_HOME%\bin;%M2_HOME%\bin;...
```

## Dependencies
Before building this module, the following modules must have been downloaded and built, using "mvn install"
to add them into your local Maven Repository.

1) a2si-capacity-information

## Use maven to build the project:

cd {projectRoot}
mvn clean install

the Maven "install" goal stores the built artifact into the local Maven repository, 
making it accessible to other projects using this repository.

Note that the Rest Client expects environment variables to define the Capacity Service API's username
and password. The environment variables to be used and populated with the correct values are:

- CAPACITY_SERVICE_CLIENT_API_USERNAME &
- CAPACITY_SERVICE_CLIENT_API_USERNAME 

when used as environment variables in AWS Elastic Beanstalk

or
- capacity.service.client.api.username &
- capacity.service.client.api.password 

when used as environment variables running the application from a terminal.


Note that it is the applications that use the capacity service client that should set the 
environment variables, the client itself is not a runnable artifact.


