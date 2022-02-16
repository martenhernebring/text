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
import se.epochtimes.backend.text.model.header.Subject;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.main.MainComponent;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    dto = new ArticleDTO(header, "headline", "lead");
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
  void saveArticle() {
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.add(dto);
    assertEquals(dto, result);
  }

  void stubOneArticleSaved() {
    doReturn(List.of(article)).when(mockedArticleRepository)
      .findByHeader(any(HeaderComponent.class));
  }

  @Test
  void getArticle() {
    stubOneArticleSaved();
    ArticleDTO result = articleServiceTest.getByHeader(header);
    assertEquals(dto, result);
  }

  @Test
  void editArticle() {
    stubOneArticleSaved();
    MainComponent mainComp = new MainComponent(dto.getHeadline(), dto.getLead());
    final String newLead = "New lead";
    dto.setLead(newLead);
    mainComp.setLead(newLead);
    article.setMainComponent(mainComp);
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.edit(dto);
    assertEquals(dto.getLead(), result.getLead());
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
    assertEquals(dto, result.get(0));
  }

}
