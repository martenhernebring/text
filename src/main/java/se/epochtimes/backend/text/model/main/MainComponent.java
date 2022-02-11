package se.epochtimes.backend.text.model.main;

public class MainComponent {
  private final String headline;
  private String lead;

  public MainComponent(String headline) {
    this.headline = headline;
  }

  public String getHeadline() {
    return headline;
  }

  public String getLead() {return lead; }

  public void setLead(String lead) {
    this.lead = lead;
  }
}
