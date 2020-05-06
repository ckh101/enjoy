package com.hbnet.fastsh.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 短信发送辅助类
 */
@Slf4j
public class AliSMSUtil {
	private static final String ACCESSKEYID = "";
	private static final String ACCESSKEYSECRET = "";

	// 短信签名
	public static final String SMS_SIGN = "";

	public static boolean sendCode(String phone, String code) throws Exception {
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数
		final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
		// 替换成你的AK
		final String accessKeyId = ACCESSKEYID;// 你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = ACCESSKEYSECRET;// 你的accessKeySecret，参考本文档步骤2
		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(SMS_SIGN);
		// 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
		request.setTemplateCode("SMS_158035838");
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{  \"code\":\"" + code + "\"}");
		// 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//		request.setOutId("yourOutId");
		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				return true;
			}
		} catch (ClientException e) {

			e.printStackTrace();
		}
		return false;

	}

	public static boolean adOrderNotify(String phone, Map<String, String> params) throws Exception{
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_168822414");
        request.putQueryParameter("TemplateParam", "{\"adname\":\""+params.get("adname")+"\",\"ordernum\":\""+params.get("ordernum")+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if("OK".equals(data.getString("Message"))){
                return true;
            }else{
                log.error(data.toJSONString());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adAuditStateNotify(String phone, Map<String, String> params) throws Exception{
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_168822135");
        request.putQueryParameter("TemplateParam", "{\"adname\":\""+params.get("adname")+"\",\"status\":\""+params.get("status")+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if("OK".equals(data.getString("Message"))){
                return true;
            }else{
                log.error(data.toJSONString());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adFirstOrderNotify(String phone, int adId, String adName) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_171351012");
        request.putQueryParameter("TemplateParam", "{\"adId\":\"" + String.valueOf(adId) + "\",\"adName\":\"" + adName + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if ("OK".equals(data.getString("Message"))) {
                return true;
            } else {
                log.error("首单通知失败,adId={},phone={},ret={}", adId, phone, data);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adOrderIntervalNotify(String phone, Integer adId, String adName, int total) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_171356045");
        request.putQueryParameter("TemplateParam", "{\"adId\":\"" + adId + "\",\"adName\":\"" + adName + "\",\"total\":\"" + total + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if ("OK".equals(data.getString("Message"))) {
                return true;
            } else {
                log.error("订单间隔通知失败,adId={},phone={},ret={}", adId, phone, data);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adCostThresholdExceedNotify(String phone, Integer adId, String adName, int currentCost, int dailyBudget) {
	    double currentCostYuan = currentCost / 100D;
        double dailyBudgetYuan = dailyBudget / 100D;
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_171356056");
        request.putQueryParameter("TemplateParam", "{\"adId\":\"" + adId + "\",\"adName\":\"" + adName + "\",\"amount\":\"" + currentCostYuan + "\",\"limitAmount\":\"" + dailyBudgetYuan + "\"" + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if ("OK".equals(data.getString("Message"))) {
                return true;
            } else {
                log.error("预算消耗通知失败,adId={},phone={},ret={}", adId, phone, data);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adBudgetThresholdExceedNotify(String phone, Integer adId, String adName, double dailyBudgetThreshold) {
	    String percent = (int)(dailyBudgetThreshold * 10000) / 100D + "";
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_171351034");
        request.putQueryParameter("TemplateParam", "{\"adId\":\"" + adId + "\",\"adName\":\"" + adName + "\",\"percent\":\"" + percent + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if ("OK".equals(data.getString("Message"))) {
                return true;
            } else {
                log.error("预算剩余通知失败,adId={},phone={},ret={}", adId, phone, data);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adRoiThresholdExceedNotify(String phone, Integer adId, String adName, double currentRoi, double roiThreshold) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_171356064");
        request.putQueryParameter("TemplateParam", "{\"adId\":\"" + adId + "\",\"adName\":\"" + adName + "\",\"roi\":\"" + currentRoi + "\",\"roiLimit\":\"" + roiThreshold + "\"" + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if ("OK".equals(data.getString("Message"))) {
                return true;
            } else {
                log.error("ROI过低通知失败,adId={},phone={},ret={}", adId, phone, data);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean adRoiCauseAutoPauseNotify(String phone, Integer adId, String adName, double currentRoi, double roiThreshold) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SMS_SIGN);
        request.putQueryParameter("TemplateCode", "SMS_171351045");
        request.putQueryParameter("TemplateParam", "{\"adId\":\"" + adId + "\",\"adName\":\"" + adName + "\",\"roi\":\"" + currentRoi + "\",\"roiLimit\":\"" + roiThreshold + "\"" + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject data = JSONObject.parseObject(response.getData());
            if ("OK".equals(data.getString("Message"))) {
                return true;
            } else {
                log.error("ROI过低自动暂停通知失败,adId={},phone={},ret={}", adId, phone, data);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
