package com.hbnet.fastsh.utils;

import com.danga.MemCached.MemCachedClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

;

@Slf4j
public class MemcachedUtils {

    private static MemCachedClient memcachedClient;  
   
    public static void setClient(MemCachedClient client){
    	memcachedClient=client;
    }
  
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */  
    public static boolean set(String key, Object value){  
        return setExp(key, value, 0);  
    }  
  
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    public static boolean set(String key, Object value, int expire){  
        return setExp(key, value, expire);  
    }  
    /**
     * 添加一个健到分组
     * @param groupKey
     * @param fieldKey
     * @param value
     * @return
     */
    public static boolean setGroupField(String groupKey,String fieldKey,Object value){
    	Object groupObj=get(groupKey);
    	if(groupObj==null){
    		groupObj=new HashSet<String>();
    		((HashSet)groupObj).add(resolveKey(fieldKey));
    		set(groupKey, groupObj);
    	}else{
    		((HashSet<String>)groupObj).add(resolveKey(fieldKey));
    		set(groupKey,groupObj);
    	}
    	return set(fieldKey,value);
    }
    /**
     * 获取分组长度
     * @param groupKey
     * @return
     */
    public static int getGroupLen(String groupKey){
    	Set<String> fields=(Set<String>)get(groupKey);
    	if(fields==null){
    		return 0;
    	}else{
    		return fields.size();
    	}
    }
    
    /**
     * 删除一个分组
     * @param groupKey
     * @return
     */
    public static boolean delGroup(String groupKey){
    	Object groupObj=get(groupKey);
    	Set<String> groupFields=(HashSet<String>)groupObj;
    	Iterator<String>it=groupFields.iterator();
    	while(it.hasNext()){
    		String field=it.next();
    		delete(field);
    	}
    	return delete(groupKey);
    }
    
    /**
     * 删除分组中的一个元素
     * @param groupKey
     * @param filedKey
     * @return
     */
    public static boolean delGroupField(String groupKey,String filedKey){
    	Object groupObj=get(groupKey);
    	Set<String> groupFields=(HashSet<String>)groupObj;
    	if(groupFields!=null) {
    		groupFields.remove(resolveKey(filedKey));
        	set(groupKey,groupFields);
    	}
    	return  delete(filedKey);
    }
    /**
     * 过期一定时间
     * @param key
     * @param expire
     * @return
     */
    public static boolean expire(String key,Integer expire){
    	boolean flag = false;  
    	try{  
            flag = memcachedClient.set(resolveKey(key),memcachedClient.get(resolveKey(key)), expire);
        }catch (Exception e){  
        	log.error(e.getMessage());
        }  
        return flag;  
    }
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean setExp(String key, Object value, Integer expire){  
        boolean flag = false;  
        try{  
            flag = memcachedClient.set(resolveKey(key), value, expire);
        }catch (Exception e){ 
        	log.error(e.getMessage());
        }  
        return flag;  
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */  
    public static boolean add(String key, Object value){  
        return addExp(key, value, 0);  
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    public static boolean add(String key, Object value, int expire){  
        return addExp(key, value, expire);  
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean addExp(String key, Object value, int expire){  
        boolean flag = false;  
        try{  
            flag = memcachedClient.add(resolveKey(key), value, expire);  
        }catch (Exception e){  
        	log.error(e.getMessage());
        }  
        return flag;  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */  
    public static boolean replace(String key, Object value){  
        return replaceExp(key, value, 0);  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    public static boolean replace(String key, Object value, int expire){  
        return replaceExp(key, value, expire);  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean replaceExp(String key, Object value, int expire){  
        boolean flag = false;  
        try{  
            flag = memcachedClient.replace(resolveKey(key), value, expire);  
        }catch (Exception e){  
        	log.error(e.getMessage());
        }  
        return flag;  
    }  
  
    /** 
     * get 命令用于检索与之前添加的键值对相关的值。 
     *  
     * @param key 
     *            键 
     * @return 
     */  
    public static Object get(String key){  
        Object obj = null;  
        try{  
            obj = memcachedClient.get(resolveKey(key));  
        }catch (Exception e)  {  
        	log.error(e.getMessage());
        }  
        return obj;  
    }  
  
    /** 
     * 删除 memcached 中的任何现有值。 
     *  
     * @param key 
     *            键 
     * @return 
     */  
    public static boolean delete(String key){  
        return deleteExp(key, 0);  
    }  
  
    /** 
     * 删除 memcached 中的任何现有值。 
     *  
     * @param key 
     *            键 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    public static boolean delete(String key, int expire){  
        return deleteExp(key, expire);  
    }  
  
    /** 
     * 删除 memcached 中的任何现有值。 
     *  
     * @param key 
     *            键 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean deleteExp(String key, int expire){  
        boolean flag = false;  
        try{  
            flag = memcachedClient.delete(resolveKey(key));
        }catch (Exception e)  {  
        	log.error(e.getMessage());
        }  
        return flag;  
    }  
  
    /** 
     * 清理缓存中的所有键/值对 
     *  
     * @return 
     */  
    public static boolean flashAll(){  
        boolean flag = true;  
        try  {  
            memcachedClient.flushAll();  
        }catch (Exception e)  {  
        	flag=false;
        	log.error(e.getMessage());
        }  
        return flag;  
    }  
    
    
    /**
     * 处理key
     * @param key
     * @return
     */
    public static String resolveKey(String key){
    	if(!Tools.isBlank(key)) {
    		key=key.replaceAll(" ", "");
    	}else {
    		key="";
    	}
    	return key;
    }
}
