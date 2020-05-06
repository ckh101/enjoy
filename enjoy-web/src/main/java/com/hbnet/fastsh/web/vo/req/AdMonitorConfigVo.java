package com.hbnet.fastsh.web.vo.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @ClassName: AdMonitorConfigVo
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/23 10:56
 */
@Data
public class AdMonitorConfigVo implements Serializable {
    @NotNull(message = "广告标识不能为空")
    private Integer adId;

    @Min(value = 0, message = "订单通知间隔配置有误")
    @NotNull(message = "订单通知间隔配置不能为空")
    private Integer dailyOrderInterval;

    @Min(value = 0, message = "首单预警配置有误")
    @Max(value = 1, message = "首单预警配置有误")
    @NotNull(message = "首单预警配置不能为空")
    private Integer dailyFirstOrderAlarm;

    @Min(value = 0, message = "消耗预警配置有误")
    @NotNull(message = "消耗预警配置不能为空")
    private Integer dailyCostThreshold;

    @NotNull(message = "消耗剩余预警配置不能为空")
    private Double dailyBudgetThreshold;

    @NotNull(message = "ROI预警设置不能为空")
    private Double roiThreshold;

    @Min(value = 0, message = "ROI自动暂停设置有误")
    @Max(value = 1, message = "ROI自动暂停设置有误")
    @NotNull(message = "ROI自动暂停设置不能为空")
    private Integer roiAlarmAutoPause;

    @Pattern(regexp = "^1[3456789][0-9]{9}(\\,1[3456789][0-9]{9}){0,4}$",message = "接收手机格式错误")
    @NotNull(message = "接收手机设置不能为空")
    private String receivePhone;

}
