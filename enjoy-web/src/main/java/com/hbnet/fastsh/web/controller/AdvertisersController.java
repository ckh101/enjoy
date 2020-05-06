package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.*;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.AdvertiserWechatSpec;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("admin/advertisers")
public class AdvertisersController {


    @Autowired
    AdvertiserService advertiserService;

    @Autowired
    AgentService agentService;

    @Autowired
    AdvertiserWechatSpecService advertiserWechatSpecService;

    @Autowired
    AdvertiserFundService advertiserFundService;

    @Autowired
    UserService userService;

    @RequestMapping()
    public String list(Advertiser adv, PageInfo<Advertiser> pageInfo, ModelMap model){
        advertiserService.page(adv, pageInfo, SessionUtil.getCurUser());
        model.addAttribute("page", pageInfo);
        model.addAttribute("advertiser",adv);
        model.addAttribute("authCount", advertiserService.countAuth());
        model.addAttribute("wxAuthCount", advertiserService.countWxAuth());
        return "/advertisers/list";
    }

    @RequestMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("method","add");
        return "/advertisers/edit";
    }

    @RequestMapping("edit/{aid}")
    public String edit(ModelMap model, @PathVariable Long aid) {
        Advertiser adv = advertiserService.findById(aid);
        model.addAttribute("advertiser",adv);
        model.addAttribute("method", "edit");
        return "/advertisers/edit";
    }

    @RequestMapping(value="save", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse save(Advertiser advertiser, String method){
        ApiResponse result = new ApiResponse();
        if("add".equals(method)){
            if(advertiserService.findByAccountId(advertiser.getAccountId()) != null){
                result.setMsg("广告主已经存在");
            }else{
                JSONObject advertiserJson = advertiserService.getByGdt(advertiser, true);
                if(advertiserJson != null){
                    if(advertiserJson.getIntValue("code") == 0){
                        JSONObject data = advertiserJson.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");
                        if(list.size() == 0){
                            result.setMsg("查不到该广告主信息，请联系服务商进行绑定");
                        }else{
                            JSONObject json = list.getJSONObject(0);
                            advertiserService.setInfo(advertiser, json);
                            advertiser.setWxAuthStatus(2);
                            advertiser.setAuthStatus(2);
                            advertiser.setCreateTime(new Date());
                            advertiserService.saveOrUpdate(advertiser);
                            advertiserFundService.updateOrSaveFunds(advertiser);
                            User user = userService.findById(SessionUtil.getCurUser().getId());
                            List<Advertiser> advs = user.getAdvertisers();
                            advs.add(advertiser);
                            userService.saveOrUpdate(user);
                            result.setStatus(1);
                        }
                    }else{
                        result.setMsg(advertiserJson.toJSONString());
                    }
                }else{
                    result.setMsg("无法访问GDT广告主接口，请联系服务商");
                }
            }

        }else if("edit".equals(method)){
            Advertiser advertiser1 = advertiserService.findById(advertiser.getId());
            if(advertiser1 != null){
               if(!Tools.isBlank(advertiser.getWxOfficialAccount()) && !Tools.isBlank(advertiser.getWxAppId()) && !Tools.isBlank(advertiser.getOriginalId())) {
                   advertiser1.setWxOfficialAccount(advertiser.getWxOfficialAccount());
                   advertiser1.setWxAppId(advertiser.getWxAppId());
                   advertiser1.setOriginalId(advertiser.getOriginalId());
                   advertiser1.setWebUserActionSetId(advertiser.getWebUserActionSetId());
                   advertiser1.setUpdateTime(new Date());
                   advertiserService.saveOrUpdate(advertiser1);
               }
               result.setStatus(1);
            }

        }
        return result;
    }

    @RequestMapping("bindWx")
    @ResponseBody
    public ApiResponse bindWx(long aId, HttpServletRequest request) throws UnsupportedEncodingException {
        ApiResponse result = new ApiResponse();
        Advertiser adv = advertiserService.findById(aId);
        if(adv == null||Tools.isBlank(adv.getWxAppId())){
            result.setMsg("广告主不存在或者找不到公众号信息！");
        }else{
            String accessToken = adv.getAccessToken();
            String source = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
            String redirectUrl = URLEncoder.encode(source + "/admin/advertisers/wxBindCallBack", "UTF-8");
            String url = "https://developers.e.qq.com/authorization/wechat_bind?access_token=%s&account_id=%s&wechat_account_id=%s&redirect_uri=%s";
            result.setStatus(1);
            result.setData(String.format(url, accessToken, adv.getAccountId(), adv.getWxAppId(), redirectUrl));
        }
        return result;
    }

    @RequestMapping("wxDetail")
    @ResponseBody
    public ApiResponse wxDetail(String wxAppId){
        ApiResponse result = new ApiResponse();
        AdvertiserWechatSpec advertiserWechatSpec = advertiserWechatSpecService.getByWxAppId(wxAppId);
        if(advertiserWechatSpec == null){
            result.setMsg("找不到信息，可能尚未绑定");
        }else{
            result.setData(advertiserWechatSpec);
            result.setStatus(1);
        }
        return result;
    }

    @RequestMapping(value="wxBindCallBack")
    public String wxBindCallBack(HttpServletRequest req){
        JSONObject result = new JSONObject();
        int accountId = Tools.integerValue(req.getParameter("account_id"), 0);
        Advertiser adv = advertiserService.findByAccountId(accountId);
        if(adv == null){
            req.setAttribute("error", "找不到广告主");
            return "/binderror";
        }
        JSONObject advertiserJson = advertiserService.getByGdt(adv, false);
        if(advertiserJson != null){
            if(advertiserJson.getIntValue("code") == 0){
                JSONObject advertiserWechatSpecJson = advertiserJson.getJSONObject("data").getJSONArray("list").getJSONObject(0).getJSONObject("wechat_spec");
                advertiserWechatSpecService.updateOrSaveAdvertiserWechatSpec(advertiserWechatSpecJson, adv.getAccountId());
                adv.setWxAuthStatus(1);
                adv.setUpdateTime(new Date());
                advertiserService.saveOrUpdate(adv);
            }else{
                req.setAttribute("error", advertiserJson+"GDT获取广告主信息失败,请联系服务商");
                return "/binderror";
            }
        }else{
            req.setAttribute("error", advertiserJson+"GDT获取广告主信息失败,请联系服务商");
            return "/binderror";
        }
        return "redirect:/index";
    }

    @RequestMapping(value="authcallback/{accountId}")
    public String authCallBack(HttpServletRequest req, @PathVariable int accountId){
        String authorization_code = req.getParameter("authorization_code");
        String source = req.getScheme() + "://" + req.getServerName() + req.getContextPath();
        String callBackUrl = source + "/admin/advertisers/authcallback/"+accountId;
        Advertiser adv = advertiserService.findByAccountId(accountId);
        JSONObject json = GDTApiUtils.getAccessToken(authorization_code, callBackUrl);
        if(json != null){
            if(json.getIntValue("code") == 0){
                String token = json.getJSONObject("data").getString("access_token");
                int expiresIn = json.getJSONObject("data").getIntValue("access_token_expires_in");
                String refreshtoken = json.getJSONObject("data").getString("refresh_token");
                int refreshExpiresIn = json.getJSONObject("data").getIntValue("refresh_token_expires_in");
                adv.setAccessToken(token);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 21);
                cal.set(Calendar.MINUTE, 50);
                adv.setExpiresTime(cal.getTime());
                adv.setRefreshAccessToken(refreshtoken);
                adv.setRefreshExpiresTime(Tools.addSeconds(new Date(), refreshExpiresIn - 600));
                adv.setAuthStatus(1);
                adv.setUpdateTime(new Date());
                advertiserService.saveOrUpdate(adv);
            }else{
                req.setAttribute("error", "GDT获取Token失败,请联系服务商");
                return "/binderror";
            }
        }else{
            req.setAttribute("error", "请求GDT获取token接口失败,请联系服务商");
            return "/binderror";
        }


        return "redirect:/index";
    }
    @RequestMapping("syncAdvInfo")
    @ResponseBody
    public ApiResponse syncAdvInfo(long aId){
        ApiResponse result = new ApiResponse();
        Advertiser adv = advertiserService.findById(aId);
        if(adv == null){
            result.setMsg("找不到广告主信息");
        }else{
            advertiserService.syncAdvInfo(adv);
            result.setStatus(1);
            result.setMsg("同步信息成功");
        }
        return result;
    }

    @RequestMapping("refreshToken/{aId}")
    @ResponseBody
    public JSONObject refreshAccessToken(@PathVariable long aId){
        return advertiserService.refreshToken(aId);
    }

    @RequestMapping("checkAccountId")
    @ResponseBody
    public JSONObject checkAccountId(Integer accountId) {
        JSONObject result = new JSONObject();
        result.put("valid", !advertiserService.isExist(accountId));
        return result;
    }

    @RequestMapping("uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws IOException {

        ApiResponse result = new ApiResponse();
        long aId = Tools.longValue(req.getParameter("aId"), 0);
        String type = req.getParameter("adcreative_type");
        Advertiser adv = advertiserService.findById(aId);
        if(adv != null){
            String accessToken = adv.getAccessToken();
            byte[] fileBytes = file.getBytes();
            String signature = Tools.md5(fileBytes);
            JSONObject image = null;
            if("image".equals(type)){
                image = GDTApiUtils.addImages(accessToken, String.valueOf(adv.getAccountId()), fileBytes, signature);
            }else{
                image = GDTApiUtils.addVideos(accessToken, String.valueOf(adv.getAccountId()), fileBytes, signature, "朋友圈视频");
            }
            if(image == null){
                result.setMsg("服务器出错，请联系服务商");
            }else{
                if(image.getIntValue("code") == 0){
                    result.setStatus(1);
                    result.setData(image);
                }else{
                    result.setMsg(image.toJSONString());
                }
            }
        }else{
            result.setMsg("找不到广告主");
        }
        return JsonUtils.toJson(result);
    }

    @RequestMapping("getVideoInfo")
    @ResponseBody
    public ApiResponse getVideoInfo(Integer videoId, Long aId){
        ApiResponse response = new ApiResponse();
        Advertiser adv = advertiserService.findById(aId);
        Map<String, String> params = new HashMap<>();
        params.put("account_id", adv.getAccountId().toString());
        params.put("filtering","[{\"field\":\"media_id\",\"operator\":\"EQUALS\",\"values\":["+videoId+"]}]");
        params.put("fields", "[\"preview_url\",\"system_status\"]");
        JSONObject video = GDTApiUtils.getVideos(adv.getAccessToken(), params);
        if(video != null && video.getIntValue("code") == 0){
            response.setStatus(1);
            response.setData(video.getJSONObject("data").getJSONArray("list").get(0));

        }else{
            response.setMsg(video == null?"获取视频信息错误":video.toJSONString());
        }
        return response;
    }
}
