package me.zhengjie.modules.dayufeng.repository;

import me.zhengjie.modules.dayufeng.data.SysDeviceFreezer;
import me.zhengjie.modules.dayufeng.data.SysDeviceSmokeAlarm;
import me.zhengjie.modules.ez.config.bean.SysDeviceCamera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysDeviceSmokeRepository extends JpaRepository<SysDeviceSmokeAlarm, Long> {

    // 根据deptId查询烟雾报警器列表
    List<SysDeviceSmokeAlarm> findByDeptIdIn(List<Long> deptIds);
}