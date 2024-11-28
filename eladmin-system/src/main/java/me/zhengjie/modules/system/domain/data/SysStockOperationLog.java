package me.zhengjie.modules.system.domain.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sys_stock_operation_log")
public class SysStockOperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private SysStockItem item;

    private Integer quantity;
    private String operator;
    private LocalDateTime operationTime;
    private String note;

    public enum OperationType {
        INBOUND, OUTBOUND
    }
}
