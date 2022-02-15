package se.epochtimes.backend.text.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Subject;
import se.epochtimes.backend.text.service.ArticleService;

import java.util.List;

@RestController("articleController")
@RequestMapping(value = "/v1/articles")
public class ArticleController {

  final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  /**
   * @param newArticle Newspaper article draft
   * @return Saved newspaper article
   */
  @Operation(summary = "Register new article.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200",
      description = "Successfully registered the article",
    content = @Content(mediaType = "application/json",
    schema = @Schema(implementation = ArticleDTO.class))),
    @ApiResponse(responseCode = "400", description =
        "Unquoted character ->\\ and \", New line -> \\n, Subject -> EKONOMI",
      content = @Content),
    @ApiResponse(responseCode = "409",
      description = "Headline and lead has already been posted.",
      content = @Content)})
  @RequestMapping(value = "", method = RequestMethod.POST)
  public ArticleDTO post(@RequestBody ArticleDTO newArticle) {
    return articleService.add(newArticle);
  }

  /**
   * @return List of all article.
   */
  @Operation(summary = "Get all articles.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved articles",
      content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = ArticleDTO.class)))),
  })
  @RequestMapping(value = "", method = RequestMethod.GET)
  public List<ArticleDTO> getAllArticles() {
    return articleService.getAllUnsorted();
  }

  /**
   * @param header Article subject, year, vignette and id
   */
  @Operation(summary = "Delete an article.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully deleted the article", content = @Content)
  })
  @RequestMapping(value = "", method = RequestMethod.DELETE)
  public void deleteArticle(@RequestBody HeaderComponent header) {
    articleService.removeArticle(header);
  }

  /**
   * @return Matching article
   */
  @Operation(summary = "Get article based on header.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the article.",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ArticleDTO.class))),
  })
  @RequestMapping(value = "/inrikes/2022/ekonomi/{articleId}", method = RequestMethod.GET)
  public ArticleDTO getArticleByHeader(@PathVariable String articleId) {
    return articleService.getByHeader(
      new HeaderComponent(Subject.EKONOMI, 2022, "INRIKES", articleId)
    );
  }

}
