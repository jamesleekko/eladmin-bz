package me.zhengjie.modules.system.domain.data;

import lombok.Data;
import me.zhengjie.modules.system.domain.Dept;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_article_list")
public class NewsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "type", insertable = false, updatable = false)
    private NewsCategory articleType;

    @ManyToOne
    @JoinColumn(name = "article_kind_name", referencedColumnName = "name", insertable = false, updatable = false)
    private NewsKind kind;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id", insertable = false, updatable = false)
    private Dept dept;

    @Column(name = "update_time")
    private String updateTime;
}
