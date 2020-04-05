### 5.1 
+ 前面我们使用 Docker 的时候，定义 Dockerfile 文件，然后使用 docker build、docker run 等命令操作容器。然而微服务架构的应用系
  统一般包含若干个微服务，每个微服务一般都会部署多个实例，如果每个微服务都要手动启停，那么效率之低，维护量之大可想而知  
+ 使用 Docker Compose 可以轻松、高效的管理容器，它是一个用于定义和运行多容器 Docker 的应用程序工具

#### 5.1.1 Docker compose
是一个编排多容器分布式部署的工具，提供命令集管理容器化应用的完整开发周期，包括服务构建，启动和停止。使用步骤
1 利用Dockerfile定义运行环境镜像
2 使用docker-compose.yml定义组成应用的各服务
3 运行docker-compose up启动应用

#### 5.1.2 docker-compose.yml常用指令
+ image 指定镜像名称和id
+ build 指定Dockerfile文件的路径，有两种方式
    1 可以是一个路径 
    ```
        build: ./dir
    ```
    2 也可以是一个对象，用以指定Dockerfile和参数
    ```
    build:
        context: ./dir
        dockerfile: Dockerfile-alternate
        args:
            buildno: 1
    ```
    
.......
[具体请参考官方文档,click this](https://docs.docker.com/compose/compose-file/)

有一个需要强调一下, 我们先创建一个docker-compose.yml, 
```
version: '3'
services:
  tomcat:  #docker服务名，该服务在容器中的hostname
    image: tomcat
    ports:
      - "8080:8080"
```
然后通过docker-compose up启动, 然后进入docker伪终端， 直接ping服务名发现是可以ping通的
```
depu@ubuntu:~$ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS                    NAMES
2eb00329f0e9        tomcat              "catalina.sh run"   44 seconds ago      Up 41 seconds       0.0.0.0:8080->8080/tcp   dev_tomcat_1
depu@ubuntu:~$ docker container exec -it 2eb00329f0e9 /bin/bash
root@2eb00329f0e9:/usr/local/tomcat# ping tomcat #ping 服务名, 就等同于ping hostname
PING tomcat (172.18.0.2) 56(84) bytes of data.
64 bytes from 2eb00329f0e9 (172.18.0.2): icmp_seq=1 ttl=64 time=0.054 ms
64 bytes from 2eb00329f0e9 (172.18.0.2): icmp_seq=2 ttl=64 time=0.049 ms
64 bytes from 2eb00329f0e9 (172.18.0.2): icmp_seq=3 ttl=64 time=0.050 ms
64 bytes from 2eb00329f0e9 (172.18.0.2): icmp_seq=4 ttl=64 time=0.051 ms
64 bytes from 2eb00329f0e9 (172.18.0.2): icmp_seq=5 ttl=64 time=0.051 ms
64 bytes from 2eb00329f0e9 (172.18.0.2): icmp_seq=6 ttl=64 time=0.051 ms
^C
--- tomcat ping statistics ---
6 packets transmitted, 6 received, 0% packet loss, time 123ms
rtt min/avg/max/mdev = 0.049/0.051/0.054/0.001 ms
root@2eb00329f0e9:/usr/local/tomcat# 

```
