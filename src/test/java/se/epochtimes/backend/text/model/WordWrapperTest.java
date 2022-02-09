package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.epochtimes.backend.text.model.WordWrapper.NL;

public class WordWrapperTest {

  private String output;

  private void setOutput(List<String> lines) {
    StringBuilder sb = new StringBuilder();
    for(String l: lines) {
      sb.append(l);
      sb.append(NL);
    }
    output = sb.toString();
  }

  private void defaultInsert(String insert) {
    WordWrapper ww = new WordWrapper(insert, Format.DEFAULT);
    setOutput(ww.wrapWordsWithBisect());
  }

  private void paragraphInsert(String insert, int startingSpaces) {
    WordWrapper ww = new WordWrapper(insert, Format.PARAGRAPH, startingSpaces);
    setOutput(ww.wrapWordsWithBisect());
  }

  @Test
  void throwsExceptionWhenNull() {
    assertThrows(NullPointerException.class, () -> defaultInsert(null));
  }

  @Test
  void sameLetterIfLetter() {
    defaultInsert("a");
    assertThat(output, is("a" + NL));
  }

  @Test
  void sendEmptyIfWhiteSpace() {
    defaultInsert(" ");
    assertThat(output, is(NL));
  }

  @Test
  void sameShortIfShort() {
    defaultInsert("ab");
    assertThat(output, is("ab" + NL));
  }

  @Test
  void noLeadingWhiteSpace() {
    defaultInsert(" a");
    assertThat(output, is("a" + NL));
  }

  @Test
  void wrapParagraphIfTwoLongWords() {
    defaultInsert("Storbritanniens invandringslagstiftning");
    assertThat(output,
      is("Storbritanniens" + NL + "invandringslagstiftning" + NL)
    );
  }

  @Test
  void wrapParagraphIfThreeWordsTooWide() {
    defaultInsert("opinionsundersökning från SvD/GP/Sifo.");
    assertThat(output, is("opinionsundersökning från" + NL + "SvD/GP/Sifo." + NL));
  }

  final String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den särskilda insats som inleddes " +
      "efter att det inkommit ett 20-tal observationer av drönare runt om i " +
      "Sverige. Bland annat observerades drönare över kärnkraftverk.";

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    defaultInsert(unprocessPreamble.substring(0, 34));
    assertThat(output, is("Polisen kommer under veckan" + NL + "trappa" + NL));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    defaultInsert(unprocessPreamble.substring(0, 63));
    assertThat(output, is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL + "som" + NL));
  }

  @Test
  void veryLongWordShouldBreakItself() {
    defaultInsert("gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("gravmonumentsindustrif-"
      + NL + "abrikationsprodukterna" + NL));
  }

  @Test
  void veryLongWordAtTheEnd() {
    defaultInsert("trappa ned den särskilda insats " +
      "gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("trappa ned den särskilda insats" + NL +
      "gravmonumentsindustrif-" + NL + "abrikationsprodukterna" + NL));
  }

  @Test
  void breakLineAtEnd() {
    defaultInsert("inkommit ett 20-tal observationer");
    assertThat(output, is("inkommit ett 20-tal" + NL + "observationer" + NL));
  }

  @Test
  void veryLongLeftOver() {
    defaultInsert("trappa gravmonumentsindustrifabrikationsprodukterna");
    assertThat(output, is("trappa gravmonumentsindustrif-"
      + NL + "abrikationsprodukterna" + NL));
  }

  @Test
  void wrapParagraphFourTimesWithNineteenWords() {
    defaultInsert("Polisen kommer under veckan trappa ned den särskilda insats som " +
      "inleddes efter att det inkommit ett 20-tal observationer av drönare");
    assertThat(output, is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL + "som inleddes efter att det"
      + NL + "inkommit ett 20-tal" + NL + "observationer av drönare" + NL));
  }

  @Test
  void wrapParagraphSixTimesWithTwentyNineWords() {
    defaultInsert(unprocessPreamble);
    assertThat(output, is("" +
      "Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL +
      "som inleddes efter att det inko-" + NL +
      "mmit ett 20-tal observationer av" + NL +
      "drönare runt om i Sverige. Bland" + NL +
      "annat observerades drönare över" + NL +
      "kärnkraftverk." + NL));
  }

  @Test
  void longArticle() {
    defaultInsert("Det samlade skuldberget hos Kronofogden " +
      "uppgår till 94 miljarder kronor.");
    assertThat(output, is("Det samlade skuldberget hos" + NL +
      "Kronofogden uppgår till 94" + NL + "miljarder kronor." + NL));
  }

  @Test
  void title() {
    WordWrapper ww = new WordWrapper(
      "Kronofogden: De samlade skulderna större än någonsin", Format.HEADLINE);
    setOutput(ww.wrapWords());
    assertThat(output, is("Kronofogden:" + NL + "De samlade" + NL +
      "skulderna" + NL + "större än" + NL + "någonsin" + NL));
  }

  @Test
  void ingress() {
    WordWrapper ww = new WordWrapper(
      "Antalet svenskar som har skulder hos Kronofogden är det lägsta " +
      "på 30 år. Däremot är det samlade skuldberget större än någonsin och " +
      "växer snabbt. Det visar ny statistik från myndigheten.", Format.LEAD);
    setOutput(ww.wrapWords());
    assertThat(output, is("" +
      "Antalet svenskar som har" + NL +
      "skulder hos Kronofogden är det" + NL +
      "lägsta på 30 år. Däremot är" + NL +
      "det samlade skuldberget större" + NL +
      "än någonsin och växer snabbt." + NL +
      "Det visar ny statistik från" + NL +
      "myndigheten." + NL));
  }

  @Test
  void body1() {
    paragraphInsert("I slutet av 2021 fanns drygt 391 000 " +
      "personer registrerade hos Kronofogden. Det rör sig om en minskning " +
      "med lite över 11 000 personer jämfört med året innan. Kronofogdens " +
      "analytiker Davor Vuleta säger i ett uttalande att en av orsakerna " +
      "till att antalet blivit färre har att göra med att skulderna till " +
      "staten minskar.", 3);
    assertThat(output, is("" +
      "   I slutet av 2021 fanns drygt" + NL +
      "391 000 personer registrerade hos" + NL +
      "Kronofogden. Det rör sig om en" + NL +
      "minskning med lite över 11 000" + NL +
      "personer jämfört med året innan." + NL +
      "Kronofogdens analytiker Davor" + NL +
      "Vuleta säger i ett uttalande att" + NL +
      "en av orsakerna till att antalet" + NL +
      "blivit färre har att göra med att" + NL +
      "skulderna till staten minskar." + NL));
  }

  @Test
  void body2() {
    paragraphInsert("– Det beror främst på att radio- " +
      "och tv-avgiften avskaffats, men också på att färre får skulder rela" +
      "terade till exempelvis fordon, skatt och studier.", 2);
    assertThat(output, is("" +
      "  – Det beror främst på att" + NL +
      "radio- och tv-avgiften avska-" + NL +
      "ffats, men också på att färre får" + NL +
      "skulder relaterade till exemp-" + NL +
      "elvis fordon, skatt och studier." + NL));
  }

  @Test
  @Disabled
  void body3() {
    paragraphInsert("Enligt Vuleta handlar det också om att " +
      "\"den ekonomiska återhämtningen varit stark\".", 2);
    assertThat(output, is("" +
      "  Enligt Vuleta handlar det" + NL +
      "också om att \"den ekonomiska" + NL +
      "återhämtningen varit stark\"." + NL
    ));
  }

}
