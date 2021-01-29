# Heimdall SpringBoot 开发脚手架项目

&emsp;&emsp;**Heimdall** SpringBoot开发脚手架，封装常用 SpringBoot 功能模块，方便集成开发。


 

## 主要功能

* **Boot 公共模块**：SpringBoot 公共模块，包括异常处理和 JSON设置等。
* **Captcha 模块**：图形验证码模块，支持本地缓存和 Redis 缓存
* **JPA 模块**：常用 JPA 配置、通用 Hibernate 泛型 DAO，全局事务切面等
* **syslog 模块**：全局日志注解，通过注解方式记录 Web 层访问日志
* **utils 模块**：全局工具类
* **xss 模块**：xss 防御模块
* 更多功能持续完善中......

- - -



## 开发
1. 从 Gitee 或者 github clone 代码到本地
1. 修改根pom.xml中properties节点下dest.path为自己本地打包文件输出目录
1. 在根pom.xml所在目录执行 mvn clean package



## 交流反馈,参与贡献
- Github

<a target="_blank" href="https://github.com/luterc/heimdall">**Heimdall 框架**</a>

<a target="_blank" href="https://github.com/luterc/heimdall-admin">**Heimdall-admin  权限管理系统** </a>

<a target="_blank" href="https://github.com/luterc/heimdall-admin-ui">**Heimdall-admin-ui  管理系统前端 UI** </a>

**技术交流QQ群:	554290469**

欢迎fork，star，欢迎提需求，欢迎吐槽，支持共建！

*如果你感觉好用的话，支持刷火箭 !*




## License
Apache License, Version 2.0

