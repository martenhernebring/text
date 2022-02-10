package se.epochtimes.backend.text.dto;

import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;
import se.epochtimes.backend.text.model.wrap.Article;

public record ArticleDTO(HeaderComponent header, MainComponent main, ImageComponent image) {
  public ArticleDTO(Article article) {
    this(article.getHeader(), article.getMain(), article.getImage());
  }
}
