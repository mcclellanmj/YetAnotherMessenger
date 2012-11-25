YetAnotherMessenger
===================
This is an instant messenger project that will initially utilize JMS for message delivery

About Maven Configuration
-------------------------
Project is using a multi-parent setup
The bottom level which is empty is intended to be standard cod
The upper levels are end projects

As of now the base pom.xml exists and a WebApps/pom.xml which inherits from it but is itself also a parent.  WebApps/Rest-Server/pom.xml is the rest server WebApps/Web-Ui/pom.xml is the wicket frontend

Running Embedded Maven Server
-----------------------------
Stub templates have been created to allow you to run glassfish embedded for an Apache Wicket project and a Jersey (JAX-RS) project

To run these use the provided 'clean-start-ui' and 'clean-start-rest'

Building Eclipse Config
-----------------------
- Run 'mvn eclipse:eclipse' to build Eclipse configuration
  - After this you should be able to import as a standard Eclipse project
