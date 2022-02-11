package se.epochtimes.backend.text.model.main;

import java.io.Serializable;

public class MainComponent implements Serializable {
  private final String headline;
  private String lead;

  public MainComponent(String headline, String lead) {
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
}
