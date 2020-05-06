package com.ckh.enjoy.security.entity;



import com.ckh.enjoy.utils.SessionUtil;
import com.ckh.enjoy.web.entity.User;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.HashSet;
import java.util.Set;



public class ShiroAuthorizationInfo extends SimpleAuthorizationInfo{
	private static final long serialVersionUID = -112490140602893893L;
	private Set<String> urlPermSet = new HashSet<String>();
	
	
	public Boolean isPermission(String url) {
		User cur = SessionUtil.getCurUser();
		if(cur.getIsRoot() == 1 || "admin".equals(cur.getRole().getFlagStr())){
			return true;
		}else if (urlPermSet == null || urlPermSet.isEmpty() || urlPermSet.size() == 1){
			return true;
		}
		
		return urlPermSet.contains(url);
	}

	public Set<String> getPermissions() {
		return urlPermSet;
	}

	public void addPermission(String perm) {
		if (urlPermSet == null) {
			urlPermSet = new HashSet<String>();
		}
		urlPermSet.add(perm);
	}

	public void addPermissions(Set<String> perms) {
		if (perms == null || perms.isEmpty())
			return;
		urlPermSet.addAll(perms);
	}

	
}
