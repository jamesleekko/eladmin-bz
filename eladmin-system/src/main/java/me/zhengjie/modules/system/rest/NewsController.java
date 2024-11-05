package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.data.NewsCategory;
import me.zhengjie.modules.system.domain.data.NewsItem;
import me.zhengjie.modules.system.domain.data.NewsItemDto;
import me.zhengjie.modules.system.domain.data.NewsListRequest;
import me.zhengjie.modules.system.repository.NewsCategoryRepository;
import me.zhengjie.modules.system.repository.NewsListRepository;
import me.zhengjie.modules.system.service.mapstruct.NewsItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Api(tags = "新闻相关接口")
public class NewsController {

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private NewsListRepository newsListRepository;

    @Autowired
    private NewsItemMapper newsItemMapper;

    @ApiOperation("获取新闻类型列表")
    @GetMapping(value = "/category_list")
    public ResponseEntity<List<NewsCategory>> getNewsCategoryList() {
        List<NewsCategory> categories = newsCategoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @ApiOperation("获取新闻列表")
    @PostMapping("/news_list")
    public ResponseEntity<Map<String, Object>> getNewsList(
            @RequestBody NewsListRequest request
    ) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<NewsItem> newsPage = newsListRepository.findByArticleType_Type(request.getType(), pageable);

        List<NewsItemDto> newsDtos = newsItemMapper.toDtoList(newsPage.getContent());

        Map<String, Object> response = new HashMap<>();
        response.put("list", newsDtos);
        response.put("total", newsPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
