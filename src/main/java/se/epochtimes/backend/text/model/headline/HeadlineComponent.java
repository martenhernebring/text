package se.epochtimes.backend.text.model.headline;

import java.io.Serializable;

public class HeadlineComponent implements Serializable {
  private String headline;
  private String lead;

  public HeadlineComponent(String headline, String lead) {
    this.headline = headline;
    this.lead = lead;
  }

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
}
