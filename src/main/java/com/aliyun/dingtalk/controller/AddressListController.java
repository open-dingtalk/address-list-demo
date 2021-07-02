package com.aliyun.dingtalk.controller;

import com.aliyun.dingtalk.model.Dept;
import com.aliyun.dingtalk.model.Role;
import com.aliyun.dingtalk.model.User;
import com.aliyun.dingtalk.model.UserRole;
import com.aliyun.dingtalk.service.AddressListService;
import com.aliyun.dingtalk.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 钉钉h5企业内部应用DEMO, 实现了同步企业通讯录到钉钉
 */
@RestController
public class AddressListController {

    @Autowired
    private AddressListService addressListService;

    /**
     * 欢迎页面, 检查后端服务是否启动
     *
     * @return
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 同步部门信息
     *
     * @param deptList 部门信息
     * @return
     */
    @PostMapping("/dept")
    public ServiceResult syncDeptList(@RequestBody List<Dept> deptList) {

        return ServiceResult.getSuccessResult(addressListService.syncDeptList(deptList));

    }

    /**
     * 同步员工信息
     *
     * @param users 员工信息
     * @return
     */
    @PostMapping("/user")
    public ServiceResult syncUsers(@RequestBody List<User> users) {

        return ServiceResult.getSuccessResult(addressListService.syncUsers(users));

    }

    /**
     * 同步角色组信息
     *
     * @param roleGroupNameList 角色组名称列表
     * @return
     */
    @PostMapping("/role/group")
    public ServiceResult syncRoleGroupList(@RequestBody List<String> roleGroupNameList) {

        return ServiceResult.getSuccessResult(addressListService.syncRoleGroupList(roleGroupNameList));

    }

    /**
     * 同步角色信息
     *
     * @param roleList 角色列表
     * @return
     */
    @PostMapping("/role")
    public ServiceResult syncRoleList(@RequestBody List<Role> roleList) {

        return ServiceResult.getSuccessResult(addressListService.syncRoleList(roleList));

    }

    /**
     * 同步用户角色信息
     *
     * @param userRole 用户角色
     * @return
     */
    @PostMapping("/user/role")
    public ServiceResult syncUserRole(@RequestBody UserRole userRole) {

        return ServiceResult.getSuccessResult(addressListService.syncUserRole(userRole));

    }


}
