### SonarQube
开源的源码质量管理平台，目的是对项目进行持续的分析和测量技术质量，可用于快速地定位代码中潜在的或明显的错误。并且可以通过各种开源的插件进行
功能的扩展。


sudo chmod -R 777 /home/sonarqube

1 先下载，然后启动sonarqube
```
docker pull sonarqube:sonarqube:7.4-community

docker run -d -p 8200:9000 -p 8292:9092 --name sonarqube  \
-e SONARQUBE_JDBC_USERNAME=sonar \
-e SONARQUBE_JDBC_PASSWORD=sonar \
-e SONARQUBE_JDBC_URL="jdbc:mysql://192.168.31.204:3306/sonar?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance&useSSL=false " \
-v /home/sonarqube/conf:/opt/sonarqube/conf \
-v /home/sonarqube/data:/opt/sonarqube/data \
-v /home/sonarqube/logs:/opt/sonarqube/logs \
-v /home/sonarqube/extensions:/opt/sonarqube/extensions \
sonarqube:7.4-community
```

2 修改mven配置 setting.xml
```
<pluginGroup>org.sonarsource.scanner.maven</pluginGroup>

<profile>
      <id>sonar</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
          <sonar.jdbc.url>jdbc:mysql://localhost:3306/sonar?useUnicode=true&amp;characterEncoding=utf8</sonar.jdbc.url>
           <sonar.jdbc.driverClassName>com.mysql.jdbc.Driver</sonar.jdbc.driverClassName>
           <sonar.jdbc.username>sonar</sonar.jdbc.username>
           <sonar.jdbc.password>sonar</sonar.jdbc.password>
           <sonar.host.url>http://localhost:8200</sonar.host.url>
      </properties>
  </profile>

<activeProfiles>
    <activeProfile>sonar</activeProfile>
  </activeProfiles>
```

admin/admin

3 执行sonar命令
```
mvn sonar:sonar 
```


把下载好的插件放到/sonarqube/extensions/plugins中去，执行
```docker restart [containId]```
>插件需要注意版本 sonarqube:7.4 sonar-java-plugin:5.8

