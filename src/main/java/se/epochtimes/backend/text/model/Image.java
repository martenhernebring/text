package se.epochtimes.backend.text.model;

import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.wrap.Format;
import se.epochtimes.backend.text.model.wrap.WordWrapper;

import javax.persistence.*;

@Entity
@SecondaryTable(name = "header",
  pkJoinColumns = @PrimaryKeyJoinColumn(name = "header_id"))
@Table(name = "imagetext")
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  @Embedded
  private HeaderComponent header;

  private String imageText;
  private String imageCredit;

  public HeaderComponent getHeader() {
    return this.header;
  }

  public String getImageText() {
    return this.imageText;
  }

  public String getImageCredit() { return this.imageCredit; }

  public void setHeader(HeaderComponent header) {
    this.header = header;
  }

  public void setImageText(String imageText) {
    this.imageText = WordWrapper.format(imageText, Format.PARAGRAPH);
  }

  public void setImageCredit(String imageCredit) { this.imageCredit = imageCredit.toUpperCase(); }

  //for jpa
  public Image() {}
}
