package se.epochtimes.backend.text.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.epochtimes.backend.text.model.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findByHeadlineAndYear(String headline, int year);
  List<Article> findByArticleId(String articleId);
}
