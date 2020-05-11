### 写给自己(出自左耳听风)
#### 作为一个软件工程师，需要拥有技术领导力
+ 扎实的基础技术；
+ 非同一般的学习能力；
+ 坚持做正确的事；
+ 不断提高对自己的要求标准；

#### 高标准要求自己
+ Google 的自我评分卡
+ 敏锐的技术嗅觉
+ 强调实践，学以致用
+ Lead by Example

#### 

#### 如何拥有技术领导力
+ `能够发现问题`。 能够发现现有方案的问题。
+ 能够提供解决问题的思路和方案，并能比较这些方案的优缺点。
+ `能够做出正确的技术决定`。用什么样的技术、什么解决方案、怎样实现来完成一个项目。
+ 能够用更优雅，更简单，更容易的方式来解决问题
+ 能够提高代码或软件的扩展性、重用性和可维护性
+ `能够用正确的方式管理团队`, 所谓正确的方式，一方面是，让正确的人做正确的事，并发挥每个人的潜力；另一方面是，可以提高团队的生产力和人效，找到最有价值的需求，用最少的成本实现之。并且，可以不断地提高自身和团队的标准 
+ `创新能力`, 能够使用新的方法新的方式解决问题，追逐新的工具和技术

#### 吃透基础技术。基础技术是各种上层技术共同的基础
##### 编程部分
+ C 语言 
> 相对于很多其他高级语言来说，C 语言更接近底层。在具备跨平台能力的前提下，它可以比较容易地被人工翻译成相应的汇编代码。它的内存管
>理更为直接，可以让我们直接和内存地址打交道。
+ 编程范式
> 学好编程范式，有助于培养你的抽象思维，同时也可以提高编程效率，提高程序的结构合理性、可读性和可维护性，降低代码的冗余度，进而提
>高代码的运行效率。要学习编程范式，你还可以多了解各种程序设计语言的功能特性。
+ 算法和数据结构
> 算法（及其相应的数据结构）是程序设计的有力支撑。适当地应用算法，可以有效地抽象问题，提高程序的合理性和执行效率。算法是编程中最
>最重要的东西，也是计算机科学中最重要的基础。

##### 系统部分
+ 计算机系统原理
> CPU 的体系结构（指令集 [CISC/RISC]、分支预测、缓存结构、总线、DMA、中断、陷阱、多任务、虚拟内存、虚拟化等），内存的原理与性
>能特点（SRAM、DRAM、DDR-SDRAM 等），磁盘的原理（机械硬盘 [盘面、磁头臂、磁头、启停区、寻道等]、固态
>硬盘 [页映射、块的合并与回收算法、TRIM 指令等]），GPU 的原理等。
>
>学习计算机系统原理的价值在于，除了能够了解计算机的原理之外，你还能举一反三地反推出高维度的分布式架构和高并发高可用的架构设
>计。比如虚拟化内存就和今天云计算中的虚拟化的原理是相通的，计算机总线和分布式架构中的 ESB 也有相通之处，计算机指令调度、并发控
>制可以让你更好地理解并发编程和程序性能调优……这里，推荐书籍《深入理解计算机系统》
+ 操作系统原理和基础
> 进程、进程管理、线程、线程调度、多核的缓存一致性、信号量、物理内存管理、虚拟内存管理、内存分配、文件系统、磁盘管理等。学习
>操作系统的价值在于理解程序是怎样被管理的，操作系统对应用程序提供了怎样的支持，抽象出怎样的编程接口（比如 POSIX/Win32 API），性
>能特性如何（比如控制合理的上下文切换次数），怎样进行进程间通信（如管道、套接字、内存映射等），以便让不同的软件配合一起运行等。
>
>学习操作系统知识，一是要仔细观察和探索当前使用的操作系统，二是要阅读操作系统原理相关的图书，三是要阅读 API 文
>档（如 man pages 和 MSDN Library），并编写调用操作系统功能的程序。这里推荐三本书《UNIX 环境高级编程》、《UNIX 网络编程》
>和《Windows 核心编程》。
>
>学习操作系统基础原理的好处是，这是所有程序运行的物理世界，无论上层是像 C/C++ 这样编译成机器码的语言，还是像 Java 这样有 JVM 做
>中间层的语言，再或者像 Python/PHP/Perl/Node.js 这样直接在运行时解释的语言，其在底层都逃离不了操作系统这个物理世界的“物理定律”。
+ 网络基础
> 计算机网络是现代计算机不可或缺的一部分。需要了解基本的网络层次结构（ISO/OSI 模型、TCP/IP 协议栈），包括物理层、数据链路
>层（包含错误重发机制）、网络层（包含路由机制）、传输层（包含连接保持机制）、会话层、表示层、应用层（在 TCP/IP 协议栈里，这三层可以并为一层）。
>
>比如，底层的 ARP 协议、中间的 TCP/UDP 协议，以及高层的 HTTP 协议。这里推荐书籍《TCP/IP 详解》，学习这些基础的网络协议，可以为
>我们的高维分布式架构中的一些技术问题提供很多的技术方案。比如 TCP 的滑动窗口限流，完全可以用于分布式服务中的限流方案。

+ 数据库原理
> 数据库管理系统是管理数据库的利器。通常操作系统提供文件系统来管理文件数据，而文件比较适合保存连续的信息，如一篇文章、一个图片等。
>但有时需要保存一个名字等较短的信息。如果单个文件只保存名字这样的几个字节的信息的话，就会浪费大量的磁盘空间，而且无法方便地查询（除非使用索引服务）
>
> 但数据库则更适合保存这种短的数据，而且可以方便地按字段进行查询。现代流行的数据库管理系统有两大类：SQL（基于 B+ 树，强一致性）
>和 NoSQL（较弱的一致性，较高的存取效率，基于哈希表或其他技术）
>
> 学习了数据库原理之后便能了解数据库访问性能调优的要点，以及保证并发情况下数据操作原子性的方法。要学习数据库，你可以阅读各类数据库
>图书，并多做数据库操作以及数据库编程，多观察分析数据库在运行时的性能
+ 分布式技术架构
> 数据库和应用程序服务器在应对互联网上数以亿计的访问量的时候，需要能进行横向扩展，这样才能提供足够高的性能。为了做到这一点，要学
>习分布式技术架构，包括负载均衡、DNS 解析、多子域名、无状态应用层、缓存层、数据库分片、容错和恢复机制、Paxos、Map/Reduce 操
>作、分布式 SQL 数据库一致性（以 Google Cloud Spanner 为代表）等知识点。

`上面这些基础知识通常不是可以速成的。虽然说，你可以在一两年内看完相关的书籍或论文，但是，我想说的是，这些基础技术是需要你用一生的
时间来学习的，因为基础上的技术和知识，会随着阅历和经验的增加而有不同的感悟`。

