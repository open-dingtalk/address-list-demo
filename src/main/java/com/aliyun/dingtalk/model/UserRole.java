package com.aliyun.dingtalk.model;


import lombok.Data;

@Data
public class UserRole {

    /**
     * 角色roleId列表，多个roleId用英文逗号（,）分隔，最多可传20个
     */
    private String roleIds;

    /**
     * 员工的userId，多个userId用英文逗号（,）分隔，最多可传20个。
     */
    private String userIds;


}
