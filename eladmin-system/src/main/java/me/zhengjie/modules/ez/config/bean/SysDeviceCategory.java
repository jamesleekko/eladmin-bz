package me.zhengjie.modules.ez.config.bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_device_type")
public class SysDeviceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "type")
    private Integer type;
}