package me.zhengjie.modules.ez.config.bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_device_camera")
public class SysDeviceCamera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_serial", unique = true)
    private String deviceSerial;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "device_ip")
    private String deviceIp;

    @Column(name = "device_status")
    private Integer deviceStatus;

    @Column(name = "camera_no")
    private Integer cameraNo;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "zb_type")
    private Integer ZBType;
}
