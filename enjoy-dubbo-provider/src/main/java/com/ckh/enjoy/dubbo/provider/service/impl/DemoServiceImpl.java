package com.ckh.enjoy.dubbo.provider.service.impl;

import com.ckh.enjoy.dubbo.api.service.DemoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Author: kyle_chan
 * @CreateDate: 2020/5/7
 */
@Service(version = "${dubbo.provider.version}")
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "Hello:"+name;
    }
}
