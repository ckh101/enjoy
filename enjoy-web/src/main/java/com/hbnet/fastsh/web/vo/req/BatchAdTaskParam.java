package com.hbnet.fastsh.web.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: BatchAdTaskParam
 * @Auther: zoulr@qq.com
 * @Date: 2019/8/22 16:23
 * @Description: 批量操作任务参数
 */
@Data
public class BatchAdTaskParam {
    private List<Long> idList;
    private long campaignId;
    private String taskType;
}
