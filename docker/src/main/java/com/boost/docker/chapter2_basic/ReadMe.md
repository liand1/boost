基于Ubunutu
### 2.1 镜像加速
以下为aliyun官方文档
```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://15qq3jne.mirror.aliyuncs.com"] // 阿里云个人镜像加速
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
``` 

### 2.2 运行hello-world
运行这条命令
```
docker run hello-world
```
这时候docker会进行如下操作
+ Docker在本机中寻找该镜像 
    >本机中没有：去Docker Hub上查找镜像
          1 找到了：下载该镜像到本地,该镜像为模板生产容器实例运行 
          2 没有找到： 返回失败错误，查不到该镜像
                      
    >本机中有：该镜像为模板生产容器实例运行   
                                                                                                                                                 
                                                                                                                                                 
### 2.3 命令

#### 2.3.1 帮助命令
```
docker --help 
```
#### 2.3.2 镜像命令
+ 显示摘要信息
```
depu@ubuntu:~$ docker images --digests
REPOSITORY          TAG                 DIGEST                                                                    IMAGE ID            CREATED             SIZE
mongo               latest              sha256:d8539c76abc3202ba0e00b7b178cebf42a855aa4cc302b17957d75edbf635301   965553e202a4        4 months ago        363MB
rabbitmq            management          sha256:ff92870e678bbf18868a4da3da7a00048da04504cd34d8848d70bd2c5f64c9e9   8b98d04578a5        6 months ago        179MB
hello-world         latest              sha256:f9dfddf63636d84ef479d645ab5885156ae030f611a56f3a7ac7f2fdd86d7e4e   fce289e99eb9        15 months ago       1.84kB
```
+ 显示完整信息(完整的image ID)
```
depu@ubuntu:~$ docker images --no-trunc
REPOSITORY          TAG                 IMAGE ID                                                                  CREATED             SIZE
mongo               latest              sha256:965553e202a44592bc26d8c076eafef996a6dfc0de5bb2c1cf1795cd3b3a7373   4 months ago        363MB
rabbitmq            management          sha256:8b98d04578a543bbe15ce23cd3c651d38faeb8219eeb7bb2be62423f5a159d21   6 months ago        179MB
hello-world         latest              sha256:fce289e99eb9bca977dae136fbe2a82b6b7d4c372474c9235adc1741675f587e   15 months ago       1.84kB

depu@ubuntu:~$ docker images(对比一下区别IMAGE ID)
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mongo               latest              965553e202a4        4 months ago        363MB
rabbitmq            management          8b98d04578a5        6 months ago        179MB
hello-world         latest              fce289e99eb9        15 months ago       1.84kB
```

+ 查找镜像 
```
docker search tomcat //在仓库中查找tomcat镜像 
``` 
```
docker search -s 30 tomcat 在仓库中查找点赞数超过30的tomcat镜像
```

 
#### 2.3.3 容器命令
+ 列出当前运行的所有容器
```
docker ps 现在所有运行中的容器
docker ps -l 上一此运行的容器
docker ps -lq 上一此运行的容器编号
docker ps -n 5 前面运行过的5个容器

```
+ 运行一个镜像，命名为mycentOS,并且打开一个可交互的伪终端
```
docker run -it --name mycentOS 470671670cac
depu@ubuntu:~$ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
56fb7d629bea        470671670cac        "/bin/bash"         11 seconds ago      Up 9 seconds                            mycentOS
```
+ exit 退回到宿主机，同时关闭容器
+ docker rm -f $(docker ps -a -q) 删除所有容器 -f强制删除，可以删除正在运行的
+ docker run -d centos 后台运行一个容器，但是这个容器启动后会紧接着关闭，用docker ps查看不到正在运行的该容器
  很重要的一点：`Docker容器后台运行，就必须有一个前台进程例如(-it)，容器运行命令如果不是那些一直挂起的命令（top,tail），就是会自动
  退出的`。-itd不会自动退出
+ docker top d0ba5dffac8d 查看指定容器中的进程
+ docker inspect  d0ba5dffac8d 查看容器内部细节
+ docker attach d0ba5dffac8d 退出容器伪终端后，重新建立伪终端连接 `attach 直接进入容器启动命令的终端，不会启动新的进程`
+ docker exec -t d0ba5dffac8d ls 进入到容器，然后执行ls命令  `是在容器中打开新的终端，并且可以启动新的进程`
+ docker cp d0ba5dffac8d:/usr/tmp.log /root把容器中的tmp.log文件拷贝到宿主机的root目录下面, 通常用来进行数据拷贝
+ docker run -it -p 8888:8080 tomcat 将tomcat的端口8080映射一个对外暴露的端口8888 
                                                                                                                                                   
                                                                                                                                                             