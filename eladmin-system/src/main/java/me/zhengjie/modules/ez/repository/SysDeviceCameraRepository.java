package me.zhengjie.modules.ez.repository;

import me.zhengjie.modules.ez.config.bean.SysDeviceCamera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysDeviceCameraRepository extends JpaRepository<SysDeviceCamera, Long> {

    // 根据 deviceSerial 查找设备
    Optional<SysDeviceCamera> findByDeviceSerial(String deviceSerial);

    // 根据部门ID列表查询设备
    List<SysDeviceCamera> findByDeptIdIn(List<Long> deptIds);
}