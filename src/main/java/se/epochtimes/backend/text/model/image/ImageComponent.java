package se.epochtimes.backend.text.model.image;

import java.io.Serializable;

public class ImageComponent implements Serializable {
  private final Image image;
  private final String text;
  private final String credit;

  public ImageComponent(Image image, String text, String credit) {
    this.image = image;
    this.text = text;
    this.credit = credit;
  }

  public Image getImage() {
    return image;
  }

  public String getText() {
    return text;
  }

  public String getCredit() {
    return credit;
  }
}
