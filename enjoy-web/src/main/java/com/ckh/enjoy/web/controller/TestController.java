package com.ckh.enjoy.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.ckh.enjoy.web.entity.Test;
import com.ckh.enjoy.web.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    TestService testService;
    @RequestMapping("test")
    public JSONObject test(){
        JSONObject json = new JSONObject();
        Test test = new Test();
        test.setName("test");
        json.put("test", testService.save(test));
        return  json;
    }
}
