package com.ckh.enjoy.constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * @description: 常量
 */  
public class WebConstants {
    public static final JSONObject ADSENSES = new JSONObject(true);
    static{
        JSONObject wechatOfficialAccountsBottom = new JSONObject();
        wechatOfficialAccountsBottom.put("id",567);
        wechatOfficialAccountsBottom.put("adsense","WECHAT_OFFICIAL_ACCOUNTS_BOTTOM");
        wechatOfficialAccountsBottom.put("height",334);
        wechatOfficialAccountsBottom.put("width",960);
        wechatOfficialAccountsBottom.put("name","微信公众号文章底部");
        wechatOfficialAccountsBottom.put("size",100);
        wechatOfficialAccountsBottom.put("support_miniprogram",true);
        wechatOfficialAccountsBottom.put("tips","(尺寸960*334，图片小于100k)");
        wechatOfficialAccountsBottom.put("adcreative_type", "image");
        wechatOfficialAccountsBottom.put("adcreative_type_desc", "单图文");
        JSONObject wechatOfficialAccountsMiddle = new JSONObject();
        wechatOfficialAccountsMiddle.put("id",484);
        wechatOfficialAccountsMiddle.put("adsense","WECHAT_OFFICIAL_ACCOUNTS_MIDDLE");
        wechatOfficialAccountsMiddle.put("height",540);
        wechatOfficialAccountsMiddle.put("width",960);
        wechatOfficialAccountsMiddle.put("name","微信公众号文章中部");
        wechatOfficialAccountsMiddle.put("size",80);
        wechatOfficialAccountsMiddle.put("support_miniprogram",true);
        wechatOfficialAccountsMiddle.put("tips","(尺寸960*540，图片小于80k)");
        wechatOfficialAccountsMiddle.put("adcreative_type", "image");
        wechatOfficialAccountsMiddle.put("adcreative_type_desc", "单图文");
        JSONObject wechatMiniprogram = new JSONObject();
        wechatMiniprogram.put("id",608);
        wechatMiniprogram.put("adsense","WECHAT_MINIPROGRAM");
        wechatMiniprogram.put("height",334);
        wechatMiniprogram.put("width",960);
        wechatMiniprogram.put("name","微信小程序");
        wechatMiniprogram.put("size",101);
        wechatMiniprogram.put("support_miniprogram",true);
        wechatMiniprogram.put("tips","(尺寸960*334，图片小于101k)");
        wechatMiniprogram.put("adcreative_type", "image");
        wechatMiniprogram.put("adcreative_type_desc", "单图文");
        JSONObject wechatMoments310 = new JSONObject();
        wechatMoments310.put("id",310);
        wechatMoments310.put("adsense","WECHAT_MOMENTS_310");
        wechatMoments310.put("height",800);
        wechatMoments310.put("width",640);
        wechatMoments310.put("name","微信朋友圈信息流");
        wechatMoments310.put("size",300);
        wechatMoments310.put("support_miniprogram",true);
        wechatMoments310.put("tips","(尺寸640*800，图片小于300k)");
        wechatMoments310.put("adcreative_type", "image");
        wechatMoments310.put("adcreative_type_desc", "单图文");
        JSONObject wechatMoments452 = new JSONObject();
        wechatMoments452.put("id",452);
        wechatMoments452.put("adsense","WECHAT_MOMENTS_452");
        wechatMoments452.put("height",360);
        wechatMoments452.put("width",640);
        wechatMoments452.put("name","微信朋友圈视频");
        wechatMoments452.put("size",3072);
        wechatMoments452.put("support_miniprogram",true);
        wechatMoments452.put("tips","(尺寸640*360，小于3072k，时长：6~30 s，格式：*.mp4)");
        wechatMoments452.put("adcreative_type", "video");
        wechatMoments452.put("adcreative_type_desc", "视频");
        ADSENSES.put("WECHAT_OFFICIAL_ACCOUNTS_BOTTOM", wechatOfficialAccountsBottom);
        ADSENSES.put("WECHAT_OFFICIAL_ACCOUNTS_MIDDLE", wechatOfficialAccountsMiddle);
        ADSENSES.put("WECHAT_MINIPROGRAM", wechatMiniprogram);
        ADSENSES.put("WECHAT_MOMENTS_310", wechatMoments310);
        ADSENSES.put("WECHAT_MOMENTS_452", wechatMoments452);
    }
	/** 导航栏KEY */
	public static final String KEY_LEFT_NAV = "_nav";

	/** 登录用户KEY */
	public static final String KEY_USER = "_user";
	/** 登录用户role */
	public static final String KEY_ROLE = "_role";
	/** 分页 */
	public static final int PAGE_NO_1 = 1;
	public static final int PAGE_SIZE_MAX = Integer.MAX_VALUE; 
	public static final int PAGE_DEFAULT_SIZE = 10;

	/** 批量任务redis key */
	public static final String REDIS_KEY_BATCH_TASK_QUEUE = "enjoy-batch_task_queue";
	
	/**登录**/
	public static final String LOGIN_CHECK = "/logincheck";
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";
	
	/**主页**/
	public static final String INDEX = "/index";
	/**模块**/
	public static final String MODULE_LIST = "/module/list";
	public static final String MODULE_DEL = "/module/del";
	public static final String MODULE_TREE = "/module/tree";
	public static final String MODULE_DETAIL = "/module/detail";
	public static final String MODULE_CREATE_OR_UP = "/module/createorup";
	public static final String MODULE_LOOKUP = "/module/lookup";
	public static final String MODULE_SUGGEST = "module/suggest";
	
	/**角色**/
	public static final String ROLE_LIST = "/role/list";
	public static final String ROLE_DEL = "/role/del";
	public static final String ROLE_DETAIL = "/role/detail";
	public static final String ROLE_CREATE_OR_UP = "/role/createorup";
	public static final String ROLE_AUTH = "/role/auth";
	/**用户**/
	public static final String USER_LIST = "/user/list";
	public static final String USER_DEL = "/user/del";
	public static final String USER_DETAIL = "/user/detail";
	public static final String USER_CREATE_OR_UP = "/user/createorup";
	public static final String USER_RESETPASSWORD = "/user/resetpassword";
	public static final String USER_PASSWORD = "/user/password";
}
