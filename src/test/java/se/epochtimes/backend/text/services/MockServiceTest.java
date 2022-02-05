package se.epochtimes.backend.text.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.dto.TextDTO;
import se.epochtimes.backend.text.model.ParagraphSingleton;
import se.epochtimes.backend.text.model.Subject;
import se.epochtimes.backend.text.model.Text;
import se.epochtimes.backend.text.repository.TextRepository;

import java.util.ArrayList;

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
    text = new Text(Subject.EKONOMI, ParagraphSingleton.getInstance());
  }

  @Test
  void getAllPreviousThatWasZero() {
    when(textServiceTest.getAllPreviousTexts()).thenReturn(new ArrayList<>());
    assertEquals(0, textServiceTest.getAllPreviousTexts().size());
  }

  @Test
  void verifyTextAdded() {
    long id = 1L;
    text.setId(id);
    TextDTO dto = new TextDTO(text);
    when(mockedTextRepository.save(any(Text.class))).thenReturn(text);
    textServiceTest.process(dto);
    when(mockedTextRepository.getById(id)).thenReturn(text);
    TextDTO mockedStored = textServiceTest.get(id);
    assertEquals(dto, mockedStored);
    assertEquals(dto.hashCode(), mockedStored.hashCode());
  }
}
