package com.aliyun.dingtalk.service.impl;

import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.model.Dept;
import com.aliyun.dingtalk.model.Role;
import com.aliyun.dingtalk.model.User;
import com.aliyun.dingtalk.model.UserRole;
import com.aliyun.dingtalk.service.AddressListService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRoleAddRoleRequest;
import com.dingtalk.api.request.OapiRoleAddrolegroupRequest;
import com.dingtalk.api.request.OapiRoleAddrolesforempsRequest;
import com.dingtalk.api.request.OapiV2DepartmentCreateRequest;
import com.dingtalk.api.request.OapiV2UserCreateRequest;
import com.dingtalk.api.response.OapiRoleAddRoleResponse;
import com.dingtalk.api.response.OapiRoleAddrolegroupResponse;
import com.dingtalk.api.response.OapiRoleAddrolesforempsResponse;
import com.dingtalk.api.response.OapiV2DepartmentCreateResponse;
import com.dingtalk.api.response.OapiV2UserCreateResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class AddressListServiceImpl implements AddressListService {

    @Autowired
    private AppConfig appConfig;

    /**
     * 同步企业部门到钉钉
     *
     * @param deptList
     * @return
     */
    @Override
    public Map syncDeptList(List<Dept> deptList) {

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CREATE_DEPT_URL);

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        // 企业部门ID和钉钉部门ID映射
        final Map<Integer, Long> deptIdAndDingTalkDeptIdMap = new HashMap<>();

        syncDeptList(deptIdAndDingTalkDeptIdMap, client, accessToken, deptList,null);

        return deptIdAndDingTalkDeptIdMap;
    }

    /**
     * 递归调用接口同步所有部门
     * 注意接口调用频率的限制 https://developers.dingtalk.com/document/app/invocation-frequency-limit?spm=ding_open_doc.document.0.0.7db77c8dvB26a6#topic-2021708
     * 创建部门参考文档 https://developers.dingtalk.com/document/app/create-a-department-v2
     *
     * @param deptIdAndDingTalkDeptMap
     * @param client
     * @param accessToken
     * @param deptList
     * @param parentDingTalkDeptId
     */
    private void syncDeptList(Map deptIdAndDingTalkDeptMap, DingTalkClient client, String accessToken, List<Dept> deptList, Long parentDingTalkDeptId) {

        deptList.forEach(dept -> {
            OapiV2DepartmentCreateRequest req = new OapiV2DepartmentCreateRequest();
            req.setParentId(dept.getParentId());
            if (parentDingTalkDeptId != null) {
                req.setParentId(parentDingTalkDeptId);
            }
            req.setName(dept.getName());
//            req.setOuterDept(true);
//            req.setHideDept(true);
//            req.setCreateDeptGroup(true);
//            req.setOrder(10L);
//            req.setSourceIdentifier("HR部门");
//            req.setDeptPermits("3,4,5");
//            req.setUserPermits("100,200");
//            req.setOuterPermitUsers("500,600");
//            req.setOuterPermitDepts("6,7,8");
//            req.setOuterDeptOnlySelf(true);

            try {

                OapiV2DepartmentCreateResponse rsp = client.execute(req, accessToken);
                if (!Objects.isNull(rsp)) {
                    if (rsp.isSuccess()) {
                        OapiV2DepartmentCreateResponse.DeptCreateResponse deptCreateResponse = rsp.getResult();
                        Long dingTalkDeptId = deptCreateResponse.getDeptId();
                        deptIdAndDingTalkDeptMap.put(dept.getId(), dingTalkDeptId);
                        if (!CollectionUtils.isEmpty(dept.getChildren())) {
                            syncDeptList(deptIdAndDingTalkDeptMap, client, accessToken, dept.getChildren(),  dingTalkDeptId);
                        }
                    } else {
                        log.error("sync dept error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                    }
                } else {
                    log.error("sync dept fail!");
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 同步用户信息
     *
     * @param users 用户列表
     * @return
     */
    @Override
    public List<OapiV2UserCreateResponse.UserCreateResponse> syncUsers(List<User> users) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CREATE_USER_URL);

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        // 同步员工信息到钉钉
        List<OapiV2UserCreateResponse.UserCreateResponse> userList = getUserCreateResponseList(users, client, accessToken);
        return userList;
    }

    /**
     * 同步用户信息到钉钉
     * 注意接口调用频率的限制 https://developers.dingtalk.com/document/app/invocation-frequency-limit?spm=ding_open_doc.document.0.0.7db77c8dvB26a6#topic-2021708
     * 创建用户参考文档 https://developers.dingtalk.com/document/app/user-information-creation
     *
     * @param users
     * @param client
     * @param accessToken
     * @return
     */
    private List<OapiV2UserCreateResponse.UserCreateResponse> getUserCreateResponseList(List<User> users, DingTalkClient client, String accessToken) {

        // 钉钉用户信息集合
        List<OapiV2UserCreateResponse.UserCreateResponse> userList = new ArrayList<>();
        users.forEach(user -> {
            OapiV2UserCreateRequest req = new OapiV2UserCreateRequest();
            req.setUserid(user.getUserid());
            req.setName(user.getName());
            req.setMobile(user.getMobile());
            req.setDeptIdList(user.getDeptIdList());
//            req.setHideMobile(false);
//            req.setTelephone("010-8xxxxx6-2345");
//            req.setJobNumber("4");
//            req.setTitle("技术总监");
//            req.setEmail("test@xxx.com");
//            req.setOrgEmail("test@xxx.com");
//            req.setWorkPlace("未来park");
//            req.setRemark("备注备注");
//            req.setExtension("{\"爱好\":\"旅游\"}");
//            req.setSeniorMode(false);
//            req.setHiredDate(1597573616828L);
            try {
                OapiV2UserCreateResponse rsp = client.execute(req, accessToken);
                if (!Objects.isNull(rsp)) {
                    if (rsp.isSuccess()) {
                        OapiV2UserCreateResponse.UserCreateResponse result = rsp.getResult();
                        userList.add(result);
                    } else {
                        log.error("sync user error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                    }
                } else {
                    log.error("sync user fail!");
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }

        });
        return userList;
    }


    /**
     *
     * 同步角色组信息
     *
     * @param roleGroupNameList
     * @return
     */
    @Override
    public Map<String, Long> syncRoleGroupList(List<String> roleGroupNameList) {
        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CREATE_ROLE_GROUP_URL);

        // 同步角色组信息到钉钉
        Map<String, Long> groupNameAndGroupIdMap = getGroupNameAndGroupIdMap(roleGroupNameList, accessToken, client);

        return groupNameAndGroupIdMap;
    }

    /**
     * 同步角色组信息到钉钉
     * 注意接口调用频率的限制 https://developers.dingtalk.com/document/app/invocation-frequency-limit?spm=ding_open_doc.document.0.0.7db77c8dvB26a6#topic-2021708
     * 创建角色组参考文档 https://oapi.dingtalk.com/role/add_role_group
     *
     * @param roleGroupNameList
     * @param accessToken
     * @param client
     * @return
     */
    private Map<String, Long> getGroupNameAndGroupIdMap(List<String> roleGroupNameList, String accessToken, DingTalkClient client) {

        // 角色组名称和角色组ID映射
        Map<String, Long> groupNameAndGroupIdMap = new HashMap<>();
        roleGroupNameList.forEach(roleGroupName -> {

            OapiRoleAddrolegroupRequest req = new OapiRoleAddrolegroupRequest();
            req.setName(roleGroupName);
            try {
                OapiRoleAddrolegroupResponse rsp = client.execute(req, accessToken);
                if (!Objects.isNull(rsp)) {
                    if (rsp.isSuccess()) {
                        Long groupId = rsp.getGroupId();
                        groupNameAndGroupIdMap.put(roleGroupName, groupId);
                    } else {
                        log.error("sync role group error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                    }
                } else {
                    log.error("sync role group fail!");
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }

        });
        return groupNameAndGroupIdMap;
    }


    /**
     * 同步角色信息
     *
     * @param roleList
     * @return
     */
    @Override
    public Map<String, Long> syncRoleList(List<Role> roleList) {
        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CREATE_ROLE_URL);

        // 同步角色信息到钉钉
        Map<String, Long> groupIdAndRoleIdMap = getGroupIdAndRoleIdMap(roleList, accessToken, client);

        return groupIdAndRoleIdMap;
    }


    /**
     * 同步角色信息到钉钉
     * 注意接口调用频率的限制 https://developers.dingtalk.com/document/app/invocation-frequency-limit?spm=ding_open_doc.document.0.0.7db77c8dvB26a6#topic-2021708
     * 创建角色参考文档 https://developers.dingtalk.com/document/app/add-role
     *
     * @param roleList
     * @param accessToken
     * @param client
     * @return
     */
    private Map<String, Long> getGroupIdAndRoleIdMap(List<Role> roleList, String accessToken, DingTalkClient client) {

        // 角色组ID和角色ID映射
        Map<String, Long> roleNameAndRoleIdMap = new HashMap<>();

        roleList.forEach(role -> {
            OapiRoleAddRoleRequest req = new OapiRoleAddRoleRequest();
            req.setRoleName(role.getRoleName());
            req.setGroupId(role.getGroupId());
            try {
                OapiRoleAddRoleResponse rsp = client.execute(req, accessToken);
                if (!Objects.isNull(rsp)) {
                    if (rsp.isSuccess()) {
                        Long roleId = rsp.getRoleId();
                        roleNameAndRoleIdMap.put(role.getRoleName(), roleId);
                    } else {
                        log.error("sync role error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                    }
                } else {
                    log.error("sync role fail!");
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        });
        return roleNameAndRoleIdMap;
    }

    /**
     * 同步用户角色信息
     * 注意接口调用频率的限制 https://developers.dingtalk.com/document/app/invocation-frequency-limit?spm=ding_open_doc.document.0.0.7db77c8dvB26a6#topic-2021708
     * 批量添加用户角色参考文档 https://developers.dingtalk.com/document/app/add-role-information-to-employees-in-batches
     *
     * @param userRole
     * @return
     */
    @Override
    public String syncUserRole(UserRole userRole) {
        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.ADD_USER_ROLE_URL);

        OapiRoleAddrolesforempsRequest req = new OapiRoleAddrolesforempsRequest();
        req.setRoleIds(userRole.getRoleIds());
        req.setUserIds(userRole.getUserIds());
        try {
            OapiRoleAddrolesforempsResponse rsp = client.execute(req, accessToken);
            if (!Objects.isNull(rsp)) {
                if (rsp.isSuccess()) {
                    return "success";
                } else {
                    log.error("sync user role error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                }
            } else {
                log.error("sync user role fail!");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }



}
