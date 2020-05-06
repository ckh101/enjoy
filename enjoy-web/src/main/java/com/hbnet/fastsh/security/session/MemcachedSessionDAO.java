package com.hbnet.fastsh.security.session;

import com.hbnet.fastsh.utils.MemcachedUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.*;

@Slf4j
public class MemcachedSessionDAO extends AbstractSessionDAO implements CustomSessionDAO {

    public static final String  SESSION_GROUPS = "memcached_shiro_session_group";
    
    public static final String  SESSION_PREFIX = "session_";
    public static final String  SESSION_FIELD_PREFIX = "field_";
    
    @Override
    public void update(Session session) throws UnknownSessionException{
    	if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }

        if (session instanceof ShiroSession) {
            // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
            ShiroSession ss = (ShiroSession) session;
            if (!ss.isChanged()) {
                return;
            }
            //如果没有返回 证明有调用 setAttribute往rmc 放的时候永远设置为false
            ss.setChanged(false);
        }
        if (session == null || session.getId() == null) { return; }
        String key = String.valueOf(SESSION_PREFIX + session.getId());
        int timeoutSeconds = (int) (session.getTimeout() / 1000);
        MemcachedUtils.set(key, session,timeoutSeconds);
        MemcachedUtils.setGroupField(SESSION_GROUPS, SESSION_FIELD_PREFIX+key, session.getId()+"|"+session.getTimeout() + "|" + session.getLastAccessTime().getTime());
    }
    
    /**
     * 清空会话及缓存
     */
    public static void clean(){
    	MemcachedUtils.delGroup(SESSION_GROUPS);
    
    }
    
    @Override
    public void delete(Session session){
        if (session == null || session.getId() == null) { return; }
        String key = String.valueOf(SESSION_PREFIX + session.getId());
        MemcachedUtils.delGroupField(SESSION_GROUPS, SESSION_FIELD_PREFIX+key);
        MemcachedUtils.delete(key);
        
    }
    
    @Override
    public Collection<Session> getActiveSessions(){
        return getActiveSessions(true);
    }
    
    /**
     * 获取活动会话
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave){
        return getActiveSessions(includeLeave, null, null);
    }
    
    /**
     * 获取活动会话
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @param principal 根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession){
    	Set<Session> sessions = new HashSet<Session>();
    	Set<String> fields =(Set<String>)MemcachedUtils.get(SESSION_GROUPS);
    	if(fields==null){
        	return sessions;
        }
    	Iterator<String> it=fields.iterator();
    	while (it.hasNext()){
    		String field=it.next();
    		String value=(String)MemcachedUtils.get(field);
            if (StringUtils.isNotBlank(field)&&StringUtils.isNotBlank(value) ){
                String[] ss = StringUtils.split(value, "|");
                if (ss != null && ss.length == 3){
                    SimpleSession session = new SimpleSession();
                    session.setId(field);
                    session.setTimeout(Long.valueOf(ss[1]));
                    session.setLastAccessTime(new Date(Long.valueOf(ss[2])));
                    try{
                        // 验证SESSION
                        session.validate();
                        boolean isActiveSession = false;
                        // 不包括离线并符合最后访问时间小于等于3分钟条件。
                        if (includeLeave ){
                            isActiveSession = true;
                        }
                        // 过滤掉的SESSION
                        if(filterSession != null && filterSession.getId().equals(session.getId())){
                            isActiveSession = false;
                        }
                        if (isActiveSession){
                            sessions.add(session);
                        }
                    }catch (Exception e2){
                    	// SESSION验证失败
                    	MemcachedUtils.delGroupField(SESSION_GROUPS, field);
                    }
                }else{
                	// 存储的SESSION不符合规则
                	MemcachedUtils.delGroupField(SESSION_GROUPS, field);
                }
            }else if (StringUtils.isNotBlank(field)){
            	// 存储的SESSION无Value
            	MemcachedUtils.delGroupField(SESSION_GROUPS, field);
            }
        }
        log.info("getActiveSessions size: {} ", sessions.size());
    	return sessions;
    }
    
    @Override
    protected Serializable doCreate(Session session){
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.update(session);
        return sessionId;
    }
    
    @Override
    protected Session doReadSession(Serializable sessionId){
        Session session = null;
        try{
            String key = String.valueOf(SESSION_PREFIX + sessionId);
            session = (Session)MemcachedUtils.get(key);
        }catch (Exception e){
            log.error("doReadSession {} {}", sessionId, e);
        }
        return session;
    }
    
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException{
        try{
            return super.readSession(sessionId);
        }catch (UnknownSessionException e){
            return null;
        }
    }

}
