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
import se.epochtimes.backend.text.model.Subject;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;
import se.epochtimes.backend.text.model.wrap.Article;
import se.epochtimes.backend.text.model.wrap.Image;
import se.epochtimes.backend.text.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    header = new HeaderComponent(Subject.EKONOMI, 2022);
    main = new MainComponent("headline");
    image = new ImageComponent(new Image(), "Bildtext", "Bildkredit");
    articleDTO = new ArticleDTO(header, main, image);
    articleId = "inreko1234";
    article = new Article(articleDTO);
    article.setArticleId(articleId);
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
  void tryToFindNonExistingByNameAndYearIsNotLegal() {
    when(mockedArticleRepository
      .findByHeadlineAndYear(any(String.class), any(int.class))
    ).thenReturn(new ArrayList<>());
    assertThrows(ArticleNotFoundException.class, () -> articleServiceTest.
      getByHeadlineAndYear(main.getHeadline(), header.getYear()));
  }

  @Test
  void tryToFindNonExistingByArticleIdIsNotLegal() {
    when(mockedArticleRepository.findByArticleId(any(String.class))
    ).thenReturn(new ArrayList<>());
    assertThrows(ArticleNotFoundException.class, () -> articleServiceTest.
      getByArticleId(articleId));
  }

  @Test
  void doubleWithSameHeadlineAndYearIsAServerError() {
    when(mockedArticleRepository
      .findByHeadlineAndYear(any(String.class), any(int.class))
    ).thenReturn(List.of(article, new Article(articleDTO)));
    assertThrows(ConflictException.class, () -> articleServiceTest
      .getByHeadlineAndYear(main.getHeadline(), header.getYear()));
  }

  @Test
  void doubleWithSameArticleIdIsAServerError() {
    when(mockedArticleRepository.findByArticleId(any(String.class))
    ).thenReturn(List.of(article, new Article(articleDTO)));
    assertThrows(ConflictException.class, () -> articleServiceTest
      .getByArticleId(articleId));
  }

  @Test
  void saveArticle() {
    Article article = new Article(articleDTO);
    when(mockedArticleRepository.save(any(Article.class))).thenReturn(article);
    ArticleDTO result = articleServiceTest.add(articleDTO);
    assertEquals(articleDTO, result);
  }

  @Test
  void findArticleByNameAndYear() {
    when(mockedArticleRepository
      .findByHeadlineAndYear(any(String.class), any(int.class))
    ).thenReturn(List.of(new Article(articleDTO)));
    ArticleDTO existing = articleServiceTest
      .getByHeadlineAndYear(main.getHeadline(), header.getYear());
    assertNotNull(existing);
  }

  @Test
  void getByArticleId() {
    when(mockedArticleRepository.findByArticleId(articleId.toLowerCase())
    ).thenReturn(List.of(article));
    ArticleDTO result = articleServiceTest.getByArticleId(articleId);
    assertEquals(articleDTO, result);
  }

}
