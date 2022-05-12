package se.epochtimes.backend.text.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.dto.ImageDTO;
import se.epochtimes.backend.text.service.ImageService;

@RestController("imageController")
@RequestMapping(value = "/v1/articles/images")
public class ImageController {

  final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  /**
   * @param imageText Newspaper article image text
   * @return Saved image text from newspaper article
   */
  @Operation(summary = "Register new image text.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200",
      description = "Successfully registered the image texts",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ImageDTO.class)))})
  @PostMapping(value = "")
  public ImageDTO post(@RequestBody ImageDTO imageText) {
    return imageService.add(imageText);
  }

}
