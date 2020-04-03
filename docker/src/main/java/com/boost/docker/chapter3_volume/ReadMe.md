### 3.1 数据管理
在容器中管理数据主要有两种方式： 
+ 数据卷(Volumes) 
+ 挂载主机目录(Bind mounts) 

#### 3.1.1 数据卷
数据卷是一个可供一个或多个容器使用的特殊目录，它绕过UFS,可以提供很多有用的特性：
1. 数据卷可以在容器之间共享和重用  
2. 对数据卷的修改会立马生效
3. 对数据卷的更新，不会影响镜像
4. 数据卷默认会一直存在，即使容器被删除
> 注意： 数据卷的使用，类似于Linux下对目录或文件进行mount，镜像中的被指定为挂载点的目录中的文件会隐藏掉，能显示看的是挂载的数据卷。


#### 3.1.2 添加数据卷方式

#####3.1.2.1 直接命令添加
```
docker run -it -v /宿主机绝对路径:/容器内目录  镜像名 // -v为volume的缩写， 如果没有文件夹则会新建, 读写权限
docker run -it -v /myDataVolume:/dateVolumeContainer:ro centos //ro为read only 宿主机可以进行写操作，但是容器内不能进行写操作
```
+ mysql
```
docker run -it -p 8888:3306 --name mysql
-v /depu/mysql/conf:/etc/mysql/conf.d
-v /depu/mysql/logs:/logs
-v /depu/mysql/data:/var/lib/mysql
-e MYSQL_ROOT_PASSWORD=root
-d mysql:5.6
```
+ redis
```
docker run -it -p 6379:6379 --name redis
-v /depu/myredis/data:/data
-v /depu/myredis/conf/redis.conf:/usr/local/etc/redis/redis.conf
-d redis:3.2 redis-server /usr/local/etc/redis/redis.conf
--appendonly yes
```
#####3.1.2.3 DockerFile添加
1 depu@ubuntu:~$ cd /  
2 depu@ubuntu:/$ sudo mkdir /mydocker  
3 depu@ubuntu:/$ cd mydocker/  
4 depu@ubuntu:/mydocker$ sudo vim dockerfile  
```
FROM centos
VOLUME ["/dataVolumeContainer1","/dataVolumeContainer2"]
CMD echo "finished,-----------success!"
CMD /bin/bash  
//上面这4行可以看作 dokcer run -it -v /host1:/dataVolumeContainer1 -v /host2:/dataVolumeContainer2 centos /bin/bash
//但是出于可移植和分享的考虑 用-v 主机目录:容器目录 这种方式不能直接在DockerFile上面实现。由于宿主机目录是依赖于特定宿主机的
//并不能保证在所有的宿主机上都存在这样的特定目录 
//因为没有分配宿主机目录，dockers会给你自动分配一个目录，在第6步后可以运行 docker inspect depu/centos查看信息
```
5 docker build -f /mydocker/dockerfile -t depu/centos . //build构建一个容器模板  
```
Sending build context to Docker daemon  2.048kB
Step 1/4 : FROM centos
 ---> 470671670cac
Step 2/4 : VOLUME ["/dataVolumeContainer1","/dataVolumeContainer2"]
 ---> Running in ec9972b31819
Removing intermediate container ec9972b31819
 ---> 2949c836149c
Step 3/4 : CMD echo "finished,-----------success!"
 ---> Running in 75ae2ae8f2fb
Removing intermediate container 75ae2ae8f2fb
 ---> 84a009c900e2
Step 4/4 : CMD /bin/bash
 ---> Running in ec1c8fa104fa
Removing intermediate container ec1c8fa104fa
 ---> ff8a467cd398
Successfully built ff8a467cd398
Successfully tagged depu/centos:latest
```
6 depu@ubuntu:/mydocker$ docker run -it depu/centos
```
[root@81a319179a30 /]# cd /
[root@81a319179a30 /]# ls // 这时候我们可以看到我们自己创建的容器卷了
bin		      dev   lib		media  proc  sbin  tmp
dataVolumeContainer1  etc   lib64	mnt    root  srv   usr
dataVolumeContainer2  home  lost+found	opt    run   sys   var
```

###3.2 volumes-from容器间传递共享
以下操作说明，数据卷的生命周期会一直持续到没有容器使用它为止
```
depu@ubuntu:~$ docker run -it --name dc01 depu/centos //创建一个容器 命名为dc01
[root@ef7b6f120c04 /]# cd dataVolumeContainer2
[root@ef7b6f120c04 dataVolumeContainer2]# touch dc01_add.txt //给容器dc01创建一个文件dc01_add.txt

depu@ubuntu:~$ docker run -it --name dc02 --volumes-from dc01 depu/centos //创建一个容器 命名为dc02 通过--volumes-from共享dc01的数据卷 
[root@05b45691aa1f /]# cd dataVolumeContainer2
[root@05b45691aa1f dataVolumeContainer2]# ls // 查看文件夹，发现能看到dc01创建的文件
dc01_add.txt
[root@05b45691aa1f dataVolumeContainer2]# touch dc02_add.txt //给容器dc02创建一个文件dc02_add.txt

depu@ubuntu:~$ docker run -it --name dc03 --volumes-from dc01 depu/centos //创建一个容器 命名为dc03 通过--volumes-from共享dc01的数据卷
[root@b54235b64cf1 /]# cd dataVolumeContainer2
[root@b54235b64cf1 dataVolumeContainer2]# ls // 查看文件夹，发现能看到dc01和dc02创建的文件
dc01_add.txt  dc02_add.txt
[root@b54235b64cf1 dataVolumeContainer2]# touch dc03_add.txt
[root@b54235b64cf1 dataVolumeContainer2]# depu@ubuntu:~$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS               NAMES
b54235b64cf1        depu/centos         "/bin/sh -c /bin/bash"   27 seconds ago       Up 25 seconds                           dc03
05b45691aa1f        depu/centos         "/bin/sh -c /bin/bash"   About a minute ago   Up About a minute                       dc02
ef7b6f120c04        depu/centos         "/bin/sh -c /bin/bash"   5 minutes ago        Up 5 minutes                            dc01
depu@ubuntu:~$ docker attach dc01 //重新打开dc01的交互终端
[root@ef7b6f120c04 dataVolumeContainer2]# ls // 查看文件夹，发现能看到dc01和dc02， dc03创建的文件
dc01_add.txt  dc02_add.txt  dc03_add.txt 

删除同理，删除到只剩一个容器仍然能查看,包括删除dc01
^Pdepu@ubuntu:~$ docker rm -f dc01 //强制删除dc01容器
depu@ubuntu:~$ docker attach dc02  //打开dc02的伪终端
[root@05b45691aa1f dataVolumeContainer2]# ls  // 查看发现还能查看到所有的数据                                                     
dc01_add.txt  dc02_add.txt  dc03_add.txt
```

