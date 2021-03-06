package se.epochtimes.backend.text.service;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.ArticleNotFoundException;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.headline.HeadlineComponent;
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
    Article toBeSaved = new Article(dto);
    HeadlineComponent hc = toBeSaved.getHC();
    List<Article> existingHeadline = articleRepository
      .findByHeadline(hc.getHeadline(), hc.getLeader());
    if(existingHeadline.size() > 0) {
      throw new ConflictException(
        "The article has already been posted. Please get by following header: "
          + existingHeadline.get(0).getHeader()
      );
    }
    var header = toBeSaved.getHeader();
    var existingHeader = articleRepository.findByHeader(
      toBeSaved.getHeader().getArticleId(),
      header.getCategory().getCode(),
      header.getVignette(),
      header.getPubYear()
    );
    if(existingHeader.size() > 0) {
      throw new ConflictException(
        "The header " + header + " is already in use. Please use another header."
      );
    }
    Article saved = articleRepository.save(toBeSaved);
    return new ArticleDTO(saved);
  }

  public ArticleDTO getByHeader(HeaderComponent header) {
    return new ArticleDTO(findByHeader(header));
  }

  public ArticleDTO edit(ArticleDTO articleDTO) {
    Article article = findByHeader(articleDTO.getHeader());
    HeadlineComponent hc = new HeadlineComponent();
    hc.setHeadline(articleDTO.getHeadline().isEmpty() ?
      article.getHC().getHeadline() : articleDTO.getHeadline());
    hc.setLeader(articleDTO.getLeader().isEmpty() ?
      article.getHC().getLeader() : articleDTO.getLeader());
    article.setHC(hc);
    if(!articleDTO.getSupport().isEmpty()) {
      article.setBody(articleDTO.getSupport());
    }
    Article savedArticle = articleRepository.save(article);
    return new ArticleDTO(savedArticle);
  }

  public void removeArticle(HeaderComponent header) {
    Article article = findByHeader(header);
    articleRepository.delete(article);
  }

  private Article findByHeader(HeaderComponent header) {
    var existing = articleRepository.findByHeader(
      header.getArticleId(),
      header.getCategory().getCode(),
      header.getVignette(),
      header.getPubYear()
    );
    if(existing.size() > 1) {
      throw new ConflictException(
        "Server error: More than one article with the arguments exists.");
    } else {
      try {
        return existing.get(0);
      } catch(IndexOutOfBoundsException ex) {
        throw new ArticleNotFoundException(
          "No article with the arguments was found."
        );
      }
    }
  }

  public List<ArticleDTO> getAllUnsorted() {
    return articleRepository
      .findAll()
      .stream()
      .map(ArticleDTO::new)
      .collect(Collectors.toList());
  }
}
