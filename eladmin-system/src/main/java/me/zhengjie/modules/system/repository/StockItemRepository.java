package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.SysStockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockItemRepository extends JpaRepository<SysStockItem, Long> {
    /**
     * 根据名称查询库存物品
     *
     * @param name 物品名称
     * @return SysStockItem
     */
    Optional<SysStockItem> findByName(String name);

    /**
     * 根据类别查询库存物品列表
     *
     * @param category 类别
     * @return List<SysStockItem>
     */
    List<SysStockItem> findByCategory(SysStockItem.Category category);

    /**
     * 模糊搜索物品名称
     *
     * @param name 物品名称关键字
     * @return List<SysStockItem>
     */
    @Query("SELECT s FROM SysStockItem s WHERE s.name LIKE %:name%")
    List<SysStockItem> searchByName(@Param("name") String name);

    /**
     * 获取某种类别的物品数量
     *
     * @param category 类别
     * @return long
     */
    @Query("SELECT COUNT(s) FROM SysStockItem s WHERE s.category = :category")
    long countByCategory(@Param("category") SysStockItem.Category category);

    /**
     * 删除指定类别的所有物品
     *
     * @param category 类别
     */
    void deleteByCategory(SysStockItem.Category category);

    /**
     * 查询所有单位为某种类型的物品
     *
     * @param unit 单位
     * @return List<SysStockItem>
     */
    List<SysStockItem> findByUnit(SysStockItem.Unit unit);
}
