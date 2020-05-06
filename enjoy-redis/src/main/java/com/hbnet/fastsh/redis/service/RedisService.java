package com.hbnet.fastsh.redis.service;

import com.alibaba.fastjson.JSON;
import com.hbnet.fastsh.redis.consumer.RedisMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
public class RedisService {
   
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(final String key, final Object value, final long liveTime) {
        try {
            if(redisTemplate != null){
                redisTemplate.execute((RedisCallback<Object>) connection -> {
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] bkey = serializer.serialize(key);
                    byte[] bvalue = serializer.serialize(String.valueOf(value));
                    if(connection.exists(bkey)){
                        connection.set(bkey,bvalue);
                    }else{
                        connection.setNX(bkey,bvalue);
                    }
                    if (liveTime > 0) {
                        connection.pExpire(bkey,liveTime);
                    }
                    return null;
                });
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }

    }

    public String get(final String key) {
        try {
            if(redisTemplate != null){
                return redisTemplate.execute(new RedisCallback<String>() {

                    public String doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                        byte[] bkey = serializer.serialize(key);
                        if (connection.exists(bkey)) {
                            return String.valueOf(serializer.deserialize(connection.get(bkey)));
                        }
                        return null;
                    }
                });
            }else{
                log.error("get redis connect error");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
        return null;
    }

    public List<String> getList(final List<String> keyList) {
        try {
            if(redisTemplate != null){
                return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
                    List<String> datas = new ArrayList<>();
                    if(keyList != null && !keyList.isEmpty()){
                        Iterator<String> keys = keyList.iterator();
                        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                        while(keys.hasNext()){
                            byte[] key = serializer.serialize(keys.next());
                            byte[] value = connection.get(key);
                            String data = String.valueOf(redisTemplate.getStringSerializer().deserialize(value));
                            datas.add(data);
                        }
                    }
                    return datas;
                });
            }else{
                log.error("get redis connect error");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
        return null;
    }
    public void delete(final String key) {
        try {
            if(redisTemplate != null){
                redisTemplate.execute((RedisCallback<Object>) connection -> {
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();;
                    byte[] bkey = serializer.serialize(key);
                    if(connection.exists(bkey)){
                        connection.del(bkey);
                    }
                    return null;
                });
            }else{

                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }

    }
    public void hashopsput(String key,String file,Object value){
        try {
            if(redisTemplate != null){
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                hashOper.put(key, file, value);
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
    }

    public void hashopsdel(String key, String file) {
        try {
            if(redisTemplate != null){
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                if(hashOper.hasKey(key, file)){
                    hashOper.delete(key, file);
                }
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
    }

    public String hashopsget(String key, String file) {
        try {
            if(redisTemplate != null){
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                if(hashOper.hasKey(key, file)){
                    return String.valueOf(hashOper.get(key, file));
                }
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
        return null;

    }
    public List<Object> hgetValues(String key) {
        try {
            if(redisTemplate != null){
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                return hashOper.values(key);
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
        return null;
    }
    public List<Object> hgetList(String key, List<String> fields) {
        try {
            if(redisTemplate != null){
                List<Object> list = new ArrayList<Object>();
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                for(String field:fields){
                    if(hashOper.hasKey(key, field)){
                        list.add(hashOper.get(key, field));
                    }
                }
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
        return null;
    }
    public void hputall(String key, Map<String, String> files){
        try {
            if(redisTemplate != null){
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                hashOper.putAll(key, files);
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
    }
    public void hdelall(String key, List<String> files){
        try {
            if(redisTemplate != null){
                HashOperations<String,String,Object> hashOper = redisTemplate.opsForHash();
                for(String file:files){
                    hashOper.delete(key, file);
                }
            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
    }
    public HashOperations<String,String,Object> hashopsget(){
        try {
            if(redisTemplate != null){
                return redisTemplate.opsForHash();

            }else{
                log.error("get redis connect error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis opera error");
        }
        return null;
    }
    public long lset(String key, String value){
        if(redisTemplate != null){
            ListOperations<String,String> listOper = redisTemplate.opsForList();
            return listOper.leftPush(key, value);
        }else{
            log.error("dsp get redis connect error");
            return -1;
        }
    }
    public void ldel(String key,String value){
        if(redisTemplate != null){
            ListOperations<String,String> listOper = redisTemplate.opsForList();
            listOper.remove(key, 0, value);
        }else{
            log.error("dsp get redis connect error");
        }
    }
    public void lbatchDel(String key, List<String> value){
        if(redisTemplate != null){
            ListOperations<String,String> listOper = redisTemplate.opsForList();
            value.forEach(obj->listOper.remove(key, 0, obj));
        }else{
            log.error("dsp get redis connect error");
        }
    }
    public List<String> lget(String key){
        if(redisTemplate != null){
            BoundListOperations<String,String> listOper = redisTemplate.boundListOps(key);
            if(listOper.size() > 0){
                return listOper.range(0, listOper.size()-1);
            }
        }else{
            log.error("dsp get redis connect error");
        }
        return null;
    }

    public List<String> lget(String key, int size){
        if(redisTemplate != null){
            BoundListOperations<String,String> listOper = redisTemplate.boundListOps(key);
            if(listOper.size() > 0){
                return listOper.range(0, size);
            }
        }else{
            log.error("dsp get redis connect error");
        }
        return null;
    }

    /**
     * @param key
     * @param liveTime 单位秒
     * @return
     */
    public Long incr(String key, long liveTime) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        byte[] bkey = keySerializer.serialize(key);

        RedisAtomicLong entityIdCounter = new RedisAtomicLong(new String(bkey, UTF_8), redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }

    public boolean expireAt(String key, Date expireAt) {
        return redisTemplate.expireAt(key, expireAt);
    }

    public boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    public void publish(String channel, RedisMessage msg) {
        redisTemplate.convertAndSend(channel, JSON.toJSONString(msg));
    }

    /**
     * 入队
     * @param key
     * @param value
     * @return
     */
    public Long queuePush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 出队
     * @param key
     * @return
     */
    public String queuePop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public void releaseConnect(){
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    }
}
