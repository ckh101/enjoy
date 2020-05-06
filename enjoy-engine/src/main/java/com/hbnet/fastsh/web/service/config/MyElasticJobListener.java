package com.hbnet.fastsh.web.service.config;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyElasticJobListener  implements ElasticJobListener {

    private long beginTime = 0;
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
    }
}
