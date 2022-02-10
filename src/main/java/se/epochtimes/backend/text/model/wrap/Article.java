package se.epochtimes.backend.text.model.wrap;

import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  private final HeaderComponent headerComponent;
  private final MainComponent mainComponent;
  private final ImageComponent imageComponent;
  private String articleId;

  public Article(ArticleDTO dto) {
    headerComponent = dto.header();
    mainComponent = dto.main();
    imageComponent = dto.image();
  }

  public HeaderComponent getHeader() {
    return this.headerComponent;
  }

  public MainComponent getMain() {
    return this.mainComponent;
  }

  public ImageComponent getImage() {
    return this.imageComponent;
  }

  public void setArticleId(String articleId) {
    this.articleId = articleId;
  }
}
