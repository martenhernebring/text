package se.epochtimes.backend.text.model;

import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;

import javax.persistence.*;

@Entity
@Table(name = "newspaperarticle")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  private HeaderComponent headerComponent;
  private MainComponent mainComponent;
  private ImageComponent imageComponent;

  public Article(ArticleDTO dto) {
    headerComponent = dto.header();
    mainComponent = dto.main();
    imageComponent = dto.image();
  }

  public HeaderComponent getHeaderComponent() {
    return this.headerComponent;
  }

  public MainComponent getMainComponent() {
    return this.mainComponent;
  }

  public ImageComponent getImageComponent() {
    return this.imageComponent;
  }

  public void setHeaderComponent(HeaderComponent header) {
    this.headerComponent = header;
  }

  public void setMainComponent(MainComponent main) {
    this.mainComponent = main;
  }

  public void setImageComponent(ImageComponent image) {
    this.imageComponent = image;
  }

  //for jpa
  public Article() {}
}
