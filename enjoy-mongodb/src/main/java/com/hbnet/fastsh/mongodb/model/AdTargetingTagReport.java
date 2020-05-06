package com.hbnet.fastsh.mongodb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "smartad_AdTargeingTagReport")
public class AdTargetingTagReport {

    @Id
    private String  _id;

    private Integer accountId;

    private Integer adgroup_id;

    private String date;

    private String age;

    private String gender;

    private Integer region_id;

    private Integer city_id;

    private Integer view_count;

    private Integer valid_click_count;

    private Float ctr;

    private Integer cpc;

    private Integer cost;

    private Integer activated_cost;

    private Float activated_rate;

    private Integer activated_count;

    private Integer app_key_page_view_count;

    private Integer app_add_to_car_count;

    private Integer app_add_to_car_cost;

    private Integer app_checkout_count;

    private Integer app_checkout_cost;

    private Integer app_register_count;

    private Integer app_register_cost;

    private Integer app_order_count;

    private Integer app_order_cost;

    private Integer click_activated_rate;

    private Integer comment_count;

    private Integer comment_cost;

    private Integer download_count;

    private Float download_rate;

    private Integer download_cost;

    private Integer follow_count;

    private Integer  follow_cost;

    private Integer forward_count;

    private Integer forward_cost;

    private Integer install_count;

    private Integer install_cost;

    private Integer own_page_navigation_count;

    private Integer praise_cost;

    private Integer praise_count;

    private Integer web_register_count;

    private Integer web_add_to_car_count;

    private Integer  web_add_to_car_cost;

    private Integer web_checkout_count;

    private Integer  web_checkout_cost;

    private Integer web_phone_call_direct_count;

    private Integer  web_phone_call_back_count;

    private Integer web_phone_call_direct_cost;

    private Integer web_phone_call_back_cost;

    private Integer web_order_cost;

    private Float web_order_rate;

    private Integer web_key_page_view_cost;

    private Integer web_key_page_view_count;

    private Integer  web_consult_count;

    private Integer   web_consult_cost;

    private Integer web_page_reservation_count;

    private Integer web_page_reservation_cost;

    private Float web_page_reservation_rate;

    private Integer read;

    private Integer share;
}
