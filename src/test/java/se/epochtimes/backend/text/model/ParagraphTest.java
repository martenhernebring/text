package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static se.epochtimes.backend.text.model.Paragraph.wrap;

public class ParagraphTest {

  private String output;

  private void insert(String insert) {
    output = wrap(insert).build().getParagraph();
  }

  @Test
  void emptyStringWhenNull() {
    insert(null);
    assertThat(output, is(emptyString()));
  }

  @Test
  void sameLetterIfLetter() {
    insert("a");
    assertThat(output, is("a"));
  }

  @Test
  @Disabled
  void sameShortIfShort() {
    insert("ab");
    assertThat(output, is("ab"));
  }

  @Test
  @Disabled
  void noLeadingWhiteSpace() {
    insert(" a");
    assertThat(output, is("a"));
  }

  @Test
  @Disabled
  void wrapParagraphIfLong() {
    insert("abcdefghijklmnopqrstuvxyzABCDEF");
  }

}
