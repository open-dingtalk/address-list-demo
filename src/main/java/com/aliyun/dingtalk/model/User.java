package com.aliyun.dingtalk.model;

import lombok.Data;

import java.util.List;

/**
 * 用户
 * 可以自行添加需要传入的字段
 */
@Data
public class User {

    /**
     * 员工唯一标识ID（不可修改），企业内必须唯一。
     *
     * 长度为1~64个字符，如果不传，将自动生成一个userid。
     */
    private String userid;

    /**
     * 员工名称，长度最大80个字符。
     */
    private String name;

    /**
     * 手机号码，企业内必须唯一，不可重复。
     *
     * 如果是国际号码，请使用+xx-xxxxxx的格式。
     */
    public String mobile;

    /**
     * 所属部门id列表 eq:"2,3,4"
     */
    private String deptIdList;

}
