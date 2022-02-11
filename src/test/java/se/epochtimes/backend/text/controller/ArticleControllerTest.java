package se.epochtimes.backend.text.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.exception.ArticleNotFoundException;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.header.Subject;
import se.epochtimes.backend.text.model.image.Image;
import se.epochtimes.backend.text.model.image.ImageComponent;
import se.epochtimes.backend.text.model.main.MainComponent;
import se.epochtimes.backend.text.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "se.epochtimes.backend.text.controller")
@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc
public class ArticleControllerTest {
  @MockBean
  private ArticleService mockedService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String BASE_URL = "/v1/articles";

  private HeaderComponent hc;
  private ArticleDTO dto;

  @BeforeEach
  void setUp() {
    hc = new HeaderComponent(Subject.EKONOMI, 2022, "Vignette", "");
    dto = new ArticleDTO(hc, new MainComponent("headline", "lead"),
      new ImageComponent(new Image(), "Bildtext", "Bildkredit")
    );
  }

  @Test
  void postArticle() throws Exception {
    when(mockedService.add(any(ArticleDTO.class))).thenReturn(dto);

    String aRJ = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
      .content(objectMapper.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
      .andReturn()
      .getResponse()
      .getContentAsString();

    assertTrue(aRJ.substring(aRJ.indexOf("\"articleId\":\"") + 13,
      aRJ.indexOf("\"},\"main\"")).matches("[0-9]{4}"));
  }

  @Test
  void postingTwiceShouldThrowClientError() throws Exception {
    when(mockedService.add(any(ArticleDTO.class)))
      .thenThrow(new ConflictException("The article has already been posted."));

    this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
        .content(objectMapper.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CONFLICT.value()));
  }

  @Test
  void getAllUnsorted() throws Exception {
    final List<ArticleDTO> dtos = new ArrayList<>();
    dtos.add(dto);
    when(mockedService.getAllUnsorted()).thenReturn(dtos);
    MvcResult mvcResult = mockMvc
      .perform(get(BASE_URL))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
      .andReturn();

    String actualResponseJson = mvcResult.getResponse().getContentAsString();
    String expectedResultJson = objectMapper.writeValueAsString(dtos);
    assertEquals(expectedResultJson, actualResponseJson);
  }

  @Test
  void deleteOne() throws Exception {
    mockMvc.perform(delete(BASE_URL)
        .content(objectMapper.writeValueAsString(hc))
        .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isOk()).andReturn();
  }

  @Test
  void deletingTwiceShouldThrowClientError() throws Exception {
    doThrow(new ArticleNotFoundException("No article with the arguments was found."))
      .when(mockedService).removeArticle(any(HeaderComponent.class));

    mockMvc.perform(delete(BASE_URL)
      .content(objectMapper.writeValueAsString(hc))
      .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNotFound()).andReturn();
  }
}
