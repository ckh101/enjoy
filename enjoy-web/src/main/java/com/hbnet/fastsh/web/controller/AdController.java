package com.hbnet.fastsh.web.controller;


import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import com.hbnet.fastsh.web.annotations.AdRequestAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("admin/ad")
@Controller
public class AdController {

    @Autowired
    AdvertiserService advertiserService;


    @RequestMapping("adIndex/{aId}")
    @AdRequestAuth
    public String adIndex(){
        return "adIndex";
    }

    @RequestMapping("autherror")
    public String autherror(){
        return "adAutherror";
    }

}
