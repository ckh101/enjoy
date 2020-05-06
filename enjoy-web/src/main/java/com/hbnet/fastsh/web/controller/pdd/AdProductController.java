package com.hbnet.fastsh.web.controller.pdd;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.Num62;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.utils.UploadUtils;
import com.hbnet.fastsh.web.constant.PddAdProductStatus;
import com.hbnet.fastsh.web.entity.PddAdProduct;
import com.hbnet.fastsh.web.entity.PddStore;
import com.hbnet.fastsh.web.exception.ServiceException;
import com.hbnet.fastsh.web.service.impl.PddAdProductService;
import com.hbnet.fastsh.web.service.impl.PddStoreService;
import com.hbnet.fastsh.web.vo.req.PddAdProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName: AdProductController
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/26 11:26
 * @Description: 拼多多店铺广告产品相关
 */
@RequestMapping("admin/pdd/adProduct")
@Controller
@Slf4j
public class AdProductController extends PddController{
    private static final Pattern HOST_PATTERN = Pattern.compile("(?<=http[s]?://|\\.)[^.]*?\\.com",Pattern.CASE_INSENSITIVE);
    @Autowired
    private PddStoreService pddStoreService;

    @Autowired
    private PddAdProductService pddAdProductService;

    /**
     * 添加产品
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResponse add(@Validated PddAdProductVo param, BindingResult br, String storeId) {
        if (br.hasErrors()) {
            return ApiResponse.error(br.getFieldError().getDefaultMessage());
        }

        if (!checkStoreId(storeId)) {
            return ApiResponse.error("权限不足！");
        }

        PddStore store = pddStoreService.selectByStoreId(storeId);
        if (store == null) {
            return ApiResponse.error("店铺不存在！");
        }

        if (!checkTargetUrl(param.getTargetUrl())) {
            return ApiResponse.error("仅允许投放yangkeduo.com或其子域的链接！");
        }

        if (!checkCrowdPackUrl(param.getCrowdPackUrl())) {
            return ApiResponse.error("人群包文件格式有误！");
        }

        PddAdProduct record = new PddAdProduct();
        BeanUtils.copyProperties(param, record);

        String productId = Num62.longToN62(System.currentTimeMillis()) + RandomStringUtils.random(4, true, true);
        record.setAdProductId(productId);
        record.setStoreId(storeId);
        record.setStatus(1); // 待提交
        record.setCreateTime(new Date());

        pddAdProductService.save(record);

        return ApiResponse.ok();
    }

    /**
     * 修改产品
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ApiResponse update(PddAdProductVo param, String adProductId) {
        PddAdProduct record = pddAdProductService.selectByAdProductId(adProductId);
        if (record == null) {
            return ApiResponse.error("找不到指定记录！");
        }

        if (!checkStoreId(record.getStoreId())) {
            return ApiResponse.error("权限不足！");
        }

        if (record.getStatus() == PddAdProductStatus.COMPLETE.getCode()) {
            return ApiResponse.error("已经投放完成，不允许修改！");
        }

        if (record.getStatus() == PddAdProductStatus.WAIT_POST.getCode()) { // 待投放时允许修改其他属性
            if (StringUtils.isBlank(param.getName())) {
                return ApiResponse.error("产品名称不允许为空！");
            }

            if (StringUtils.isBlank(param.getTargetUrl())) {
                return ApiResponse.error("投放链接不允许为空！");
            }

            if (!checkTargetUrl(param.getTargetUrl())) {
                return ApiResponse.error("仅允许投放yangkeduo.com或其子域的链接！");
            }

            BeanUtils.copyProperties(param, record);
        } else {
            record.setCrowdPackUrl(param.getCrowdPackUrl()); // 其他状态仅允许修改人群包
        }

        record.setUpdateTime(new Date());
        pddAdProductService.save(record);

        return ApiResponse.ok();
    }


    /**
     * 设置投放金额并提交待投放，并冻结资金
     * @return
     */
    @PostMapping("/place")
    @ResponseBody
    public ApiResponse place(String adProductId, long amount) {
        PddAdProduct record = pddAdProductService.selectByAdProductId(adProductId);
        if (record == null) {
            return ApiResponse.error("找不到指定记录！");
        }

        if (!checkStoreId(record.getStoreId())) {
            return ApiResponse.error("权限不足！");
        }

        if (StringUtils.isBlank(record.getTargetUrl())) {
            return ApiResponse.error("投放链接未设置！");
        }

        if (amount < 100000 || amount % 10000 != 0) { // 此处amount单位是分
            return ApiResponse.error("投放金额需>1000元且整百！");
        }

        if (record.getStatus() > 1) {
            return ApiResponse.error("已经提交投放，不允许修改！");
        }

        try {
            pddStoreService.place(pddStoreService.selectByStoreId(record.getStoreId()), record, amount, SessionUtil.getCurUser());
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                return ApiResponse.error(ex.getMessage());
            } else {
                log.error("投放产品出错!adProductId={},amount={}", adProductId, amount, ex);
                return ApiResponse.error("投放产品时出错！");
            }
        }

        return ApiResponse.ok();
    }

    /**
     * 取消投放。将产品状态改回待提交，并返还冻结资金至可用余额
     * <br/>只分配给管理员操作
     * @return
     */
    @PostMapping("/cancelPlace")
    @ResponseBody
    public ApiResponse cancelPlace(String adProductId) {
        PddAdProduct record = pddAdProductService.selectByAdProductId(adProductId);
        if (record == null) {
            return ApiResponse.error("找不到指定记录！");
        }

        if (record.getStatus() == 1) {
            return ApiResponse.error("该产品未提交投放！");
        }

        try {
            pddStoreService.unPlace(pddStoreService.selectByStoreId(record.getStoreId()), record, SessionUtil.getCurUser());
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                return ApiResponse.error(ex.getMessage());
            } else {
                log.error("撤销产品投放出错!adProductId={}", adProductId, ex);
                return ApiResponse.error("撤销产品投放时出错！");
            }
        }

        return ApiResponse.ok();
    }

    /**
     * 查看投放产品列表，非管理员只能查看自己的店铺
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(required = false, defaultValue = "") String storeId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int pageSize,
                             @RequestParam Map<String, String> params) {
        pageSize = Math.max(5, Math.min(pageSize, 100)); // 5-100条
        pageNumber = Math.max(1, pageNumber); // 页码最小为1

        ModelAndView view = new ModelAndView("pdd/adProductList");
        Page<PddAdProduct> page;
        if (!checkStoreId(storeId)) { // 非管理员只能看指定店铺
            page = new PageImpl(Collections.emptyList(), PageRequest.of(pageNumber - 1, pageSize), 0);
        } else {
            Map<Integer, Long> statusGroupBy = null; // 按状态分组统计数量

            if (StringUtils.isNotBlank(storeId)) {
                params.put("search_EQ_storeId", storeId);

                statusGroupBy = pddAdProductService.countGroupByStatus(storeId);
                view.addObject("statusCnt", statusGroupBy);
            }

            page = pddAdProductService.page(pddAdProductService.getSearchFilter(params), pageNumber, pageSize);
        }

        view.addObject("page", page);
        return view;
    }

    /**
     * 获取投放产品详情
     * @return
     */
    @RequiresPermissions("/admin/pdd/adProduct/update")
    @GetMapping("/detail")
    @ResponseBody
    public ApiResponse detail(String adProductId) {
        PddAdProduct record = pddAdProductService.selectByAdProductId(adProductId);
        if (record == null) {
            return ApiResponse.error("找不到指定记录！");
        }

        if (!checkStoreId(record.getStoreId())) {
            return ApiResponse.error("权限不足！");
        }

        return ApiResponse.ok(record);
    }


    /**
     * 修改投放产品的状态
     * <br/>只分配给管理员操作
     * @return
     */
    @PostMapping("/updatePlaceStatus")
    @ResponseBody
    public ApiResponse updatePlaceStatus(String adProductId, int status) {
        PddAdProduct record = pddAdProductService.selectByAdProductId(adProductId);
        if (record == null) {
            return ApiResponse.error("找不到指定记录！");
        }

        PddAdProductStatus newStatus = PddAdProductStatus.getByCode(status);
        if (newStatus == null) {
            return ApiResponse.error("状态设置有误！");
        }

        try {
            pddAdProductService.updatePlaceStatus(record, newStatus, SessionUtil.getCurUser());
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                return ApiResponse.error(ex.getMessage());
            } else {
                log.error("设置产品状态出错!adProductId={}", adProductId, ex);
                return ApiResponse.error("设置产品状态时出错！");
            }
        }

        return ApiResponse.ok();
    }

    /**
     * 查询最近30天内投放中和投放完成的产品列表
     * @return
     */
    @RequestMapping("/recentlyPlace")
    @ResponseBody
    public ApiResponse recentlyPlace(String storeId) {
        PddStore store = pddStoreService.selectByStoreId(storeId);
        if (store == null) {
            return ApiResponse.error("店铺不存在！");
        }

        List<PddAdProduct> productList = pddAdProductService.recentlyPlace(store);
        if (productList.isEmpty()) {
            return ApiResponse.ok(Collections.emptyList());
        }

        List<JSONObject> data = productList.stream().map(p-> {
            JSONObject json = new JSONObject();
            json.put("id", p.getAdProductId());
            json.put("text", p.getName());
            return json;
        }).collect(Collectors.toList());

        return ApiResponse.ok(data);
    }

    /**
     * 上传人群包
     * @return
     */
    @PostMapping("/uploadCrowdPack")
    @ResponseBody
    public ApiResponse uploadCrowdPack(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".zip") && !fileName.endsWith(".txt")) {
            return ApiResponse.error("请上传zip或txt格式的文件！");
        }

        String key = "smartad/crowdPack/" + System.currentTimeMillis() + "_"+ fileName;
        try {
            String url = UploadUtils.upImgBytesToQiniu(key, file.getBytes());
            JSONObject data = new JSONObject();
            data.put("fileName", fileName);
            data.put("url", url);

            return ApiResponse.ok(data);
        } catch (IOException e) {
            log.error("上传人群包文件失败！", e);
            return ApiResponse.error("上传失败，系统繁忙！");
        }
    }

    private boolean checkTargetUrl(String targetUrl) {
        Matcher matcher = HOST_PATTERN.matcher(targetUrl);
        if (!matcher.find()) {
            return false;
        }

        String host = matcher.group();
        return host.equals("yangkeduo.com") || host.endsWith(".yangkeduo.com");
    }

    private boolean checkCrowdPackUrl(String crowdPackUrl) {
        return StringUtils.isEmpty(crowdPackUrl) || crowdPackUrl.matches("^https://.*?/smartad/crowdPack/.*?\\.(txt|zip)$");
    }
}
