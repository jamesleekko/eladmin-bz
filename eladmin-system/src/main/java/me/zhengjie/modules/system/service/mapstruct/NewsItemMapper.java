package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.modules.system.domain.data.NewsItem;
import me.zhengjie.modules.system.domain.data.NewsItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsItemMapper {
    @Mapping(source = "articleType.displayName", target = "typeName")
    @Mapping(source = "dept.name", target = "deptName")
    @Mapping(source = "kind.name", target = "kindName")
    @Mapping(source = "kind.displayName", target = "kindDisplayName")
    NewsItemDto toDto(NewsItem newsItem);

    List<NewsItemDto> toDtoList(List<NewsItem> newsItems);
}