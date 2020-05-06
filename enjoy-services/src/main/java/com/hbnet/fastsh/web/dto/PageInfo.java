package com.hbnet.fastsh.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: PageInfo
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/18 14:43
 */
@Data
public class PageInfo {
    @JSONField(name = "total_number")
    private int totalNumber;

    @JSONField(name = "page_size")
    private int pageSize;

    @JSONField(name = "total_page")
    private int totalPage;

    @JSONField(name = "page")
    private int page;
}
