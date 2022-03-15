package se.epochtimes.backend.text.dto;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Category;

import static org.junit.jupiter.api.Assertions.*;

class ArticleDTOTest {

  @Test
  void setHeader() {
    ArticleDTO byConstructor = new ArticleDTO(
      new HeaderComponent(Category.INRIKES, 2022, "EKONOMI", ""),
      "headline", "leader", "support");
    var hc = byConstructor.getHeader();
    byConstructor.setHeader(
      new HeaderComponent(Category.INRIKES, 2022, "POLITIK", "")
    );
    assertNotEquals(hc, byConstructor.getHeader());
  }
}