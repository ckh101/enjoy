package com.hbnet.fastsh.web.service.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RefreshTokenJob implements SimpleJob {

    @Autowired
    AdvertiserService advertiserService;

    @Override
    public void execute(ShardingContext shardingContext) {
        int count = (int)advertiserService.countNeedRefreshToken();
        PageInfo<Advertiser> pageInfo = new PageInfo<>();
        if(count > 0){
            if(count > 10){
                int pageSize = count/shardingContext.getShardingTotalCount()+count%shardingContext.getShardingTotalCount();
                pageInfo.setPageNumber(shardingContext.getShardingItem()+1);
                pageInfo.setPageSize(pageSize);
                process(pageInfo);
            }else if(shardingContext.getShardingItem() == 0){
                pageInfo.setPageNumber(1);
                pageInfo.setPageSize(count);
                process(pageInfo);
            }

        }
    }
    private void process(PageInfo<Advertiser> pageInfo){
        List<Advertiser> list = advertiserService.getNewRefreshTokenPageList(pageInfo);
        if(list != null && !list.isEmpty()){
            list.forEach(advertiser -> {
                advertiserService.refreshToken(advertiser.getId());
            });
        }
    }
}
