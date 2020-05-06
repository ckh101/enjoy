package com.ckh.enjoy.redis.consumer.vo;

import lombok.Data;

/**
 * @ClassName: CommonBody
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/18 17:08
 */
@Data
public class CommonBody {
    private String bodyId;
    private String data;

    public CommonBody() {
    }

    public CommonBody(String bodyId, String data) {
        this.bodyId = bodyId;
        this.data = data;
    }
}
