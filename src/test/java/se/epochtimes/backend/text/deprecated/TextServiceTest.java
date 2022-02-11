package se.epochtimes.backend.text.deprecated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.deprecated.text.StringListSingleton;
import se.epochtimes.backend.text.model.header.Subject;
import se.epochtimes.backend.text.deprecated.text.Text;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextServiceTest {

  @Mock
  private TextRepository mockedTextRepository;

  @InjectMocks
  private TextService textServiceTest;

  private Text text;

  @BeforeEach
  void setUp() {
    text = new Text(Subject.EKONOMI, StringListSingleton.getInstance());
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
