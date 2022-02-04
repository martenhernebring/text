package se.epochtimes.backend.text.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.dto.TextDTO;
import se.epochtimes.backend.text.model.ArticleElementSingelton;
import se.epochtimes.backend.text.model.Subject;
import se.epochtimes.backend.text.model.Text;
import se.epochtimes.backend.text.repository.TextRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockServiceTest {

  @Mock
  private TextRepository mockedTextRepository;

  @InjectMocks
  private TextService textServiceTest;

  private Text text;

  @BeforeEach
  void setUp() {
    text = new Text(Subject.EKONOMI, ArticleElementSingelton.getInstance());
  }

  @Test
  void getAllPreviousThatWasOne() {
    when(textServiceTest.getAllPreviousTexts()).thenReturn(List.of(new TextDTO(text)));
    assertEquals(1, textServiceTest.getAllPreviousTexts().size());
  }

  @Test
  void verifyTextAdded() {
    long id = 1L;
    text.setId(id);
    TextDTO dto = new TextDTO(text);
    textServiceTest.add(dto);
    verify(mockedTextRepository, times(1)).save(any(Text.class));
    when(mockedTextRepository.getById(id)).thenReturn(text);
    TextDTO mockedStored = textServiceTest.get(id);
    assertEquals(dto, mockedStored);
  }
}
