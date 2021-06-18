package com.aliyun.dingtalk.service;

import com.aliyun.dingtalk.model.Dept;

import java.util.List;
import java.util.Map;

public interface AddressListService {
    Map syncDeptList(List<Dept> deptList);
}
