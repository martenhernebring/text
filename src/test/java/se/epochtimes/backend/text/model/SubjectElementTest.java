package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubjectElementTest {
  @Test
  void addSubject() {
    Subject ekonomi = Subject.EKONOMI;
    assertNotNull(ekonomi);
  }
}