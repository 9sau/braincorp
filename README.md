Passwd as a Service

LANGUAGE AND FRAMEWORKS USED - Java, Java Spring Boot, Junit

CONFIGURING FILE PATHS

1. To configure the path for passwd file locate "application.properties" under braincorp/src/main/resources and set the following without quotes

	app.passwd.directory = "path to directory"
	
	app.passwd.filename = "filename"

	Default directory is /private/etc
	
	Default filename is passwd

2. To configure the path for group file locate application.properties under braincorp/src/main/resources and set the following without quotes

	app.group.directory = "path to directory"
	
	app.group.filename = "filename"

	Default directory is /private/etc
	
	Default filename is group


RUNNING THE APPLICATION

	1. Open a command line window or a terminal
	2. Navigate to the project directory
	3. Type mvn spring-boot:run

RUNNING THE TESTS

	1. Open a command line window or a terminal
	2. Navigate to the project directory
	3. Type mvn test

	


