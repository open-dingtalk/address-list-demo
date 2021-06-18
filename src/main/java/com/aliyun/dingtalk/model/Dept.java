package com.aliyun.dingtalk.model;

import lombok.Data;

import java.util.List;

/**
 * 可以自行添加需要传入的字段
 */
@Data
public class Dept {

    /**
     * 企业部门ID
     */
    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 钉钉父部门ID，根部门ID为1。默认为根部门
     */
    private Long parentId = 1L;

    /**
     * 子部门
     */
    private List<Dept> children;
}
