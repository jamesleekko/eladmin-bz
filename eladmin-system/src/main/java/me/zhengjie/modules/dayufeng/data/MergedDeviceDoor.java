package me.zhengjie.modules.dayufeng.data;

import lombok.Data;

@Data
public class MergedDeviceDoor {
    private Integer id;

    private String doorName;

    private String doorSN;

    private Integer doorStatus;

    private Integer lineWay;

    private String doorGuid;

    private Long deptId;

    private Integer ZBType;
}
