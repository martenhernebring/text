package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.header.Subject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubjectElementTest {
  @Test
  void addSubject() {
    Subject ekonomi = Subject.EKONOMI;
    assertNotNull(ekonomi);
  }

  @Test
  void illegalShouldThrow() {
    assertThrows(IllegalArgumentException.class, () -> Subject.valueOf(-1));
  }
}
