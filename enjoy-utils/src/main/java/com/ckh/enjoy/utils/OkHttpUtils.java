package com.ckh.enjoy.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";

    private static volatile OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private static String PARAM_SPLIT_FIRST = "?";
    private static String PARAM_SPLIT_VAL = "=";
    private static String PARAM_SPLIT = "&";
    private static String DEFAULT_ENCODE = "UTF-8";
    private OkHttpUtils(){

        ConnectionPool pool = new ConnectionPool(200, 5, TimeUnit.MINUTES);
        //https
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLSocketFactory sslSocketFactory =  null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {

        }

        System.setProperty ("jsse.enableSNIExtension", "false");

        //构建OkHttpClient
        mOkHttpClient = new OkHttpClient.Builder()
                .connectionPool(pool)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory,trustManager)
                .hostnameVerifier(DO_NOT_VERIFY)
                .addInterceptor(new HttpLoggingInterceptor(new HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

    }
    /**
     * 类的初始化，使用单例模式
     * @return
     */
    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    private okhttp3.Request.Builder createBuilder(String url, Map<String, String> headers) {
        okhttp3.Request.Builder builder = new Request.Builder().url(url);
        if (null == headers) {
            return builder;
        }

        for (Map.Entry<String, String> en : headers.entrySet()) {
            builder.addHeader(en.getKey(), en.getValue());
        }
        return builder;
    }

    private String buildUrl(String url, Map<String, String> params) {
        if (null == params) {
            return url;
        }
        StringBuilder sb = new StringBuilder();

        try {
            for (Map.Entry<String, String> en : params.entrySet()) {
                if (!Tools.isBlank(en.getKey()) && !Tools.isBlank(en.getValue())) {
                    sb.append(PARAM_SPLIT);
                    sb.append(URLEncoder.encode(en.getKey(), DEFAULT_ENCODE));
                    sb.append(PARAM_SPLIT_VAL);
                    sb.append(URLEncoder.encode(en.getValue(), DEFAULT_ENCODE));
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("url=" + url, e);
        }
        if (sb.length() > 0) {
            if (!url.contains(PARAM_SPLIT_FIRST)) {
                url = url + PARAM_SPLIT_FIRST;
                url = url + sb.substring(1);
            }else{
                url = url+sb.toString();
            }
        }
        return url;
    }

    private okhttp3.Request.Builder createGetBuilder(String url, Map<String, String> headers, Map<String, String> params)
    {
        String realUrl = url;
        if (null != params){
            realUrl = buildUrl(url, params);
        }

        okhttp3.Request.Builder requestBuilder = createBuilder(realUrl, headers);
        return requestBuilder;
    }

    private okhttp3.Request.Builder createPostBuilder(String url, Map<String, String> headers, Map<String, String> params){

        FormBody.Builder formBuilder = new FormBody.Builder();
        if (null != params){
            for (Map.Entry<String, String> en : params.entrySet()) {
                formBuilder.add(en.getKey(), en.getValue());
            }
        }
        okhttp3.Request.Builder requestBuilder = createBuilder(url, headers).post(formBuilder.build());
        return requestBuilder;
    }

    private okhttp3.Request.Builder createPostJsonBuilder(String url, Map<String, String> headers, String json){

        RequestBody body = FormBody.create(MediaType.parse("application/json"), json);;
        Request.Builder requestBuilder = (new Request.Builder()).post(body).url(url);
        if(headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while(iteratorHeader.hasNext()) {
                String key = (String)iteratorHeader.next();
                requestBuilder.addHeader(key, (String)headers.get(key));
            }
        }
        return requestBuilder;
    }

    private okhttp3.Request.Builder createPostTextBuilder(String url, Map<String, String> headers,MediaType mediaType, String json){

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);;
        Request.Builder requestBuilder = (new Request.Builder()).url(url);
        if(headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while(iteratorHeader.hasNext()) {
                String key = (String)iteratorHeader.next();
                requestBuilder.addHeader(key, (String)headers.get(key));
            }
        }
        return requestBuilder;
    }

    private okhttp3.Request.Builder createUploadBuilder(String urlStore, byte[] fileBytes, String fileName,Map<String, String> headers,MediaType mediaType, String fileType, Map<String, String> params){
        RequestBody fileBody = RequestBody.create(mediaType , fileBytes);
        okhttp3.MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("desc" , fileName)
                .addFormDataPart(fileType , fileName, fileBody);
        if (null != params){
            for (Map.Entry<String, String> en : params.entrySet()){
                mBodyBuilder.addFormDataPart(en.getKey(), en.getValue());
            }
        }
        okhttp3.Request.Builder requestBuilder = createBuilder(urlStore, headers);
        requestBuilder.post(mBodyBuilder.build());
        return requestBuilder;
    }
    /*
       同步请求
    */
    private String doSync(okhttp3.Request.Builder requestBuilder){
        try {
            String response = mOkHttpClient.newCall(requestBuilder.build()).execute().body().string();
            return response;
        } catch (IOException e) {

        }
        return null;

    }


    public String get(String url, Map<String, String> headers, Map<String, String> params){
        return doSync(createGetBuilder(url,headers,params));
    }

    public String get(String url, Map<String, String> params){
        return doSync(createGetBuilder(url,null,params));
    }


    public String post(String url, Map<String, String> headers, Map<String, String> params){
        return doSync(createPostBuilder(url,headers,params));
    }

    public String post(String url, Map<String, String> params){
        return doSync(createPostBuilder(url, null, params));
    }

    public String postJson(String url, String json){
        return doSync(createPostJsonBuilder(url, null, json));
    }

    public String postText(String url, String mediaType, String text){
        return doSync(createPostTextBuilder(url, null, MediaType.parse(mediaType),text));
    }

    public String uploadFile(String url, byte[] fileBytes, String fileName, String fileType, Map<String, String> params){
        return doSync(createUploadBuilder(url,fileBytes,fileName,null,MediaType.parse("application/octet-stream"), fileType, params));
    }

    public byte[] getRemoteImage(String url){
        try {
            return mOkHttpClient.newCall(createGetBuilder(url,null,null).build()).execute().body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

