package me.zhengjie.modules.system.domain.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "sys_stock_item_list")
public class SysStockItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private String description;

    public enum Category {
        VEGETABLE, FRUIT, MEAT, RICE, SEASONING, EGG
    }

    public enum Unit {
        KG, ITEM
    }

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JSONField(serialize = false)
    private List<SysStock> stocks; // 一对多关联
}