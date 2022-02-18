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
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Subject;
import se.epochtimes.backend.text.model.main.MainComponent;
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
    final String hl = "Kronofogden: De samlade skulderna större än någonsin";
    final String lead = "Antalet svenskar som har skulder hos Kronofogden " +
      "är det lägsta på 30 år. Däremot är det samlade skuldberget större än " +
      "någonsin och växer snabbt. Det visar ny statistik från myndigheten.";
    dto = new ArticleDTO(header, hl, lead);
    article = new Article(dto);
  }

  @Test
  void nullHeaderIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(null, dto.getHeadline(), dto.getLead())));
  }

  @Test
  void nullMainIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, null, dto.getLead())));
  }

  @Test
  void nullImageIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, dto.getHeadline(), null)));
  }

  @Test
  void postingTwiceIsNotLegal() {
    when(mockedArticleRepository.findByMain(any(MainComponent.class))
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
    String formatted = "Kronofogden:" + NL + "De samlade" + NL +
      "skulderna" + NL + "större än" + NL + "någonsin" + NL;
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
    String formatted = "" +
      "Antalet svenskar som har" + NL +
      "skulder hos Kronofogden är det" + NL +
      "lägsta på 30 år. Däremot är" + NL +
      "det samlade skuldberget större" + NL +
      "än någonsin och växer snabbt." + NL +
      "Det visar ny statistik från" + NL +
      "myndigheten." + NL;
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
    MainComponent mainComp = new MainComponent(dto.getHeadline(), dto.getLead());
    final String formattedNewLead = "" +
      "Regeringen föreslår att det" + NL +
      "ska bli tydligare krav och" + NL +
      "skärpta regler för religiösa" + NL +
      "inslag i förskolor, skolor" + NL +
      "och fritidshem. Bland annat" + NL +
      "handlar det om en noggrannare" + NL +
      "kontroll av huvudmännen." + NL;
    mainComp.setLead(formattedNewLead);
    article.setMainComponent(mainComp);
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
  void getAllArticles() {
    doReturn(List.of(article)).when(mockedArticleRepository).findAll();
    List<ArticleDTO> result = articleServiceTest.getAllUnsorted();
    assertEquals(new ArticleDTO(article), result.get(0));
    assertNotEquals(dto, result.get(0));
  }

}
