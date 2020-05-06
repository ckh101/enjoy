package com.hbnet.fastsh.web.vo.task;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: AdTaskParam
 * @Auther: zoulr@qq.com
 * @Date: 2019/8/23 11:05
 * @Description: 广告批量任务参数
 */
@Data
public class AdTaskParam {
    private String taskId;
    private String type;
    private int accountId;
    private int campaignId;
    private List<Long> ids;
    private Long creatorId;
}
