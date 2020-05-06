package com.hbnet.fastsh.web.controller.pdd;

import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.web.entity.PddAdProduct;
import com.hbnet.fastsh.web.entity.PddAdReport;
import com.hbnet.fastsh.web.service.impl.PddAdProductService;
import com.hbnet.fastsh.web.service.impl.PddAdReportService;
import com.hbnet.fastsh.web.vo.req.PddAdReportVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: AdReportController
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/26 18:14
 * @Description: 拼多多店铺统计
 */
@RequestMapping("admin/pdd/adReport")
@Controller
@Slf4j
public class AdReportController extends PddController {
    @Autowired
    private PddAdReportService pddAdReportService;

    @Autowired
    private PddAdProductService pddAdProductService;

    private final Pattern DATE_RANGE_PATTER = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\- \\d{4}-\\d{2}-\\d{2}");

    /**
     * 查看投放产品投放数据，非管理员只能查看自己的店铺
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(required = false, defaultValue = "") String storeId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int pageSize,
                             @RequestParam Map<String, String> params, @RequestParam(required = false, defaultValue = "") String statDateRange) {
        pageSize = Math.max(5, Math.min(pageSize, 100)); // 5-100条
        pageNumber = Math.max(1, pageNumber); // 页码最小为1

        if (StringUtils.isNotBlank(statDateRange) && DATE_RANGE_PATTER.matcher(statDateRange).matches()) {
            String[] range = StringUtils.splitByWholeSeparator(statDateRange, " - ");
            params.put("search_GTE_statDate_D", range[0]);
            params.put("search_LTE_statDate_D", range[1]);
        }

        ModelAndView view = new ModelAndView("/pdd/adReportList");
        Page<PddAdReport> page;
        if (!checkStoreId(storeId)) { // 非管理员只能看指定店铺
            page = new PageImpl(Collections.emptyList(), PageRequest.of(pageNumber - 1, pageSize), 0);
        } else {
            if (StringUtils.isNotBlank(storeId)) {
                params.put("search_EQ_storeId", storeId);
            }

            page = pddAdReportService.page(pddAdReportService.getSearchFilter(params), pageNumber, pageSize);
        }

        view.addObject("page", page);
        return view;
    }

    /**
     * 添加数据
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResponse add(PddAdReportVo param) {
        PddAdProduct db = pddAdProductService.selectByAdProductId(param.getAdProductId());
        if (db == null) {
            return ApiResponse.error("该产品不存在！");
        }

        if (param.getStatDate() == null) {
            return ApiResponse.error("统计日期不能为空！");
        }

        if (pddAdReportService.checkExist(param.getAdProductId(), param.getStatDate())) {
            return ApiResponse.error("重复添加！");
        }

        PddAdReport report = new PddAdReport();
        BeanUtils.copyProperties(param, report);

        report.setStoreId(db.getStoreId());
        report.setAdProductName(db.getName());
        report.setCreateTime(new Date());

        pddAdReportService.save(report);

        return ApiResponse.ok();
    }

    /**
     * 修改数据
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ApiResponse update(PddAdReportVo param, Long id) {
        PddAdReport db = pddAdReportService.selectById(id);
        if (db == null) {
            return ApiResponse.error("该记录不存在！");
        }

        if (param.getStatDate() == null) {
            return ApiResponse.error("统计日期不能为空！");
        }

        db.setDisplayCnt(param.getDisplayCnt());
        db.setClickCnt(param.getClickCnt());
        db.setCost(param.getCost());
        db.setUpdateTime(new Date());

        pddAdReportService.save(db);

        return ApiResponse.ok();
    }


    /**
     * 获取数据项详情
     * @return
     */
    @RequiresPermissions("/admin/pdd/adReport/update")
    @GetMapping("/detail")
    @ResponseBody
    public ApiResponse detail(Long id) {
        PddAdReport report = pddAdReportService.selectById(id);
        if (report == null) {
            return ApiResponse.error("找不到指定记录！");
        }

        return ApiResponse.ok(report);
    }

    /**
     * 删除数据
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ApiResponse delete(Long id) {
        PddAdReport db = pddAdReportService.selectById(id);
        if (db == null) {
            return ApiResponse.error("该记录不存在！");
        }

        pddAdReportService.deleteById(id);

        return ApiResponse.ok();
    }

}
