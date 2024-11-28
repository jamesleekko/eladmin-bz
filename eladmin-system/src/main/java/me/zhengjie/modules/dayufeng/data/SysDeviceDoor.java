package me.zhengjie.modules.dayufeng.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_device_door")
public class SysDeviceDoor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "door_guid")
    private String doorGuid;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "zb_type")
    private Integer ZBType;
}