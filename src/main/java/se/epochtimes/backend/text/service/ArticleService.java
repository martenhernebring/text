package se.epochtimes.backend.text.service;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.ArticleNotFoundException;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.repository.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleService {

  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public ArticleDTO add(ArticleDTO dto) {
    List<Article> existing = articleRepository.findByMain(dto.main());
    if(existing.size() > 0) {
      throw new ConflictException(
        "The article has already been posted. Please get by following header: "
          + existing.get(0).getHeaderComponent()
      );
    }
    Article article = articleRepository.save(new Article(dto));
    return new ArticleDTO(article);
  }

  public ArticleDTO getByHeader(HeaderComponent header) {
    return new ArticleDTO(findByHeader(header));
  }

  public ArticleDTO edit(ArticleDTO articleDTO) {
    Article article = findByHeader(articleDTO.header());
    article.setHeaderComponent(articleDTO.header());
    article.setMainComponent(articleDTO.main());
    article.setImageComponent(articleDTO.image());
    Article savedArticle = articleRepository.save(article);
    return new ArticleDTO(savedArticle);
  }

  public void removeArticle(HeaderComponent header) {
    Article article = findByHeader(header);
    articleRepository.delete(article);
  }

  private Article findByHeader(HeaderComponent header) {
    var existing = articleRepository.findByHeader(header);
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

  public List<ArticleDTO> getAllUnsorted() {
    return articleRepository.findAll().stream().map(ArticleDTO::new).collect(Collectors.toList());
  }
}
