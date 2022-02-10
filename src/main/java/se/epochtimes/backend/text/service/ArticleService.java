package se.epochtimes.backend.text.service;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.AlreadySubmittedException;
import se.epochtimes.backend.text.exception.ArticleNotFoundException;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.wrap.Article;
import se.epochtimes.backend.text.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  private List<Article> findByHeadlineAndYear(String headline, int year) {
    return articleRepository.findByHeadlineAndYear(headline, year);
  }

  public ArticleDTO add(ArticleDTO dto) {
    List<Article> existing = findByHeadlineAndYear(
      dto.main().getHeadline(), dto.header().getYear());
    if(existing.size() > 0) {
      throw new AlreadySubmittedException(
        "The article has already been posted. Please get previous one.");
    }
    Article article = articleRepository.save(new Article(dto));
    return new ArticleDTO(article);
  }

  public ArticleDTO getByHeadlineAndYear(String headline, int year) {
    List<Article> existing = findByHeadlineAndYear(headline, year);
    int size = existing.size();
    if(size > 1) {
      throw new ConflictException(
        "Server error: More than one article with the same headline " + headline + " and year " + year + "exists.");
    } else {
      try {
        return new ArticleDTO(existing.get(0));
      } catch(IndexOutOfBoundsException ex) {
        throw new ArticleNotFoundException("No article with headline " + headline
          + " and year" + year + " found. " +
          "Please try again with different parameters.");
      }
    }
  }

  public ArticleDTO getByArticleId(String articleId) {
    List<Article> existing = articleRepository.findByArticleId(articleId);
    if(existing.size() > 1) {
      throw new ConflictException("Server error: Multiple articles with same id");
    } else {
      try {
        return new ArticleDTO(existing.get(0));
      } catch(IndexOutOfBoundsException ex) {
        throw new ArticleNotFoundException("No article with articleId" +
          articleId + " was found. Please try again with different articleId."
        );
      }
    }
  }
}
