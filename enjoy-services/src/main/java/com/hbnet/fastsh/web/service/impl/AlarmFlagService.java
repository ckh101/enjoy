package com.hbnet.fastsh.web.service.impl;

import com.hbnet.fastsh.web.entity.AlarmFlag;
import com.hbnet.fastsh.web.repositories.AlarmFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: AlarmFlagService
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/29 10:07
 */
@Service
public class AlarmFlagService {
    public static final String FLAG_ROI_THRESHOLD_ALARM = "roiThresholdAlarm";
    @Autowired
    private AlarmFlagRepository alarmFlagRepository;

    public AlarmFlag findByFlag(String flag) {
        return alarmFlagRepository.findByFlag(flag);
    }

    public void delete(AlarmFlag alarmFlag) {
        alarmFlagRepository.delete(alarmFlag);
    }

    public void deleteByFlag(String flag) {
        alarmFlagRepository.deleteAllByFlag(flag);
    }

    public AlarmFlag createFlag(String flag, String content) {
        AlarmFlag alarmFlag = new AlarmFlag();
        alarmFlag.setFlag(flag);
        alarmFlag.setContent(content);
        alarmFlag.setCreateTime(new Date());

        alarmFlagRepository.save(alarmFlag);

        return alarmFlag;
    }

    public void clearFlag(String flag) {
        alarmFlagRepository.deleteAllByFlag(flag);

    }
}
