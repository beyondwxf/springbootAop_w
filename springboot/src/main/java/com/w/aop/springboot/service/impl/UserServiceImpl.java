package com.w.aop.springboot.service.impl;

import com.w.aop.springboot.annotation.OperationLogDetail;
import com.w.aop.springboot.enums.OperationType;
import com.w.aop.springboot.enums.OperationUnit;
import com.w.aop.springboot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @OperationLogDetail(detail = "通过手机号{{{tel}}}获取用户名",level = 3,operationType = OperationType.UNKNOWN,operationUnit = OperationUnit.USER)
    @Override
    public String findUserName(String tel,String currentUserName) {
        System.out.println("tel:" + tel);
        return "zhangsan"+tel;

    }
}
