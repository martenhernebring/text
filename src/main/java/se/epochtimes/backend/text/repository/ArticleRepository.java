package se.epochtimes.backend.text.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.epochtimes.backend.text.model.Article;

import java.util.List;

@Repository("articleRepository")
public interface ArticleRepository extends JpaRepository<Article, Long> {
  @Query(value = "SELECT * FROM newspaperarticle na WHERE " +
    "na.article_id = ?1 AND na.subject = ?2 AND na.vignette = ?3 AND na.pub_year = ?4",
    nativeQuery = true)
  List<Article> findByHeader(
    @Param(value = "article_id") String article_id,
    @Param(value = "subject") int subject,
    @Param(value = "vignette") String vignette,
    @Param(value = "pub_year") int pub_year
  );

  @Query(value = "SELECT * FROM newspaperarticle na WHERE na.headline = ?1 AND na.leader = ?2",
    nativeQuery = true)
  List<Article> findByHeadline(
    @Param(value = "headline") String headline,
    @Param(value = "leader") String leader
  );
}
