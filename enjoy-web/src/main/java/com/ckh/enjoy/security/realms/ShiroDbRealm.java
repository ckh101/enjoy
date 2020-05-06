package com.ckh.enjoy.security.realms;


import com.ckh.enjoy.security.entity.ShiroAuthorizationInfo;
import com.ckh.enjoy.utils.SessionUtil;
import com.ckh.enjoy.utils.Tools;
import com.ckh.enjoy.web.entity.Module;
import com.ckh.enjoy.web.entity.Role;
import com.ckh.enjoy.web.entity.User;
import com.ckh.enjoy.web.service.impl.ModuleService;
import com.ckh.enjoy.web.service.impl.RoleService;
import com.ckh.enjoy.web.service.impl.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @description: 基于数据库Realm
 */
@Component
public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
    UserService userService;
	@Autowired
	RoleService roleService;
	
	@Autowired
	ModuleService moduleService;
	
	private static String indexPerm = "/index";
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		return SecurityUtils.getSubject().getSession().getId();
	}
	

	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		ShiroAuthorizationInfo info = (ShiroAuthorizationInfo) getAuthorizationInfo(principals);
		return info.isPermission(permission);
	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		checkNotNull(principals,
				"PrincipalCollection method argument cannot be null.");
		User currentUser = SessionUtil.getCurUser();
		ShiroAuthorizationInfo info = new ShiroAuthorizationInfo();
		Set<String> permission = new HashSet<String>();
		permission.add(indexPerm);
		if(!Tools.isBlank(currentUser.getPermissions())){
			permission.addAll(currentUser.getPermissions());
		}
		info.addPermissions(permission);
		return info;
	}
	
	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		String username = userToken.getUsername();
		checkNotNull(username, "Null username are not allowed by this realm.");
		User user = userService.findByAccount(username);
        Module module = new Module();
        module.setId(1L);
		if(user.getIsRoot() != 1){
			Role role = user.getRole();
            List<Module> permissions = role.getModules();
            Set<String> pers = new HashSet<String>();
            List<Module> parent = permissions.stream().filter(m->{pers.add(m.getUrl());return m.getLevel() == 1;}).collect(Collectors.toList());
            Map<Long, Module> childrenMap = permissions.stream().filter(m->m.getLevel()==2).collect(Collectors.toMap(Module::getId, Function.identity(), (key1, key2) -> key2));
            parent.forEach(p->{
                List<Module> children = p.getChildren();
                if(!Tools.isBlank(children)){
                    Iterator<Module> it = children.iterator();
                    while(it.hasNext()){
                        Module m = it.next();
                        if(!childrenMap.containsKey(m.getId())){
                            it.remove();
                        }
                    }
                }
            });
            module.setChildren(parent);
            user.setPermissions(pers);
		}else{
			moduleService.getTree(0, 3, module);
		}
        user.setModtree(module);
		checkNotNull(user, "No account found for user [" + username + "]");
		SessionUtil.setCurUser(user);
		return new SimpleAuthenticationInfo(username, DigestUtils.md5Hex(user.getPhone()).toCharArray(), getName());
	}
	private void checkNotNull(Object reference, String message) {
		if (reference == null) {
			throw new AuthenticationException(message);
		}
	}
	
}
