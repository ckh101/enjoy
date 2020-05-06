package com.hbnet.fastsh.web.vo.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
*  拼多多产品消耗统计
*/
@Data
public class PddAdReportVo implements Serializable {
    private static final long serialVersionUID = -2326066821385054868L;

    /**
    * 广告产品id
    */
    private String adProductId;

    /**
    * 展示数
    */
    private Integer displayCnt;

    /**
    * 点击数
    */
    private Integer clickCnt;

    /**
    * 消耗金额
    */
    private Long cost;

    /**
    * 统计日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date statDate;

}