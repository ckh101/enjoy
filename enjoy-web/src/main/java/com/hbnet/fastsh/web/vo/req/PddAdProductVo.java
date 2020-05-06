package com.hbnet.fastsh.web.vo.req;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
*  拼多多店铺广告产品
*/
@Data
public class PddAdProductVo implements Serializable {
    private static final long serialVersionUID = 8571944478384002903L;

    /**
    * 产品名称
    */
    @NotNull(message = "产品名称不能为空")
    private String name;

    /**
    * 投放链接
    */
    @NotNull(message = "投放链接不能为空")
    private String targetUrl;

    /**
    * 人群包链接
    */
    private String crowdPackUrl;
}