### 3.1 Shell变量
+ Shell变量最基本规则：变量只有字符串和整数两种类型; 在shell运算中都是整数运算或者字符串操作运算。
#### 3.1.1 本地变量
`本地变量在用户现有的shell生命期的脚本中使用。本地变量随着当前终端的关闭而消失`, 名推荐写法：MY_开头， eg: MY_VAR ，以与环境变量相区别，且便于刷选。    
```
depu@ubuntu:~$ MY_VAL=10
depu@ubuntu:~$ set |grep MY_* # set 显示本地所有的变量
MY_VAL=10
depu@ubuntu:~$ echo $MY_VAL # 打印变量的值
10
depu@ubuntu:~$ unset MY_VAL #删除变量
```
>  readonly myvar1="test2" #只读变量
#### 3.1.2 环境变量   
+ 环境变量用于所有用户进程（经常称为子进程）。登录进程称为父进程。shell中执行的用户进程均成为子进程。不像本地变量
（只用于现在的shell），环境变量可用于所有子进程，这包括编辑器、脚本和应用程序。  
+ 注意常用的环境变量名称全部为大写，PATH、HOME（注意随登录用户改变）  
+ 如果不去改变环境变量，那么环境变量一直是默认值。如果改变了环境变量，当前终端结束后，再重新开启一个终端，会恢复成为原来
（之前）的样子。  
```
depu@ubuntu:~$ export PATH_MONGO=./lib/mongo #设置环境变量
depu@ubuntu:~$ export PATH_MONGO=$PATH_MONGO:newaddPath #追加环境变量
depu@ubuntu:~$ unset PATH_MONGO #删除变量
```

```
#!/bin/bash
ls
echo [ $? ] #执行上一条命令的值
```