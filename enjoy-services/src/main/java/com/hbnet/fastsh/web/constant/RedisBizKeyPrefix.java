package com.hbnet.fastsh.web.constant;

/**
 * @ClassName: RedisBizKeyPrefix
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/19 14:32
 * @Description: Redis业务key前缀
 */
public class RedisBizKeyPrefix {
    /**
     * 针对计划，首单通知标识前缀
     */
    public static final String CAMPAIGN_FIRST_ORDER_NOTIFY = "smt-cp_fst_odr_ntf-";

    /**
     * 针对计划，当天消耗超过通知
     */
    public static final String CAMPAIGN_COST_NOTIFY = "smt-cp_cost_ntf-";

    /**
     * 针对计划，预算低于设置值通知
     */
    public static final String CAMPAIGN_BUDGET_LEFT_NOTIFY = "smt-cp_bgt_left_ntf-";

    /**
     * 针对计划，roi低于某个值通知
     */
    public static final String CAMPAIGN_ROI_NOTIFY = "smt-cp_roi_ntf-";
}
