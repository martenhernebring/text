package se.epochtimes.backend.text.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.main.MainComponent;

import java.util.List;

@Repository("articleRepository")
public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query(value = "SELECT * FROM newspaperarticle na WHERE na.header_component = ?1",
    nativeQuery = true)
  List<Article> findByHeader(
    @Param(value = "headerComponent") HeaderComponent headerComponent
  );

  @Query(value = "SELECT * FROM newspaperarticle na WHERE na.main_component = ?1",
    nativeQuery = true)
  List<Article> findByMain(@Param(value = "mainComponent") MainComponent mainComponent);
}
