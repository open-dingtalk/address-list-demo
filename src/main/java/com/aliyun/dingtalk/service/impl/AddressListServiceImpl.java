package com.aliyun.dingtalk.service.impl;

import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.model.Dept;
import com.aliyun.dingtalk.service.AddressListService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentCreateRequest;
import com.dingtalk.api.response.OapiV2DepartmentCreateResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        sync(deptIdAndDingTalkDeptIdMap, client, accessToken, deptList,null);

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
    private void sync(Map deptIdAndDingTalkDeptMap, DingTalkClient client, String accessToken, List<Dept> deptList,  Long parentDingTalkDeptId) {

        deptList.forEach(dept -> {
            OapiV2DepartmentCreateRequest req = new OapiV2DepartmentCreateRequest();
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
                            sync(deptIdAndDingTalkDeptMap, client, accessToken, dept.getChildren(),  dingTalkDeptId);
                        }
                    }
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        });
    }

}
