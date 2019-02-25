package com.w.aop.springboot.service;

public interface UserService {

    /**
     * 获取用户信息
     * @param tel
     * @return
     */
    String findUserName(String tel,String currentUserName );

}
