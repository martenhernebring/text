package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleElementTest {

  private static final String FIRST = ArticleElementSingelton.getInstance().get(0);

  @Test
  void firstSentenceIsInSwedish() {
    assertEquals("Många företag planerar att höja priserna.", FIRST);
  }

  @Test
  void firstLetterIsUpperCase() {
    assertTrue(Character.isUpperCase(FIRST.charAt(0)));
  }

  @Test
  void secondCharacterIsLowerCase() {
    assertTrue(Character.isLowerCase(FIRST.charAt(1)));
  }

  @Test
  void lastCharacterIsNotLetter() {
    assertFalse(Character.isLetter(FIRST.charAt(FIRST.length() - 1)));
  }

  @Test
  void lastCharacterIsNotWhiteSpace() {
    assertFalse(Character.isWhitespace(FIRST.charAt(FIRST.length() - 1)));
  }

  @Test
  void secondToLastCharacterIsLetter() {
    assertTrue(Character.isLetter(FIRST.charAt(FIRST.length() - 2)));
  }

  @Test
  void addFirstSentence() {
    final List<String> text = new ArrayList<>();
    text.add(FIRST);
    assertEquals(1, text.size());
  }

  @Test
  void addSecondSentence() {
    final List<String> text = new ArrayList<>();
    text.add(FIRST);
    text.add(ArticleElementSingelton.getInstance().get(1));
    assertEquals(2, text.size());
  }
}
