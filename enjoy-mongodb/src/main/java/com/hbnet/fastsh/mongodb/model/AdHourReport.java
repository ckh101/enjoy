package com.hbnet.fastsh.mongodb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "smartad_AdHourReport")
public class AdHourReport {

    @Id
    private String _id;

    private Integer accountId;

    private String date;

    private Integer hour;

    private Integer campaign_id;

    private Integer adgroup_id;

    private Integer ad_id;

    private String promoted_object_type;

    private String promoted_object_id;

    private Integer view_count;

    private Integer valid_click_count;

    private Float ctr;

    private Integer cpc;

    private Integer thousand_display_price;

    private Integer cost;

    private Integer activated_count;

    private Integer activated_cost;

    private Float activated_rate;

    private Integer app_add_to_car_amount;

    private Integer app_add_to_car_count;

    private Integer app_add_to_car_cost;

    private Integer app_key_page_view_count;

    private Integer app_checkout_count;

    private Integer app_checkout_cost;

    private Integer app_checkout_amount;

    private Integer app_commodity_page_view_count;

    private Integer app_commodity_page_view_cost;

    private Float app_commodity_page_view_rate;

    private Integer app_register_count;

    private Integer app_register_cost;

    private Integer app_order_count;

    private Integer app_order_cost;

    private Integer app_application_count;

    private Integer app_application_cost;

    private Integer comment_cost;

    private Integer comment_count;

    private Integer click_activated_rate;

    private Integer download_count;

    private Float download_rate;

    private Integer download_cost;

    private Integer follow_count;

    private Integer follow_cost;

    private Integer forward_count;

    private Integer forward_cost;

    private Integer install_count;

    private Integer install_cost;

    private Integer image_click;

    private Integer like_or_comment;

    private Integer own_key_page_view_cost;

    private Integer own_key_page_view_count;

    private Integer own_page_coupon_get_count;

    private Integer own_page_coupon_get_cost;

    private Integer own_page_navigation_cost;

    private Integer own_page_navigation_count;

    private Integer platform_key_page_view_count;

    private Integer platform_key_page_view_cost;

    private Integer platform_page_navigation_count;

    private Integer platform_page_navigation_cost;

    private Integer platform_shop_navigation_count;

    private Integer platform_shop_navigation_cost;

    private Integer platform_coupon_get_count;

    private Integer platform_coupon_get_cost;

    private Integer praise_count;

    private Integer praise_cost;

    private Float retention_rate;

    private Integer retention_cost;

    private Integer read_count;

    private Integer read_cost;

    private Integer retention_count;

    private Integer web_key_page_view_count;

    private Integer web_key_page_view_cost;

    private Integer web_add_to_car_count;

    private Integer web_add_to_car_cost;

    private Integer web_checkout_count;

    private Integer web_phone_call_direct_count;

    private Integer web_phone_call_back_count;

    private Integer web_phone_call_direct_cost;

    private Integer web_phone_call_back_cost;

    private Integer web_checkout_amount;

    private Integer web_checkout_cost;

    private Integer web_page_reservation_count;

    private Integer web_page_reservation_cost;

    private Float web_page_reservation_rate;

    private Integer web_commodity_page_view_count;

    private Float  web_commodity_page_view_rate;

    private Integer  web_commodity_page_view_cost;

    private Integer web_application_cost;

    private Integer web_deliver_count;

    private Integer web_sign_in_count;

    private Integer web_order_count;

    private Float web_order_rate;

    private  Integer web_order_unit_price;

    private Integer  web_order_roi;

    private Integer web_order_cost;

    private Integer web_order_amount;

    private Integer web_application_count;

    private Integer  web_register_cost;

    private Integer web_register_count;

    private Integer web_consult_count;

    private Integer  web_consult_cost;

    private Integer web_sign_in_cost;

    private Integer web_deliver_cost;

    private Integer read;

    private Integer share;
}
