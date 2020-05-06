package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.web.annotations.AdRequestAuth;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.Campaign;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.service.impl.AdService;
import com.hbnet.fastsh.web.service.impl.CampaignService;
import com.hbnet.fastsh.web.service.task.BatchTaskService;
import com.hbnet.fastsh.web.vo.req.BatchAdTaskParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: BatchTaskController
 * @Auther: zoulr@qq.com
 * @Date: 2019/8/22 11:47
 * @Description: 批量任务接口
 */
@Controller
@RequestMapping("admin/ad/task/batch")
public class BatchTaskController {
    @Autowired
    private BatchTaskService batchTaskService;

    @Autowired
    private AdService adService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private RedisService redisService;

    @PostMapping("add/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse addBatchTask(HttpServletRequest request, @RequestBody BatchAdTaskParam param) {
        if (CollectionUtils.isEmpty(param.getIdList())) {
            return ApiResponse.error("广告ID参数有误！");
        }

        if (param.getIdList().size() > 100) {
            return ApiResponse.error(String.format("最多支持操作100条广告！当前已选中%s条", param.getIdList().size()));
        }

        Advertiser adv = (Advertiser) request.getAttribute("adv");
        if (!adService.checkAdOwner(adv.getAccountId(), param.getIdList())) {
            return ApiResponse.error("当前选中的广告包含无权限或无效数据！");
        }

        String taskId;
        switch (StringUtils.defaultString(param.getTaskType())) {
            case "adCopy": // 复制任务
                Campaign campaign = null;
                if(param.getCampaignId() > 0){
                    campaign = campaignService.findById(param.getCampaignId());
                    if (campaign == null) {
                        return ApiResponse.error("未找到选中计划！");
                    }
                }
                taskId = batchTaskService.createCopyTask(adv, campaign, param.getIdList());
                break;
            case "adSuspend": // 暂停任务
                taskId = batchTaskService.createSuspendTask(adv, param.getIdList());
                break;
            case "adActivate": // 启动任务
                taskId = batchTaskService.createActivateTask(adv, param.getIdList());
                break;
            case "adDelete": // 删除任务
                taskId = batchTaskService.createDeleteTask(adv, param.getIdList());
                break;
            default:
                return ApiResponse.error("任务类型有误！");
        }

        JSONObject data = new JSONObject();
        data.put("taskId", taskId);

        return ApiResponse.ok(data);
    }

    /**
     * 查询任务处理进度
     * @param taskId
     * @return
     */
    @GetMapping("/queryTaskState")
    @ResponseBody
    public ApiResponse queryTaskStat(String taskId) {
        String cacheKey = "smartad-task-" + taskId;
        String cache = redisService.get(cacheKey);
        if (StringUtils.isBlank(cache)) {
            return ApiResponse.error("未找到指定任务！");
        }

        User loginUser = SessionUtil.getCurUser();
        JSONObject json = JSON.parseObject(cache);
        if (!loginUser.getId().equals(json.getLong("creatorId"))) {
            return ApiResponse.error("未找到指定任务！");
        }

        if (json.containsKey("succedCnt")) {
            return ApiResponse.ok(json);
        } else {
            return ApiResponse.result(1, "任务处理中……", null);
        }
    }
}
