package se.epochtimes.backend.text.dto;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Subject;

import static org.junit.jupiter.api.Assertions.*;

class ArticleDTOTest {

  @Test
  void setHeader() {
    ArticleDTO byConstructor = new ArticleDTO(
      new HeaderComponent(Subject.EKONOMI, 2022, "vignette", ""),
      "headline", "leader", "support");
    var hc = byConstructor.getHeader();
    byConstructor.setHeader(
      new HeaderComponent(Subject.EKONOMI, 2022, "other", "")
    );
    assertNotEquals(hc, byConstructor.getHeader());
  }
}