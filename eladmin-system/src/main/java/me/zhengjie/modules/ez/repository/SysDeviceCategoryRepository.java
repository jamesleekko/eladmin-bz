package me.zhengjie.modules.ez.repository;

import me.zhengjie.modules.ez.config.bean.SysDeviceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeviceCategoryRepository extends JpaRepository<SysDeviceCategory, Long> {

    // 设备类型列表
    List<SysDeviceCategory> findAll();
}