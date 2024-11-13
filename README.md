# billServer


Frame-相关组件：
-
* maven
* spring-boot
* swagger
* mysql
* spring-data-jpa

build - 打包
-
使用以下命令进行打包，打包后的文件在target目录下，文件名为billServer-0.0.1-SNAPSHOT.jar
```
mvn package spring-boot:repackage
```

run - 启动
-
将jar包上传到指定服务器上，采用 java -jar *.jar 的方式  
linux下可以通过nohup或者supervisor(推荐）进行启动
启动命令中建议加入spring.profiles.active参数，指定使用生产环境的配置，该配置可以application-prod.properties中指定
使用prod模式时，日志文件会自动输出到当前目录的logs文件中，可通过```tail -100f logs/billServer.log```进行查看
```
nohup java -jar -Dspring.profiles.active=prod billServer-0.0.1-SNAPSHOT.jar > console.file 2>&1 &
```


