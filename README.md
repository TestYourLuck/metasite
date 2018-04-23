# metasite

![Project Diagram](/project/projectDiagram.png?raw=true "Project Diagram")

## Project info

* Rest API with 3 endpoints:
* * /metasite/echo - outputs "Hello!" for checking if application has started
* * /metasite/countWords - calculates word counts in uploaded files and stores results in A-G.txt, H-N.txt, O-U.txt, V-Z.txt under configured directory
* * /metasite/results - outputs results list in form of "_word_: _count_"
* Application should be accessible on localhost (or docker ip as domain name), 8080 port
* Spring Boot for application startup
* Dockerfile configured backend module
* * Builds image and launches backend-app-fat.jar
* * Default results directory configured for docker: -DoutputDir="/usr/lib/metasite/results"

## Future plans

* Create UI application with react and typescript
* Configure h2 database and refactor code to store old results in DB
* Optionally connect with kafka events instead of DB
