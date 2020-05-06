package com.ckh.enjoy.security.cache;

import com.danga.MemCached.MemCachedClient;
import com.ckh.enjoy.utils.MemcachedUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
@Slf4j
public class ShiroMcCache<K, V> implements Cache<K, V>
{
    
    private String cacheKeyName = null;
   
    public ShiroMcCache(String cacheKeyName,  MemCachedClient mcc){
    	MemcachedUtils.setClient(mcc);
        this.cacheKeyName = cacheKeyName;
    }
    
    @Override
    public V get(K key) throws CacheException{
    	return (V)MemcachedUtils.get(String.valueOf(key));
    }
    
    @Override
    public V put(K key, V value) throws CacheException{
    	MemcachedUtils.setGroupField(cacheKeyName, String.valueOf(key), (Object)value);
        return value;
    }
    
    @Override
    public V remove(K key) throws CacheException{
    	V value=(V)MemcachedUtils.get(String.valueOf(key));
        MemcachedUtils.delGroupField(cacheKeyName, String.valueOf(key));
        return value;
    }
    
    @Override
    public void clear() throws CacheException{
    	MemcachedUtils.delGroup(cacheKeyName);
    }
    
    @Override
    public int size(){
        //return JedisUtils.getMapLen(cacheKeyName);
    	Set<K> fields=(Set<K>)MemcachedUtils.get(cacheKeyName);
    	if(fields==null){
    		return 0;
    	}else{
    		return fields.size();
    	}
    }
    
    @Override
    public Set<K> keys(){
    	Set<K> fields=(Set<K>)MemcachedUtils.get(cacheKeyName);
    	return fields;
    }
    
    @Override
    public Collection<V> values(){
        
        Collection<V> col=new ArrayList<V>();
        Set<K>fields=(Set<K>)MemcachedUtils.get(cacheKeyName);
        Iterator<K> it=fields.iterator();
        while(it.hasNext()){
        	K k=it.next();
        	V v=(V)MemcachedUtils.get(String.valueOf(k));
        	col.add(v);
        }
        return col;
    }
}