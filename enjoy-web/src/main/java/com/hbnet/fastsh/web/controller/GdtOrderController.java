package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.mongodb.model.GdtOrder;
import com.hbnet.fastsh.mongodb.service.base.PageInfo;
import com.hbnet.fastsh.mongodb.service.impl.GdtOrderService;
import com.hbnet.fastsh.utils.ExcelUtil;
import com.hbnet.fastsh.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin/gdt_order")
public class GdtOrderController {

    @Autowired
    GdtOrderService gdtOrderService;

    @RequestMapping()
    public String list(@RequestParam Map<String, String> params, ModelMap model){
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        PageInfo<GdtOrder> pageInfo = new PageInfo<>();
        pageInfo.setPageNumber(Tools.intValue(params.get("pageNumber"),1));
        if(!Tools.isBlank(startDate)&&!Tools.isBlank(endDate)){
            gdtOrderService.findByDate(pageInfo, startDate, endDate, params);
            model.addAllAttributes(params);
        }
        model.addAttribute("page", pageInfo);
        return "/gdtorder/list";
    }

    @RequestMapping("/ExcelDownload")
    public void excelDownload(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        if(!Tools.isBlank(startDate)&&!Tools.isBlank(endDate)){
            List<GdtOrder> orders = gdtOrderService.findByDate(null, startDate, endDate, params);
            List<List<String>> list = Lists.newArrayList();
            orders.forEach(obj->{
                List<String> l = Lists.newArrayList();
                Field[] field = obj.getClass().getDeclaredFields();
                for (int i = 0; i < field.length; i++) {
                    try {
                        field[i].setAccessible(true);
                        l.add(String.valueOf(field[i].get(obj)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        l.add("");
                    }
                }
                list.add(l);
            });
            String sheetName = "枫叶订单";
            String fileName =  "order-" + startDate+ "-" + endDate+ ".xls".replaceAll(" ","");
            ExcelUtil.exportExcel(response, list, sheetName, fileName, 15);
        }
    }

}
