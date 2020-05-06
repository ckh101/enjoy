package com.hbnet.fastsh.web.api;

import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.service.impl.DuoDuoTiOrderMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DuoDuoTiDataApi {

    @Autowired
    DuoDuoTiOrderMapService duoDuoTiOrderMapService;

    @RequestMapping("intf/ddt_data/district_order_counts")
    @ResponseBody
    public ApiResponse getDistrictOrderCounts(){
        ApiResponse response = new ApiResponse();
        response.setStatus(1);
        response.setData(duoDuoTiOrderMapService.getDistrictOrderCounts());
        return response;
    }

    @RequestMapping("intf/ddt_data/search_orders")
    @ResponseBody
    public ApiResponse searchOrders(String city, String district){
        ApiResponse response = new ApiResponse();
        Map<String, String> params = new HashMap<>();
        if(!Tools.isBlank(city)){
            params.put("city", city);
        }
        if(!Tools.isBlank(district)){
            params.put("district", district);
        }
        response.setStatus(1);
        response.setData(duoDuoTiOrderMapService.searchOrdersByParams(params));
        return response;
    }

}
