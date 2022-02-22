package se.epochtimes.backend.text.model.wrap;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.headline.HeadlineComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.NL;

public class WordWrapperTest {

  private int max = 33;

  private String paragraphInsert(String insert) {
    return paragraphInsert(insert, 0);
  }

  private String paragraphInsert(String insert, int startingSpaces) {
    return WordWrapper.formatParagraph(insert, startingSpaces);
  }

  private boolean isValid(String actualText, int expectedNewLines, Format format) {
    this.max = format.getMax();
    return switch (format) {
      case HEADLINE, LEAD -> isValid(actualText, expectedNewLines);
      case PARAGRAPH -> isValidBody(actualText, expectedNewLines);
    };
  }

  private boolean isValidBody(String actualText, int expectedNewLines) {
      String[] paragraphs = actualText.split("(\r\n )+|(\r )+|(\n )+");
      int actualNewLines = -1;
      int maxLineLength = 0;
      boolean first = true;
      for (String p : paragraphs) {
        StringBuilder sb = new StringBuilder();
        if (!first) {
          sb.append(" ");
        }
        first = false;
        sb.append(p);
        Matcher m = Pattern.compile("(\r\n)|(\r)|(\n)").matcher(sb.toString());
        int newLines = 0, previous = -1;
        while (m.find()) {
          newLines++;
          int latest = m.start();
          int lineLength = latest - previous - 1;
          previous = latest;
          if (lineLength > maxLineLength) {
            maxLineLength = lineLength;
          }
        }
        actualNewLines += newLines + 1;
      }
      return (maxLineLength <= max) & (expectedNewLines == actualNewLines);
  }

  private boolean isValid(String wrappedText, int expectedNewLines) {
    Matcher m = Pattern.compile("(\r\n)|(\r)|(\n)").matcher(wrappedText);
    int actualNewLines = 0, previous = -1, maxLineLength = 0;
    while (m.find()){
      actualNewLines ++;
      int latest = m.start();
      int lineLength = latest - previous - 1;
      previous = latest;
      if(lineLength > maxLineLength) {
        maxLineLength = lineLength;
      }
    }
    return (maxLineLength <= max) & (expectedNewLines == actualNewLines);
  }

  @Test
  void throwsExceptionWhenNull() {
    assertThrows(NullPointerException.class, () -> paragraphInsert(null));
  }

  @Test
  void sameLetterIfLetter() {
    final String given = "a";
    final String when = paragraphInsert(given);

    assertThat(when, is(given + NL));
    assertThat(when.length(), lessThan(max));
  }

  @Test
  void sendEmptyIfWhiteSpace() {
    final String given = " ";
    final String when = paragraphInsert(given);

    assertThat(when, is(NL));
    assertThat(when.length(), lessThan(max));
  }

  @Test
  void sameShortIfShort() {
    final String given = "ab";
    final String when = paragraphInsert(given);

    assertThat(when, is(given + NL));
    assertThat(when.length(), lessThan(max));
  }

  @Test
  void noLeadingWhiteSpace() {
    final String given = " a";
    final String when = paragraphInsert(given);

    assertThat(when, is(given.trim() + NL));
    assertThat(when.length(), lessThan(max));
  }

  @Test
  void veryLongWordShouldBreakItself() {
    final String given = "gravmonumentsindustrifabrikationsprodukterna";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void wrapParagraphIfTwoLongWords() {
    final String given = "Storbritanniens invandringslagstiftning";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void wrapParagraphIfThreeWordsTooWide() {
    final String given = "opinionsundersökning från SvD/GP/Sifo.";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void bisectIfThreeLongWords() {
    final String given = "abcdefghijklmno " + "abcdefghijklmnopqrstuvwxyzåäö " + "pqrstuvwxyzåäö";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void notBisectIfThreeLongWordsAndNoRoom() {
    final String given = "abcdefghijklmnopqrstuvwxyzåäö" + "abcdefghijklmnopqrstuvwxyzåäö " + "pqrstuvwxyzåäö";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 3));
  }

  @Test
  void veryLongLeftOver() {
    final String given = "trappa " +
      "gravmonumentsindustrifabrikationsprodukterna";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void veryLongWordAtTheEnd() {
    final String given = "trappa ned den särskilda insats " +
      "gravmonumentsindustrifabrikationsprodukterna";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 3));
  }

  final String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den särskilda insats som inleddes "
      + "efter att det inkommit ett 20-tal observationer av drönare runt om i "
      + "Sverige. Bland annat observerades drönare över kärnkraftverk.";

  @Test
  void wrapParagraphIfFourWordsTooWide() {
    final String given = unprocessPreamble.substring(0, 34);
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void wrapParagraphTwoTimesWithTenWords() {
    final String given = unprocessPreamble.substring(0, 63);
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 3));
  }

  @Test
  void wrapParagraphFourTimesWithNineteenWords() {
    final String given = unprocessPreamble.substring(0, 131);
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 5));
  }

  @Test
  void wrapParagraphSixTimesWithTwentyNineWords() {
    final String when = paragraphInsert(unprocessPreamble);
    assertTrue(isValid(when, 7));
  }

  @Test
  void imageCredit() {
    final String given = "Det samlade skuldberget hos " +
      "Kronofogden uppgår till 94 miljarder kronor.";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 3));
  }

  @Test
  void body1() {
    final String given = "I slutet av 2021 fanns drygt " +
      "391 000 personer registrerade hos Kronofogden. Det rör sig om en " +
      "minskning med lite över 11 000 personer jämfört med året innan. " +
      "Kronofogdens analytiker Davor Vuleta säger i ett uttalande att en av " +
      "orsakerna till att antalet blivit färre har att göra med att " +
      "skulderna till staten minskar.";
    final String when = paragraphInsert(given, 3);
    assertTrue(isValid(when, 10));
  }

  @Test
  void body2() {
    final String given = "– Det beror främst på att radio- "
      + "och tv-avgiften avskaffats, men också på att färre får skulder rela" +
      "terade till exempelvis fordon, skatt och studier.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 6));
  }

  @Test
  void body3() {
    final String given = "Enligt Vuleta handlar det också " +
      "om att \"den ekonomiska återhämtningen varit stark\".";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 3));
  }

  @Test
  void body4() {
    final String given = "– De olika stödpaketen har mildrat "
      + "effekterna av pandemin och fungerat som en sorts krockkudde.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 4));
  }

  @Test
  void body5() {
    final String given = "Vid årsskiftet var den samlade " +
      "skulden hos Kronofogden 94 miljarder kronor, en ökning med sju " +
      "miljarder kronor sedan 2020 eller nästan 20 miljoner kronor om dagen." +
      " På tio år har skuldberget vuxit med över 32 miljarder kronor.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 8));
  }

  @Test
  void body6() {
    final String given = "De personer som har skulder hos " +
      "Kronofogden betalar i regel först räntor och avgifter. Endast om " +
      "pengarna räcker går betalningarna till själva skulden.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 5));
  }

  @Test
  void body7() {
    final String given = "– Många skuldsatta betalar år " +
      "efter år genom att vi mäter ut deras lön. Trots det är grundskulden " +
      "kvar. Det de betalar går till räntorna. Det tycker vi är ett " +
      "systemfel som gör att människor fastnar i decennier med skulder hos " +
      "oss, säger biträdande rikskronofogde Cecilia Hegethorn Mogensen.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 10));
  }

  @Test
  void body8() {
    final String given = "I 46 av landets 290 kommuner " +
      "ökar antalet personer med skulder till Kronofogden.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 3));
  }

  @Test
  void body9() {
    final String given = "Högst andel skuldsatta invånare " +
      "återfinns i kommunen Ljusnarsberg i Örebro län följt av Perstorp i " +
      "Skåne och Eda i Värmland. Lägst andel skuldsatta har skånska Lomma, " +
      "som följs av Danderyd och Täby i Stockholmsområdet";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 7));
  }

  @Test
  void newBody1() {
    final String given = "S gick till val 2018 på att " +
      "förbjuda religiösa friskolor. Men ett förbud finns det i dag inte en " +
      "majoritet för i riksdagen. Skolministern säger emellertid att ett " +
      "etableringsstopp bereds just nu i regeringskansliet. Ett " +
      "etableringsstopp har dock fått kritik då det riskerar att bryta mot " +
      "religionsfriheten såväl som Europakonventionen.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 11));
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

  private static final String givenBody = """
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

  private static final String expectedBody = """
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
      """;

  @Test
  void integrationOldArticle() {
    final HeadlineComponent givenHC = new HeadlineComponent(HEADLINE, LEAD);
    final String actualBody = WordWrapper.formatBody(givenBody);
    final HeadlineComponent actualHC = WordWrapper.format(givenHC);
    int expectedHeadlineNewLines = countNewLines(FORMATTED_HEADLINE);
    int expectedLeadNewLines = countNewLines(FORMATTED_LEAD);
    int expectedBodyNewLines = countNewLines(expectedBody);
    assertTrue(isValid(actualHC.getHeadline(), expectedHeadlineNewLines, Format.HEADLINE));
    assertTrue(isValid(actualHC.getLead(), expectedLeadNewLines, Format.LEAD));
    assertTrue(isValid(actualBody, expectedBodyNewLines, Format.PARAGRAPH));
  }

  private int countNewLines(String wrappedText) {
    Matcher m = Pattern.compile("(\r\n)|(\r)|(\n)").matcher(wrappedText);
    int newLines = 0;
    while (m.find())
      newLines ++;
    return newLines;
  }

}
