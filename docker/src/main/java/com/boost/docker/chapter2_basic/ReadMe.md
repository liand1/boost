##本章内容
镜像加速  
镜像命令  
容器命令  
构建镜像  
docker的网络连接  

### 2.1 镜像加速
以下为aliyun官方文档
```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://15qq3jne.mirror.aliyuncs.com"] // 阿里云个人镜像加速,需要改
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
docker images [optsions] [repository]
-a,--all 默认=false 默认是显示所有镜像
-f,filter 默认=[] 筛选
--no-trunc 默认=false 不使用截断的形式显示数据
-q,--quiet 默认=false 只显示镜像id
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
启动容器
docker run IMAGE [COMMAND] [ARG...]
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
+ docker run -it -p 8888:8080 tomcat 将tomcat的端口8080映射一个对外暴露的端口8888， -i(interactive) -t(tty) 
+ docker start d0ba5dffac8d 重新启动停止的容器
+ docker restart d0ba5dffac8d 重新启动容器
+ docker stop d0ba5dffac8d 停止守护式容器，stop命令会向docker容器进程发送SIGTERM信号
+ docker kill d0ba5dffac8d 快速停止某个容器，想容器进程发送SIGKILL信号
+ docker run --restart=always --name mytomcat -d tomcat 自动重启容器--restart标志，默认不会重启，`--restart=on-failure:5`代表当容器退出代码为非0  
  的时候，才会自动重启，最多重启5次。   
                                                                                                                                                   
 ### 2.4 构建镜像
 1 docker commit 通过容器构建  
 ```
docker commit -a 'depu' -m 'nginx' commit_test /depu/commit_test用现有的commit_test容器构建一个镜像，-a(author),-m(commit msg)
```
 2 通过dockerfile构建   
 详细请看第四章        
 
### 2.4 docker容器的网络连接
  当docker用某个镜像创建了一个新容器，该容器拥有自己的网络，ip地址，以及一个用来和宿主机进行通信的桥接网络接口。
+ docker容器的网络基础 (不理解，待完善) 
  1首先先运行```ifconfig```查看系统的网络设备，可以看到系统中存在一个docker0的网络设备,docker的守护进程就是通过docker0
  为docker容器提供网络连接的，docker0是linux的虚拟网桥，它的特点是可以设置ip地址，相当于拥有一个隐藏的虚拟网卡
  ```
  docker0   Link encap:Ethernet  HWaddr 02:42:45:45:9a:2c (mac地址) 
            inet addr:172.17.0.1(ip)  Bcast:172.17.255.255  Mask:255.255.0.0(子网掩码)
            UP BROADCAST MULTICAST  MTU:1500  Metric:1
            RX packets:0 errors:0 dropped:0 overruns:0 frame:0
            TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
            collisions:0 txqueuelen:0 
            RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)

  ```
  2查看网桥
  ```
  depu@ubuntu:~$ sudo brctl show
  bridge name	bridge id		STP enabled	interfaces
  docker0		8000.024245459a2c	no	
  ```
  3运行一个centos，进去查看网络设备，发现docker自动创建了一个eth0的网卡,ip被分配成了 172.17.0.2
  ```
  [root@e9030d2c7f3f /]# ifconfig
  eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
          inet 172.17.0.2  netmask 255.255.0.0  broadcast 172.17.255.255
          ether 02:42:ac:11:00:02  txqueuelen 0  (Ethernet)
          RX packets 4886  bytes 12853230 (12.2 MiB)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 3078  bytes 170670 (166.6 KiB)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  
  lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
          inet 127.0.0.1  netmask 255.0.0.0
          loop  txqueuelen 1000  (Local Loopback)
          RX packets 0  bytes 0 (0.0 B)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 0  bytes 0 (0.0 B)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  

  ```
  4 我们再查看网桥的状态
+ docker容器的互联  
    1 允许所有容器互联
    容器间是可以互联的，但是问题的容器每次启动的时候分配的IP都可能不一样,docker提供了另外一种方式就是--link，它是在容器启动时以指定的代号
        来访问到响应的容器(DOCKER官方文档并不推荐使用link，新建自定义网络，且用hostname来进行互联才是推荐的做法。)
    ```
    docker run --link=[CONTAINER_NAME]:[ALIAS] [IMAGE][COMMAND] #  官方不推荐使用
    ```    
    2 拒绝容器间互联, 修改守护进程的启动选项
    ```DOCKER_OPS="--ICC=false"```
    3 允许特定容器间的连接
    ```--iptables=true #它的意思是允许将docker容器将配置的添加linux的iptables设置中， iptables是linux中控制网络访问的重要组件```

+ docker容器的外部网络的连接                                                                                                                                                 