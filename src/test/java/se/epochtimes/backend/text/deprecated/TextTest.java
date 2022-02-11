package se.epochtimes.backend.text.deprecated;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.deprecated.text.Text;
import se.epochtimes.backend.text.deprecated.text.StringListSingleton;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextTest {

  private final List<String> sentences = StringListSingleton.getInstance();

  @Test
  void addAllSentencesInText() {
    assertEquals(5, sentences.size());
  }

  @Test
  void startIsASentence() {
    final String start = sentences.get(0);
    assertTrue(Character.isSpaceChar(start.charAt(0)));
    assertTrue(Character.isSpaceChar(start.charAt(1)));
    assertTrue(Character.isUpperCase(start.charAt(2)));
    assertTrue(Character.isLowerCase(start.charAt(3)));
  }

  @Test
  void endingIsASentence() {
    final String ending = sentences.get(sentences.size() -1);
    assertFalse(Character.isLetter(ending.charAt(ending.length() - 1)));
    assertFalse(Character.isWhitespace(ending.charAt(ending.length() - 1)));
    assertTrue(Character.isLetter(ending.charAt(ending.length() - 2)));
  }

  @Test
  void hasEmptyConstructorAndId() {
    Text text = new Text();
    text.setId(0L);
    assertEquals(0L, text.getId());
  }
}
