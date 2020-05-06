package com.hbnet.fastsh.web.service.job;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hbnet.fastsh.utils.JsonUtils;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.AdService;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class SyncAdJob implements SimpleJob {
    private int PARALLEL_SIZE = 10; // 任务并行最大数量
    private Semaphore semaphore = new Semaphore(PARALLEL_SIZE, true);
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(PARALLEL_SIZE, PARALLEL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    @Autowired
    AdService adService;

    @Autowired
    AdvertiserService advertiserService;
    @Override
    public void execute(ShardingContext shardingContext) {
        long count = advertiserService.countNorMalAdvertiser();
        PageInfo<Advertiser> pageInfo = new PageInfo<>();
        if(count > 0){
            if(count > 10){
                long pageSize = count/shardingContext.getShardingTotalCount()+count%shardingContext.getShardingTotalCount();
                pageInfo.setPageNumber(shardingContext.getShardingItem()+1);
                pageInfo.setPageSize((int)pageSize);
                process(pageInfo);
            }else if(shardingContext.getShardingItem() == 0){
                pageInfo.setPageNumber(1);
                pageInfo.setPageSize((int)count);
                process(pageInfo);
            }
        }
    }

    private void process(PageInfo<Advertiser> pageInfo){
        List<Advertiser> list = advertiserService.getNorMalAdvertiserPageList(pageInfo);
        if(list != null && !list.isEmpty()){
            CountDownLatch latch = new CountDownLatch(list.size());
            list.forEach(advertiser -> {
                semaphore.acquireUninterruptibly();
                pool.submit(() -> {
                    try{
                        adService.syncAdByAccountId(advertiser);
                    }finally {
                        semaphore.release();
                        latch.countDown();
                    }
                });
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
