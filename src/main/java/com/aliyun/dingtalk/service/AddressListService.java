package com.aliyun.dingtalk.service;

import com.aliyun.dingtalk.model.Dept;
import com.aliyun.dingtalk.model.Role;
import com.aliyun.dingtalk.model.User;
import com.aliyun.dingtalk.model.UserRole;
import com.dingtalk.api.response.OapiV2UserCreateResponse;

import java.util.List;
import java.util.Map;

public interface AddressListService {
    Map syncDeptList(List<Dept> deptList);

    List<OapiV2UserCreateResponse.UserCreateResponse> syncUsers(List<User> users);

    Map<String, Long> syncRoleGroupList(List<String> roleGroupNameList);

    Map<String, Long> syncRoleList(List<Role> roleList);

    String syncUserRole(UserRole userRole);
}
