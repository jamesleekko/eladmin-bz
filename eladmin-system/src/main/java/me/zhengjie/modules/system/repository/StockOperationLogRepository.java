package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.SysStockOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockOperationLogRepository extends JpaRepository<SysStockOperationLog, Long> {

    // 查询指定物品的操作日志
    List<SysStockOperationLog> findByItem_ItemId(Long itemId);

    // 查询某个部门的操作日志（通过入库/出库记录关联）
    @Query("SELECT l FROM SysStockOperationLog l WHERE l.item.itemId IN " +
            "(SELECT s.item.itemId FROM SysStock s WHERE s.deptId = :deptId)")
    List<SysStockOperationLog> findByDeptId(@Param("deptId") Long deptId);

    // 按时间范围查询操作日志
    List<SysStockOperationLog> findByOperationTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // 查询指定部门和时间范围的操作日志
    @Query("SELECT l FROM SysStockOperationLog l WHERE l.item.itemId IN " +
            "(SELECT s.item.itemId FROM SysStock s WHERE s.deptId = :deptId) " +
            "AND l.operationTime BETWEEN :startTime AND :endTime")
    List<SysStockOperationLog> findByDeptIdAndOperationTimeBetween(
            @Param("deptId") Long deptId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
