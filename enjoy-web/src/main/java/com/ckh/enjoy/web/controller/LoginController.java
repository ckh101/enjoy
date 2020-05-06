package com.ckh.enjoy.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.ckh.enjoy.constants.WebConstants;
import com.ckh.enjoy.utils.*;
import com.ckh.enjoy.web.cache.CacheClient;
import com.ckh.enjoy.web.entity.User;
import com.ckh.enjoy.web.entity.WxUser;
import com.ckh.enjoy.web.service.base.ProfileConfig;
import com.ckh.enjoy.web.service.impl.UserService;
import com.ckh.enjoy.web.service.impl.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;



/**
 * @description: 用户登录控制器
 */
@Controller
@Slf4j
public class LoginController {

    /** md5密钥 */
    private String secretKey;
    @Autowired
    CacheClient mcc;


    @Autowired
    WxUserService wus;

    @Autowired
    UserService us;

    @Autowired
    ProfileConfig profileConfig;


    /**
     * 登录


     * @return
     */
    @RequestMapping(WebConstants.LOGIN_CHECK)
    public ModelAndView logincheck(HttpServletRequest req) {
        String code = req.getParameter("code");
        if(mcc.get(code) != null) {
            return null;
        }else {
            mcc.set(code, 1, Tools.addSeconds(new Date(), 10));
        }
        String token = WxAppUtil.userAccessToken(code, WxAppUtil.LOGIN_APPID, WxAppUtil.LOGIN_SECRET);
        JSONObject tokenJson = JSONObject.parseObject(token);
        String accessToken = tokenJson.getString("access_token");
        if(Tools.isBlank(accessToken)) {
            return new ModelAndView("redirect:/login");
        }
        String refreshToken = tokenJson.getString("refresh_token");
        String openId = tokenJson.getString("openid");
        if(Tools.isBlank(openId)){
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView("login_bind");
        String userInfo = WxAppUtil.getOpenWxUserInfo(accessToken, openId);
        JSONObject infoJson = JSONObject.parseObject(userInfo);
        String nickname = infoJson.getString("nickname");
        int sex = infoJson.getIntValue("sex");
        String headUrl = infoJson.getString("headimgurl");
        String unionId = infoJson.getString("unionid");
        WxUser wu = wus.findByOpenId(openId);
        if(wu==null) {
            wu = new WxUser();
            wu.setHeadUrl(headUrl);
            wu.setNickName(nickname);
            wu.setOpenId(openId);
            wu.setSex(sex);
            wu.setUnionId(unionId);
        }else {
            User user = wu.getUser();
            if(user != null) {
                wu.setHeadUrl(headUrl);
                wu.setNickName(nickname);
                wu.setRefreshToken(refreshToken);
                wu.setToken(accessToken);
                UsernamePasswordToken loginToken = new UsernamePasswordToken(user.getAccount(), DigestUtils.md5Hex(user.getPhone()));
                //loginToken.setRememberMe(true);
                // 获取当前subject
                Subject currentSubject = SecurityUtils.getSubject();
                currentSubject.login(loginToken);
                if (currentSubject.isAuthenticated()) {
                    SecurityUtils.getSubject().getSession().setAttribute("nickname", wu.getNickName());
                    SecurityUtils.getSubject().getSession().setAttribute("headurl", wu.getHeadUrl());
                    user.setLastLoginTime(new Date());
                    user.setWxUser(wu);
                    us.saveOrUpdate(user);
                    return new ModelAndView("redirect:/index");
                }
            }
        }
        mav.addObject("wxuser", wu);
        return mav;
    }
    @RequestMapping(WebConstants.LOGIN)
    public String login(){
        String profile = profileConfig.getActiveProfile();
        if(ProfileConfig.DEV_PROFILE.equalsIgnoreCase(profile)){
            UsernamePasswordToken token = new UsernamePasswordToken("admin", DigestUtils.md5Hex("15889961234"));
            Subject currentSubject = SecurityUtils.getSubject();
            currentSubject.login(token);
            if (currentSubject.isAuthenticated()) {
                return "redirect:/index";
            }
            return "login";
        }else{
            SecurityUtils.getSubject().getSession().setAttribute("token", TokenProcessor.getInstance().generateTokeCode());
            return "wxlogin";
        }
    }
    @RequestMapping("/images/captcha.jpg")
    public ModelAndView getaptchaImage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 禁止图像缓存,使得单击验证码可以刷新验证码图片
        resp.setHeader("Pragma", "nocache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        // 验证码图片的宽度。         
        int width = 100;
        // 验证码图片的高度。         
        int height = 33;
        // 验证码字符个数         
        int codeCount = 4;
        int x = width / (codeCount + 1);;
        // 字体高度         
        int fontHeight = height - 2;
        int codeY = height - 4;
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 创建一个随机数生成器类         
        Random random = new Random();
        g.setColor(new Color(33,150,243));
        g.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。         
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // 设置字体。         
        g.setFont(font);
        // 画边框。         
        g.setColor(new Color(33,150,243));
        g.drawRect(0, 0, width - 1, height - 1);
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。         
        StringBuffer randomCode = new StringBuffer();
        // 随机产生codeCount数字的验证码。         
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。         
            String strRand = String.valueOf(codeSequence[random.nextInt(34)]);
            g.setColor(Color.WHITE);
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。         
            randomCode.append(strRand);
        }
        mcc.delete(SecurityUtils.getSubject().getSession().getId()+"_gl");
        mcc.set(SecurityUtils.getSubject().getSession().getId()+"_gl", randomCode.toString().toLowerCase(), mcc.cacheTime(30*60*1000));
        // 将图像输出到Servlet输出流中。         
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
        return null;
    }
    /**
     * 退出登录
     * @return
     */
    @RequestMapping(WebConstants.LOGOUT)
    public String logout() {
        Subject currentSubject = SecurityUtils.getSubject();

        currentSubject.logout();
        return "redirect:/login";
    }

    @RequestMapping("/verifyLogin")
    @ResponseBody
    public ApiResponse verifyLogin() {
        ApiResponse result = new ApiResponse();
        Subject currentSubject = SecurityUtils.getSubject();
        if(currentSubject.isAuthenticated()) {
            result.setStatus(1);
        }
        return result;
    }


    @RequestMapping("/sendPhoneCode")
    @ResponseBody
    public ApiResponse sendPhoneCode(String phone) throws Exception {
        ApiResponse result = new ApiResponse();
        String phoneStr = (String)mcc.get("enjoy_"+phone);
        if(!Tools.isBlank(phoneStr)) {
            result.setStatus(-1);
            result.setMsg("请求频繁");
        }else {
            String code = Tools.getRandomNumCode(6);
            boolean  flag = AliSMSUtil.sendCode(phone, code);
            if(flag) {
                mcc.set("phone_"+phone, code, Tools.addMinute(new Date(), 5));
                mcc.set("enjoy_"+phone, phone,Tools.addSeconds(new Date(), 55));
                result.setStatus(1);
            }else {
                result.setMsg("发送失败");
            }

        }
        return result;
    }


    @RequestMapping(value="/loginbind",method = RequestMethod.POST)
    public ModelAndView loginBind(WxUser wxUser, String captcha) {
        ModelAndView mav = new ModelAndView("login_bind");
        User user = us.findByPhone(wxUser.getPhone());
        if(user == null) {
            mav.addObject("binderror", "用户不存在");
            return mav;
        }
        WxUser wu = user.getWxUser();
        mav.addObject("wxuser", wu);
        String code = (String)mcc.get("phone_"+wxUser.getPhone());
        if(Tools.isBlank(code)||code.equals("captcha")) {
            mav.addObject("binderror", "验证码错误");
            return mav;
        }
        wu.setHeadUrl(wxUser.getHeadUrl());
        wu.setNickName(wxUser.getNickName());
        wu.setOpenId(wxUser.getOpenId());
        wu.setSex(wxUser.getSex());
        wu.setUnionId(wxUser.getUnionId());
        wu.setPhone(wxUser.getPhone());
        wus.saveOrUpdate(wu);
        UsernamePasswordToken loginToken = new UsernamePasswordToken(user.getAccount(), DigestUtils.md5Hex(user.getPhone()));
        Subject currentSubject = SecurityUtils.getSubject();
        currentSubject.login(loginToken);
        if (currentSubject.isAuthenticated()) {
            SecurityUtils.getSubject().getSession().setAttribute("nickName", wu.getNickName());
            SecurityUtils.getSubject().getSession().setAttribute("headUrl", wu.getHeadUrl());

        }
        return new ModelAndView("redirect:/index");
    }

    @Value("J9vP3gruQad6Q9lP")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
