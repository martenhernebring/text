package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.epochtimes.backend.text.model.WordWrapper.NL;

public class WordWrapperTest {

  private String output;

  private void insert(String insert) {
    WordWrapper ww = new WordWrapper(insert);
    output = ww.wrapWords();
  }

  @Test
  void throwsExceptionWhenNull() {
    assertThrows(NullPointerException.class, () -> insert(null));
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

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    insert(unprocessPreamble.substring(0, 34));
    assertThat(output, is("Polisen kommer under veckan" + NL + "trappa"));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    insert(unprocessPreamble.substring(0, 63));
    assertThat(output, is("Polisen kommer under veckan tra-" + NL +
      "ppa ned den särskilda insats som"));
  }

  @Test
  void veryLongWordShouldBreakItself() {
    insert("gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("gravmonumentsindustrif-" + NL + "abrikationsprodukterna"));
  }

  @Test
  void veryLongWordAtTheEnd() {
    insert("trappa ned den särskilda insats gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("trappa ned den särskilda insats" + NL + "gravmonumentsindustrif-" + NL + "abrikationsprodukterna"));
  }

  @Test
  void breakLineAtEnd() {
    insert("inkommit ett 20-tal observationer");
    assertThat(output, is("inkommit ett 20-tal" + NL + "observationer"));
  }

  @Test
  void veryLongLeftOver() {
    insert("trappa gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("trappa gravmonumentsindustrif-" + NL + "abrikationsprodukterna"));
  }

  @Test
  @Disabled
  void wrapParagraphThreeTimesWithFifteenWords() {
    insert(unprocessPreamble);
    assertThat(output, is("Polisen kommer under veckan tra-" + NL + "ppa ned den särskilda insats som inle-" + NL + "ddes efter att det inkommit ett 20-" + NL + "tal observationer av drönare" +NL + "runt om i Sverige. Bland annat observ-" + NL + "erades drönare över kärnkraftverk."));
  }

  //Testa tre långa ord
  //Smallest word to cut: 7

}
