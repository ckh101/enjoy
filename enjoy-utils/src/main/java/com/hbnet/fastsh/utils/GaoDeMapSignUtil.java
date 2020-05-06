package com.hbnet.fastsh.utils;

import java.util.Map;

public class GaoDeMapSignUtil {
    public static String createSign(Map<String, String> param, String key){
        String str = Tools.sortParamsToStr(param)+key;
        str+="key="+key;
        return Tools.MD5Encode(str,"utf-8");
    }
}
