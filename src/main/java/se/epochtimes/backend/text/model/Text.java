package se.epochtimes.backend.text.model;

import se.epochtimes.backend.text.dto.TextDTO;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "textarticle")
public class Text {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private Subject subject;
  @ElementCollection
  private List<String> article;

  public Text(TextDTO text) {
    this.subject = text.subject();
    this.article = text.article();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Text() {}

  public Text(Subject subject, List<String> article) {
    this.subject = subject;
    this.article = article;
  }

  public Subject getSubject() {
    return subject;
  }

  public List<String> getArticle() {
    return article;
  }
}