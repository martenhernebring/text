package se.epochtimes.backend.text.dto;

import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;
import se.epochtimes.backend.text.model.Article;

import java.util.concurrent.ThreadLocalRandom;

public record ArticleDTO(HeaderComponent header, MainComponent main, ImageComponent image) {

  public ArticleDTO(Article article) {
    this(article.getHeader(), article.getMain(), article.getImage());
  }

  private String generateIdPrefix() {
    String prefix = header.getSubject().getPrint().substring(0, 3) + header.getVignette().substring(0, 3);
    String lowerCase = prefix.toLowerCase();
    return org.apache.commons.lang3.StringUtils.stripAccents(lowerCase);
  }

  public static String generateIdSuffix() {
    return String.format("%04d", ThreadLocalRandom.current().nextInt(0, 9999 + 1));
  }

  public String generateId() {
    return generateIdPrefix() + generateIdSuffix();
  }
}
