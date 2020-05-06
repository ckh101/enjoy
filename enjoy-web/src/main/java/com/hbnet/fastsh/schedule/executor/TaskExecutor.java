package com.hbnet.fastsh.schedule.executor;

import com.hbnet.fastsh.web.vo.task.AdTaskParam;

public interface TaskExecutor {
    void submit(AdTaskParam taskParam);
}
