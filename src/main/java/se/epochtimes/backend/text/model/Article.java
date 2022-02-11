package se.epochtimes.backend.text.model;

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
  private HeaderComponent headerComponent;
  private MainComponent mainComponent;
  private ImageComponent imageComponent;
  private final String articleId;

  public Article(ArticleDTO dto, String articleId) {
    headerComponent = dto.header();
    mainComponent = dto.main();
    imageComponent = dto.image();
    this.articleId = articleId;
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

  public String getArticleId() {
    return articleId;
  }

  public void setHeader(HeaderComponent header) {
    this.headerComponent = header;
  }

  public void setMain(MainComponent main) {
    this.mainComponent = main;
  }

  public void setImage(ImageComponent image) {
    this.imageComponent = image;
  }
}
