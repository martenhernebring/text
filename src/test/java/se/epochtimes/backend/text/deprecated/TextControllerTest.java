package se.epochtimes.backend.text.deprecated;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import se.epochtimes.backend.text.model.header.Subject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ComponentScan(basePackages = "se.epochtimes.backend.text.deprecated")
@WebMvcTest(TextController.class)
@AutoConfigureMockMvc
public class TextControllerTest {
  @MockBean
  private TextService mockedService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String BASE_URL = "/v1/texts";

  private final TextDTO dto = new TextDTO(Subject.EKONOMI, List.of("sentence1", "sentence2"));

  @Test
  void getAllPrevious() throws Exception {
    var dtos = List.of(dto);
    when(mockedService.getAllPreviousTexts()).thenReturn(dtos);
    MvcResult mvcResult = this.mockMvc
      .perform(MockMvcRequestBuilders.get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
      .andReturn();

    String actualResponseJson = mvcResult.getResponse().getContentAsString();
    String expectedResultJson = objectMapper.writeValueAsString(dtos);
    assertEquals(expectedResultJson, actualResponseJson);
  }

  @Test
  void postArticle() throws Exception {
    when(mockedService.process(any(TextDTO.class))).thenReturn(dto);

    String expectedResultJson = objectMapper.writeValueAsString(dto);
    String actualResponseJson = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
      .content(expectedResultJson).contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
      .andReturn()
      .getResponse()
      .getContentAsString();

    assertEquals(expectedResultJson, actualResponseJson);
  }

}
