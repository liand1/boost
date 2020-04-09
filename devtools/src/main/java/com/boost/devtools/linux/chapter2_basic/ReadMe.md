
### 2.1 vim
#### 2.1.1 vim三种模式
+ 命令模式(默认进入的模式)
> 在该模式下是不能对文件进行直接编辑，可以输入快捷键进行一些操作(删除行，复制行，移动过光标，粘贴等等)  
> vim +10 /etc/passwd：打开指定文件, 并且将光标移动到指定行  
> vim +/depu /etc/passwd: 打开指定文件, 并且将关键字高亮  
> shift+6或^: 移动到光标当前行的行首  
> shift+4或$: 移动到光标当前行的行尾  
> gg: 移动到首行  
> G: 移动到末行  
> pageUp或ctrl+b向上翻屏  
> pageDown或ctrl+f向下翻屏   
> yy: 复制  
> 10 yy: 向下复制10行，包含光标所在行  
> ctrl+v: 可视化复制, 配合方向键来选中要复制的内容, 然后按yy复制，p粘贴  
> p: 粘贴  
> dd: 剪切,删除当前所在行, 删除之后下一行上移，严格意义是剪切命令  
> 10 dd: 向下剪切10行  
> D: 删除当前行，下一行不上移  
> :u: 撤销  
> ctrl+r： 恢复之前的撤销  
> 10+G: 将光标移动到第10行
> 数字+方向键: 移动光标，上下左右都可
> 末行模式输入10+回车: 将光标移动到第10行
+ 编辑模式(输入模式)
> 可以对文件进行编辑  
> i: 在光标所在字符前插入  
> a: 在光标所在字符后插入  
> Esc: 退出  
+ 末行模式(尾行模式)
> 在末行输入命令来对文件进行操作(搜索，替换，保存，退出..)  
> ":": 进入末行模式  
> Esc/删除末行的所有全部命令: 退出末行模式  
> w: write, 保存  
> w + path： 另存到指定路径  
> w + q: write, quite    
> `X：大写X，文件加密，加密后需要输入密码才能打开`  
> `x: 保存退出, 跟wq的区别在于，文件没有修改，就是类似于q， 修改时间不会更新，如果文件修改了，就等同于wq，修改时间会更新，wq不管文件是否修改，更新时间都会变化`  
> `q!：强制退出，刚才做的修改不保存`  
> `/关键字：搜索, 用N/n切换上一个/下一个结果`   
> `s/user/User: 把光标所在行的第一个user替换成USER`  
> `s/user/User/g: global, 把光标所在行的所有user替换成USER`  
> `%s/user/User: 把所有行的每行的第一个user替换成USER`  
> `%s/user/User/g: global, 把所有user替换成USER`  
> `set nu: 显示行号`  
> `set nonu: 取消显示行号`  
> 'files: 查看打开的文件 %a, 表示active状态的, #表示上一个打开的(前提是同时用vim命令打开了多个文件 vim ~/dev/pwd1 ~/dev/pwd2)'  
```
systemd-network:x:101:103:systemd Network Management,,,:/run/systemd/netif:/bin/false
:files
  1 %a=  "passwd"                       line 1
  2  #    "pwd"                          line 0
```
> `open pwd2: 切换到pwd2文件`  
> `:bp/bn：back next/previous切换到上一个文件/下一个文件`  


#### 2.1.2 vim配置
Vim可以有自己的配置
1 在文件打开的时候在末行下输入的配置(temporary)  
2 个人配置(~/.vimrc， 没有可以自己创建)  
```
    1 set nu
    2 syntax on
```
3 全局配置(/etc/vimrc), `如果个人配置和全局配置有冲突的情况下,会覆盖个人配置的`  

#### 2.1.3 vim异常退出
解决办法: 将交换文件删除, 同目录下的同文件名以swp后缀的文件

### 2.2 别名机制
相当于创建一些属于自己的自定义命令
依靠一个别名映射文件: ~/.bashrc, 编辑之后需要重新登陆
```
alias cls='clear'
```
### 2.3 Linux 自有服务
不需要用户独立安装的软件的服务，而是系统安装好之后就可以直接使用的服务(内置)
#### 2.3.1 运行模式
也称之为运行级别. 在Linux中存在一个进程：init(initialize, 初始化), PID为1，该进程存在一个对应的配置文件:/etc/inittab

### 2.4 用户管理
/etc/passwd: 存储用户的关键信息  
/etc/group: 存储用户组的关键信息  
/etc/shadow: 存储用户的密码信息  
+ 添加用户  useradd 
```useradd 选项 用户名 # 创建完成之后会在home文件夹下生成一个同名的家目录
depu@ubuntu:/home$ ll
drwxr-xr-x  3 root root 4096 Sep 27  2019 ./
drwxr-xr-x 24 root root 4096 Apr  7 07:19 ../
drwxr-xr-x 20 depu depu 4096 Apr  8 07:07 depu/
```  
>-g: 指定用户组, 可以是id也可以是组名
>-G: 指定附加组, 可以是id也可以是组名
>-u: uid, 用户的标识符， 指定后，系统不会默认分配

创建完用户后，可以通过```cat /etc/passwd```来查看用户名是否创建成功，文件结构如下
case1: 创建depu用户主组为500，附加组为501
```
usermod -g 500 -G 501 depu
```
```
#我们发现每一行被":"分隔成多个字符串，现在解释一下，每一个字符串的含义,以第一行为例
depu: 创建的新用户的名称
x: 密码的占位符
1000: 用户的识别符 uid
1000: 用户主组id
ubunut-desktop,,,: 注释, 可以在创建用户的时候用 -c来添加注释
/home/depu: 用户的家目录
/bin/bash: 解释器sheel, 等待用户进入系统后,用户输入指令之后，该解释器会收集用户输入的指令，传递给内核处理
depu:x:1000:1000:ubunut-desktop,,,:/home/depu:/bin/bash
epmd:x:121:129::/var/run/epmd:/bin/false
rabbitmq:x:122:130:RabbitMQ messaging server,,,:/var/lib/rabbitmq:/bin/false
```
+ 修改用户 usermod
case1: 修改depu用户主组为500，附加组为501
```
usermod -g 500 -G 501 depu
```
>-g: 指定用户组, 可以是id也可以是组名
>-G: 指定附加组, 可以是id也可以是组名
>-u: uid, 用户的标识符， 指定后，系统不会默认分配
>-l: 修改用户名(小写字母L)
+ 设置密码 
Linux不允许没有密码的用户登陆到系统，因此开始创建的用户都是锁定状态，需要设置密码才能登陆到计算机
```
depu@ubuntu:~$ passwd depu
Changing password for depu.
(current) UNIX password: 
```
+ 删除用户 userdel depu 
如果该用户还有对应的进程，需要先kill掉
> -r: 删除用户的用户删除其家目录

### 2.5 用户组管理 
类似用户管理,这里不做过多解释

### 2.6 网络设置
centos中网卡的信息放在/etc/sysconfig/network-scripts下面  
重启网卡  
+ service network restart  
+ /etc/init.d/network restart  
+ ifdown eth0, 停止eth0这个网卡
+ ifup eth0, 启动eth0这个网卡

### 2.7 SSH(secure shell，安全外壳协议)
配置文件目录在这cat /etc/ssh/ssh_config
### 2.8 修改文件权限
+ chmod: change mod, r = 4, w=2, x=1,-=0, `下面的777中的每个数字分别代表所属用户权限，所属组权限，其他用户权限`
`drwxr-xr-x 文件的权限， 属主：rwx=7, 用户组r-x=5, 其他用户r-x=5`
> chmod 777 /etc/log.log  
> chmod -R 777 /dev/env/: Recursion,目录下的所有文件或子目录设置为777权限  
> chmod u+rw /etc/log.log: 给文件添加属主读写权限  
> chmod g+w /etc/log.log: 给文件添加属组写权限  
> chmod o+x /etc/log.log: 给文件添加其它执行权限 
> chmod a-w /etc/log.log: 给文件属主，属组和其他去掉写权限  
> chmod -w /etc/log.log: 给文件属主，属组和其他去掉写权限,等同于chmod a-w /etc/log.log  
> chmod o=x /etc/log.log: 给文件添加其它的权限设置为只有执行权限  
> chmod u=x,g=ewx,o=w /etc/log.log  
> chmod ugo-w /etc/log.log  

+ chown: change file owner and group
> chown root a.sh: 改变用户属主  
> chown .root a.sh: 改变用户属主,属组, 等同于chgrp root a.sh  
> chown root.root a.sh: 改变用户属主,属组  
> chown -R root.root /dev/: 改变用户属主,属组  

+ chgrp: change file group