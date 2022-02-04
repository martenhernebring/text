package se.epochtimes.backend.text.dto;

import se.epochtimes.backend.text.model.Subject;
import se.epochtimes.backend.text.model.Text;

import java.util.List;

public record TextDTO(Subject subject, List<String> article) {
  public TextDTO(Text text) {
    this(text.getSubject(), text.getArticle());
  }
}
