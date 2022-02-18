package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.wrap.Format;
import se.epochtimes.backend.text.model.wrap.WordWrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.NL;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.join;

public class WordWrapperTest {

  private String defaultInsert(String insert) {
    WordWrapper ww = new WordWrapper(insert, Format.DEFAULT);
    return join(ww.wrapWordsWithBisect());
  }

  private String paragraphInsert(String insert, int startingSpaces) {
    WordWrapper ww = new WordWrapper(insert, Format.PARAGRAPH, startingSpaces);
    return join(ww.wrapWordsWithBisect());
  }

  @Test
  void throwsExceptionWhenNull() {
    assertThrows(NullPointerException.class, () -> defaultInsert(null));
  }

  @Test
  void sameLetterIfLetter() {
    assertThat(defaultInsert("a"), is("a" + NL));
  }

  @Test
  void sendEmptyIfWhiteSpace() {
    assertThat(defaultInsert(" "), is(NL));
  }

  @Test
  void sameShortIfShort() {
    assertThat(defaultInsert("ab"), is("ab" + NL));
  }

  @Test
  void noLeadingWhiteSpace() {
    assertThat(defaultInsert(" a"), is("a" + NL));
  }

  @Test
  void wrapParagraphIfTwoLongWords() {
    assertThat(defaultInsert("Storbritanniens invandringslagstiftning"),
      is("Storbritanniens" + NL + "invandringslagstiftning" + NL)
    );
  }

  @Test
  void wrapParagraphIfThreeWordsTooWide() {
    assertThat(defaultInsert("opinionsundersökning från SvD/GP/Sifo."),
      is("opinionsundersökning" + NL + "från SvD/GP/Sifo." + NL));
  }

  final String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den särskilda insats som inleddes " +
      "efter att det inkommit ett 20-tal observationer av drönare runt om i " +
      "Sverige. Bland annat observerades drönare över kärnkraftverk.";

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    assertThat(defaultInsert(unprocessPreamble.substring(0, 34)),
      is("Polisen kommer under" + NL + "veckan trappa" + NL));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    assertThat(defaultInsert(unprocessPreamble.substring(0, 63)),
      is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda" + NL + "insats som" + NL));
  }

  @Test
  void veryLongWordShouldBreakItself() {
    assertThat(defaultInsert("gravmonumentsindustrifabrikationsprodukterna"),
      is("gravmonumentsindustrif-" + NL + "abrikationsprodukterna" + NL));
  }

  @Test
  void veryLongWordAtTheEnd() {
    assertThat(defaultInsert("trappa ned den särskilda insats " +
      "gravmonumentsindustrifabrikationsprodukterna"),
      is("trappa ned den särskilda" + NL + "insats gravmonumentsindustrif-"
        + NL + "abrikationsprodukterna" + NL));
  }

  @Test
  void breakLineAtEnd() {
    assertThat(defaultInsert("inkommit ett 20-tal observationer"),
      is("inkommit ett 20-tal" + NL + "observationer" + NL));
  }

  @Test
  void veryLongLeftOver() {
    assertThat(defaultInsert("trappa " +
      "gravmonumentsindustrifabrikationsprodukterna"),
      is("trappa gravmonumentsindustrif-"
      + NL + "abrikationsprodukterna" + NL));
  }

  @Test
  void wrapParagraphFourTimesWithNineteenWords() {
    assertThat(defaultInsert("Polisen kommer under veckan trappa ned den " +
      "särskilda insats som inleddes efter att det inkommit ett 20-tal " +
      "observationer av drönare"), is("Polisen kommer under veckan" + NL +
      "trappa ned den särskilda insats" + NL + "som inleddes efter att det"
      + NL + "inkommit ett 20-tal" + NL + "observationer av drönare" + NL));
  }

  @Test
  void wrapParagraphSixTimesWithTwentyNineWords() {
    assertThat(defaultInsert(unprocessPreamble), is("" +
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
    assertThat(defaultInsert("Det samlade skuldberget hos Kronofogden " +
      "uppgår till 94 miljarder kronor."), is("Det samlade skuldberget hos"
      + NL + "Kronofogden uppgår till 94" + NL + "miljarder kronor." + NL));
  }

  @Test
  void temp() {
    WordWrapper ww = new WordWrapper(
      "Regeringen föreslår att det ska bli tydligare krav och skärpta " +
        "regler för religiösa inslag i förskolor, skolor och fritidshem. " +
        "Bland annat handlar det om en noggrannare kontroll av huvudmännen.",
      Format.LEAD);
    assertThat(join(ww.wrapWords()), is("" +
      "Regeringen föreslår att det" + NL +
      "ska bli tydligare krav och" + NL +
      "skärpta regler för religiösa" + NL +
      "inslag i förskolor, skolor" + NL +
      "och fritidshem. Bland annat" + NL +
      "handlar det om en noggrannare" + NL +
      "kontroll av huvudmännen." + NL));
  }

  @Test
  void body1() {
    assertThat(paragraphInsert("I slutet av 2021 fanns drygt 391 000 " +
      "personer registrerade hos Kronofogden. Det rör sig om en minskning " +
      "med lite över 11 000 personer jämfört med året innan. Kronofogdens " +
      "analytiker Davor Vuleta säger i ett uttalande att en av orsakerna " +
      "till att antalet blivit färre har att göra med att skulderna till " +
      "staten minskar.", 3), is("" +
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
    assertThat(paragraphInsert("– Det beror främst på att radio- " +
      "och tv-avgiften avskaffats, men också på att färre får skulder rela" +
      "terade till exempelvis fordon, skatt och studier.", 2), is("" +
      "  – Det beror främst på att" + NL +
      "radio- och tv-avgiften avska-" + NL +
      "ffats, men också på att färre får" + NL +
      "skulder relaterade till exemp-" + NL +
      "elvis fordon, skatt och studier." + NL));
  }

  @Test
  void body3() {
    assertThat(paragraphInsert("Enligt Vuleta handlar det också om att " +
      "\"den ekonomiska återhämtningen varit stark\".", 2), is("" +
      "  Enligt Vuleta handlar det" + NL +
      "också om att \"den ekonomiska" + NL +
      "återhämtningen varit stark\"." + NL
    ));
  }

  @Test
  void body4() {
    assertThat(paragraphInsert("– De olika stödpaketen har mildrat " +
      "effekterna av pandemin och fungerat som en sorts krockkudde.", 2),
      is("  – De olika stödpaketen har" + NL +
      "mildrat effekterna av pandemin" + NL +
      "och fungerat som en sorts" + NL +
      "krockkudde." + NL
    ));
  }

  @Test
  void body5() {
    assertThat(paragraphInsert("Vid årsskiftet var den samlade skulden hos " +
      "Kronofogden 94 miljarder kronor, en ökning med sju miljarder " +
      "kronor sedan 2020 eller nästan 20 miljoner kronor om dagen. På tio " +
      "år har skuldberget vuxit med över 32 miljarder kronor.", 2), is("" +
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
    assertThat(paragraphInsert("De personer som har skulder hos Kronofogden " +
      "betalar i regel först räntor och avgifter. Endast om pengarna räcker " +
      "går betalningarna till själva skulden.", 2), is("" +
      "  De personer som har skulder hos" + NL +
      "Kronofogden betalar i regel först" + NL +
      "räntor och avgifter. Endast om" + NL +
      "pengarna räcker går betalningarna" + NL +
      "till själva skulden." + NL
    ));
  }

  @Test
  void body7() {
    assertThat(paragraphInsert("– Många skuldsatta betalar år efter år " +
      "genom att vi mäter ut deras lön. Trots det är grundskulden kvar. Det " +
      "de betalar går till räntorna. Det tycker vi är ett systemfel som gör " +
      "att människor fastnar i decennier med skulder hos oss, säger " +
      "biträdande rikskronofogde Cecilia Hegethorn Mogensen.", 2), is("" +
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
    assertThat(paragraphInsert("I 46 av landets 290 kommuner ökar antalet " +
      "personer med skulder till Kronofogden.", 2), is("" +
      "  I 46 av landets 290 kommuner" + NL +
      "ökar antalet personer med" + NL +
      "skulder till Kronofogden." + NL
    ));
  }

  @Test
  void body9() {
    assertThat(paragraphInsert("Högst andel skuldsatta invånare återfinns i " +
      "kommunen Ljusnarsberg i Örebro län följt av Perstorp i Skåne och Eda " +
      "i Värmland. Lägst andel skuldsatta har skånska Lomma, som följs av " +
      "Danderyd och Täby i Stockholmsområdet", 2), is("" +
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
