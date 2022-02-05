package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleElementTest {

  private static final String FIRST = ParagraphSingleton.getInstance().get(0);

  @Test
  void firstSentenceIsInSwedish() {
    assertEquals("  Många företag planerar att höja priserna. Två av tre företag i industrin och drygt " +
      "hälften av tjänsteföretagen räknar med att ta ut högre priser av sina kunder. Det visar " +
      "en undersökning som Swedbank har gjort bland inköpschefer och i tjänstesektorn.", FIRST);
  }

  @Test
  void firstCharacterIsSpace() {
    assertTrue(Character.isSpaceChar(FIRST.charAt(0)));
  }

  @Test
  void secondCharacterIsSpace() {
    assertTrue(Character.isSpaceChar(FIRST.charAt(1)));
  }

  @Test
  void thirdCharacterIsUpperCase() {
    assertTrue(Character.isUpperCase(FIRST.charAt(2)));
  }

  @Test
  void fourthCharacterIsLowerCase() {
    assertTrue(Character.isLowerCase(FIRST.charAt(3)));
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
  void secondToLastCharacterIsLowerCase() {
    assertTrue(Character.isLowerCase(FIRST.charAt(FIRST.length() - 2)));
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
    text.add(ParagraphSingleton.getInstance().get(1));
    assertEquals(2, text.size());
  }
}
