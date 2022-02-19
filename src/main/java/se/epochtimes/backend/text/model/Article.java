package se.epochtimes.backend.text.model;

import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.main.HeadlineComponent;
import se.epochtimes.backend.text.model.wrap.WordWrapper;

import javax.persistence.*;

@Entity
@Table(name = "newspaperarticle")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  private HeaderComponent headerComponent;
  private HeadlineComponent headlineComponent;
  @Column(name = "body", length = 4096)
  private String body;

  public Article(ArticleDTO dto) {
    headerComponent = dto.getHeader();
    setHeadlineComponent(new HeadlineComponent(dto.getHeadline(), dto.getLead()));
    setBody(dto.getSupport());
  }

  public HeaderComponent getHeaderComponent() {
    return this.headerComponent;
  }

  public HeadlineComponent getHeadlineComponent() {
    return this.headlineComponent;
  }

  public void setHeaderComponent(HeaderComponent header) {
    this.headerComponent = header;
  }

  public void setHeadlineComponent(HeadlineComponent headline) {
    this.headlineComponent = WordWrapper.format(headline);
  }

  //for jpa
  public Article() {}

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = WordWrapper.formatBody(body);
  }
}
