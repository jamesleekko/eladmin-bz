package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.SysStockInboundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockInboundRecordRepository extends JpaRepository<SysStockInboundRecord, Long> {

    // 查询某个部门的入库记录
    List<SysStockInboundRecord> findByDeptId(Long deptId);

    // 查询指定物品的入库记录
    List<SysStockInboundRecord> findByItem_ItemId(Long itemId);

    // 按时间范围查询入库记录
    List<SysStockInboundRecord> findByInboundTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // 查询指定部门和时间范围的入库记录
    List<SysStockInboundRecord> findByDeptIdAndInboundTimeBetween(Long deptId, LocalDateTime startTime, LocalDateTime endTime);
}
