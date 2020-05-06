package com.hbnet.fastsh.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GDTApiUtils {

    private static final String CLIENT_ID = "1108287192";

    private static final String CLIENT_SECRET = "uVAimD9P3ujGJYEv";

    private static final String VERSION = "v1.1";

    private static final String API_URL = "https://api.e.qq.com/%s/%s/%s?access_token=%s&timestamp=%s&nonce=%s";

    private static final String TOKEN_URL = "https://api.e.qq.com/oauth/token?1=1";

    public static final String ALL_TIEM_SERIES = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";


    private static String getApi(String resource, String action, String accessToken) {
        return String.format(API_URL, VERSION, resource, action, accessToken, LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")), Tools.guid(""));
    }

    private static JSONObject get(String resource, String action, String accessToken, Map<String, String> params) {
        String url = getApi(resource, action, accessToken);
        return get(url, params);
    }

    private static JSONObject get(String apiUrl, Map<String, String> params) {
        String result = OkHttpUtils.getInstance().get(apiUrl, params);
        return Tools.isBlank(result) ? null : JSONObject.parseObject(result);
    }

    private static JSONObject post(String apiUrl, Map<String, String> params) {
        String result = OkHttpUtils.getInstance().post(apiUrl, params);
        return Tools.isBlank(result) ? null : JSONObject.parseObject(result);
    }

    public static JSONObject getAccessToken(String authorizationCode, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("authorization_code", authorizationCode);
        params.put("redirect_uri", url);
        return get(TOKEN_URL, params);
    }

    public static JSONObject refreshAccessToken(String refreshToken) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        return get(TOKEN_URL, params);
    }

    public static JSONObject addAdvertiser(String accessToken, Map<String, String> params) {
        String api = getApi("advertiser", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateAdvertiser(String accessToken, Map<String, String> params) {
        String api = getApi("advertiser", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject getAdvertiser(String accessToken, Map<String, String> params) {
        String api = getApi("advertiser", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addQualifications(String accessToken, Map<String, String> params) {
        String api = getApi("qualifications", "get", accessToken);
        return post(api, params);
    }

    public static JSONObject updateQualifications(String accessToken, Map<String, String> params) {
        String api = getApi("qualifications", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteQualifications(String accessToken, Map<String, String> params) {
        String api = getApi("qualifications", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getQualifications(String accessToken, Map<String, String> params) {
        String api = getApi("qualifications", "get", accessToken);
        return post(api, params);
    }

    public static JSONObject addFundTransfer(String accessToken, Map<String, String> params) {
        String api = getApi("fund_transfer", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject getFunds(String accessToken, Map<String, String> params) {
        String api = getApi("funds", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getFundStatementsDaily(String accessToken, Map<String, String> params) {
        String api = getApi("fund_statements_daily", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getFundDtatementsDetailed(String accessToken, Map<String, String> params) {
        String api = getApi("fund_statements_detailed", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addPromotedObjects(String accessToken, Map<String, String> params) {
        String api = getApi("promoted_objects", "add", accessToken);
        return get(api, params);
    }

    public static JSONObject updatePromotedObjects(String accessToken, Map<String, String> params) {
        String api = getApi("promoted_objects", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject getPromotedObjects(String accessToken, Map<String, String> params) {
        String api = getApi("promoted_objects", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getPages(String accessToken, Map<String, String> params) {
        String api = getApi("pages", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addVideos(String accessToken, String accountId, byte[] file, String signature, String description){
        String url = getApi("videos", "add", accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("signature", signature);
        params.put("account_id", accountId);
        params.put("description", description);
        String result = OkHttpUtils.getInstance().uploadFile(url, file, signature,"video_file", params);
        return Tools.isBlank(result) ? null : JSONObject.parseObject(result);
    }

    public static JSONObject getVideos(String accessToken, Map<String, String> params) {
        String api = getApi("videos", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addImages(String accessToken, String accountId, byte[] file, String signature) throws IOException {
        String url = getApi("images", "add", accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("signature", signature);
        params.put("account_id", accountId);
        params.put("upload_type", "UPLOAD_TYPE_FILE");
        String result = OkHttpUtils.getInstance().uploadFile(url, file, signature,"file", params);
        return Tools.isBlank(result) ? null : JSONObject.parseObject(result);
    }

    public static JSONObject getImages(String accessToken, Map<String, String> params) {
        String api = getApi("images", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getProductCatalogs(String accessToken, Map<String, String> params) {
        String api = getApi("product_catalogs", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject updateProductItems(String accessToken, Map<String, String> params) {
        String api = getApi("product_items", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject getDynamicAdTemplates(String accessToken, Map<String, String> params) {
        String api = getApi("dynamic_ad_templates", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addDynamicAdImages(String accessToken, Map<String, String> params) {
        String api = getApi("dynamic_ad_images", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject getDynamicAdImages(String accessToken, Map<String, String> params) {
        String api = getApi("dynamic_ad_images", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addCampaigns(String accessToken, Map<String, String> params) {
        String api = getApi("campaigns", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateCampaigns(String accessToken, Map<String, String> params) {
        String api = getApi("campaigns", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteCampaigns(String accessToken, Map<String, String> params) {
        String api = getApi("campaigns", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getCampaigns(String accessToken, Map<String, String> params) {
        String api = getApi("campaigns", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addAdGroups(String accessToken, Map<String, String> params) {
        String api = getApi("adgroups", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateAdGroups(String accessToken, Map<String, String> params) {
        String api = getApi("adgroups", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteAdGroups(String accessToken, Map<String, String> params) {
        String api = getApi("adgroups", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getAdGroups(String accessToken, Map<String, String> params) {
        String api = getApi("adgroups", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addAdcreatives(String accessToken, Map<String, String> params) {
        String api = getApi("adcreatives", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateAdcreatives(String accessToken, Map<String, String> params) {
        String api = getApi("adcreatives", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteAdcreatives(String accessToken, Map<String, String> params) {
        String api = getApi("adcreatives", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getAdcreatives(String accessToken, Map<String, String> params) {
        String api = getApi("adcreatives", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addAds(String accessToken, Map<String, String> params) {
        String api = getApi("ads", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteAds(String accessToken, Map<String, String> params) {
        String api = getApi("ads", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject updateAds(String accessToken, Map<String, String> params) {
        String api = getApi("ads", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject getAds(String accessToken, Map<String, String> params) {
        String api = getApi("ads", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addTargetings(String accessToken, Map<String, String> params) {
        String api = getApi("targetings", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateTargetings(String accessToken, Map<String, String> params) {
        String api = getApi("targetings", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteTargetings(String accessToken, Map<String, String> params) {
        String api = getApi("targetings", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getTargetings(String accessToken, Map<String, String> params) {
        String api = getApi("targetings", "get", accessToken);
        return post(api, params);
    }

    public static JSONObject getTargetingTags(String accessToken, Map<String, String> params) {
        String api = getApi("targeting_tags", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getAdcreativeTemplates(String accessToken, Map<String, String> params) {
        String api = getApi("adcreative_templates", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getCapabilities(String accessToken, Map<String, String> params) {
        String api = getApi("capabilities", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getEstimation(String accessToken, Map<String, String> params) {
        String api = getApi("estimation", "get", accessToken);
        return post(api, params);
    }

    public static JSONObject addAdcreativePreviews(String accessToken, Map<String, String> params) {
        String api = getApi("adcreative_previews", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject getAdcreativePreviews(String accessToken, Map<String, String> params) {
        String api = getApi("adcreative_previews", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getRealtimeCost(String accessToken, Map<String, String> params) {
        String api = getApi("realtime_cost", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getDailyReports(String accessToken, Map<String, String> params) {
        String api = getApi("daily_reports", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getHourlyReports(String accessToken, Map<String, String> params) {
        String api = getApi("hourly_reports", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getTargetingTagReports(String accessToken, Map<String, String> params) {
        String api = getApi("targeting_tag_reports", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getCustomAudienceInsights(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audience_insights", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getTrackingReports(String accessToken, Map<String, String> params) {
        String api = getApi("tracking_reports", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getEcommerceOrder(String accessToken, Map<String, String> params) {
        String api = getApi("ecommerce_order", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject updateEcommerceOrder(String accessToken, Map<String, String> params) {
        String api = getApi("ecommerce_order", "update", accessToken);
        return get(api, params);
    }

    public static JSONObject addAsyncTasks(String accessToken, Map<String, String> params) {
        String api = getApi("async_tasks", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject getAsyncTasks(String accessToken, Map<String, String> params) {
        String api = getApi("async_tasks", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getAsyncTasksFiles(String accessToken, Map<String, String> params) {
        String api = getApi("async_task_files", "get", accessToken).replaceAll("api.e.qq.com", "dl.e.qq.com");
        return get(api, params);
    }

    public static JSONObject getWechatAdvertiser(String accessToken, Map<String, String> params){
        String api = getApi("wechat_advertiser", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getWechatAdvertiserDetail(String accessToken, Map<String, String> params){
        String api = getApi("wechat_advertiser_detail", "get", accessToken);
        return get(api, params);
    }
    public static JSONObject addUserActionSets(String accessToken, String json) {
        String api = getApi("user_action_sets", "add", accessToken);
        String result = OkHttpUtils.getInstance().postJson(api, json);
        return result == null?null:JSONObject.parseObject(result);
    }

    public static JSONObject getUserActionSets(String accessToken, Map<String, String> params) {
        String api = getApi("user_action_sets", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getUserActionSetReports(String accessToken, Map<String, String> params) {
        String api = getApi("user_action_set_reports", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addUserActions(String accessToken, String json) {
        String api = getApi("user_actions", "add", accessToken);
        String result = OkHttpUtils.getInstance().postJson(api, json);
        return result == null?null:JSONObject.parseObject(result);
    }

    public static JSONObject addCustomAudiences(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audiences", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateCustomAudiences(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audiences", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteCustomAudiences(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audiences", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getCustomAudiences(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audiences", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addCustomAudienceFiles(String accessToken, byte[] file, String signature, Map<String, String> params) {
        String url = getApi("custom_audience_files", "add", accessToken);
        String result = OkHttpUtils.getInstance().uploadFile(url, file, signature,"file", params);
        return Tools.isBlank(result) ? null : JSONObject.parseObject(result);
    }

    public static JSONObject getCustomAudienceFiles(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audience_files", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getCustomAudienceEstimations(String accessToken, Map<String, String> params) {
        String api = getApi("custom_audience_estimations", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addCustomTags(String accessToken, Map<String, String> params) {
        String api = getApi("custom_tags", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateCustomTags(String accessToken, Map<String, String> params) {
        String api = getApi("custom_tags", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject deleteCustomTags(String accessToken, Map<String, String> params) {
        String api = getApi("custom_tags", "delete", accessToken);
        return post(api, params);
    }

    public static JSONObject getCustomTags(String accessToken, Map<String, String> params) {
        String api = getApi("custom_tags", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addCustomTagFiles(String accessToken, byte[] file, String signature, Map<String, String> params){
        String url = getApi("custom_tag_files", "add", accessToken);
        String result = OkHttpUtils.getInstance().uploadFile(url, file, signature,"file", params);
        return Tools.isBlank(result) ? null : JSONObject.parseObject(result);
    }

    public static JSONObject getCustomTagFiles(String accessToken, Map<String, String> params) {
        String api = getApi("custom_tag_files", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addUserPropertySets(String accessToken, Map<String, String> params) {
        String api = getApi("user_property_sets", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject updateUserPropertySets(String accessToken, Map<String, String> params) {
        String api = getApi("user_property_sets", "update", accessToken);
        return post(api, params);
    }

    public static JSONObject getUserPropertySets(String accessToken, Map<String, String> params) {
        String api = getApi("user_property_sets", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject addUserProperties(String accessToken, Map<String, String> params) {
        String api = getApi("user_properties", "add", accessToken);
        return post(api, params);
    }

    public static JSONObject getComplianceValidation(String accessToken, Map<String, String> params){
        String api =  getApi("compliance_validation", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getSystemStatus(String accessToken, Map<String, String> params){
        String api =  getApi("system_status", "get", accessToken);
        return get(api, params);
    }

    public static JSONObject getDiagnosis(String accessToken, Map<String, String> params){
        String api =  getApi("diagnosis", "get", accessToken);
        return get(api, params);
    }


    public static StringBuilder getTimeSeries(StringBuilder builder, String beginTime, String endTime){
        String[] beginInfos = beginTime.split(":");
        String[] endInfos = endTime.split(":");
        int begin = Integer.parseInt(beginInfos[0])*2+Integer.parseInt(beginInfos[1])/30;
        int end = Integer.parseInt(endInfos[0])*2+Integer.parseInt(endInfos[1])/30;
        StringBuilder timeSeries = new StringBuilder();
        for(int i=begin; i < end; i++){
            timeSeries.append("1");
        }
        for(int i = 0; i < 7; i++){
            builder.replace(i*48+begin, i*48+end, timeSeries.toString());
        }
        return builder;
    }

    public static StringBuilder getTimeSeries(StringBuilder builder, String beginTime, String endTime, int week){
        String[] beginInfos = beginTime.split(":");
        String[] endInfos = endTime.split(":");
        int begin = Integer.parseInt(beginInfos[0])*2+Integer.parseInt(beginInfos[1])/30;
        int end = Integer.parseInt(endInfos[0])*2+Integer.parseInt(endInfos[1])/30;
        StringBuilder timeSeries = new StringBuilder();
        for(int i=begin; i < end; i++){
            timeSeries.append("1");
        }
        return builder.replace(48*week+begin, 48*week+end, timeSeries.toString());
    }

    public static Map<String, Stack<Map<String, String>>> transTimeSeries(String timeSeries){
        Map<String, Stack<Map<String, String>>> times = new HashMap<>();
        for(int i = 0; i < 336; i++){
            String week = i/48 + "";
            Stack<Map<String, String>> stack = times.get(week);
            if(stack == null){
                stack = new Stack<>();
                times.put(week, stack);
            }
            if(timeSeries.charAt(i) == '1'){
                int index = i%48;
                String beginTime;
                String endTime;
                if(index%2 == 0){
                    beginTime = index/2+":00";
                    endTime = (index+1)/2+":30";
                }else{
                    beginTime = index/2+":30";
                    endTime = (index+1)/2+":00";
                }
                if(stack.isEmpty()){
                    Map<String, String> timeMap = new HashMap<>();
                    timeMap.put("start", beginTime);
                    timeMap.put("end", endTime);
                    stack.push(timeMap);
                }else{
                    Map<String, String> lastTimeMap = stack.peek();
                    if(lastTimeMap.get("end").equals(beginTime)){
                        lastTimeMap.put("end",endTime);
                    }else{
                        Map<String, String> timeMap = new HashMap<>();
                        timeMap.put("start", beginTime);
                        timeMap.put("end", endTime);
                        stack.push(timeMap);
                    }
                }
            }
        }
        return times;
    }

    public static void main(String[] args){
        //"access_token":"a97250640170e97669c08f116450f276","refresh_token":"e662237e292a3240726bf497a6197426"
       //getWechatAdvertiser("2df436b4d1b51513fd81e950a4fa2410", new HashMap<>());
        //Map<String, String> params = new HashMap<>();
        //{"account_id":9230153},{"account_id":9010659}
        //params.put("account_id", "9010659");
        //params.put("fields","[\"campaign_name\"]");
        //getWechatAdvertiserDetail("2df436b4d1b51513fd81e950a4fa2410", params);
        //getCampaigns("2df436b4d1b51513fd81e950a4fa2410", params);
        //GDTApiUtils.getAccessToken("d4e73b86d69c7921b456e4a131118506","https://smartad.akeyn.com/smartad-web/admin/advertisers/authcallback/wx8f7ed53cf2134f29");
        /*Map<String, String> params = new HashMap<>();
        params.put("account_id", "14770701");
        params.put("status_type", "STATUS_TYPE_OCPA_LEARNING");
        params.put("status_spec","{\"ocpa_learning_spec\":{\"adgroup_id_list\":[223972418]}}");
        params.put("fields","[\"adgroup_id\"]");
        getSystemStatus("bc169da9879e2793c0d0cd43991a4295", params);*/
        /*Map<String, String> params = new HashMap<>();
        params.put("account_id", "14770701");
        params.put("fields","[\"adgroup_id\"]");
        params.put("filtering", "[{\"field\":\"configured_status\",\"operator\":\"EQUALS\",\"values\":[\"AD_STATUS_NORMAL\"]},{\"field\":\"system_status\",\"operator\":\"EQUALS\",\"values\":[\"AD_STATUS_NORMAL\"]}]");
        getAds("bc169da9879e2793c0d0cd43991a4295", params);*/
        Map<String, String> params = new HashMap<>();
        params.put("account_id", "14770701");
        params.put("adgroup_id_list", "[223972418]");
        getDiagnosis("bc169da9879e2793c0d0cd43991a4295", params);
    }
}



