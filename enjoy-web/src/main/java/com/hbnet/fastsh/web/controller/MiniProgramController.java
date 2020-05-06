package com.hbnet.fastsh.web.controller;

import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.web.entity.MiniProgram;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.MiniProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="admin/miniprogram")
public class MiniProgramController {

    @Autowired
    MiniProgramService miniProgramService;

    @RequestMapping()
    public String list(MiniProgram miniProgram, PageInfo<MiniProgram> pageInfo, ModelMap model){
        miniProgramService.page(miniProgram, pageInfo);
        model.addAttribute("page", pageInfo);
        model.addAttribute("miniProgram", miniProgram);
        return "/miniprogram/list";
    }

    @RequestMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("method","add");
        return "/miniprogram/edit";
    }

    @RequestMapping("edit/{mid}")
    public String edit(ModelMap model, @PathVariable Long mid) {
        MiniProgram miniProgram = miniProgramService.findById(mid);
        model.addAttribute("miniProgram",miniProgram);
        model.addAttribute("method", "edit");
        return "/miniprogram/edit";
    }

    @RequestMapping(value="save", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse  save(MiniProgram miniProgram, String method){
        ApiResponse result = new ApiResponse();
        if("edit".equals(method)){
            MiniProgram mp = miniProgramService.findById(miniProgram.getId());
            if(mp != null) {
                mp.setMiniProgramId(miniProgram.getMiniProgramId());
                mp.setMiniProgramName(mp.getMiniProgramName());
                miniProgramService.savaOrUpdate(mp);
            }
            result.setStatus(1);
        }else{
            miniProgramService.savaOrUpdate(miniProgram);
            result.setStatus(1);
        }
        return result;
    }
    @RequestMapping(value = "delete/{mid}")
    @ResponseBody
    public ApiResponse del(@PathVariable Long mid, HttpServletResponse resp){
        ApiResponse result = new ApiResponse();
        try {
            if(mid > 0){
                miniProgramService.delById(mid);
                result.setStatus(1);
                result.setMsg("操作成功");
            }else{
                result.setMsg("缺少ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
