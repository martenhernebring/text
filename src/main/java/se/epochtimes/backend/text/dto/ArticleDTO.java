package se.epochtimes.backend.text.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.header.HeaderComponent;

import java.io.Serializable;

public class ArticleDTO implements Serializable {

  private HeaderComponent header;
  private String support;
  private String headline;
  private String leader;

  public ArticleDTO(HeaderComponent header,
                    String headline,
                    String leader,
                    String support) {
    this.header = header;
    this.headline = headline;
    this.leader = leader;
    this.support = support;
  }

  public ArticleDTO(Article article) {
    this.header = article.getHeader();
    var hc = article.getHC();
    this.headline = hc.getHeadline();
    this.leader = hc.getLeader();
    this.support = article.getBody();
  }

  public HeaderComponent getHeader() {
    return header;
  }

  public void setHeader(HeaderComponent header) {
    this.header = header;
  }

  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public String getLeader() {
    return leader;
  }

  public void setLeader(String leader) {
    this.leader = leader;
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
      .append(leader, that.leader).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(header)
      .append(headline)
      .append(leader)
      .toHashCode();
  }
}
