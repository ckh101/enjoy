package com.ckh.enjoy.security.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import com.danga.MemCached.MemCachedClient;

public class ShiroMcCacheManager implements CacheManager{
    // fast lookup by name map
    private static final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    private String cacheKey;
    private MemCachedClient mcc;
    public ShiroMcCacheManager(String cacheKey, MemCachedClient mcc) {
    	this.cacheKey = cacheKey;
    	this.mcc = mcc;
    }
    
    /**
     * 清空缓存
     */
    public static void clear(){
        if (null != caches && caches.size() > 0){
            for (Cache cache : caches.values()){
                cache.clear();
            }
        }
    }
    
    /**
     * 取缓存对象
     *
     * @param name
     * @param <K>
     * @param <V>
     * @return
     * @throws CacheException
     */
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException{
        Cache cache = caches.get(name);
        if (null == cache){
            // create a new cache instance
            cache = new ShiroMcCache<K, V>(this.cacheKey,this.mcc);
            // add it to the cache collection
            caches.put(name, cache);
        }
        return cache;
    }
}
