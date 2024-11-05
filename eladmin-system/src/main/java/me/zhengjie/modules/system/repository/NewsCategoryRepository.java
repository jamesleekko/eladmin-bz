package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

    // 文章分类列表
    List<NewsCategory> findAll();
}
