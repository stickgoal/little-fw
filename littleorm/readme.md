littleORM
--

实现类似于hibernate的自动化ORM框架

## 组成部分

- entity支持，通过定义注解指定映射关系
- dao支持 自动生成sql并发送给数据库
- session 接口

## 流程

- 启动阶段，扫描所有的@Entity注解的类并解析出对应的SQL
- 调用阶段，根据调用来拼装SQL，并执行ORM
