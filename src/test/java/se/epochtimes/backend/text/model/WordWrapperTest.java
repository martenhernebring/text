package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.main.HeadlineComponent;
import se.epochtimes.backend.text.model.wrap.Format;
import se.epochtimes.backend.text.model.wrap.WordWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.NL;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.join;

public class WordWrapperTest {

  private String paragraphInsert(String insert) {
    return paragraphInsert(insert, 0);
  }

  private String paragraphInsert(String insert, int startingSpaces) {
    WordWrapper ww = new WordWrapper(insert, Format.PARAGRAPH, startingSpaces);
    return join(ww.wrapWordsWithBisect());
  }

  private int countNewLines(String wrappedText) {
    Matcher m = Pattern.compile("(\r\n)|(\r)|(\n)").matcher(wrappedText);
    int newLines = 0;
    while (m.find())
      newLines ++;
    return newLines;
  }

  @Test
  void throwsExceptionWhenNull() {
    assertThrows(NullPointerException.class, () -> paragraphInsert(null));
  }

  @Test
  void sameLetterIfLetter() {
    assertThat(paragraphInsert("a"), is("a" + NL));
  }

  @Test
  void sendEmptyIfWhiteSpace() {
    assertThat(paragraphInsert(" "), is(NL));
  }

  @Test
  void sameShortIfShort() {
    assertThat(paragraphInsert("ab"), is("ab" + NL));
  }

  @Test
  void noLeadingWhiteSpace() {
    assertThat(paragraphInsert(" a"), is("a" + NL));
  }

  @Test
  void wrapParagraphIfTwoLongWords() {
    final String wrapped = paragraphInsert(
      "Storbritanniens invandringslagstiftning");
    assertThat(countNewLines(wrapped), is(2));
  }

  @Test
  void wrapParagraphIfThreeWordsTooWide() {
    final String wrapped = paragraphInsert(
      "opinionsundersökning från SvD/GP/Sifo.");
    assertThat(countNewLines(wrapped), is(2));
  }

  final String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den särskilda insats som inleddes "
      + "efter att det inkommit ett 20-tal observationer av drönare runt om i "
      + "Sverige. Bland annat observerades drönare över kärnkraftverk.";

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    final String wrapped = paragraphInsert(unprocessPreamble.substring(0, 34));
    assertThat(countNewLines(wrapped), is(2));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    final String wrapped = paragraphInsert(unprocessPreamble.substring(0, 63));
    assertThat(countNewLines(wrapped), is(3));
  }

  @Test
  void veryLongWordShouldBreakItself() {
    final String wrapped = paragraphInsert(
      "gravmonumentsindustrifabrikationsprodukterna");
    assertThat(countNewLines(wrapped), is(2));
  }

  @Test
  void veryLongWordAtTheEnd() {
    final String wrapped = paragraphInsert("trappa ned den särskilda insats " +
      "gravmonumentsindustrifabrikationsprodukterna");
    assertThat(countNewLines(wrapped), is(3));
  }

  @Test
  void veryLongLeftOver() {
    final String wrapped = paragraphInsert("trappa " +
      "gravmonumentsindustrifabrikationsprodukterna");
    assertThat(countNewLines(wrapped), is(2));
  }

  @Test
  void wrapParagraphFourTimesWithNineteenWords() {
    final String wrapped = paragraphInsert("Polisen kommer under veckan " +
      "trappa ned den särskilda insats som inleddes efter att det inkommit " +
      "ett 20-tal observationer av drönare");
    assertThat(countNewLines(wrapped), is(5));
  }

  @Test
  void wrapParagraphSixTimesWithTwentyNineWords() {
    final String wrapped = paragraphInsert(unprocessPreamble);
    assertThat(countNewLines(wrapped), is(7));
  }

  @Test
  void longArticle() {
    final String wrapped = paragraphInsert("Det samlade skuldberget hos " +
      "Kronofogden uppgår till 94 miljarder kronor.");
    assertThat(countNewLines(wrapped), is(3));
  }

  @Test
  void body1() {
    final String wrapped = paragraphInsert("I slutet av 2021 fanns drygt " +
      "391 000 personer registrerade hos Kronofogden. Det rör sig om en " +
      "minskning med lite över 11 000 personer jämfört med året innan. " +
      "Kronofogdens analytiker Davor Vuleta säger i ett uttalande att en av " +
      "orsakerna till att antalet blivit färre har att göra med att " +
      "skulderna till staten minskar.", 3);
    assertThat(countNewLines(wrapped), is(10));
  }

  @Test
  void body2() {
    final String wrapped = paragraphInsert("– Det beror främst på att radio- "
      + "och tv-avgiften avskaffats, men också på att färre får skulder rela" +
      "terade till exempelvis fordon, skatt och studier.", 2);
    assertThat(countNewLines(wrapped), is(6));
  }

  @Test
  void body3() {
    final String wrapped = paragraphInsert("Enligt Vuleta handlar det också " +
      "om att \"den ekonomiska återhämtningen varit stark\".", 2);
    assertThat(countNewLines(wrapped), is(3));
  }

  @Test
  void body4() {
    final String wrapped = paragraphInsert("– De olika stödpaketen har mildrat "
      + "effekterna av pandemin och fungerat som en sorts krockkudde.", 2);
    assertThat(countNewLines(wrapped), is(4));
  }

  @Test
  void body5() {
    final String wrapped = paragraphInsert("Vid årsskiftet var den samlade " +
      "skulden hos Kronofogden 94 miljarder kronor, en ökning med sju " +
      "miljarder kronor sedan 2020 eller nästan 20 miljoner kronor om dagen." +
      " På tio år har skuldberget vuxit med över 32 miljarder kronor.", 2);
    assertThat(countNewLines(wrapped), is(8));
  }

  @Test
  void body6() {
    final String wrapped = paragraphInsert("De personer som har skulder hos " +
      "Kronofogden betalar i regel först räntor och avgifter. Endast om " +
      "pengarna räcker går betalningarna till själva skulden.", 2);
    assertThat(countNewLines(wrapped), is(5));
  }

  @Test
  void body7() {
    final String wrapped = paragraphInsert("– Många skuldsatta betalar år " +
      "efter år genom att vi mäter ut deras lön. Trots det är grundskulden " +
      "kvar. Det de betalar går till räntorna. Det tycker vi är ett " +
      "systemfel som gör att människor fastnar i decennier med skulder hos " +
      "oss, säger biträdande rikskronofogde Cecilia Hegethorn Mogensen.", 2);
    assertThat(countNewLines(wrapped), is(10));
  }

  @Test
  void body8() {
    final String wrapped = paragraphInsert("I 46 av landets 290 kommuner " +
      "ökar antalet personer med skulder till Kronofogden.", 2);
    assertThat(countNewLines(wrapped), is(3));
  }

  @Test
  void body9() {
    final String wrapped = paragraphInsert("Högst andel skuldsatta invånare " +
      "återfinns i kommunen Ljusnarsberg i Örebro län följt av Perstorp i " +
      "Skåne och Eda i Värmland. Lägst andel skuldsatta har skånska Lomma, " +
      "som följs av Danderyd och Täby i Stockholmsområdet", 2);
    assertThat(countNewLines(wrapped), is(7));
  }

  @Test
  void newBody1() {
    final String wrapped = paragraphInsert("S gick till val 2018 på att " +
      "förbjuda religiösa friskolor. Men ett förbud finns det i dag inte en " +
      "majoritet för i riksdagen. Skolministern säger emellertid att ett " +
      "etableringsstopp bereds just nu i regeringskansliet. Ett " +
      "etableringsstopp har dock fått kritik då det riskerar att bryta mot " +
      "religionsfriheten såväl som Europakonventionen.", 2);
    assertThat(countNewLines(wrapped), is(11));
  }

  public static final String HEADLINE =
    "Kronofogden: De samlade skulderna större än någonsin";
  public static final String LEAD =
    "Antalet svenskar som har skulder hos Kronofogden är det lägsta på 30 " +
      "år. Däremot är det samlade skuldberget större än någonsin och växer " +
      "snabbt. Det visar ny statistik från myndigheten.";

  public static final String FORMATTED_LEAD = "" +
    "Antalet svenskar som har" + NL +
    "skulder hos Kronofogden är det" + NL +
    "lägsta på 30 år. Däremot är" + NL +
    "det samlade skuldberget större" + NL +
    "än någonsin och växer snabbt." + NL +
    "Det visar ny statistik från" + NL +
    "myndigheten." + NL;

  public static final String FORMATTED_HEADLINE = "Kronofogden:" + NL +
    "De samlade" + NL + "skulderna" + NL + "större än" + NL + "någonsin" + NL;

  @Test
  void integrationOldArticle() {
    HeadlineComponent fhc = WordWrapper
      .format(new HeadlineComponent(HEADLINE, LEAD));
    String body = """
      I slutet av 2021 fanns drygt 391 000 personer registrerade hos Kronofogden. Det rör sig om en minskning med lite över 11 000 personer jämfört med året innan. Kronofogdens analytiker Davor Vuleta säger i ett uttalande att en av orsakerna till att antalet blivit färre har att göra med att skulderna till staten minskar.
      – Det beror främst på att radio- och tv-avgiften avskaffats, men också på att färre får skulder relaterade till exempelvis fordon, skatt och studier.
      Enligt Vuleta handlar det också om att "den ekonomiska återhämtningen varit stark".
      – De olika stödpaketen har mildrat effekterna av pandemin och fungerat som en sorts krockkudde.
      Vid årsskiftet var den samlade skulden hos Kronofogden 94 miljarder kronor, en ökning med sju miljarder kronor sedan 2020 eller nästan 20 miljoner kronor om dagen. På tio år har skuldberget vuxit med över 32 miljarder kronor.
      De personer som har skulder hos Kronofogden betalar i regel först räntor och avgifter. Endast om pengarna räcker går betalningarna till själva skulden."
      – Många skuldsatta betalar år efter år genom att vi mäter ut deras lön. Trots det är grundskulden kvar. Det de betalar går till räntorna. Det tycker vi är ett systemfel som gör att människor fastnar i decennier med skulder hos oss, säger biträdande rikskronofogde Cecilia Hegethorn Mogensen.
      I 46 av landets 290 kommuner ökar antalet personer med skulder till Kronofogden.
      Högst andel skuldsatta invånare återfinns i kommunen Ljusnarsberg i Örebro län följt av Perstorp i Skåne och Eda i Värmland. Lägst andel skuldsatta har skånska Lomma, som följs av Danderyd och Täby i Stockholmsområdet
      """;
    assertThat(FORMATTED_HEADLINE, is(fhc.getHeadline()));
    assertThat(FORMATTED_LEAD, is(fhc.getLead()));
    assertThat(WordWrapper.formatBody(body), is("""
         I slutet av 2021 fanns drygt
      391 000 personer registrerade hos
      Kronofogden. Det rör sig om en
      minskning med lite över 11 000
      personer jämfört med året innan.
      Kronofogdens analytiker Davor
      Vuleta säger i ett uttalande att
      en av orsakerna till att antalet
      blivit färre har att göra med att
      skulderna till staten minskar.
        – Det beror främst på att
      radio- och tv-avgiften
      avskaffats, men också på att
      färre får skulder relaterade
      till exempelvis fordon, skatt och
      studier.
        Enligt Vuleta handlar det
      också om att "den ekonomiska
      återhämtningen varit stark".
        – De olika stödpaketen har
      mildrat effekterna av pandemin
      och fungerat som en sorts
      krockkudde.
        Vid årsskiftet var den samlade
      skulden hos Kronofogden 94
      miljarder kronor, en ökning med
      sju miljarder kronor sedan 2020
      eller nästan 20 miljoner kronor
      om dagen. På tio år har
      skuldberget vuxit med över 32
      miljarder kronor.
        De personer som har skulder hos
      Kronofogden betalar i regel först
      räntor och avgifter. Endast om
      pengarna räcker går betalningarna
      till själva skulden."
        – Många skuldsatta betalar år
      efter år genom att vi mäter ut
      deras lön. Trots det är
      grundskulden kvar. Det de betalar
      går till räntorna. Det tycker vi
      är ett systemfel som gör att
      människor fastnar i decennier med
      skulder hos oss, säger biträdande
      rikskronofogde Cecilia Hegethorn
      Mogensen.
        I 46 av landets 290 kommuner
      ökar antalet personer med
      skulder till Kronofogden.
        Högst andel skuldsatta invånare
      återfinns i kommunen Ljusnarsberg
      i Örebro län följt av Perstorp i
      Skåne och Eda i Värmland. Lägst
      andel skuldsatta har skånska
      Lomma, som följs av Danderyd och
      Täby i Stockholmsområdet
      """));
  }

}
