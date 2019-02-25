package com.w.aop.springboot.controller;

import com.w.aop.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/findUserNameByTel")
    public  String findUserNameByTel(@RequestParam("tel") String  tel,String currentUserName) {
//        return  "ssssssssssssss";
        return  userService.findUserName(tel,currentUserName);
    }
}
