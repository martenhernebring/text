package se.epochtimes.backend.text.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.ArticleNotFoundException;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.WordWrapperTest;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Subject;
import se.epochtimes.backend.text.model.main.HeadlineComponent;
import se.epochtimes.backend.text.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static se.epochtimes.backend.text.model.wrap.WordWrapper.NL;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

  @Mock
  private ArticleRepository mockedArticleRepository;

  @InjectMocks
  private ArticleService articleServiceTest;

  private HeaderComponent header;
  private ArticleDTO dto;
  private Article article;

  @BeforeEach
  void setUp() {
    header = new HeaderComponent(Subject.EKONOMI, 2022, "Vignette", "");
    final String hl = WordWrapperTest.HEADLINE;
    final String lead = WordWrapperTest.LEAD;
    final String support = """
      I samband med januariavtalet 2019 kom Socialdemokraterna överens med Miljöpartiet, Centerpartiet och Liberalerna om att utreda skärpta regler för så kallade konfessionella friskolor och ett stopp för nya religiösa friskolor.
      - Vi har sett exempel på att aktörer inom offentlig sektor har använt statliga och kommunala medel till antidemokratisk verksamhet. Av Säkerhetspolisens arbete och rapporter från bland annat Försvarshögskolan framgår det att det exempelvis har förekommit kopplingar mellan skolverksamhet och den våldsbejakande miljön. Så här kan vi inte ha det, säger skolminister Lina Axelsson Kihlblom (S) på en pressträff den 4 februari.
      Lämplighetsprövningen av enskilda som ansöker om att bli huvudmän inom skolväsendet föreslås utökas med demokrativillkor. Regeringen vill också att utrymmet som finns för religiösa inslag i skolan förtydligas. Detta så att elever kan välja om de vill delta eller ej.
      - Det behövs skarpare och effektivare verktyg för tillstånd och tillsyn så att oseriösa och olämpliga aktörer förhindras och stoppas. Verksamheter som inte följer reglerna kan stängas genom att deras godkännande återkallas, säger Lina Axelsson Kihlblom.
      S gick till val 2018 på att förbjuda religiösa friskolor. Men ett förbud finns det i dag inte en majoritet för i riksdagen. Skolministern säger emellertid att ett etableringsstopp bereds just nu i regeringskansliet. Ett etableringsstopp har dock fått kritik då det riskerar att bryta mot religionsfriheten såväl som Europakonventionen.
      Lagändringarna vad gäller skärpta regler för religiösa inslag i skolor föreslås träda i kraft den 1 januari 2023.
      """;
    dto = new ArticleDTO(header, hl, lead, support);
    article = new Article(dto);
  }

  @Test
  void nullHeaderIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(null, dto.getHeadline(), dto.getLead(), dto.getSupport())
    ));
  }

  @Test
  void nullHeadlineIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, null, dto.getLead(), dto.getSupport())
    ));
  }

  @Test
  void nullLeadIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, dto.getHeadline(), null, dto.getSupport())
    ));
  }

  @Test
  void nullSupportIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, dto.getHeadline(), dto.getLead(), null)
    ));
  }

  @Test
  void postingTwiceIsNotLegal() {
    when(mockedArticleRepository.findByHeadline(any(HeadlineComponent.class))
    ).thenReturn(List.of(article));
    assertThrows(ConflictException.class, () ->
      articleServiceTest.add(dto));
  }

  @Test
  void tryToFindNonExistingByArticleIdIsNotLegal() {
    when(mockedArticleRepository.findByHeader(any(HeaderComponent.class))
    ).thenReturn(new ArrayList<>());
    assertThrows(ArticleNotFoundException.class, () ->
      articleServiceTest.edit(dto));
  }

  @Test
  void doubleWithSameArticleIdIsAServerError() {
    when(mockedArticleRepository.findByHeader(any(HeaderComponent.class))
    ).thenReturn(List.of(article, article));
    assertThrows(ConflictException.class, () -> articleServiceTest
      .removeArticle(header));
  }

  @Test
  void saveHeadlineFormatted() {
    String formatted = WordWrapperTest.FORMATTED_HEADLINE;
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.add(dto);
    assertThat(result.getHeadline(), is(formatted));
    assertTrue(dto.hashCode() != result.hashCode() );
  }

  void stubOneArticleSaved() {
    doReturn(List.of(article)).when(mockedArticleRepository)
      .findByHeader(any(HeaderComponent.class));
  }

  @Test
  void getLeadFormatted() {
    stubOneArticleSaved();
    String formatted = WordWrapperTest.FORMATTED_LEAD;
    ArticleDTO result = articleServiceTest.getByHeader(header);
    assertEquals(result.getLead(), formatted);
    assertNotEquals(dto, result);
  }

  @Test
  void editArticle() {
    stubOneArticleSaved();
    final String newLead =
      "Regeringen föreslår att det ska bli tydligare krav och skärpta " +
      "regler för religiösa inslag i förskolor, skolor och fritidshem. " +
      "Bland annat handlar det om en noggrannare kontroll av huvudmännen.";
    dto.setLead(newLead);
    HeadlineComponent mc = new HeadlineComponent(dto.getHeadline(), dto.getLead());
    final String formattedNewLead = "" +
      "Regeringen föreslår att det" + NL +
      "ska bli tydligare krav och" + NL +
      "skärpta regler för religiösa" + NL +
      "inslag i förskolor, skolor" + NL +
      "och fritidshem. Bland annat" + NL +
      "handlar det om en noggrannare" + NL +
      "kontroll av huvudmännen." + NL;
    mc.setLead(formattedNewLead);
    article.setHeadlineComponent(mc);
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.edit(dto);
    assertEquals(formattedNewLead, result.getLead());
    assertNotEquals(dto.getLead(), result.getLead());
  }

  @Test
  void deleteArticle() {
    stubOneArticleSaved();
    articleServiceTest.removeArticle(header);
    verify(mockedArticleRepository, times(1)).delete(article);
  }

  @Test
  void getAllArticlesWithSupportFormatted() {
    doReturn(List.of(article)).when(mockedArticleRepository).findAll();
    List<ArticleDTO> result = articleServiceTest.getAllUnsorted();
    String sr = result.get(0).getSupport();
    /*
    assertEquals("""
         I samband med januariavtalet
      2019 kom Socialdemokraterna
      överens med Miljöpartiet, Cen-
      terpartiet och Liberalerna om
      att utreda skärpta regler för så
      kallade konfessionella friskolor
      och ett stopp för nya religiösa
      friskolor.
        - Vi har sett exempel på att
      aktörer inom offentlig sektor
      har använt statliga och kom-
      munala medel till antidemokra-
      tisk verksamhet. Av Säkerhets-
      polisens arbete och rapporter
      från bland annat Försvarshög-
      skolan framgår det att det exem-
      pelvis har förekommit kopp-
      lingar mellan skolverksamhet
      och den våldsbejakande miljön.
      Så här kan vi inte ha det, säger
      skolminister Lina Axelsson Kihl-
      blom (S) på en pressträff den 4
      februari.
        Lämplighetsprövningen av
      enskilda som ansöker om att bli
      huvudmän inom skolväsendet
      föreslås utökas med demokrati-
      villkor. Regeringen vill också att
      utrymmet som finns för religiösa
      inslag i skolan förtydligas. Detta
      så att elever kan välja om de vill
      delta eller ej.
        - Det behövs skarpare och
      effektivare verktyg för tillstånd
      och tillsyn så att oseriösa och
      olämpliga aktörer förhindras
      och stoppas. Verksamheter som
      inte följer reglerna kan stängas
      genom att deras godkännande
      återkallas, säger Lina Axelsson
      Kihlblom.
        S gick till val 2018 på att för-
      bjuda religiösa friskolor. Men
      ett förbud finns det i dag inte en
      majoritet för i riksdagen. Skolmi-
      nistern säger emellertid att ett
      etableringsstopp bereds just nu
      i regeringskansliet. Ett etable-
      ringsstopp har dock fått kritik då
      det riskerar att bryta mot religi-
      onsfriheten såväl som Europa-
      konventionen.
        Lagändringarna vad gäller
      skärpta regler för religiösa inslag
      i skolor föreslås träda i kraft den
      1 januari 2023.
      """, sr);
     */
    assertEquals("""
         I samband med januariavtalet
      2019 kom Socialdemokraterna
      överens med Miljöpartiet,
      Centerpartiet och Liberalerna om
      att utreda skärpta regler för så
      kallade konfessionella friskolor
      och ett stopp för nya religiösa
      friskolor.
        - Vi har sett exempel på att
      aktörer inom offentlig sektor
      har använt statliga och kommunala
      medel till antidemokratisk
      verksamhet. Av Säkerhetspolisens
      arbete och rapporter från bland
      annat Försvarshögskolan framgår
      det att det exempelvis har
      förekommit kopplingar mellan
      skolverksamhet och den
      våldsbejakande miljön. Så här kan
      vi inte ha det, säger
      skolminister Lina Axelsson
      Kihlblom (S) på en pressträff den
      4 februari.
        Lämplighetsprövningen av
      enskilda som ansöker om att bli
      huvudmän inom skolväsendet
      föreslås utökas med
      demokrativillkor. Regeringen vill
      också att utrymmet som finns för
      religiösa inslag i skolan
      förtydligas. Detta så att elever
      kan välja om de vill delta eller
      ej.
        - Det behövs skarpare och
      effektivare verktyg för tillstånd
      och tillsyn så att oseriösa och
      olämpliga aktörer förhindras och
      stoppas. Verksamheter som inte
      följer reglerna kan stängas genom
      att deras godkännande återkallas,
      säger Lina Axelsson Kihlblom.
        S gick till val 2018 på att
      förbjuda religiösa friskolor. Men
      ett förbud finns det i dag inte
      en majoritet för i riksdagen.
      Skolministern säger emellertid
      att ett etableringsstopp bereds
      just nu i regeringskansliet. Ett
      etableringsstopp har dock fått
      kritik då det riskerar att bryta
      mot religionsfriheten såväl som
      Europakonventionen.
        Lagändringarna vad gäller
      skärpta regler för religiösa
      inslag i skolor föreslås träda
      i kraft den 1 januari 2023.
      """, sr);
    assertEquals(article.getBody(), sr);
    assertNotEquals(dto, result.get(0));
  }

}