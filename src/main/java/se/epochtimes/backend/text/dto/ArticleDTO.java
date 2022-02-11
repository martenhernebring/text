package se.epochtimes.backend.text.dto;

import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;

import java.io.Serializable;

public record ArticleDTO(HeaderComponent header, MainComponent main, ImageComponent image) implements Serializable {

  public ArticleDTO(Article article) {
    this(article.getHeaderComponent(), article.getMainComponent(), article.getImageComponent());
  }
}
