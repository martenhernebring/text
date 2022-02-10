package se.epochtimes.backend.text.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.epochtimes.backend.text.dto.TextDTO;
import se.epochtimes.backend.text.service.TextService;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/texts")
public class TextController {

  final TextService textService;

  public TextController(TextService textService) {
    this.textService = textService;
  }

  /**
   * @return List of all texts.
   */
  @Operation(summary = "Get all texts.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved texts",
      content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = TextDTO.class))))})
  @RequestMapping(value = "", method = RequestMethod.GET)
  public List<TextDTO> getAllEmployees() {
    return textService.getAllPreviousTexts();
  }

  /**
   * @return Newspaper formatted text
   * @param unprocessedText Newspaper paragraph draft
   */
  @Operation(summary = "Register new text.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200",
      description = "Successfully registered the text",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = TextDTO.class))),
    @ApiResponse(responseCode = "400",
      description = "Illegal unquoted character has to be escaped using backslash to be " +
        "included in string value. New line has to be replaced by \\n",
      content = @Content)
  })
  @RequestMapping(value = "", method = RequestMethod.PUT)
  public TextDTO processText(@RequestBody TextDTO unprocessedText) {
    return textService.process(unprocessedText);
  }
}
