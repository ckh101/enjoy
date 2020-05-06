package com.hbnet.fastsh.redis.consumer.vo;

import lombok.Data;

/**
 * @ClassName: CompleteOrderBody
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/16 16:50
 */
@Data
public class CompleteOrderBody {
    private String accountId;
    private String adGroupId;
    private String clickId;

    public CompleteOrderBody(String accountId, String adGroupId, String clickId) {
        this.accountId = accountId;
        this.adGroupId = adGroupId;
        this.clickId = clickId;
    }
}
