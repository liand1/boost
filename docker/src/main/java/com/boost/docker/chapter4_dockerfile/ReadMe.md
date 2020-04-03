###4.1 熟悉dockerfile

####4.1.1 基础知识
1 每条保留字指令都必须为大写字母且后面要跟随至少一个参数  
2 指令按照从上到小，顺序执行  
3 #表示注释  
4 `每条指令都会创建一个新的镜像层，并对镜像进行提交`

####4.1.2 docker执行dockerfile的大致流程
1 docker从基础镜像运行一个容器  
2 执行一条指令并对容器做出修改  
3 执行类似docker commit的操作提交一个新的镜像层  
4 docker再基于刚提交的镜像运行一个新容器  
5 执行dockerfile中的下一条指令直到所有指令执行完成  


####4.1.3 保留字指令
+ FROM 基础镜像，当前新镜像是基于哪个镜像的  
+ MAINTAINER 镜像维护者的姓名和邮箱地址  
+ RUN 容器构建时需要运行的命令  
+ EXPOSE 当前容器对外暴露的端口
+ WORKDIR 指定在创建容器后，终端默认登陆的进来工作目录，一个落脚点，不加的话默认时根目录
+ ENV 用来在构建镜像过程中设置环境变量
+ ADD 与COPY类似，拷贝，但是还会解压缩  比如 ADD centos-7-docker-tar.xz /
+ COPY 拷贝文件和目录到镜像中，将从构建上下文目录中<源路径>的文件/目录复制到新的一层的镜像内的<目标路径>位置  
+ VOLUME 容器数据卷，用于数据保存和持久化工作
+ CMD 指定一个容器启动时要运行的命令，dockerfile中可以有多个CMD指令，但只有最后一个生效，`CMD会被docker run之后的参数替换(详见案例2)`  
+ ENTRYPOINT 指定一个容器启动时要运行的命令，ENTRYPOINT的目的和CMD一样，都是在指定容器启动程序及参数
+ ONBUILD 当构建一个被继承的Dockerfile时运行命令，父镜像在被子继承后父镜像的onbuild被触发

下面是hub docker中centos6.8的dockerfile
```
FROM scratch # scratch是所有镜像的父类，类似于java中的Object
MAINTAINER The CentOS Project <cloud-ops@centos.org>
ADD c68-docker.tar.xz /
LABEL name="CentOS Base Image" \ #label等于是说明
    vendor="CentOS" \
    license="GPLv2" \
    build-date="2016-06-02"

# Default command
CMD ["/bin/bash"]
```

####4.1.4 案例
+ 案例1 case1 docker中初始的centos默认路径是/，默认不支持vim，不支持ifconfig，我们现在修改一下('#'和'#'不要粘贴)  
step1
```
FROM centos
MAINTAINER depu<depu@qq.com>

ENV MYPATH /usr/local
WORKDIR $MYPATH #修改默认路径

RUN yum -y install vim #安装vim -y代表自动，不需要手动选择y
RUN yum -y install net-tools #支持ifconfig

EXPOSE 80
CMD echo $MYPATH
CMD echo "------------success--------------"
CMD /bin/bash
```
step2 然后开始执行
```
#构建 小数点.其实就是将当前目录设置为上下文路径, 比如要打包的文件都在这个路径中
depu@ubuntu:/mydocker$ docker build -f /mydocker/dockerfile2 -t mycentos:1.3 . 
Sending build context to Docker daemon  3.072kB #以下都是构建日志
Step 1/10 : FROM centos
 ---> 470671670cac
Step 2/10 : MAINTAINER depu<depu@qq.com>
 ---> Running in 9ae8e9221c44
Removing intermediate container 9ae8e9221c44
 ---> c1ffba563f32
Step 3/10 : ENV MYPATH /usr/local
 ---> Running in 4368f811f093
Removing intermediate container 4368f811f093
 ---> 0d370e9f9028
Step 4/10 : WORKDIR $MYPATH #修改默认路径
 ---> Running in edc3819492a6
Removing intermediate container edc3819492a6
 ---> 904f966661ab
Step 5/10 : RUN yum -y install vim #安装vim -y代表自动，不需要手动选择y
 ---> Running in 2656c2707597
CentOS-8 - AppStream                            335 kB/s | 6.6 MB     00:20    
CentOS-8 - Base                                 1.3 MB/s | 5.0 MB     00:03    
CentOS-8 - Extras                               5.5 kB/s | 4.4 kB     00:00    
Dependencies resolved.
.............中间省去一部分日志.............
Step 9/10 : CMD echo "------------success--------------"
 ---> Running in 9167ef13e5de
Removing intermediate container 9167ef13e5de
 ---> e5c586008b88
Step 10/10 : CMD /bin/bash
 ---> Running in 3596163ef9a3
Removing intermediate container 3596163ef9a3
 ---> 73c9bf8b79f7
Successfully built 73c9bf8b79f7
Successfully tagged mycentos:1.3
```
step3 构建完成之后我们可以发现docker images中多了mycentos:1.3这个镜像，进去后面我们也发现工作目录改了，也支持了vim和config
`我们可以通过下面的指令查看构建操作日志`
```
depu@ubuntu:/mydocker$ docker images mycentos
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mycentos            1.3                 73c9bf8b79f7        9 minutes ago       327MB
depu@ubuntu:/mydocker$ docker history 73c9bf8b79f7
IMAGE               CREATED             CREATED BY                                      SIZE                COMMENT
73c9bf8b79f7        10 minutes ago      /bin/sh -c #(nop)  CMD ["/bin/sh" "-c" "/bin…   0B                  
e5c586008b88        10 minutes ago      /bin/sh -c #(nop)  CMD ["/bin/sh" "-c" "echo…   0B                  
e6f19249df08        10 minutes ago      /bin/sh -c #(nop)  CMD ["/bin/sh" "-c" "echo…   0B                  
6b8dc38c6021        10 minutes ago      /bin/sh -c #(nop)  EXPOSE 80                    0B                  
b3cf27dab52c        10 minutes ago      /bin/sh -c yum -y install net-tools #支持ifc…     26.6MB              
9ddd9a9930b0        10 minutes ago      /bin/sh -c yum -y install vim #安装vim -y代…       63.5MB              
904f966661ab        11 minutes ago      /bin/sh -c #(nop) WORKDIR /usr/local #修改默…      0B                  
0d370e9f9028        11 minutes ago      /bin/sh -c #(nop)  ENV MYPATH=/usr/local        0B                  
c1ffba563f32        11 minutes ago      /bin/sh -c #(nop)  MAINTAINER depu<depu@qq.c…   0B                  
470671670cac        2 months ago        /bin/sh -c #(nop)  CMD ["/bin/bash"]            0B                  
<missing>           2 months ago        /bin/sh -c #(nop)  LABEL org.label-schema.sc…   0B                  
<missing>           2 months ago        /bin/sh -c #(nop) ADD file:aa54047c80ba30064…   237MB    
```

+ 案例2 这种情况下容器不会被创建, 原因是CMD会被docker run之后的参数替换(这段指令是'ls -l')
```
depu@ubuntu:/mydocker$ docker run -it -p 8080:8080 tomcat ls -l
total 156
-rw-r--r-- 1 root root 19318 Mar 11 10:06 BUILDING.txt
-rw-r--r-- 1 root root  5408 Mar 11 10:06 CONTRIBUTING.md
-rw-r--r-- 1 root root 57011 Mar 11 10:06 LICENSE
-rw-r--r-- 1 root root  1726 Mar 11 10:06 NOTICE
-rw-r--r-- 1 root root  3255 Mar 11 10:06 README.md
-rw-r--r-- 1 root root  7136 Mar 11 10:06 RELEASE-NOTES
-rw-r--r-- 1 root root 16262 Mar 11 10:06 RUNNING.txt
drwxr-xr-x 2 root root  4096 Mar 17 23:14 bin
drwxr-xr-x 2 root root  4096 Mar 11 10:06 conf
drwxr-xr-x 2 root root  4096 Mar 17 23:14 include
drwxr-xr-x 2 root root  4096 Mar 17 23:13 lib
drwxrwxrwx 2 root root  4096 Mar 11 10:03 logs
drwxr-xr-x 3 root root  4096 Mar 17 23:14 native-jni-lib
drwxrwxrwx 2 root root  4096 Mar 17 23:13 temp
drwxr-xr-x 2 root root  4096 Mar 17 23:13 webapps
drwxr-xr-x 7 root root  4096 Mar 11 10:04 webapps.dist
drwxrwxrwx 2 root root  4096 Mar 11 10:03 work
depu@ubuntu:/mydocker$ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
depu@ubuntu:/mydocker$ 
```

+ 案例3 基于案例2的优化，不会被docker run后面的命令覆盖
step1 创建dockerfile3，内容如下
```
FROM centos
RUN yum install -y curl
ENTRYPOINT ["curl", "-s", "http://www.baidu.com"]
```
step2 执行命令
```
depu@ubuntu:/mydocker$ docker build -f /mydocker/dockerfile3 -t myip .
Sending build context to Docker daemon  4.096kB
Step 1/3 : FROM centos
 ---> 470671670cac
Step 2/3 : RUN yum install -y curl
 ---> Running in 774bd546a10a
CentOS-8 - AppStream                            351 kB/s | 6.6 MB     00:19    
CentOS-8 - Base                                 1.1 MB/s | 5.0 MB     00:04    
CentOS-8 - Extras                               6.4 kB/s | 4.4 kB     00:00    
Package curl-7.61.1-11.el8.x86_64 is already installed.
Dependencies resolved.
Nothing to do.
Complete!
Removing intermediate container 774bd546a10a
 ---> 4b8bce80e178
Step 3/3 : ENTRYPOINT ["curl", "-s", "http://www.baidu.com"]
 ---> Running in 58bb86af050d
Removing intermediate container 58bb86af050d
 ---> 02116e9bd705
Successfully built 02116e9bd705
Successfully tagged myip:latest
```
step3 
```
docker run -it myip -i # -i不会覆盖/bin/bash而是追加，因为在dockerfile里面使用了entrypoint， -i是打印报文头
```

+ 案例4 基于centos自己构建一个tomcat9的镜像
dockerfile内容
```
FROM centos
MAINTAINER depu<qq@126.com>
#把宿主机当前上下文的c.txt拷贝到容器的/usr/local路径下
COPY c.txt /usr/local/container_c.txt
#把java和tomcat添加到容器中 ADD是先解压缩后拷贝
ADD jdk-8u171-linux-x64.tar.gz /user/local/
ADD apache-tomcat-9.0.8.tar.gz /user/local/
#安装vim
RUN yum -y install vim
#设置工作访问时候的WORKDIR路径，登陆的落脚点
ENV MYPATH /usr/local
WORKDIR $MYPATH
#配置环境变量
ENV JAVA_HOME /usr/local/jdk1.8.0_171
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV CATALINA_HOME /usr/local/apache-tomcat-9.0.8
ENV CATALINA_BASE /usr/local/apache-tomcat-9.0.8
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:CATALINA_HOME/bin
#容器运行时监听的端口
EXPOSE 8080
#启动时运行tomcat
# ENTRYPOINT ["/usr/local/apache-tomcat-9.0.8/bin/startup.sh"]
# CMD ["/usr/local/apache-tomcat-9.0.8/bin/catalina.sh", "run"]
CMD /usr/local/apache-tomcat-9.0.8/bin/startup.sh && tail -F /usr/local/apache-tomcat-9.0.8/bin/logs/catalina.out
```
docker run命令
```
docker run -d p 8888:8080 --name mytomcat9 \
-v /depu/mydockerfile/tomcat9/test:/usr/local/apache-tomcat-9.0.8/webapps/test \
-v /depu/mydockerfile/tomcat9/tomcat9logs:/usr/local/apache-tomcat-9.0.8/webapps/logs \
# 获取宿主机root权限（特殊权限-）
--privileged=true \
customtomcat9

```
