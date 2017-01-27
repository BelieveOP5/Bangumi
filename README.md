# Bangumi
Bangumi是基于[番组计划](http://bangumi.tv/)做的软件，这里特别向有兴趣的人推荐一下

其中工具用的是 [MVP](https://github.com/googlesamples/android-architecture
) + [Retrofit2.0](https://github.com/square/retrofit) + [ReJava2.0](https://github.com/ReactiveX/RxJava) + [greenDAO](https://github.com/greenrobot/greenDAO)

### MVP模式
MVP是从经典的模式MVC演变而来，它们的基本思想有相通的地方：Controller/Presenter负责逻辑的处理，Model提供数据，View负责显示。在该工程里的所有Activity或者Fragment都由相应的Presenter。

### Retrofit2.0
A type-safe HTTP client for Android and Java。( [官网](http://square.github.io/retrofit/) )

### ReJava2.0
RxJava is a Java VM implementation of ReactiveX (Reactive Extensions): a library for composing asynchronous and event-based programs by using observable sequences.( [官网](https://github.com/ReactiveX/RxJava/wiki) )

### greenDAO
greenDAO is a light  fast ORM for Android that maps objects to SQLite databases. Being highly optimized for Android, greenDAO offers great performance and consumes minimal memory.( [官网](http://greenrobot.org/greendao/) )

### 目标
* 用户登陆系统
* ~~获取每日放送列表 - 完成~~
* 番剧详细信息展示
* 用户番剧观看进度管理