package se.epochtimes.backend.text.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.Image;
import se.epochtimes.backend.text.model.header.HeaderComponent;

import java.io.Serializable;

public class ImageDTO implements Serializable {

  private HeaderComponent header;
  private String imageText;
  private String imageCredit;

  public ImageDTO(Image image) {
    setHeader(image.getHeader());
    setImageText(image.getImageText());
    setImageCredit(image.getImageCredit());
  }

  public ImageDTO(HeaderComponent header, String imageText, String imageCredit) {
    setHeader(header);
    setImageText(imageText);
    setImageCredit(imageCredit);
  }

  public HeaderComponent getHeader() {
    return header;
  }

  public void setHeader(HeaderComponent header) {
    this.header = header;
  }

  public String getImageText() {
    return imageText;
  }

  public void setImageText(String imageText) {
    this.imageText = imageText;
  }

  public String getImageCredit() {
    return imageCredit;
  }

  public void setImageCredit(String imageCredit) {
    this.imageCredit = imageCredit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ImageDTO that = (ImageDTO) o;

    return new EqualsBuilder()
      .append(header, that.getHeader())
      .append(imageText, that.getImageText())
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(header)
      .append(imageText)
      .toHashCode();
  }
}
