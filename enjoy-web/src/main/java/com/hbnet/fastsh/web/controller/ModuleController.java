package com.hbnet.fastsh.web.controller;

import com.hbnet.fastsh.constants.WebConstants;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.web.entity.Module;
import com.hbnet.fastsh.web.entity.Role;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.ModuleService;
import com.hbnet.fastsh.web.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
 * @description: 模块控制器
 */  
@Controller
@RequestMapping("admin/module")
public class ModuleController {
	
	@Autowired
	ModuleService moduleService;

	@Autowired
    RoleService roleService;


	@RequestMapping()
	public String list(Module mod, PageInfo<Module> pageInfo, HttpServletRequest req){
		

		List<Module> list = moduleService.getlist(mod);
		req.setAttribute("data", list);
		
		req.setAttribute("model", mod);
		return WebConstants.MODULE_LIST;
	}
	
	@RequestMapping("add")
	public String add(ModelMap model) {
		List<Module> list = moduleService.getListByLevelLt(3);
		model.addAttribute("mlist", list);
		model.addAttribute("method","add");
		return "/module/edit";
	}
	
	@RequestMapping("edit/{mid}")
	public String edit(ModelMap model, @PathVariable Long mid) {
		Module module = moduleService.findById(mid);
		List<Module> list = moduleService.getListByLevelLt(3);
		model.addAttribute("module", module);
		model.addAttribute("mlist", list);
		model.addAttribute("method", "edit");
		return "/module/edit";
	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse save(Module module, HttpServletRequest req){
			ApiResponse result = new ApiResponse();
			String method = req.getParameter("method");
			moduleService.saveOrUpdate(module);
			User user = SessionUtil.getCurUser();
			Module m = new Module();
			m.setId(1L);
			if(user.getIsRoot() == 1){
				moduleService.getTree(0, 3, m);
				user.setModtree(m);
				SessionUtil.setCurUser(user);
			}
			result.setStatus(1);
			return result;
	}
	@RequestMapping(value = "delete/{mid}")
	@ResponseBody
	public ApiResponse del(@PathVariable Long mid, HttpServletResponse resp){
		ApiResponse result = new ApiResponse();
		try {
			if(mid > 0){
				moduleService.deleteById(mid);
				result.setMsg("操作成功");
				User user = SessionUtil.getCurUser();
				Module m = new Module();
				m.setId(1L);
				if(user.getIsRoot() == 1){
					moduleService.getTree(0, 3, m);
					user.setModtree(m);
					SessionUtil.setCurUser(user);
				}
			}else{
				result.setMsg("缺少ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setStatus(1);
		return result;
	}
	@RequestMapping(value = WebConstants.MODULE_TREE)
	@ResponseBody
	public Object getTree(long rid, HttpServletRequest req){
		StringBuffer mids = new StringBuffer(","); 
        Role role = roleService.findById(rid);
		List<Module> rms = role.getModules();
		for(Module rmb:rms){
			mids.append(rmb.getId()).append(",");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("mids", mids.toString());
		
		return result;
	}
}
