package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.wrap.Format;
import se.epochtimes.backend.text.model.wrap.WordWrapper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.NL;

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
    assertThat(output, is("opinionsundersökning" + NL + "från SvD/GP/Sifo." + NL));
  }

  final String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den särskilda insats som inleddes " +
      "efter att det inkommit ett 20-tal observationer av drönare runt om i " +
      "Sverige. Bland annat observerades drönare över kärnkraftverk.";

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    defaultInsert(unprocessPreamble.substring(0, 34));
    assertThat(output, is("Polisen kommer under" + NL + "veckan trappa" + NL));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    defaultInsert(unprocessPreamble.substring(0, 63));
    assertThat(output, is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda" + NL + "insats som" + NL));
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
    assertThat(output, is("trappa ned den särskilda" + NL +
      "insats gravmonumentsindustrif-" + NL + "abrikationsprodukterna" + NL));
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
  void body3() {
    paragraphInsert("Enligt Vuleta handlar det också om att " +
      "\"den ekonomiska återhämtningen varit stark\".", 2);
    assertThat(output, is("" +
      "  Enligt Vuleta handlar det" + NL +
      "också om att \"den ekonomiska" + NL +
      "återhämtningen varit stark\"." + NL
    ));
  }

  @Test
  void body4() {
    paragraphInsert("– De olika stödpaketen har mildrat effekterna av " +
      "pandemin och fungerat som en sorts krockkudde.", 2);
    assertThat(output, is("" +
      "  – De olika stödpaketen har" + NL +
      "mildrat effekterna av pandemin" + NL +
      "och fungerat som en sorts" + NL +
      "krockkudde." + NL
    ));
  }

  @Test
  void body5() {
    paragraphInsert("Vid årsskiftet var den samlade skulden hos " +
      "Kronofogden 94 miljarder kronor, en ökning med sju miljarder " +
      "kronor sedan 2020 eller nästan 20 miljoner kronor om dagen. På tio " +
      "år har skuldberget vuxit med över 32 miljarder kronor.", 2);
    assertThat(output, is("" +
      "  Vid årsskiftet var den samlade" + NL +
      "skulden hos Kronofogden 94" + NL +
      "miljarder kronor, en ökning med" + NL +
      "sju miljarder kronor sedan 2020" + NL +
      "eller nästan 20 miljoner kronor" + NL +
      "om dagen. På tio år har" + NL +
      "skuldberget vuxit med över 32" + NL +
      "miljarder kronor." + NL
    ));
  }

  @Test
  void body6() {
    paragraphInsert("De personer som har skulder hos Kronofogden betalar i " +
      "regel först räntor och avgifter. Endast om pengarna räcker går " +
      "betalningarna till själva skulden.", 2);
    assertThat(output, is("" +
      "  De personer som har skulder hos" + NL +
      "Kronofogden betalar i regel först" + NL +
      "räntor och avgifter. Endast om" + NL +
      "pengarna räcker går betalningarna" + NL +
      "till själva skulden." + NL
    ));
  }

  @Test
  void body7() {
    paragraphInsert("– Många skuldsatta betalar år efter år genom att vi " +
      "mäter ut deras lön. Trots det är grundskulden kvar. Det de betalar " +
      "går till räntorna. Det tycker vi är ett systemfel som gör att " +
      "människor fastnar i decennier med skulder hos oss, säger biträdande " +
      "rikskronofogde Cecilia Hegethorn Mogensen.", 2);
    assertThat(output, is("" +
      "  – Många skuldsatta betalar år" + NL +
      "efter år genom att vi mäter ut" + NL +
      "deras lön. Trots det är" + NL +
      "grundskulden kvar. Det de betalar" + NL +
      "går till räntorna. Det tycker vi" + NL +
      "är ett systemfel som gör att" + NL +
      "människor fastnar i decennier med" + NL +
      "skulder hos oss, säger biträdande" + NL +
      "rikskronofogde Cecilia Hegethorn" + NL +
      "Mogensen." + NL
    ));
  }

  @Test
  void body8() {
    paragraphInsert("I 46 av landets 290 kommuner ökar antalet personer med " +
      "skulder till Kronofogden.", 2);
    assertThat(output, is("" +
      "  I 46 av landets 290 kommuner" + NL +
      "ökar antalet personer med" + NL +
      "skulder till Kronofogden." + NL
    ));
  }

  @Test
  void body9() {
    paragraphInsert("Högst andel skuldsatta invånare återfinns i kommunen " +
      "Ljusnarsberg i Örebro län följt av Perstorp i Skåne och Eda i " +
      "Värmland. Lägst andel skuldsatta har skånska Lomma, som följs av " +
      "Danderyd och Täby i Stockholmsområdet", 2);
    assertThat(output, is("" +
        "  Högst andel skuldsatta invånare" + NL +
      "återfinns i kommunen Ljusnarsberg" + NL +
      "i Örebro län följt av Perstorp i" + NL +
      "Skåne och Eda i Värmland. Lägst" + NL +
      "andel skuldsatta har skånska" + NL +
      "Lomma, som följs av Danderyd och" + NL +
      "Täby i Stockholmsområdet" + NL
    ));
  }

}
