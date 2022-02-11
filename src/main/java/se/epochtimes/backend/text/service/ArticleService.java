package se.epochtimes.backend.text.service;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.AlreadySubmittedException;
import se.epochtimes.backend.text.exception.ArticleNotFoundException;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public ArticleDTO add(ArticleDTO dto) {
    List<Article> existing = articleRepository.findByHeadlineAndYear(
      dto.main().getHeadline(), dto.header().getYear()
    );
    if(existing.size() > 0) {
      throw new AlreadySubmittedException(
        "The article has already been posted. Please get by articleId: "
          + existing.get(0).getArticleId()
      );
    }
    String articleId = dto.generateId();
    Article article = articleRepository.save(new Article(dto, articleId));
    return new ArticleDTO(article);
  }

  public ArticleDTO getByArticleId(String articleId) {
    return new ArticleDTO(findByArticleId(articleId));
  }

  public ArticleDTO edit(ArticleDTO articleDTO, String articleId) {
    Article article = findByArticleId(articleId);
    article.setHeader(articleDTO.header());
    article.setMain(articleDTO.main());
    article.setImage(articleDTO.image());
    Article savedArticle = articleRepository.save(article);
    return new ArticleDTO(savedArticle);
  }

  public void removeArticle(String articleId) {
    Article article = findByArticleId(articleId);
    articleRepository.delete(article);
  }

  private Article findByArticleId(String articleId) {
    var existing = articleRepository.findByArticleId(articleId);
    if(existing.size() > 1) {
      throw new ConflictException(
        "Server error: More than one article with the arguments exists.");
    } else {
      try {
        return existing.get(0);
      } catch(IndexOutOfBoundsException ex) {
        throw new ArticleNotFoundException("No article with the arguments was found.");
      }
    }
  }
}
