### Gerrit
 Gerrit是一个建立在Git版本控制系统之上并且基于Web的代码审查工具，是开发者和Git之间的一层屏障，不允许直接将本地修改内容同步到远程
 仓库中，Gerrit可以和Jenkins进行集成，每次代码提交后，在人工审核代码前，通过jenkins任务自动运行单元测试，构建以及自动化测试，
 Jenkins任务如果失败，会自动打回这次提交。
 
 #### 一般Gerrit， Jenkins，Git集成在一起之后的使用流程如下：
 + 1 开发者提交代码到Gerrit
 + 2 触发对应的Jenkins任务，通过以后verified 加 1
 + 3 人工审核，审核通过后code review 加 2
 + 4 确认这次提交，Gerrit 执行和Git仓库的代码同步。
 + 5 代码入库
