package me.zhengjie.modules.system.domain.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sys_stock_outbound_records")
public class SysStockOutboundRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboundId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private SysStockItem item;

    private Integer quantity;
    private String reason = "常规出库";
    private LocalDateTime outboundTime;
    private String operator;
    private String note;
    private Long deptId;
}