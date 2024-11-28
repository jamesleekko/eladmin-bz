package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.SysStockOutboundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockOutboundRecordRepository extends JpaRepository<SysStockOutboundRecord, Long> {

    // 查询某个部门的出库记录
    List<SysStockOutboundRecord> findByDeptId(Long deptId);

    // 查询指定物品的出库记录
    List<SysStockOutboundRecord> findByItem_ItemId(Long itemId);

    // 按时间范围查询出库记录
    List<SysStockOutboundRecord> findByOutboundTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // 查询指定部门和时间范围的出库记录
    List<SysStockOutboundRecord> findByDeptIdAndOutboundTimeBetween(Long deptId, LocalDateTime startTime, LocalDateTime endTime);
}