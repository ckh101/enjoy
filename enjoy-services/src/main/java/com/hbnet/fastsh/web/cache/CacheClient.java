package com.hbnet.fastsh.web.cache;

import com.danga.MemCached.MemCachedClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hbnet.fastsh.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CacheClient {

	@Autowired
	@Qualifier("mcc")
	MemCachedClient mcc;
	
	@Autowired
	CacheParser3 cacheParser;
	 
	public static long CACHE_TIME = 1000 * 60 * 60 * 4; 
    
	public Date cacheTime(){
        return cacheTime(CACHE_TIME);
    }
    
    public Date cacheTime(long time){
        return new Date(System.currentTimeMillis() + time);
    }
    
	public void setMemCachedClient(MemCachedClient mcc) {
		this.mcc = mcc;
	}

	public Object get(String key) {
		return mcc.get(key);
	}

	public boolean set(String key, Object value) {
		if(value==null)return false;
		return mcc.set(key, value);
	}
	
	public boolean set(String key,String groupKey, Object value) {
		if(value==null)return false;
		setGroup(key,groupKey,null);
        boolean b = isInGroup(key, groupKey);
        if(b){
            mcc.set(key, value);
        }
		return b;        
	}
	
	public boolean delete(String key) {
		return mcc.delete(key);
	}
	
	public boolean deleteGroup(String key) {
		Set<String> keyset = getGroup(key);
		boolean result = false;
		if(keyset!=null && !keyset.isEmpty()){
			for(Iterator<String> it = keyset.iterator();it.hasNext();){
				try {
                    if(result){
                    	mcc.delete(it.next());
                    }else{
                    	result = mcc.delete(it.next());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
			mcc.delete(key);
		}
		return result;
	}

	public long addOrIncr(String key, long count) {
		return mcc.addOrIncr(key, count);
	}
	
	public long incr(String key, long count) {
		return mcc.incr(key, count);
	}
	
	public long decr(String key, long count) {
		return mcc.decr(key, count);
	}
	
	public boolean add(String key , Object value){
		if(value==null)return false;
		return mcc.add(key, value);
	}
	
	public boolean add(String key , Object value, Date expiryAt){
        if(value==null)return false;
        return mcc.add(key, value,expiryAt);
    }

	public long getCounter(String key) {
		return mcc.getCounter(key);
	}

	public boolean storeCounter(String key, long count) {
		return mcc.storeCounter(key, count);
	}

	public boolean set(String key, String value, Date expiryAt) {
		if(Tools.isBlank(value))return false;
		return mcc.set(key, value, expiryAt);
	}

	public boolean set(String key, Object value, Date expiryAt) {
		if(value==null)return false;
		return mcc.set(key, value, expiryAt);
	}
	
	public boolean set(String key,String groupKey, String value, Date expiryAt) {
		if(Tools.isBlank(value))return false;
		setGroup(key,groupKey,expiryAt);
        boolean b = isInGroup(key, groupKey);
        if(b){
            mcc.set(key, value, expiryAt);
        }
		return b;
	}

	public boolean set(String key,String groupKey, Object value, Date expiryAt) {
		if(value==null)return false;
		setGroup(key,groupKey,expiryAt);
        boolean b = isInGroup(key, groupKey);
        if(b){
            mcc.set(key, value, expiryAt);
        }
		return b;
	}
	
	public MemCachedClient getMcc() {
		return mcc;
	}

	public void setMcc(MemCachedClient mcc) {
		this.mcc = mcc;
	}
	
	public void setGroup(String key,String groupKey){
	    setGroup(key,groupKey,null);
    }
	
	public void setGroup(String key,String groupKey, Date expiryAt){
		String keys = (String)mcc.get(groupKey);
		Set<String> keyset = null;
		if(keys!=null){
			keyset = cacheParser.parse(keys, new TypeReference<HashSet<String>>(){});
		}
		if(keyset==null){
			keyset = new HashSet<String>();
		}
		
		keyset.add(key);
		if(expiryAt==null){
			mcc.set(groupKey, cacheParser.unparse(keyset));
		}else{
			mcc.set(groupKey, cacheParser.unparse(keyset),expiryAt);
		}
	}
    
    public boolean isInGroup(String key,String groupKey){
        String keys = (String)mcc.get(groupKey);
        Set<String> keyset = null;
        if(keys!=null){
			keyset = cacheParser.parse(keys, new TypeReference<HashSet<String>>(){});
		}
        if(keyset==null){
			keyset = new HashSet<String>();
		}
        return keyset.contains(key);
    }
	
	public void setGroup(String groupKey,Set<String> keyset){
		String value = null;
		
		if(keyset!=null){
			value = cacheParser.unparse(keyset);
		}
		
		if(Tools.isBlank(value)){
			mcc.delete(groupKey);
		}else{
			mcc.set(groupKey, value);
		}
	
	}
	
	public Set<String> getGroup(String groupKey){
		Set<String> _keyset = null;
		String keys = (String)mcc.get(groupKey);
		if(keys!=null){
			_keyset = cacheParser.parse(keys, new TypeReference<HashSet<String>>(){});
		}
		return _keyset;
	}
	
	public Set<Object> getByGroup(String groupKey){
		Set<Object> set = null;
		Set<String> _keyset = getGroup(groupKey);
		if(_keyset!=null && _keyset.size()>0){
			set = new HashSet<Object>(_keyset.size());
			for(Iterator<String> it = _keyset.iterator();it.hasNext();){
				set.add(mcc.get(it.next()));
			}
		}
		return set;
	}
	
	public Map<String, Object> getMulti(String[] keys) {
		if ( keys == null || keys.length == 0 ) {
			return null;
		}else{
			return mcc.getMulti(keys);
		}
		
	}

	public Object[] getMultiArray(String[] keys) {
		return mcc.getMultiArray(keys);
	}

	public Object[] getMultiArray(String[] keys, Integer[] hashCodes, boolean asString) {
		return mcc.getMultiArray(keys, hashCodes, asString);
	}
	
}
