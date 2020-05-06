package com.hbnet.fastsh.web.controller;

import com.hbnet.fastsh.web.annotations.AdRequestAuth;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.AdvertiserFund;
import com.hbnet.fastsh.web.enums.FundTypeEnum;
import com.hbnet.fastsh.web.service.impl.AdvertiserFundService;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RequestMapping("admin/ad/overview")
@Controller
public class OverViewController {

    @Autowired
    AdvertiserFundService advertiserFundService;

    @Autowired
    AdvertiserService advertiserService;

    @RequestMapping("index/{aId}")
    @AdRequestAuth
    public String index(ModelMap model, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        List<AdvertiserFund> funds = advertiserFundService.getFundsByAccountId(adv.getAccountId());
        if(funds != null && !funds.isEmpty()){
            model.addAttribute("funds", funds);
            int sumBalance = funds.stream().mapToInt(AdvertiserFund::getBalance).sum();
            model.addAttribute("sumBalance", sumBalance);
            Optional<AdvertiserFund> cash = funds.stream().filter(fund-> FundTypeEnum.FUND_TYPE_CASH.getValue().equals(fund.getFundType())).findFirst();
            if(cash.isPresent()){
                model.addAttribute("cash", cash.get());
            }
        }
        model.put("last7DayViewCount", advertiserService.getLast7DaysData(adv));
        model.addAttribute("adv", adv);
        return "ad/overview/index";
    }

}
