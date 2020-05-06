package com.hbnet.fastsh.web.service.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hbnet.fastsh.web.service.impl.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshAgentTokenJob implements SimpleJob {

    @Autowired
    AgentService agentService;

    @Override
    public void execute(ShardingContext shardingContext) {
        agentService.refreshToken(1L);
    }
}
