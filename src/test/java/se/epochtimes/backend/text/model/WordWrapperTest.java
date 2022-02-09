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
    WordWrapper ww = new WordWrapper(insert, Format.DEFAULT);
    output = ww.wrapWordsWithBisect();
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

  final String unprocessPreamble =
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
    assertThat(output, is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL + "som"));
  }

  @Test
  void veryLongWordShouldBreakItself() {
    insert("gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("gravmonumentsindustrif-"
      + NL + "abrikationsprodukterna"));
  }

  @Test
  void veryLongWordAtTheEnd() {
    insert("trappa ned den särskilda insats " +
      "gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("trappa ned den särskilda insats" + NL +
      "gravmonumentsindustrif-" + NL + "abrikationsprodukterna"));
  }

  @Test
  void breakLineAtEnd() {
    insert("inkommit ett 20-tal observationer");
    assertThat(output, is("inkommit ett 20-tal" + NL + "observationer"));
  }

  @Test
  void veryLongLeftOver() {
    insert("trappa gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("trappa gravmonumentsindustrif-"
      + NL + "abrikationsprodukterna"));
  }

  @Test
  void wrapParagraphFourTimesWithNineteenWords() {
    insert("Polisen kommer under veckan trappa ned den särskilda insats som " +
      "inleddes efter att det inkommit ett 20-tal observationer av drönare");
    assertThat(output, is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL + "som inleddes efter att det"
      + NL + "inkommit ett 20-tal" + NL + "observationer av drönare"));
  }

  @Test
  void wrapParagraphSixTimesWithTwentyNineWords() {
    insert(unprocessPreamble);
    assertThat(output, is("" +
      "Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL +
      "som inleddes efter att det inko-" + NL +
      "mmit ett 20-tal observationer av" + NL +
      "drönare runt om i Sverige. Bland" + NL +
      "annat observerades drönare över" + NL +
      "kärnkraftverk."));
  }

  @Test
  void longArticle() {
    insert("Det samlade skuldberget hos Kronofogden " +
      "uppgår till 94 miljarder kronor.");
    assertThat(output, is("Det samlade skuldberget hos" + NL +
      "Kronofogden uppgår till 94" + NL + "miljarder kronor."));
  }

  @Test
  void title() {
    WordWrapper ww = new WordWrapper(
      "Kronofogden: De samlade skulderna större än någonsin", Format.HEADLINE);
    assertThat(ww.wrapWords(), is("Kronofogden:" + NL + "De samlade" + NL +
      "skulderna" + NL + "större än" + NL + "någonsin"));
  }

  @Test
  void ingress() {
    WordWrapper ww = new WordWrapper(
      "Antalet svenskar som har skulder hos Kronofogden är det lägsta " +
      "på 30 år. Däremot är det samlade skuldberget större än någonsin och " +
      "växer snabbt. Det visar ny statistik från myndigheten.", Format.LEAD);
    assertThat(ww.wrapWords(), is("" +
      "Antalet svenskar som har" + NL +
      "skulder hos Kronofogden är det" + NL +
      "lägsta på 30 år. Däremot är" + NL +
      "det samlade skuldberget större" + NL +
      "än någonsin och växer snabbt." + NL +
      "Det visar ny statistik från" + NL +
      "myndigheten."));
  }

  @Test
  void body1() {
    WordWrapper ww = new WordWrapper("I slutet av 2021 fanns drygt 391 000 " +
      "personer registrerade hos Kronofogden. Det rör sig om en minskning " +
      "med lite över 11 000 personer jämfört med året innan. Kronofogdens " +
      "analytiker Davor Vuleta säger i ett uttalande att en av orsakerna " +
      "till att antalet blivit färre har att göra med att skulderna till " +
      "staten minskar.", Format.PARAGRAPH);
    assertThat(ww.wrapWordsWithBisect(), is("" +
      "   I slutet av 2021 fanns drygt" + NL +
      "391 000 personer registrerade hos" + NL +
      "Kronofogden. Det rör sig om en" + NL +
      "minskning med lite över 11 000" + NL +
      "personer jämfört med året innan." + NL +
      "Kronofogdens analytiker Davor" + NL +
      "Vuleta säger i ett uttalande att" + NL +
      "en av orsakerna till att antalet" + NL +
      "blivit färre har att göra med att" + NL +
      "skulderna till staten minskar."));
  }

  @Test
  @Disabled
  void body2() {
    WordWrapper ww = new WordWrapper("I slutet av 2021 fanns drygt 391 000 " +
      "personer registrerade hos Kronofogden. Det rör sig om en minskning " +
      "med lite över 11 000 personer jämfört med året innan. Kronofogdens " +
      "analytiker Davor Vuleta säger i ett uttalande att en av orsakerna " +
      "till att antalet blivit färre har att göra med att skulderna till " +
      "staten minskar.", Format.PARAGRAPH);
    assertThat(ww.wrapWordsWithBisect(), is("" +
      "   I slutet av 2021 fanns drygt" + NL +
      "391 000 personer registrerade hos" + NL +
      "Kronofogden. Det rör sig om en" + NL +
      "minskning med lite över 11 000" + NL +
      "personer jämfört med året innan." + NL +
      "Kronofogdens analytiker Davor" + NL +
      "Vuleta säger i ett uttalande att" + NL +
      "en av orsakerna till att antalet" + NL +
      "blivit färre har att göra med att" + NL +
      "skulderna till staten minskar."));
  }

}
