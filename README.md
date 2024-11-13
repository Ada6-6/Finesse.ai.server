# Finesse.ai.server


Framework-related Components:
-
* maven
* spring-boot
* swagger
* mysql
* spring-data-jpa

Build - Packaging
-
To package the project, use the following command. The packaged file will be located in the target directory under the name```billServer-0.0.1-SNAPSHOT.jar```
```
mvn package spring-boot:repackage
```


Run - Starting the Server
-
Upload the JAR file to the specified server and start it with the ```java -jar *.jar``` command.
On Linux, you can use nohup or supervisor (recommended) for starting the application.

It's recommended to include the ```spring.profiles.active``` parameter in the startup command to specify production environment configurations, which can be set in the ```application-prod.properties``` file. 
When running in prod mode, log files will automatically be saved in the logs directory of the current path. You can ```tail -100f logs/billServer.log``` with the following command:

```
nohup java -jar -Dspring.profiles.active=prod billServer-0.0.1-SNAPSHOT.jar > console.file 2>&1 &
```


