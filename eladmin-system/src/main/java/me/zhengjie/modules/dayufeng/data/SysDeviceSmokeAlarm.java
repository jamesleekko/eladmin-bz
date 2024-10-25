package me.zhengjie.modules.dayufeng.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_device_smoke")
public class SysDeviceSmokeAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "device_no", unique = true)
    private String deviceNo;

    @Column(name = "device_alias")
    private String deviceAlias;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "zb_type")
    private Integer ZBType;
}