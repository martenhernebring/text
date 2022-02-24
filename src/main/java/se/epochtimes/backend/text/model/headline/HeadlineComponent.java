package se.epochtimes.backend.text.model.headline;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class HeadlineComponent implements Serializable {

  private String headline;
  private String leader;

  public HeadlineComponent(String headline, String leader) {
    this.headline = headline;
    this.leader = leader;
  }

  //Jpa requirement
  public HeadlineComponent() {}

  public String getHeadline() {
    return headline;
  }

  public String getLeader() {return leader; }

  public void setLeader(String lead) {
    this.leader = lead;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    HeadlineComponent that = (HeadlineComponent) o;

    return new EqualsBuilder()
      .append(headline, that.headline)
      .append(leader, that.leader)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(headline).append(leader).toHashCode();
  }
}
