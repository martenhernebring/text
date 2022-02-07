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
  void sameLetterIfLetter() {
    insert("a");
    assertThat(output, is("a"));
  }

  @Test
  void sendEmptyIfWhiteSpace() {
    insert(" ");
    assertThat(output, is(""));
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

  String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den särskilda insats som inleddes " +
      "efter att det inkommit ett 20-tal observationer av drönare runt om i " +
      "Sverige. Bland annat observerades drönare över kärnkraftverk.";

  String expectedPreamble =
    "Polisen kommer under veckan" + NL + "trappa ned den särskilda insats" + NL +
      "som inleddes efter att det" + NL + "inkommit ett 20-tal observa-" + NL +
      "tioner av drönare runt om i Sve-" + NL + "rige. Bland annat observerades"
    + NL + "drönare över kärnkraftverk.";

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    insert(unprocessPreamble.substring(0, 34));
    assertThat(output, is("Polisen kommer under veckan" + NL + "trappa"));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    insert(unprocessPreamble.substring(0, 63));
    assertThat(output, is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL + "som"));
  }

  @Test
  @Disabled
  void wrapParagraphThreeTimesWithFifteenWords() {
    insert(unprocessPreamble);
    assertThat(output, is(expectedPreamble));
  }

}
