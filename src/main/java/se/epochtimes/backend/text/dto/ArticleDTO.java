package se.epochtimes.backend.text.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.header.HeaderComponent;

import java.io.Serializable;

public class ArticleDTO implements Serializable {

  private final HeaderComponent header;
  private String support;
  private String headline;
  private String lead;

  public ArticleDTO(HeaderComponent header, String headline, String lead, String support) {
    this.header = header;
    this.headline = headline;
    this.lead = lead;
    this.support = support;
  }

  public ArticleDTO(Article article) {
    this.header = article.getHeaderComponent();
    var cc = article.getContentComponent();
    this.headline = cc.getHeadline();
    this.lead = cc.getLead();
    this.support = cc.getBody();
  }

  public HeaderComponent getHeader() {
    return header;
  }

  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public String getLead() {
    return lead;
  }

  public void setLead(String lead) {
    this.lead = lead;
  }

  public String getSupport() {
    return support;
  }

  public void setSupport(String support) {
    this.support = support;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ArticleDTO that = (ArticleDTO) o;

    return new EqualsBuilder()
      .append(header, that.header)
      .append(headline, that.headline)
      .append(lead, that.lead).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(header)
      .append(headline)
      .append(lead)
      .toHashCode();
  }
}
