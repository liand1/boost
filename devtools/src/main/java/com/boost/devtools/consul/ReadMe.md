### 什么是consul
Consul是一个支持多数据中心分布式高可用的服务发现和配置共享的服务软件

#### docker 启动consul

#### consul提供的一些关键特性：
+ service discovery: consul通过DNS或者HTTP接口使服务注册和服务发现变的容易
+ health checking: 健康检查使consul可以快速的警告在集群中的操作，和服务发现的集成，可以防止请求转发到故障的服务上面
+ key/value storage: 一个用来存储动态配置的系统，提供简单的http接口，可以在任何地方使用。

#### consul的角色
1 client
> consul的client模式，就是客户端模式，使consul节点的一种模式，这种模式下，所有注册到当前节点的服务会被转发到server，本身使不持久化这些信息
> 特点:
> + 只接受注册，并不会存储消息
> + 注册服务，转发到服务端

2 server
> server表示consul的server模式，这种模式下， 功能和client一样，唯一不同的使，它会把所有的信息持久化到本地，这样遇到故障，信息使可以被保留下来的
> 特点：
> + 持久化消息
```
consul agent -server -bootstrap-expect 3 -data-dir /tmp/consul -node=s1 -bind=10.201.102.198 -ui-dir ./consul_ui/ -rejoin -config-dir=/etc/consul.d/ -client 0.0.0.0
-server ： 定义agent运行在server模式
-bootstrap-expect ：在一个datacenter中期望提供的server节点数目，当该值提供的时候，consul一直等到达到指定sever数目的时候才会引导整个集群，该标记不能和bootstrap共用
-bind：该地址用来在集群内部的通讯，集群内的所有节点到地址都必须是可达的，默认是0.0.0.0
-node：节点在集群中的名称，在一个集群中必须是唯一的，默认是该节点的主机名
-ui-dir： 提供存放web ui资源的路径，该目录必须是可读的
-rejoin：使consul忽略先前的离开，在再次启动后仍旧尝试加入集群中。
-config-dir：配置文件目录，里面所有以.json结尾的文件都会被加载
-client：consul服务侦听地址，这个地址提供HTTP、DNS、RPC等服务，默认是127.0.0.1所以不对外提供服务，如果你要对外提供服务改成0.0.0.0
```

3 server-leader
> 负责同步注册的信息给其他的server，同时也要负责各个节点的健康监测
> 特点： 
> 1 注册服务转发
> 2 健康检查
> 3 持久化信息