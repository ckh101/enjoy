package com.hbnet.fastsh.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WxAdApiUtil {
    private static final String AGENCYID = "spidc234763971";
    private static final String APIKEY = "XLqCLB4qpwdhbyaC5MhyzlViHlYs0iZ0EtTRcB/sqVS8DEltPbjRsMfEJ3pfSDW4";
    private static final String VERSION = "v1.0";
    private static final String API_URL = "https://api.weixin.qq.com/marketing/%s/%s?access_token=%s&agency_token=%s&version=%s";

    private static String genAgencyToken() {
        long time = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        String rand = Tools.getRandomNumCode(20);
        String sign = Tools.md5(AGENCYID + time + rand + APIKEY);
        String tokenStr = AGENCYID + "," + time + "," + rand + "," + sign;
        return Base64.getEncoder().encodeToString(tokenStr.getBytes(StandardCharsets.UTF_8));
    }

    private static String getApi(String resource, String action, String accessToken) {
        return String.format(API_URL, resource, action, accessToken, genAgencyToken(), VERSION);
    }

    private static JSONObject get(String resource, String action, String accessToken, Map<String, String> params) {
        String url = getApi(resource, action, accessToken);
        String result = OkHttpUtils.getInstance().get(url, params);
        return Tools.isBlank(result)?null:JSONObject.parseObject(result);
    }
    private static JSONObject post(String resource, String action, String accessToken, Map<String, String> params) {
        String url = getApi(resource, action, accessToken);
        String result = OkHttpUtils.getInstance().post(url,null, params);
        return Tools.isBlank(result)?null:JSONObject.parseObject(result);
    }

    public static JSONObject addAdvertiser(String accessToken, Map<String,String> params){
        return post("advertiser", "add", accessToken, params);
    }

    public static JSONObject updateAdvertiser(String accessToken, Map<String,String> params){
        return post("advertiser", "update", accessToken, params);
    }

    public static JSONObject getAdvertiser(String accessToken, Map<String, String> params){
        return get("advertiser", "get", accessToken, params);
    }

    public static JSONObject addQualifications(String accessToken, Map<String,String> params){
        return post("qualifications","add", accessToken, params);
    }

    public static JSONObject getQualifications(String accessToken, Map<String, String> params){
        return get("qualifications","get", accessToken, params);
    }

    public static JSONObject delQualifications(String accessToken, Map<String,String> params){
        return post("qualifications","delete", accessToken, params);
    }

    public static JSONObject addSpEntrustment(String accessToken, Map<String,String> params){
        return post("sp_entrustment","add", accessToken, params);
    }

    public static JSONObject getSpEntrustment(String accessToken, Map<String,String> params){
        return get("sp_entrustment","get", accessToken, params);
    }

    public static JSONObject addFundTransfer(String accessToken, Map<String,String> params){
        return post("fund_transfer", "add", accessToken, params);
    }

    public static JSONObject getFunds(String accessToken, Map<String,String> params){
        return get("funds", "get", accessToken, params);
    }

    public static JSONObject getFundStatementsDetailed(String accessToken, Map<String,String> params){
        return get("fund_statements_detailed", "get", accessToken, params);
    }

    public static JSONObject addImages(String accessToken, byte[] file, String signature){
        String url = getApi("images", "add", accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("signature", signature);
        String result = OkHttpUtils.getInstance().uploadFile(url,file,signature,"file", params);
        return Tools.isBlank(result)?null:JSONObject.parseObject(result);
    }

    public static JSONObject getImages(String accessToken, Map<String, String> params){
        return get("images", "get", accessToken, params);
    }
    public static JSONObject addCampaigns(String accessToken, Map<String,String> params){
        return post("campaigns","add",accessToken,params);
    }

    public static JSONObject updateCampaigns(String accessToken, Map<String,String> params){
        return post("campaigns","update",accessToken,params);
    }

    public static JSONObject getCampaigns(String accessToken, Map<String, String> params){
        return get("campaigns","get",accessToken,params);
    }

    public static JSONObject addAdGroups(String accessToken, Map<String,String> params){
        return post("adgroups","add",accessToken, params);
    }

    public static JSONObject updateAdGroups(String accessToken, Map<String,String> params){
        return post("adgroups", "add", accessToken, params);
    }

    public static JSONObject getAdGroups(String accessToken, Map<String, String> params){
        return get("adgroups", "get", accessToken, params);
    }

    public static JSONObject delAdGroups(String accessToken, Map<String,String> params){
        return post("adgroups","delete", accessToken, params);
    }
    public static JSONObject addAdCreatives(String accessToken, Map<String,String> params){
        return post("adcreatives","add", accessToken, params);
    }

    public static JSONObject updateAdCreatives(String accessToken, Map<String,String> params){
        return post("adcreatives","update", accessToken, params);
    }

    public static JSONObject getAdCreatives(String accessToken, Map<String, String> params){
        return get("adcreatives","get", accessToken, params);
    }

    public static JSONObject addAds(String accessToken, Map<String,String> params){
        return post("ads", "add", accessToken, params);
    }

    public static JSONObject updateAds(String accessToken, Map<String,String> params){
        return post("ads", "update", accessToken, params);
    }

    public static JSONObject getAds(String accessToken, Map<String, String> params){
        return get("ads","get", accessToken, params);
    }

    public static JSONObject delAds(String accessToken, Map<String,String> params){
        return post("ads", "delete", accessToken, params);
    }

    public static JSONObject getDailyReport(String accessToken, Map<String, String> params){
        return get("daily_reports","get",accessToken, params);
    }


    public static JSONObject getRealTimeCost(String accessToken, Map<String, String> params){
        return get("realtime_cost", "get", accessToken, params);
    }

    public static JSONObject addCustomAudiences(String accessToken, Map<String,String> params){
        return post("custom_audiences","add", accessToken, params);
    }


    public static JSONObject updateCustomAudiences(String accessToken, Map<String,String> params){
        return post("custom_audiences","update", accessToken, params);
    }

    public static JSONObject getCustomAudiences(String accessToken, Map<String, String> params){
        return get("custom_audiences","get", accessToken, params);
    }


    public static JSONObject addCustomAudienceFiles(String accessToken, byte[] file, String fileName, Map<String, String> params) throws IOException {
        String url = getApi("custom_audience_files", "add", accessToken);
        String result = OkHttpUtils.getInstance().uploadFile(url, file, fileName, "file", params);
        return Tools.isBlank(result)?null:JSONObject.parseObject(result);
    }

    public static JSONObject getCustomAudienceFiles(String accessToken, Map<String, String> params){
        return get("custom_audience_files","get", accessToken, params);
    }

    public static JSONObject getEstimation(String accessToken, Map<String,String> params){
        return get("estimation","get", accessToken, params);
    }

    public static JSONObject addAsynTasks(String accessToken, Map<String, String> params){
        return post("async_tasks", "add", accessToken, params);
    }

    public static JSONObject getAsynTasks(String accessToken, Map<String, String> params){
        return get("async_tasks", "get", accessToken, params);
    }

    public static byte[] getAsynTasksFiles(String accessToken, Map<String, String> params){
       String url = getApi("async_task_files", "get", accessToken);
        try {
            String result = OkHttpUtils.getInstance().get(url, params);
            return result != null?result.getBytes("UTF-8"):null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getWeChatAdLeads(String accessToken, Map<String, String> params){    params.put("access_token", accessToken);
        params.put("version", "v1.0");
        String result = OkHttpUtils.getInstance().get("https://api.weixin.qq.com/marketing/wechat_ad_leads/get", params);
        return Tools.isBlank(result)?null:JSONObject.parseObject(result);

    }

    public static void main(String[] args){
        String accessToken = "21_nkKrYDxbhdIluE4ABjq4SkOSd4-Io8fpejMcaRzxeOaDgTM8_F8E5kY-pKsngCg_NaUAWdLCZZLfhQmsXdBvhwrq8PdWpS0W-3DjLgD1z_uilCDB1gi_JY-Ow4b28v8g6oGVHZe_zMLHiB1fLFVeAGDDXN";
        /*获取资金流水
        System.out.println(getFunds("19_YDOovq2jWwJYvP10ZeTKXWZB7ILQBy5Yn38kJgmmRD5UugTbe5CrVGerzAv7S6EGOD54UpIyVwpQ8YCD33MNjb7em2cUQZFM8hYATBxzDQtVrZmku8FeaIEyLbDGbDnp9vqI5jRs-7uYLz2uECYjAEDZXT",null));*/
        /*创建计划*/
        /*Map<String, String> params = new HashMap<>();
        params.put("campaign_name", "3.11-cpm-手表1-底部-2折-朋友圈");
        params.put("campaign_type", CampaignType.CAMPAIGN_TYPE_WECHAT_MOMENTS.getValue());
        params.put("product_type", ProductType.PRODUCT_TYPE_LINK_WECHAT.getValue());
        //params.put("daily_budget", "5000");
        System.out.println(addCampaigns(accessToken, params));*/
        /*更新计划
        Map<String, String> params = new HashMap<>();
        params.put("campaign_id", "37975151");
        params.put("campaign_name", "3.7-cpc-暖杯垫原价-定向人群");
        params.put("configured_status", ConfiguredStatus.AD_STATUS_SUSPEND.getValue());
        //params.put("daily_budget", "5000");
        System.out.println(updateCampaigns("19_UV_Mz8RhONfpfi8t44VG7gchcZvITV4MOJbnQtv7P1Wj1nzGA4yYuXvKB1e_CUfebAEEqjcINc3yYSPj_QfX_ygk-8lOZV83TZ0eFPtxMpGlP0wdc-UkToNQ1rIxSwbgQOubxQoJ9DgFuqkrNNZcAEDTTF", params));*/
        /*获取计划
        Map<String, Object> params = new HashMap<>();
        System.out.println(getCampaigns("19_3lGixIJTXzm3vmiPJdnqux3VJCNIcDNEGFKdBlcyMMigklvDhlIsfj10y-Dg8TPqzHOfD1vMBovDCOGAP0lNCwW9-fFirQU_hDvXzagocrxRw-PDGEAeFrVJt5rd0gkrn_JhidAarFKnJlF2CJVgALDOVH", params));*/


        /*创建广告组
        Map<String, String> params = new HashMap<>();
        params.put("campaign_id", "38357707");
        params.put("adgroup_name", "3.11-cpc-手表1-底部-2折-朋友圈");
        params.put("site_set", "[\"SITE_SET_WECHAT\"]");
        params.put("product_type", ProductType.PRODUCT_TYPE_LINK_WECHAT.getValue());
        JSONObject targeting = new JSONObject();
        targeting.put("age", new String[]{"30~60"});
        targeting.put("gender",new String[]{"MALE"});
        targeting.put("user_paying_type", new String[]{"已有电商付费用户"});
        JSONObject geo = new JSONObject();
        geo.put("location_types", new String[]{"LIVE_IN"});
        geo.put("regions", new String[]{"445200"});
        targeting.put("geo_location", geo);
        targeting.put("network_type", new String[]{"WIFI", "NET_4G"});
        targeting.put("user_os", new String[]{"IOS", "ANDROID"});

        params.put("targeting", targeting.toJSONString());
        params.put("daily_budget","100000");
        params.put("optimization_goal", OptimizationGoal.OPTIMIZATIONGOAL_IMPRESSION.getValue());
        params.put("billing_event", BillingEvent.BILLINGEVENT_IMPRESSION.getValue());
        params.put("bid_amount", "3000");
        params.put("begin_date", "2019-03-12");
        params.put("end_date", "2019-03-13");
        System.out.println(addAdGroups(accessToken, params));*/
        /*查询广告组
        Map<String, Object> params = new HashMap<>();
        System.out.println(getAdGroups("19_3lGixIJTXzm3vmiPJdnqux3VJCNIcDNEGFKdBlcyMMigklvDhlIsfj10y-Dg8TPqzHOfD1vMBovDCOGAP0lNCwW9-fFirQU_hDvXzagocrxRw-PDGEAeFrVJt5rd0gkrn_JhidAarFKnJlF2CJVgALDOVH", params));*/
        /*创建广告创意*/
        /*Map<String, String> params = new HashMap<>();
        params.put("campaign_id", "38265144");
        params.put("adcreative_name", "3.11-cpc-手表1-底部-2折-朋友圈");
        params.put("adcreative_template_id", "263");
        Map<String, String> ae = new HashMap<String, String>();
        ae.put("image_list","[\"9010659:8b3e3f563119623a8a91b2801df58b88\"]");
        ae.put("title","手表大甩卖");
        params.put("adcreative_elements", JsonUtils.toJson(ae));
        params.put("site_set","[\"SITE_SET_WECHAT\"]");
        params.put("product_type",ProductType.PRODUCT_TYPE_ECOMMERCE.getValue());
        params.put("destination_url","https://test2.akeyn.com");
        System.out.println(addAdCreatives(accessToken, params));*/
        /*获取广告创意*/
        /*Map<String, Object> params = new HashMap<>();
        System.out.println(getAdCreatives(accessToken, params));*/
        /*创建广告
        Map<String, String> params = new HashMap<>();
        params.put("adgroup_id","104152608");
        params.put("adcreative_id","12905048");
        params.put("ad_name","3.11-cpc-手表1-底部-2折");
        System.out.println(addAds("19_3lGixIJTXzm3vmiPJdnqux3VJCNIcDNEGFKdBlcyMMigklvDhlIsfj10y-Dg8TPqzHOfD1vMBovDCOGAP0lNCwW9-fFirQU_hDvXzagocrxRw-PDGEAeFrVJt5rd0gkrn_JhidAarFKnJlF2CJVgALDOVH",params));*/
        /*获取广告
        Map<String, Object> params = new HashMap<String, Object>();
        System.out.println(getAds(accessToken,params));*/
        /*Map<String, Object> params = new HashMap<>();
        params.put("transaction_type","TRANSACTION_EXPENSE");
        JSONObject date_range = new JSONObject();
        date_range.put("start_date","2019-05-11");
        date_range.put("end_date","2019-05-14");
        params.put("date_range", date_range);
        System.out.println(getFundStatementsDetailed(accessToken, params));*/
        /*Map<String, String> params = new HashMap<>();
        params.put("task_name", "cailide20190513");
        params.put("task_type", "TASK_TYPE_ADGROUP_DAILY_REPORT");
        JSONObject task_spec = new JSONObject();
        JSONObject task_type_adgroup_daily_report_spec = new JSONObject();
        task_type_adgroup_daily_report_spec.put("date", "2019-05-13");
        task_spec.put("task_type_adgroup_daily_report_spec", task_type_adgroup_daily_report_spec);
        params.put("task_spec", task_spec.toJSONString());
        System.out.println(addAsynTasks(accessToken, params));*/
        /*Map<String, String> params = new HashMap<>();
        System.out.println(getAsynTasks(accessToken, params));*/
        /*Map<String, String> params = new HashMap<>();
        params.put("task_id", "423856");
        params.put("file_id", "109142");
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File("d:\\tmp\\97448.csv");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte commonCsvHead[] = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            bos.write(Bytes.concat(commonCsvHead,getAsynTasksFiles(accessToken,params)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }*/
        System.out.println(WxAppUtil.refreshAppAccessToken("21_1AlZhmovB-f3BxZknLz1W9w7R5WVL238bpNNQ3phTZyxwUyEkzamb0N9t6Q0JGEJgId7pzfaBCi_fQHgbnFY9qlDnFOITUGb3YcRgSERXidvI7FA_1EJ6fe6QPzn4S_lbwX32sBUI-TMaE9PRETdAIACPE","wxa6b55d67539a6fd5","wx8f7ed53cf2134f29", "refreshtoken@@@Tlz9tb9Fw1veAEtf5aDnUjdgWrhlxkf8bYIeyIWGauk"));
        /*byte[] image = OkHttpUtils.getInstance().getRemoteImage("https://img.bazhuay.com/zbdsp/images/Catch(08-07-17-01-39)20180807171356.jpg");
        String signature = Tools.md5(image);
        System.out.println(addImages(accessToken,image,signature));*/
        Map<String, String> params = new HashMap<>();
        JSONObject time_range = new JSONObject();
        time_range.put("start_time", Tools.datetimeValue("2018-06-01 00:00:00", new Date()).getTime()/1000);
        time_range.put("end_time", Tools.datetimeValue("2019-05-28 00:00:00", new Date()).getTime()/1000);
        params.put("time_range", time_range.toJSONString());

        System.out.println(Tools.responseFormat(getWeChatAdLeads("21_QuIaUM_OuWHgT9arpoEuMKF3eL0pUtSUkrzZJXEaM_zBpAd4PZieN06u5nsSyWyBsRiwConVq1yqeueIRRuHM5lxbjkmOLmiIaYA8HPANXa0JVsx3MbXPAbiFaNdDHlTQFJAI62o-fQvOK4UHNNjALDBHH",params).toJSONString()));

    }
}
