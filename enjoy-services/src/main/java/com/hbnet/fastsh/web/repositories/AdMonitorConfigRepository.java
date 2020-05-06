package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AdMonitorConfig;

/**
 * @ClassName: AdMonitorConfigRepository
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/19 11:08
 */
public interface AdMonitorConfigRepository extends BaseRepository<AdMonitorConfig, Long> {
    AdMonitorConfig findByAdGroupId(Integer adGroupId);
    AdMonitorConfig findByAdId(int adId);
}
