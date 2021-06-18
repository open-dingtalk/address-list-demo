package com.aliyun.dingtalk.controller;

import com.aliyun.dingtalk.model.Dept;
import com.aliyun.dingtalk.service.AddressListService;
import com.aliyun.dingtalk.model.RpcServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 钉钉h5企业内部应用DEMO, 实现了根据用户授权码获取用户信息功能
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
    public RpcServiceResult syncDeptList(@RequestBody List<Dept> deptList) {

        return RpcServiceResult.getSuccessResult(addressListService.syncDeptList(deptList));

    }
}
