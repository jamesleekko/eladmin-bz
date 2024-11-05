package me.zhengjie.modules.system.domain.data;

import lombok.Data;

@Data
public class NewsItemDto {
    private Long id;
    private String title;
    private String fileName;
    private String filePath;
    private String content;
    private String updateTime;
    private String typeName;
    private String deptName;
    private String kindName;
    private String kindDisplayName;
}
