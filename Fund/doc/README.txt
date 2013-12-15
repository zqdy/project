开发说明文档：
该项目以模块进行组织构架
在src目录中：
1、fund目录放全局共有类；
2、所有UI界面（Activity）放在app目录中，在该目录中再分模块：
主要分四大模块：登录与注册、主界面、各基金信息界面、个人资产信息界面
（1）account目录放登录、注册模块；
（2）main目录放登录成功后的主界面；
（3）detail目录放各基金信息界面；
（4）property目录放个人资产信息；
3、utils目录放通用工具类。
4、models目录放各类的实体类
5、database目录放数据库访问类
6、net目录放网络访问类
7、service目录放服务类
8、providers目录放providers类
9、ui目录放自定义的控件

注意：所有的Activity都从BaseActivity继承。

test目录中主要放单元测试类

doc目录主要放关于该项目的说明