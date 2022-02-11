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
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;
import se.epochtimes.backend.text.model.Article;
import se.epochtimes.backend.text.model.image.Image;
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
  private MainComponent main;
  private ImageComponent image;
  private ArticleDTO articleDTO;
  private Article article;

  @BeforeEach
  void setUp() {
    header = new HeaderComponent(Subject.EKONOMI, 2022, "Vignette", "");
    main = new MainComponent("headline", "lead");
    image = new ImageComponent(new Image(), "Bildtext", "Bildkredit");
    articleDTO = new ArticleDTO(header, main, image);
    article = new Article(articleDTO);
  }

  @Test
  void nullHeaderIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(null, main, image)));
  }

  @Test
  void nullMainIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, null, image)));
  }

  @Test
  void nullImageIsNotLegal() {
    assertThrows(NullPointerException.class, () -> articleServiceTest.add(
      new ArticleDTO(header, main, null)));
  }

  @Test
  void postingTwiceIsNotLegal() {
    when(mockedArticleRepository.findByMain(any(MainComponent.class))
    ).thenReturn(List.of(article));
    assertThrows(ConflictException.class, () ->
      articleServiceTest.add(articleDTO));
  }

  @Test
  void tryToFindNonExistingByArticleIdIsNotLegal() {
    when(mockedArticleRepository.findByHeader(any(HeaderComponent.class))
    ).thenReturn(new ArrayList<>());
    assertThrows(ArticleNotFoundException.class, () ->
      articleServiceTest.edit(articleDTO));
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
    ArticleDTO result = articleServiceTest.add(articleDTO);
    assertEquals(articleDTO, result);
  }

  void stubOneArticleSaved() {
    doReturn(List.of(article)).when(mockedArticleRepository)
      .findByHeader(any(HeaderComponent.class));
  }

  @Test
  void getArticle() {
    stubOneArticleSaved();
    ArticleDTO result = articleServiceTest.getByHeader(header);
    assertEquals(articleDTO, result);
  }

  @Test
  void editArticle() {
    stubOneArticleSaved();
    articleDTO.main().setLead("New lead");
    article.setMainComponent(articleDTO.main());
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.edit(articleDTO);
    assertEquals(articleDTO.main().getLead(), result.main().getLead());
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
    assertEquals(articleDTO, result.get(0));
  }

}
