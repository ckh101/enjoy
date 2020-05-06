package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: AlarmFlag
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/29 9:54
 */
@Data
@Entity
@Table(name = "smartad_alarm_flag")
public class AlarmFlag extends BaseEntity {
    private static final long serialVersionUID = -24842445865610503L;

    private String flag;
    private String content;
}
