package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.SysStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<SysStock, Long> {

    // 根据 itemId 和 deptId 查询库存
    List<SysStock> findByItem_ItemIdAndDeptIdIn(Long itemId, List<Long> deptIds);

    // 查询指定部门的所有库存
    List<SysStock> findByDeptIdIn(List<Long> deptIds);

    // 查询某个物品的库存总量（可用于统计）
    @Query("SELECT SUM(s.currentQuantity) FROM SysStock s WHERE s.item.itemId = :itemId")
    Integer findTotalQuantityByItemId(@Param("itemId") Long itemId);
}
