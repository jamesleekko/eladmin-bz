package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.NewsItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsListRepository extends JpaRepository<NewsItem, Long> {
    @Query("SELECT s FROM NewsItem s WHERE s.articleType = :type")
    Page<NewsItem> findByType(@Param("type") Integer type, Pageable pageable);

    Page<NewsItem> findByArticleType_Type(Integer type, Pageable pageable);
}
