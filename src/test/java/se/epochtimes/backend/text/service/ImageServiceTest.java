package se.epochtimes.backend.text.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.dto.ArticleDTO;
import se.epochtimes.backend.text.dto.ImageDTO;
import se.epochtimes.backend.text.model.Image;
import se.epochtimes.backend.text.model.header.Category;
import se.epochtimes.backend.text.model.header.HeaderComponent;
import se.epochtimes.backend.text.model.wrap.WordWrapperTest;
import se.epochtimes.backend.text.repository.ArticleRepository;
import se.epochtimes.backend.text.repository.ImageRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

  @Mock
  private ImageRepository mockedImageRepository;

  @InjectMocks
  private ImageService imageServiceTest;

  @Test
  void saveImageTextFormatted() {
    String formatted = WordWrapperTest.FORMATTED_IMAGE_TEXT;
    Image image = new Image();
    HeaderComponent header = new HeaderComponent(Category.INRIKES, 2022, "POLITIK", "9839");
    image.setHeader(header);
    image.setImageText(WordWrapperTest.IMAGE_TEXT);
    final String imageCredit = "Ninni Andersson/Regeringskansliet";
    image.setImageCredit(imageCredit);
    when(mockedImageRepository.save(any(Image.class))).thenReturn(image);
    ImageDTO dto = new ImageDTO(header, WordWrapperTest.IMAGE_TEXT, imageCredit);
    ImageDTO result = imageServiceTest.add(dto);
    assertThat(result.getImageText(), is(formatted));
    assertTrue(dto.hashCode() != result.hashCode());
  }
}
