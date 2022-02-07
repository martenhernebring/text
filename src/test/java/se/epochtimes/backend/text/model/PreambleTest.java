package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static se.epochtimes.backend.text.model.Preamble.NL;
import static se.epochtimes.backend.text.model.Preamble.wrap;

public class PreambleTest {

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
  void sameLetterIfBigLetter() {
    insert("A");
    assertThat(output, is("A"));
  }

  @Test
  void sameShortIfShort() {
    insert("ab");
    assertThat(output, is("ab"));
  }

  @Test
  void noLeadingWhiteSpace() {
    insert(" a");
    assertThat(output, is("a"));
  }

  @Test
  void wrapParagraphIfTwoLongWords() {
    insert("Storbritanniens invandringslagstiftning");
    assertThat(output,
      is("Storbritanniens" + NL + "invandringslagstiftning")
    );
  }

  @Test
  void wrapParagraphIfThreeWordsTooWide() {
    insert("opinionsundersökning från SvD/GP/Sifo.");
    assertThat(output, is("opinionsundersökning från" + NL + "SvD/GP/Sifo."));
  }

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    insert("Polisen kommer under veckan trappa");
    assertThat(output, is("Polisen kommer under veckan" + NL + "trappa"));
  }

  @Test
  void sendEmptyIfWhiteSpace() {
    insert(" ");
    assertThat(output, is(""));
  }

  //TODO " "
}
