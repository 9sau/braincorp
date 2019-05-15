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
	This will start a server on localhost:8080 (assumed that port is 8080, however it can be configured)

RUNNING THE TESTS

	1. Open a command line window or a terminal
	2. Navigate to the project directory
	3. Type mvn test

APIs
	
	You can directly hit the below urls using a browser or a rest client like postman.
	
	1. GET localhost:8080/users
	2. GET localhost:8080/users/0
	3. GET localhost:8080/users%2Fquery%3Fname%3Droot%26uid%3D-2%26gid%3D0%26comment%3Dcaptiveagent%26home%3D%2Fvar%2Froot%26shell%3D%2Fbin%2Fsh
	4. GET localhost:8080/users/query?shell=%2Fbin%2Ffalse
	5. GET localhost:8080/users/0/groups
	6. GET localhost:8080/users/123/groups
	7. GET localhost:8080/groups
	8. GET localhost:8080/groups/0
	9. GET localhost:8080/groups/query?name=mail&gid=265
	10. GET localhost:8080/groups/query?member=_analyticsd&member=_networkd


