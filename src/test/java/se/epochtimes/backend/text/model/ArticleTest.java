package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Subject;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleTest {

  @Test
  void hasEmptyConstructorWithSubjectAndCustomId() {
    Article article = new Article();
    article.setHeader(new HeaderComponent(
      Subject.EKONOMI, 2022, "Vignette", "1234"));
    assertEquals("EKONOMI", article.getHeader().getSubject().getPrint());
    assertEquals("1234", article.getHeader().getArticleId());
  }

}