package com.hbnet.fastsh.mongodb.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "smartad_GdtOrder")
public class GdtOrder {

    private String _id;

    private String ecommerce_order_id;

    private Integer account_id;

    private String account_name;

    private String user_name;

    private String user_phone;

    private String customized_page_name;

    private String commodity_package_detail;

    private Integer quantity;

    private Integer price;

    private Integer total_price;

    private String user_province;

    private String user_city;

    private String user_area;

    private String user_address;

    private String  user_ip;

    private String user_message;

    private String destination_url;

    private String ecommerce_order_time;
}
