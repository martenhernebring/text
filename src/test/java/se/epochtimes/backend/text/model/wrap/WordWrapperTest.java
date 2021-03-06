package se.epochtimes.backend.text.model.wrap;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.headline.HeadlineComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;
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
    final String given = "opinionsunders??kning fr??n SvD/GP/Sifo.";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void bisectIfThreeLongWords() {
    final String given = "abcdefghijklmno " + "abcdefghijklmnopqrstuvwxyz?????? " + "pqrstuvwxyz??????";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 2));
  }

  @Test
  void notBisectIfThreeLongWordsAndNoRoom() {
    final String given = "abcdefghijklmnopqrstuvwxyz??????" + "abcdefghijklmnopqrstuvwxyz?????? " + "pqrstuvwxyz??????";
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
    final String given = "trappa ned den s??rskilda insats " +
      "gravmonumentsindustrifabrikationsprodukterna";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 3));
  }

  final String unprocessPreamble =
    "Polisen kommer under veckan trappa ned den s??rskilda insats som inleddes "
      + "efter att det inkommit ett 20-tal observationer av dr??nare runt om i "
      + "Sverige. Bland annat observerades dr??nare ??ver k??rnkraftverk.";

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
      "Kronofogden uppg??r till 94 miljarder kronor.";
    final String when = paragraphInsert(given);
    assertTrue(isValid(when, 3));
  }

  @Test
  void body1() {
    final String given = "I slutet av 2021 fanns drygt " +
      "391 000 personer registrerade hos Kronofogden. Det r??r sig om en " +
      "minskning med lite ??ver 11 000 personer j??mf??rt med ??ret innan. " +
      "Kronofogdens analytiker Davor Vuleta s??ger i ett uttalande att en av " +
      "orsakerna till att antalet blivit f??rre har att g??ra med att " +
      "skulderna till staten minskar.";
    final String when = paragraphInsert(given, 3);
    assertTrue(isValid(when, 10));
  }

  @Test
  void body2() {
    final String given = "??? Det beror fr??mst p?? att radio- "
      + "och tv-avgiften avskaffats, men ocks?? p?? att f??rre f??r skulder rela" +
      "terade till exempelvis fordon, skatt och studier.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 6));
  }

  @Test
  void body3() {
    final String given = "Enligt Vuleta handlar det ocks?? " +
      "om att \"den ekonomiska ??terh??mtningen varit stark\".";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 3));
  }

  @Test
  void body4() {
    final String given = "??? De olika st??dpaketen har mildrat "
      + "effekterna av pandemin och fungerat som en sorts krockkudde.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 4));
  }

  @Test
  void body5() {
    final String given = "Vid ??rsskiftet var den samlade " +
      "skulden hos Kronofogden 94 miljarder kronor, en ??kning med sju " +
      "miljarder kronor sedan 2020 eller n??stan 20 miljoner kronor om dagen." +
      " P?? tio ??r har skuldberget vuxit med ??ver 32 miljarder kronor.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 8));
  }

  @Test
  void body6() {
    final String given = "De personer som har skulder hos " +
      "Kronofogden betalar i regel f??rst r??ntor och avgifter. Endast om " +
      "pengarna r??cker g??r betalningarna till sj??lva skulden.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 5));
  }

  @Test
  void body7() {
    final String given = "??? M??nga skuldsatta betalar ??r " +
      "efter ??r genom att vi m??ter ut deras l??n. Trots det ??r grundskulden " +
      "kvar. Det de betalar g??r till r??ntorna. Det tycker vi ??r ett " +
      "systemfel som g??r att m??nniskor fastnar i decennier med skulder hos " +
      "oss, s??ger bitr??dande rikskronofogde Cecilia Hegethorn Mogensen.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 10));
  }

  @Test
  void body8() {
    final String given = "I 46 av landets 290 kommuner " +
      "??kar antalet personer med skulder till Kronofogden.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 3));
  }

  @Test
  void body9() {
    final String given = "H??gst andel skuldsatta inv??nare " +
      "??terfinns i kommunen Ljusnarsberg i ??rebro l??n f??ljt av Perstorp i " +
      "Sk??ne och Eda i V??rmland. L??gst andel skuldsatta har sk??nska Lomma, " +
      "som f??ljs av Danderyd och T??by i Stockholmsomr??det";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 7));
  }

  @Test
  void newBody1() {
    final String given = "S gick till val 2018 p?? att " +
      "f??rbjuda religi??sa friskolor. Men ett f??rbud finns det i dag inte en " +
      "majoritet f??r i riksdagen. Skolministern s??ger emellertid att ett " +
      "etableringsstopp bereds just nu i regeringskansliet. Ett " +
      "etableringsstopp har dock f??tt kritik d?? det riskerar att bryta mot " +
      "religionsfriheten s??v??l som Europakonventionen.";
    final String when = paragraphInsert(given, 2);
    assertTrue(isValid(when, 11));
  }

  public static final String HEADLINE =
    "Kronofogden: De samlade skulderna st??rre ??n n??gonsin";
  public static final String LEAD =
    "Antalet svenskar som har skulder hos Kronofogden ??r det l??gsta p?? 30 " +
      "??r. D??remot ??r det samlade skuldberget st??rre ??n n??gonsin och v??xer " +
      "snabbt. Det visar ny statistik fr??n myndigheten.";

  public static final String FORMATTED_LEAD = "" +
    "Antalet svenskar som har" + NL +
    "skulder hos Kronofogden ??r det" + NL +
    "l??gsta p?? 30 ??r. D??remot ??r" + NL +
    "det samlade skuldberget st??rre" + NL +
    "??n n??gonsin och v??xer snabbt." + NL +
    "Det visar ny statistik fr??n" + NL +
    "myndigheten." + NL;

  public static final String FORMATTED_HEADLINE = "Kronofogden:" + NL +
    "De samlade" + NL + "skulderna" + NL + "st??rre ??n" + NL + "n??gonsin" + NL;

  public static final String BODY = """
      I slutet av 2021 fanns drygt 391 000 personer registrerade hos Kronofogden. Det r??r sig om en minskning med lite ??ver 11 000 personer j??mf??rt med ??ret innan. Kronofogdens analytiker Davor Vuleta s??ger i ett uttalande att en av orsakerna till att antalet blivit f??rre har att g??ra med att skulderna till staten minskar.
      ??? Det beror fr??mst p?? att radio- och tv-avgiften avskaffats, men ocks?? p?? att f??rre f??r skulder relaterade till exempelvis fordon, skatt och studier.
      Enligt Vuleta handlar det ocks?? om att "den ekonomiska ??terh??mtningen varit stark".
      ??? De olika st??dpaketen har mildrat effekterna av pandemin och fungerat som en sorts krockkudde.
      Vid ??rsskiftet var den samlade skulden hos Kronofogden 94 miljarder kronor, en ??kning med sju miljarder kronor sedan 2020 eller n??stan 20 miljoner kronor om dagen. P?? tio ??r har skuldberget vuxit med ??ver 32 miljarder kronor.
      De personer som har skulder hos Kronofogden betalar i regel f??rst r??ntor och avgifter. Endast om pengarna r??cker g??r betalningarna till sj??lva skulden."
      ??? M??nga skuldsatta betalar ??r efter ??r genom att vi m??ter ut deras l??n. Trots det ??r grundskulden kvar. Det de betalar g??r till r??ntorna. Det tycker vi ??r ett systemfel som g??r att m??nniskor fastnar i decennier med skulder hos oss, s??ger bitr??dande rikskronofogde Cecilia Hegethorn Mogensen.
      I 46 av landets 290 kommuner ??kar antalet personer med skulder till Kronofogden.
      H??gst andel skuldsatta inv??nare ??terfinns i kommunen Ljusnarsberg i ??rebro l??n f??ljt av Perstorp i Sk??ne och Eda i V??rmland. L??gst andel skuldsatta har sk??nska Lomma, som f??ljs av Danderyd och T??by i Stockholmsomr??det
      """;

  public static final String FORMATTED_BODY = """
         I slutet av 2021 fanns drygt
      391 000 personer registrerade hos
      Kronofogden. Det r??r sig om en
      minskning med lite ??ver 11 000
      personer j??mf??rt med ??ret innan.
      Kronofogdens analytiker Davor
      Vuleta s??ger i ett uttalande att
      en av orsakerna till att antalet
      blivit f??rre har att g??ra med att
      skulderna till staten minskar.
        ??? Det beror fr??mst p?? att
      radio- och tv-avgiften
      avskaffats, men ocks?? p?? att
      f??rre f??r skulder relaterade
      till exempelvis fordon, skatt och
      studier.
        Enligt Vuleta handlar det
      ocks?? om att "den ekonomiska
      ??terh??mtningen varit stark".
        ??? De olika st??dpaketen har
      mildrat effekterna av pandemin
      och fungerat som en sorts
      krockkudde.
        Vid ??rsskiftet var den samlade
      skulden hos Kronofogden 94
      miljarder kronor, en ??kning med
      sju miljarder kronor sedan 2020
      eller n??stan 20 miljoner kronor
      om dagen. P?? tio ??r har
      skuldberget vuxit med ??ver 32
      miljarder kronor.
        De personer som har skulder hos
      Kronofogden betalar i regel f??rst
      r??ntor och avgifter. Endast om
      pengarna r??cker g??r betalningarna
      till sj??lva skulden."
        ??? M??nga skuldsatta betalar ??r
      efter ??r genom att vi m??ter ut
      deras l??n. Trots det ??r
      grundskulden kvar. Det de betalar
      g??r till r??ntorna. Det tycker vi
      ??r ett systemfel som g??r att
      m??nniskor fastnar i decennier med
      skulder hos oss, s??ger bitr??dande
      rikskronofogde Cecilia Hegethorn
      Mogensen.
        I 46 av landets 290 kommuner
      ??kar antalet personer med
      skulder till Kronofogden.
        H??gst andel skuldsatta inv??nare
      ??terfinns i kommunen Ljusnarsberg
      i ??rebro l??n f??ljt av Perstorp i
      Sk??ne och Eda i V??rmland. L??gst
      andel skuldsatta har sk??nska
      Lomma, som f??ljs av Danderyd och
      T??by i Stockholmsomr??det
      """;

  @Test
  void integrationOldArticle() {
    final HeadlineComponent givenContent = new HeadlineComponent(HEADLINE, LEAD);
    final HeadlineComponent actualContent = WordWrapper.format(givenContent);
    final String actualBody = WordWrapper.formatBody(BODY);
    int expectedHeadlineNewLines = countNewLines(FORMATTED_HEADLINE);
    int expectedLeadNewLines = countNewLines(FORMATTED_LEAD);
    int expectedBodyNewLines = countNewLines(FORMATTED_BODY);
    assertTrue(isValid(actualContent.getHeadline(), expectedHeadlineNewLines, Format.HEADLINE));
    assertTrue(isValid(actualContent.getLeader(), expectedLeadNewLines, Format.LEAD));
    assertTrue(isValid(actualBody, expectedBodyNewLines, Format.PARAGRAPH));
  }

  public static final String IMAGE_TEXT = "Skolminister Lina Axelsson " +
    "Kihlblom (S) vill ha ett f??rbud mot religi??sa friskolor.";

  public static final String FORMATTED_IMAGE_TEXT = """
      Skolminister Lina Axelsson
      Kihlblom (S) vill ha ett f??rbud
      mot religi??sa friskolor.
      """;

  @Test
  void imageText() {
    final String actualImageText = WordWrapper.format(IMAGE_TEXT, Format.PARAGRAPH);
    int expectedImageTextNewLines = countNewLines(FORMATTED_IMAGE_TEXT);
    assertTrue(isValid(actualImageText, expectedImageTextNewLines, Format.PARAGRAPH));
  }

  private int countNewLines(String wrappedText) {
    Matcher m = Pattern.compile("(\r\n)|(\r)|(\n)").matcher(wrappedText);
    int newLines = 0;
    while (m.find())
      newLines ++;
    return newLines;
  }

}
