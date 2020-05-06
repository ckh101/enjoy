package com.hbnet.fastsh.web.service.job.alarm;

import com.hbnet.fastsh.mongodb.model.alarm.AdDailyDataHistory;
import com.hbnet.fastsh.mongodb.repositories.alarm.AdDailyDataHistoryRepository;
import com.hbnet.fastsh.web.dto.DailyReportItem;
import com.hbnet.fastsh.web.entity.Advertiser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: YesterdayAdReportsQueryJob
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/23 17:55
 * @Description: 广告组昨天日报表定时查询任务
 */
@Component
@Slf4j
public class YesterdayAdReportsQueryJob extends AdReportsQueryJob {
    @Autowired
    private AdDailyDataHistoryRepository adDailyDataHistoryRepository;

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected boolean process(Advertiser adver) {
        try {
            DateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            Date yesterday = DateUtils.addDays(now, -1);
            List<DailyReportItem> costData = getReportDataByDate(adver, fomatter.format(yesterday));

            if (costData.isEmpty()) {
                return true;
            }

            List<AdDailyDataHistory> list = new ArrayList<>();
            for (DailyReportItem d : costData) {
                list.add(toDailyReport(d, now));
                if (list.size() % 50 == 0) {
                    adDailyDataHistoryRepository.saveAll(list); // 入库 mongodb
                    list.clear();
                }
            }

            if (list.size() > 0) {
                adDailyDataHistoryRepository.saveAll(list); // 入库 mongodb
            }
        } catch (Exception ex) {
            getLogger().error("查询前一天报表出错!account_id={},错误:{}", adver.getAccountId(), ex);
            return false;
        }

        getLogger().error("查询前一天报表成功!account_id={}", adver.getAccountId());

        return true;
    }

    private AdDailyDataHistory toDailyReport(DailyReportItem d, Date createTime) {
        AdDailyDataHistory his = new AdDailyDataHistory();
        BeanUtils.copyProperties(d, his);
        his.setCreateTime(createTime);

        return his;
    }

}
