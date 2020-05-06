package com.hbnet.fastsh.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadUtils {
    public static final String IMGUPLOADDOMAIN = "https://img.bazhuay.com/";
    //构造一个带指定Zone对象的配置类
    public static final Configuration cfg = new Configuration(Zone.zone0());

    private static final String accessKey = "8kEgWLWMkyBziTNz7thJKP8SbuIQ-6bdpgzInqCv";
    private static final String secretKey = "XPf0Y203DEJoykMgzFM8WL3qXPWelpEFE7Pwh1vU";
    private static final String bucket = "images";
    public static String getUpLoadToken(){
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }
    public static String getUpLoadToken(String cusbucket){
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(cusbucket);
        return upToken;
    }


    public static String upImgBytesToQiniu(String key, byte[] uploadBytes){
        if(uploadBytes == null) {
            return null;
        }

        UploadManager uploadManager = new UploadManager(cfg);

        String upToken = getUpLoadToken();
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            if(!key.equals(putRet.key)){
                return null;
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("上传到七牛云失败！resp:{}", r.toString(), ex);
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return null;
        }

        String imgurl = IMGUPLOADDOMAIN + key;
        return imgurl;
    }
}
