# Web开发的基本模式

**从宏观上讲,Web开发模式中最最最重要的一条就是分层开发模式.**

## 分层开发模式

分层开发模式指的是,将整个程序根据功能职责进行纵向划分.

常见的划分方法就是分成 表示层、业务层、持久层.

- 表示层:负责处理与界面交互相关的功能
- 业务层:负责复杂的业务逻辑计算和判断
- 持久层:负责将业务逻辑数据进行持久化存储

分层开发模式体现了"分而治之"的设计思想,并且提高了程序可读性与可扩展性.

## MVC模式

MVC模式是处理表示层的最佳实践.

- M (Model) 数据模型
- V (View)  视图展现
- C (Control) 控制器

MVC模式的工作流程图

![](https://github.com/HuangYiCheng1997/create-picture-url/blob/master/java/CrazyJava/MVCModel.png?raw=true)

