package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.data.NewsKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsKindRepository extends JpaRepository<NewsKind, Long> {

    //文章类型列表
    List<NewsKind> findAll();
}
