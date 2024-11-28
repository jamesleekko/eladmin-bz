package me.zhengjie.modules.dayufeng.repository;

import me.zhengjie.modules.dayufeng.data.Category;
import me.zhengjie.modules.dayufeng.data.SysDeviceTempAndHumi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeviceTempHumiRepository extends JpaRepository<SysDeviceTempAndHumi, Long> {

    List<SysDeviceTempAndHumi> findByDeptIdIn(List<Long> deptIds);

    List<SysDeviceTempAndHumi> findByDeptIdInAndCategory (List<Long> deptIds, Category category);
}
