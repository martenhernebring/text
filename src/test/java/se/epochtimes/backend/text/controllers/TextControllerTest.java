package se.epochtimes.backend.text.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.epochtimes.backend.text.dto.TextDTO;
import se.epochtimes.backend.text.model.ArticleElementSingelton;
import se.epochtimes.backend.text.model.Subject;

@WebMvcTest
@AutoConfigureMockMvc
public class TextControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String BASE_URL = "/v1/texts";

  @Test
  void postArticle() throws Exception {
    TextDTO textDTO = new TextDTO(Subject.EKONOMI, ArticleElementSingelton.getInstance());
    this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
      .content(objectMapper.writeValueAsString(textDTO)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
      .andReturn();
  }

}
