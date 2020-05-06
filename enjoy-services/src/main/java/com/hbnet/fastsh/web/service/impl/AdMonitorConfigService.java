package com.hbnet.fastsh.web.service.impl;

import com.hbnet.fastsh.web.entity.Ad;
import com.hbnet.fastsh.web.entity.AdGroup;
import com.hbnet.fastsh.web.entity.AdMonitorConfig;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.repositories.AdMonitorConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdMonitorConfigService {
    @Autowired
    private AdMonitorConfigRepository adMonitorConfigRepository;

    @Autowired
    private UserService userService;

    public void createDefaultConfig(Ad ad) {
        AdGroup adGroup = ad.getAdGroup();
        AdMonitorConfig amc = findByAdId(ad.getAdId());
        if (amc == null) {
            amc = new AdMonitorConfig();
            amc.setAccountId(ad.getAccountId());
            amc.setAdId(ad.getAdId());
            amc.setAdGroupId(adGroup.getAdGroupId());
            amc.setDailyFirstOrderAlarm(1);
            amc.setDailyOrderInterval(10);
            amc.setDailyCostThreshold(10000);
            amc.setDailyBudgetThreshold(0.2D);
            amc.setRoiThreshold(0D);
            amc.setRoiAlarmAutoPause(0);
            amc.setCreateTime(ad.getCreateTime());

            if (ad.getCreateUserId() != null) {
                User user = userService.findById(ad.getCreateUserId());
                amc.setReceivePhone(user == null ? null : user.getPhone());
            }
            saveOrUpdate(amc);
        }
    }

    public AdMonitorConfig findByGroupId(int adGroupId) {
        return adMonitorConfigRepository.findByAdGroupId(adGroupId);
    }

    public AdMonitorConfig saveOrUpdate(AdMonitorConfig amc) {
        return adMonitorConfigRepository.saveAndFlush(amc);
    }

    public AdMonitorConfig findByAdId(int adId) {
        return adMonitorConfigRepository.findByAdId(adId);
    }

    public void deleteById(Long id){
        adMonitorConfigRepository.deleteById(id);
    }
}
