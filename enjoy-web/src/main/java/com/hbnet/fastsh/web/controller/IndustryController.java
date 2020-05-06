package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Industry;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("admin/industry")
public class IndustryController {

    @Autowired
    IndustryService industryService;


    @RequestMapping()
    public String list(Industry industry, PageInfo<Industry> pageInfo, ModelMap model){
        industryService.page(industry, pageInfo);
        model.addAttribute("page", pageInfo);
        List<Industry> pList = industryService.getAllParentList();
        model.addAttribute("plist", pList);
        model.addAttribute("industry", industry);
        return "/industry/list";
    }

    @RequestMapping("verifyIndustry")
    @ResponseBody
    public JSONObject verifyIndustry(String industry) {
        JSONObject result = new JSONObject();
        result.put("valid", !industryService.isExist(industry));
        return result;
    }

    @RequestMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("method","add");
        List<Industry> pList = industryService.getAllParentList();
        model.addAttribute("plist", pList);
        return "/industry/edit";
    }

    @RequestMapping("edit/{id}")
    public String edit(ModelMap model, @PathVariable Long id) {
        List<Industry> pList = industryService.getAllParentList();
        model.addAttribute("plist", pList);
        model.addAttribute("method", "edit");
        Industry industry = industryService.findById(id);
        model.addAttribute("industry", industry);
        return "/industry/edit";
    }

    @RequestMapping(value="save", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse  save(HttpServletRequest req){
        ApiResponse result = new ApiResponse();
        String method = req.getParameter("method");
        long parentId = Tools.longValue(req.getParameter("parentId"), 0);
        if("edit".equals(method)){
            long id = Tools.longValue(req.getParameter("id"), 0);
            Industry i = industryService.findById(id);
            if(i != null){
                if(i.getParent().getId() != parentId){
                    i.getParent().setId(parentId);

                    industryService.saveOrUpdate(i);
                }
            }
        }else{
            String industry = req.getParameter("industry");
            Industry i = new Industry();
            i.setUpdateTime(new Date());
            i.setCreateTime(new Date());
            i.getParent().setId(parentId);
            i.setIndustry(industry);
            industryService.saveOrUpdate(i);
        }
        result.setStatus(1);
        return result;
    }
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public ApiResponse del(@PathVariable Long id, HttpServletResponse resp){
        ApiResponse result = new ApiResponse();
        industryService.deleteById(id);
        result.setStatus(1);
        return result;
    }
}
