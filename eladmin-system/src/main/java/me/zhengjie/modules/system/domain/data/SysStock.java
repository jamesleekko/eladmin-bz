package me.zhengjie.modules.system.domain.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sys_stock_list")
public class SysStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stockId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private SysStockItem item;

    private Integer currentQuantity;

    private LocalDateTime lastUpdated;

    private Long deptId;
}
