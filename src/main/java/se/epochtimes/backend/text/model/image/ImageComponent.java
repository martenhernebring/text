package se.epochtimes.backend.text.model.image;

public class ImageComponent {
  private final Image image;
  private final String text;
  private final String credit;

  public ImageComponent(Image image, String text, String credit) {
    this.image = image;
    this.text = text;
    this.credit = credit;
  }
}
