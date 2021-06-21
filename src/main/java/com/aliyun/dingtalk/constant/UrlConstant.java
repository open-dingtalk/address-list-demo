package com.aliyun.dingtalk.constant;

/**
 * 钉钉开放接口网关常量
 */
public class UrlConstant {

    /**
     * 获取access_token url
     */
    public static final String GET_ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    /**
     * 创建部门url
     */
    public static final String CREATE_DEPT_URL = "https://oapi.dingtalk.com/topapi/v2/department/create";

    /**
     * 创建用户url
     */
    public static final String CREATE_USER_URL = "https://oapi.dingtalk.com/topapi/v2/user/create";

    /**
     * 创建角色组url
     */
    public static final String CREATE_ROLE_GROUP_URL = "https://oapi.dingtalk.com/role/add_role_group";

    /**
     * 创建角色url
     */
    public static final String CREATE_ROLE_URL = "https://oapi.dingtalk.com/role/add_role";

    /**
     * 增加用户角色url
     */
    public static final String ADD_USER_ROLE_URL = "https://oapi.dingtalk.com/topapi/role/addrolesforemps";

}
