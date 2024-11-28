package me.zhengjie.modules.dayufeng.repository;

import me.zhengjie.modules.dayufeng.data.SysDeviceDoor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeviceDoorRepository extends JpaRepository<SysDeviceDoor, Long> {

    // 根据deptId查询冰箱列表
    List<SysDeviceDoor> findByDeptIdIn(List<Long> deptIds);
}