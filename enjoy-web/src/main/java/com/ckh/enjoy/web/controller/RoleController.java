package com.ckh.enjoy.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.ckh.enjoy.constants.WebConstants;
import com.ckh.enjoy.utils.ApiResponse;
import com.ckh.enjoy.utils.JsonUtils;
import com.ckh.enjoy.utils.Tools;
import com.ckh.enjoy.web.entity.Module;
import com.ckh.enjoy.web.entity.Role;
import com.ckh.enjoy.web.service.base.PageInfo;
import com.ckh.enjoy.web.service.impl.ModuleService;
import com.ckh.enjoy.web.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
 * @description: 模块控制器
 */  
@Controller
@RequestMapping("admin/role")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	ModuleService moduleService;

	@RequestMapping()
	public String list(Role role, PageInfo<Role> pageInfo, HttpServletRequest req){
		roleService.page(role, pageInfo);
		req.setAttribute("page", pageInfo);
		req.setAttribute("role", role);
		Module tree = new Module();
		tree.setId(1L);
		moduleService.getTree(0, 3, tree);
		String jsontree = JsonUtils.toJson(tree.getChildren()).replaceAll("mname", "text");
		req.setAttribute("jsontree", jsontree);
		return WebConstants.ROLE_LIST;
	}
	
	@RequestMapping("add")
	public String add(ModelMap model) {
		Module tree = new Module();
		tree.setId(1L);
		tree.setMname("root");
		moduleService.getTree(0, 3, tree);
		List<JSONObject> treeData = new ArrayList<JSONObject>();
		moduleService.toZtreeData(tree,treeData,null);
		model.addAttribute("treeData", treeData);
		model.addAttribute("method","add");
		return "/role/edit";
	}
	
	@RequestMapping("edit/{rid}")
	public String edit(ModelMap model, @PathVariable Long rid) {
		Role role = roleService.findById(rid);
		Module tree = new Module();
		tree.setId(1L);
		tree.setMname("root");
		moduleService.getTree(0, 3, tree);
		List<JSONObject> treeData = new ArrayList<JSONObject>();

		List<Module> rms = role.getModules();
		List<Long> ms = new ArrayList<>();
		if(rms != null) {
			for(Module rmd:rms) {
				ms.add(rmd.getId());
			}
		}
		moduleService.toZtreeData(tree,treeData,ms);
		
		model.addAttribute("treeData", treeData);
		model.addAttribute("method", "edit");
		model.addAttribute("role", role);
		return "/role/edit";
	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse  save(Role role, HttpServletRequest req){
			ApiResponse result = new ApiResponse();
			String method = req.getParameter("method");
			String mids = req.getParameter("mids");
			long rid = 0;
			if("admin".equals(role.getFlagStr())) {
				result.setStatus(0);
				result.setMsg("标识不能为admin");
				return result;
			}
            List<Module> rms = new ArrayList<>();
            String[] midArray = mids.split(",");
            for(String m:midArray){
                if(Tools.isBlank(m)){
                    continue;
                }
                Module rmb = new Module();
                rmb.setId(Long.parseLong(m));
                rms.add(rmb);
            }
            role.setModules(rms);
			if("edit".equals(method)){
			    role.setCreateTime(new Date());
				roleService.saveOrUpdate(role);
			}else{
				Role r = roleService.getRoleByRname(role.getRoleName());
				if(r!=null){
					result.setStatus(0);
					result.setMsg("角色存在");
					return result;
				}
				roleService.saveOrUpdate(role).getId();
			}
			result.setStatus(1);
			return result;
	}
	@RequestMapping(value = "delete/{rid}")
	@ResponseBody
	public ApiResponse del(@PathVariable Long rid, HttpServletResponse resp){
		ApiResponse result = new ApiResponse();
		try {
			if(rid > 0){
				roleService.deletById(rid);
				result.setMsg("操作成功");
			}else{
				result.setMsg("缺少ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setStatus(1);
		return result;
	}
}
