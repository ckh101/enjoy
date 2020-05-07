package com.ckh.enjoy.dubbo.consumer.controller;

import com.ckh.enjoy.dubbo.api.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: kyle_chan
 * @CreateDate: 2020/5/7
 */
@RestController
public class DemoController {
    @Reference(version = "${dubbo.provider.version}")
    DemoService demoService;

    @GetMapping("sayHello/{name}")
    public String sayHello(@PathVariable("name") String name){
        return demoService.sayHello(name);
    }
}
