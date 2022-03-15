package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleTest {

  @Test
  void hasEmptyConstructorWithCategoryAndCustomId() {
    Article article = new Article();
    article.setHeader(new HeaderComponent(
      Category.INRIKES, 2022, "EKONOMI", "1234"));
    assertEquals("INRIKES", article.getHeader().getCategory().getPrint());
    assertEquals("1234", article.getHeader().getArticleId());
  }

}