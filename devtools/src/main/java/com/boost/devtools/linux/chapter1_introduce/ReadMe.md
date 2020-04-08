>首先先使用VMware备份操作系统
快照，又称还原点，侧重于短期备份，做快照的时候虚拟机的操作系统一般处于开启状态
`VMware->虚拟机->快照->拍摄快照`
克隆，侧重于长期备份，做克隆的时候必须得关闭


linux中，一切皆是文件，包括进程，磁盘等等..

### 1.1 Linux的系统目录结构
+ bin： 全称binary, 含义是二进制。所以顾名思义放的都是二进制文件，文件都是可以被运行的  
+ dev： 主要存放的是外接设备，例如u盘，光盘。在其中的外接设备是不能直接使用的，需要挂载(类似windows下的分配盘符, windows会新建了一个盘符)  
+ etc： 主要存储一些配置文件，比如数据库的配置文件  
+ Home: 表示除了root用户以外的其它用户的家目录，类似于windows的/User用户目录  
+ Proc: process，该目录中存储的是Linux运行时候的进程  
+ Root：root用户自己的家目录  
+ sbin：Super binary， 该目录也是存储一些可以被执行的二进制文件，但是必须有root权限的才能执行  
+ tmp: temporary, 系统运行时产生的临时文件  
+ usr: 存放的是用户自己安装的软件。  
+ var：存放的程序/系统的日志文件的目录  
+ mnt：当外接设备需要挂载的时候，就需要挂载到mnt目录下 

### 1.2 Linux指令和选项
`在Linux终端(命令行)中输入的内容就称之为指令`。

一个完整的指令的标准格式: Linux通用的格式
一个指令可以包含多个选项和操作对象
```
#指令主体 [选项] 操作对象
```

### 1.3 Linux基础指令
+ ls： list 列出当前工作目录下所有文件/文件夹名称  
> -l: 表示以`详细`列表的形式进行展示  
```
drwxr-xr-x   2 root root 4096 May 11  2019 media  // drwxr-xr-x第一个字母'd'表示文件类型是一个文件夹directory, l表示link
-rwxr-xr-x   2 root root 4096 May 11  2019 log    // -rwxr-xr-x第一个'-'表示文件类型是一个文件
```
> -a: 显示所有，包括隐藏的文件/文件夹, 隐藏文档一般都是以'.'开头  
> `ls --help 可以查看更多`
```
#ls ../../root // 列出上上级目录下的root下面的文件名称
```
`
关于路径可以分为相对路径和绝对路径
相对路径：首先得有一个参照物，一般就是当前的工作路径
绝对路径：不需要参照物，直接从根目录"/"开始寻找对应路径
`  

+ pwd： print working directory没有参数, 打印当前目录    
+ cd: change directory, 切换工作目录    
> cd ~： 切换到当前用户的家目录    
+ mkdir: make directory,创建目录,可以是文件夹名称，也可以是名称的一个完整路径    
+ mv: move, 剪切,`重命名`  
+ cp: copy, 复制    
+ rm: remove, 如果不带选项，会出现是否需要删除提示     
> -f: force, 强制删除，不会提示是否删除    
> -r: recursion, 递归删除, 删除文件夹下面的所有文件/文件夹, 一般需要加-f, 如rm -rf /directory    
> rm -rf a b 批量删除多个    
> rm -f depu* 批量以depu开头的, 通配符    
+ vim：文本编辑器  
+ 输入重定向,一般命令的输出都会显示在终端中，有些时候需要将一些命令的执行结果想要保存到文件中进行后续的分析/统计，则这个时候需要用到重定向技术
> ">"：覆盖输出, 覆盖掉原先的内容, 文件不存在则自动新建 ls -la > ls.txt  
> ">>": 追加，追加到文件内容的末尾, 文件不存在则自动新建 ls -la >> ls.txt  
+ cat: 直接打开一个文件，可以对文件进行合并，把多个文件合并成一个文件  
> cat 待合并的文件路径1 待合并的文件路径2 ... > 合并之后的文件路径  
+ su: switch user, 切换用户
+ ln -s origPath desPath: 创建软链接, 给深层的文件定义一个链接，通过这个链接可以直接执行这个命令

### 1.4 Linux进阶指令
+ df: disk free 查看磁盘空间  
```
[root@ad1b28c6a518 /]# df -h
#Filesystem(磁盘分区)， Mounted on是磁盘的挂点路径，如果想访问overlay，就进入到/，如果访问tmpfs就去/dev
Filesystem      Size  Used Avail Use% Mounted on
overlay          19G  8.7G  9.1G  49% /
tmpfs            64M     0   64M   0% /dev
tmpfs           2.0G     0  2.0G   0% /sys/fs/cgroup
shm              64M     0   64M   0% /dev/shm
```
+ free 查看内容使用情况
```
Swap代表当内存不够用的时候使用磁盘空间来充当内存
[root@ad1b28c6a518 /]# free -h
              total        used        free      shared  buff/cache   available
Mem:          3.8Gi       861Mi       1.8Gi       8.0Mi       1.2Gi       2.7Gi
Swap:         974Mi          0B       974Mi
```
+ head 查看一个文件的前n行，如果不指定，默认显示前10行  
> head -5 /log.log： 查看前5行  
+ tail 查看一个文件的末n行，如果不指定，默认显示后10行  
> tail -f /log.log: 查看一个文件的动态变化,用来查看日志很方便  
+ less 查看文件，以较少的内容进行输入，按辅助功能键来查看更多  
> less /log.log: (数字+回车， 空格键+上下方向键可以查看更多)  
+ wc 统计文件内容信息(包括行数，单词数，字节数)  
> -l: lines
> -w: word
> -c: bytes
+ `date(重点)，表示操作时间(读取，设置)`, 涉及到shell脚本  
> date "+%F %T": 2020-04-06 01:04:14， %F表示完整的年月日， %T表示完整的时分秒， %Y表示四位年份  
> date -d "-1 day" "+%F %T" : 往前推一天  
> date -d "-1 year" "+%F %T" : 往前推一年  
> %m: 表示两位年份(带前导0)      
> %d: 表示两位日期(带前导0)  
> %H: 表示两位小时(带前导0)  
> %M: 表示两位分钟(带前导0)  
> %S: 表示两位秒数(带前导0)  
+ cal 输出房前月份的日历
```
[root@ad1b28c6a518 /]# cal
     April 2020     
Su Mo Tu We Th Fr Sa
          1  2  3  4
 5  6  7  8  9 10 11
12 13 14 15 16 17 18
19 20 21 22 23 24 25
26 27 28 29 30 
```
+ clear/Ctrl+L 清除终端中已经存在的命令和结果(把之前的信息隐藏到了上面，可以通过滚动条查看)  
+ 管道符, "|"， 用于过滤， 特殊，扩展处理， 不能单独使用，其作用主要是辅助    
> ls /|grep y: 列出/目录下包含y字母的文件夹/文件, grep主要用来过滤  
> cat /log.log|less: 只是了解一下特殊用法，等于于less /log.log  
> ls /|wc -l: 统计文档总数量  

### 1.5 Linux高级指令
+ hostname: 显示操作服务器的主机名(读取, 设置)  
> -f: 输出当前主机的FQDN(Fully Qualified Domain Name全限定域名)
+ id: 查看一个用户的一些基本信息(包括用户id, 用户组id, 附加组id..), 如果不指定用户显示当前用户基本信息
> 验证用户信息, /etc/passwd
> 验证组信息, /etc/group
+ whoami：显示当前用户名, 一般用于shell脚本
+ ps -ef: 主要是查看服务器的进程信息, -e 等价于"-A", 表示列出全部的进程, -f显示全部的列(显示全字段)
```
UID:用户id, 
PID:进程ID, 
PPID:parent PID,`如果一个进程的PPID找不到,那么称之为僵尸进程，是可以kill的`, 
C:cpu占用率, 
STIME:该进程的启动时间,
TTY:终端设备, 如果显示一个"？",表示不是由终端发起的
CMD:进程名称或者对应的路径
[root@ad1b28c6a518 /]# ps -ef
UID         PID   PPID  C STIME TTY          TIME CMD
root          1      0  0 00:28 pts/0    00:00:00 /bin/bash
root         38      1  0 01:54 pts/0    00:00:00 ps -ef
```
> ps -ef|grep docker: 搜索docker的进程，搜索结果至少是一个，如果只有1个，表示除了刚才搜索的这个进程，没有找到对应的进程
+ top: 查看服务器的进程占的资源, 实时动态的形式, q键退出
```
depu@ubuntu:~$ top
top: 当前时间
up: 系统运行时间
1 user: 一个用户
load average: 运行时的负载情况,第一个0.04表示5分钟内的负载情况,0.01表示10分钟内的负载情况,0.00表示15分钟内的负载情况
tasks: 任务数
1 running：一个运行的
168 sleeping: 168个休眠的
0 stopped：
0 zombie：
%Cpu(s):  0.2 us,  0.2 sy,  0.0 ni, 99.7 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st ： cpu使用情况
PR: priority  进程优先级
NI: 用户进程空间内改变过优先级的进程占用CPU百分比
VIRT：virtual memory usage，虚拟内存
RES: resident memory usage，常驻内存
SHR: shared memory，共享内存
S: state, 进程的状态(S睡眠,R运行,I表示Idle空闲...)
COMMAND: 进程的名称或者路径
------------------------------------------------------------------------------------------------------
top - 10:07:45 up  1:40,  1 user,  load average: 0.04, 0.01, 0.00
Tasks: 238 total,   1 running, 168 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.2 us,  0.2 sy,  0.0 ni, 99.7 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  4015932 total,  1933644 free,   765732 used,  1316556 buff/cache
KiB Swap:   998396 total,   998396 free,        0 used.  2876892 avail Mem 

   PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND    
  2339 depu      20   0  195456   5456   5116 S   0.3  0.1   0:00.36 ibus-engi+ 
  6199 depu      20   0   48968   3924   3220 R   0.3  0.1   0:00.12 top        
     1 root      20   0  185176   5848   4096 S   0.0  0.1   0:04.08 systemd    
     2 root      20   0       0      0      0 S   0.0  0.0   0:00.04 kthreadd   
     4 root       0 -20       0      0      0 I   0.0  0.0   0:00.00 kworker/0+ 
     6 root       0 -20       0      0      0 I   0.0  0.0   0:00.00 mm_percpu+
```
> 在运行stop的时候，按一下键可以按以下快捷键  
> M:按照内存占用率从高到低排列  
> P:按照CPU使用率从高到低排列  
> 1:查看每个CPU的使用信息  
+ du -sh: disk usage, 查看目录的真实大小
> -s: summaries: 只显示汇总的大小  
> -h: 高可读的方式显示  
+ sudo find / -name  daemon.json -type f: 搜索/目录下面的daemon.json文件,不包含文件夹 
> -name: 按照文档名称进行搜索(支持模糊搜索), sudo find / -name  *.json 
> -type: "-"表示文件,"d"表示文件,搜索的时候用"f"来代替"-"
> sudo find / -name  *.json | wc -l: 搜索/目录下面的有多少个json文件
+ service: 重点, 控制一些软件的服务的启动,停止和重启
> service 服务名 start/stop/restart