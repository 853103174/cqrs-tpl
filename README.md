# cqrs-tpl

#### 介绍
基于CQRS思想的微服务项目模版

#### 软件架构
1. Java-v17
2. Gradle-v7.5.1
3. SpringBoot-v3.0.2
4. BeetlSQL-v3.19.3
5. Jedis-v4.3.1

#### 软件分包
1. application(应用层): 组织业务场景, 编排业务, 隔离场景对领域层的差异
2. domain(领域层): 实现具体的业务逻辑和规则
3. infrastructure(基础设施层): 提供具体的技术实现, 比如存储, 基础设施对业务保持透明
4. interfaces(用户界面层): 向用户显示信息, 处理用户输入
5. 以模块划分微服务, 便于添加或修改代码
6. ~~此项目只是做了一些简单的分层, 完整分层可根据下面的文档进行调整~~
7. 可在GenerateJavaFileUtils.java文件中的main方法修改自动生成的表名和包名

#### 打包命令
gradle build -Dfile=test

#### 参考文档
1. [聊一聊DDD应用的代码结构](https://wenku.baidu.com/view/43f5184b26c52cc58bd63186bceb19e8b8f6ecfa.html?_wkts_=1674267739736)
2. [DDD目录结构](https://wenku.baidu.com/view/451bec6af4ec4afe04a1b0717fd5360cbb1a8d57?aggId=43f5184b26c52cc58bd63186bceb19e8b8f6ecfa&_wkts_=1674269092912)
3. [SOFA应用架构详解](https://mp.weixin.qq.com/s/bYuwxg43FqFKjGv3G83fIw)
4. [凤凰架构](https://icyfenix.cn/)
5. [Java全栈知识体系](https://pdai.tech/)
6. [SpringBoot内置工具类](https://mp.weixin.qq.com/s/mFovEO3jQGGP2e6TaFxuXg)
