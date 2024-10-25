package me.zhengjie.modules.dayufeng.repository;

import me.zhengjie.modules.dayufeng.data.SysDeviceFreezer;
import me.zhengjie.modules.ez.config.bean.SysDeviceCamera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysDeviceFreezerRepository extends JpaRepository<SysDeviceFreezer, Long> {

    // 根据deptId查询冰箱列表
    List<SysDeviceFreezer> findByDeptIdIn(List<Long> deptIds);
}