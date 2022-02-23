package se.epochtimes.backend.text.model;

import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.headline.ContentComponent;
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
  @Embedded
  private ContentComponent contentComponent;

  public Article(ArticleDTO dto) {
    headerComponent = dto.getHeader();
    setContentComponent(new ContentComponent(dto.getHeadline(), dto.getLead(), dto.getSupport()));
  }

  public HeaderComponent getHeaderComponent() {
    return this.headerComponent;
  }

  public ContentComponent getContentComponent() {
    return this.contentComponent;
  }

  public void setHeaderComponent(HeaderComponent header) {
    this.headerComponent = header;
  }

  public void setContentComponent(ContentComponent headline) {
    this.contentComponent = WordWrapper.format(headline);
  }

  //for jpa
  public Article() {}

}
