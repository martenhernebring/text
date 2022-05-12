package se.epochtimes.backend.text.controller;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.epochtimes.backend.text.dto.ImageDTO;
import se.epochtimes.backend.text.image.ImageController;
import se.epochtimes.backend.text.model.header.Category;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.service.ImageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ComponentScan(basePackages = "se.epochtimes.backend.text.image")
@WebMvcTest(ImageController.class)
@AutoConfigureMockMvc
public class ImageControllerTest {

  @MockBean
  private ImageService mockedService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void postImageTexts() throws Exception {
    final String BASE_URL = "/v1/articles/images";
    HeaderComponent hc = new HeaderComponent(Category.INRIKES, 2022, "EKONOMI", "");
    ImageDTO inputDto = new ImageDTO(hc, "Skolminister Lina Axelsson Kihlblom", "image credit");
    ImageDTO dto = new ImageDTO(hc, """
      Skolminister Lina Axelsson
      Kihlblom""", inputDto.getImageCredit());
    when(mockedService.add(any(ImageDTO.class))).thenReturn(dto);

    int status = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
        .content(objectMapper.writeValueAsString(inputDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
      .andReturn()
      .getResponse()
      .getStatus();
    assertEquals(200, status);
  }
}
