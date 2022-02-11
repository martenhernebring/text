package se.epochtimes.backend.text.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.AlreadySubmittedException;
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
  private String articleId;
  private Article article;

  @BeforeEach
  void setUp() {
    header = new HeaderComponent(Subject.EKONOMI, 2022, "Vignette");
    main = new MainComponent("headline");
    image = new ImageComponent(new Image(), "Bildtext", "Bildkredit");
    articleDTO = new ArticleDTO(header, main, image);
    articleId = "inreko1234";
    article = new Article(articleDTO, articleId);
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
    when(mockedArticleRepository.findByHeadlineAndYear(any(String.class),
      any(int.class))).thenReturn(List.of(article));
    assertThrows(AlreadySubmittedException.class, () -> articleServiceTest.add(articleDTO));
  }

  @Test
  void tryToFindNonExistingByArticleIdIsNotLegal() {
    when(mockedArticleRepository.findByArticleId(any(String.class))
    ).thenReturn(new ArrayList<>());
    assertThrows(ArticleNotFoundException.class, () -> articleServiceTest.
      edit(articleDTO, articleId));
  }

  @Test
  void doubleWithSameArticleIdIsAServerError() {
    when(mockedArticleRepository.findByArticleId(any(String.class))
    ).thenReturn(List.of(article, article));
    assertThrows(ConflictException.class, () -> articleServiceTest
      .removeArticle(articleId));
  }

  @Test
  void saveArticle() {
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.add(articleDTO);
    assertEquals(articleDTO, result);
  }

  void mockOneArticleSaved() {
    when(mockedArticleRepository.findByArticleId(any(String.class))
    ).thenReturn(List.of(article));
  }

  @Test
  void getArticle() {
    mockOneArticleSaved();
    ArticleDTO result = articleServiceTest.getByArticleId(articleId);
    assertEquals(articleDTO, result);
  }

  @Test
  void editArticle() {
    mockOneArticleSaved();
    articleDTO.main().setLead("New lead");
    article.setMain(articleDTO.main());
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.edit(articleDTO, articleId);
    assertEquals(articleDTO.main().getLead(), result.main().getLead());
  }

  @Test
  void deleteArticle() {
    mockOneArticleSaved();
    articleServiceTest.removeArticle(articleId);
    verify(mockedArticleRepository, times(1)).delete(article);
  }

}
