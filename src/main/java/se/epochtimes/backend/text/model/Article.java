package se.epochtimes.backend.text.model;

import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.headline.HeadlineComponent;
import se.epochtimes.backend.text.model.wrap.WordWrapper;

import javax.persistence.*;

@Entity
@SecondaryTable(name = "header",
  pkJoinColumns = @PrimaryKeyJoinColumn(name = "header_id"))
@SecondaryTable(name = "headline",
  pkJoinColumns = @PrimaryKeyJoinColumn(name = "headline_id"))
@Table(name = "newspaperarticle")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  @Embedded
  private HeaderComponent header;

  @Embedded
  private HeadlineComponent headline;

  @Column(name = "body", length = 4096)
  private String body;

  public Article(ArticleDTO dto) {
    header = dto.getHeader();
    setHC(new HeadlineComponent(dto.getHeadline(), dto.getLeader()));
    setBody(dto.getSupport());
  }

  public HeaderComponent getHeader() {
    return this.header;
  }

  public HeadlineComponent getHC() {
    return this.headline;
  }

  public void setHeader(HeaderComponent header) {
    this.header = header;
  }

  public void setHC(HeadlineComponent headline) {
    this.headline = WordWrapper.format(headline);
  }

  //for jpa
  public Article() {}

  public void setBody(String body) {
    this.body = WordWrapper.formatBody(body);
  }

  public String getBody() {
    return body;
  }
}
