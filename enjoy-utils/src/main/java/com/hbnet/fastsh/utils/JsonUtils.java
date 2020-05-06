/*
 * www.pconline.com.cn - 1997-2010.
 */
package com.hbnet.fastsh.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author xhchen
 */
abstract public class JsonUtils {

    static ObjectMapper mapper = new ObjectMapper();	//对象映射
    static JsonFactory factory = new JsonFactory();		//json工厂
    
    /**
     * 把对象转换成json数据
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * 把json数据转换成对象
     * @param s
     * @param type
     * @return
     */
    @SuppressWarnings("hiding")
	public static <T> T fromJson(String s, Class<T> type) {
        try {
            return mapper.readValue(s, type);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * 获得json指定key的值
     * @param s
     * @param key
     * @return
     */
    public static String getText(String s, String key) {
        JsonParser jp = match(s, key);
        if (jp != null) {
            try {
                return jp.getText();
            } catch (Exception e) {

            } finally {
                try {
                    jp.close();
                } catch (Exception e1) {}
            }
        }
        return null;
    }

    public static int getInt(String s, String key) {
        JsonParser jp = match(s, key);
        if (jp != null) {
            try {
                return Integer.parseInt(jp.getText());
            } catch (Exception e) {

            } finally {
                try {
                    jp.close();
                } catch (Exception e1) {}
            }
        }
        return -100;
    }

    public static JsonParser match(String s, String key) {
        if (s == null || key == null || "".equals(s) || "".equals(key)) {
            return null;
        }
        try {
            JsonParser jp = factory.createJsonParser(s);//JsonParser相当于集合类
            JsonToken t = null;
            while ((t = jp.nextToken()) != null) { //遍历JsonParser对象
                if (!JsonToken.FIELD_NAME.equals(t) || !jp.getCurrentName().equals(key)) {
                    continue;
                }
                jp.nextToken();
                return jp;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
