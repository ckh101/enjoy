package com.hbnet.fastsh.web.service.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hbnet.fastsh.web.service.impl.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DownLoadGdtOrderJob implements SimpleJob {

    @Autowired
    AgentService agentService;
    @Override
    public void execute(ShardingContext shardingContext) {
        LocalDate oneDaysAgo = LocalDate.now().minusDays(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yest = dtf.format(oneDaysAgo);
        agentService.downLoadGdtOrder(yest,yest);
    }
}
