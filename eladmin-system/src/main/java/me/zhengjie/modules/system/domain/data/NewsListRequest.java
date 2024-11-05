package me.zhengjie.modules.system.domain.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsListRequest {
    private Integer type;
    private Integer page;
    private Integer pageSize;
}