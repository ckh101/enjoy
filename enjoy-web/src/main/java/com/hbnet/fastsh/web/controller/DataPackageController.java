package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.web.entity.DataPackage;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.DataPackageService;
import com.hbnet.fastsh.web.service.impl.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin/datapackage")
public class DataPackageController {

    @Autowired
    DataPackageService dataPackageService;

    @Autowired
    IndustryService industryService;

    @RequestMapping()
    public String list(DataPackage dataPackage, PageInfo<DataPackage> pageInfo, ModelMap model){
        model.addAttribute("industrys", industryService.getAllChildrenList());
        return "/datapackage/list";
    }

    @RequestMapping("verifyPackage")
    @ResponseBody
    public JSONObject verifyIndustry(String industry) {
        JSONObject result = new JSONObject();
        result.put("valid", !industryService.isExist(industry));
        return result;
    }

    @RequestMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("method","add");
        model.addAttribute("industrys", industryService.getAllChildrenList());
        return "/datapackage/edit";
    }
}
