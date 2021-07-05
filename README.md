# address-list-demo
> 企业通讯录部门、员工、角色同步到钉钉 ，确保钉钉侧员工权限安全准确（角色权限影响智能人事、审批、钉盘、文档等多个下游应用），避免重复低效设置
>
> 企业同步通讯录到钉钉需要登陆[开发者后台](https://open-dev.dingtalk.com/)，在创建的应用中配置应用首页地址，同步信息需要申请“维护通讯录的接口访问权限”。
>
> 包含功能：

- 同步部门：同步企业部门到钉钉；
- 同步员工：同步员工信息到钉钉；
- 同步角色组：同步角色组信息到钉钉；
- 同步角色：同步角色信息到钉钉；
- 同步员工角色信息：同步员工角色信息到钉钉；

### 开发环境准备

#### 钉钉开放平台环境准备

1. 需要有一个钉钉注册企业，如果没有可以创建：https://oa.dingtalk.com/register_new.htm?source=1008_OA&lwfrom=2018122711522903000&succJump=oa#/

2. 成为钉钉开发者，参考文档：https://developers.dingtalk.com/document/app/become-a-dingtalk-developer

3. 登录钉钉开放平台后台创建一个H5应用： https://open-dev.dingtalk.com/#/index

4. 配置应用

   配置开发管理，参考文档：https://developers.dingtalk.com/document/app/configure-orgapp

   ![image-20210705113544399](/Users/wan/Library/Application Support/typora-user-images/image-20210705113544399.png)

   添加维护通讯录的接口访问权限

   ![image-20210705113646100](/Users/wan/Library/Application Support/typora-user-images/image-20210705113646100.png)

##### 获取H5钉钉应用的参数

```properties
#H5应用Key
appKey=xxxx
#H5应用秘钥
appSecret=xxxxxx
```

##### 钉钉应用参数需要登陆开发者后台

1. 进入应用-基础信息获取agentId、appKey、appSecret



## Getting Started



### 克隆代码仓库到本地

git clone

```
https://github.com/open-dingtalk/address-list-demo.git
```



### 替换后端应用配置

![image-20210705113421732](/Users/wan/Library/Application Support/typora-user-images/image-20210705113421732.png)

### 参考文档

1. 同步通讯录权限申请，文档链接：https://developers.dingtalk.com/document/app/address-book-permissions
2. 创建部门，文档链接：https://developers.dingtalk.com/document/app/create-a-department-v2
3. 创建用户，文档链接：https://developers.dingtalk.com/document/app/user-information-creation
4. 创建角色组，文档链接：https://developers.dingtalk.com/document/app/add-a-role-group
5. 创建角色，文档链接：https://developers.dingtalk.com/document/app/add-role
6. 批量增加用户角色，文档链接：https://developers.dingtalk.com/document/app/add-role-information-to-employees-in-batches