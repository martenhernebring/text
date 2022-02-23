package se.epochtimes.backend.text.model.headline;

import se.epochtimes.backend.text.model.wrap.WordWrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ContentComponent implements Serializable {
  private String headline;
  private String lead;
  @Column(name = "body", length = 4096)
  private String body;

  public ContentComponent(String headline, String lead, String body) {
    this.headline = headline;
    this.lead = lead;
    this.body = body;
  }

  //Jpa requirement
  public ContentComponent() {}

  public String getHeadline() {
    return headline;
  }

  public String getLead() {return lead; }

  public void setLead(String lead) {
    this.lead = lead;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
