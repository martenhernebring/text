package se.epochtimes.backend.text.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.epochtimes.backend.text.repository.TextRepository;
import se.epochtimes.backend.text.model.TextTest;
import se.epochtimes.backend.text.repository.TextSingleton;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockServiceTest {

  private static final String FIRST = "Många företag planerar att höja priserna";
  private static final String SECOND =
    "Två av tre företag i industrin och drygt hälften av tjänsteföretagen räkna";
  private static final String THIRD =
    "Det visar en undersökning som Swedbank har gjort bland inköpschefer och i tjänstesektorn.";

  @Mock
  private TextRepository mockedTextRepository;

  @InjectMocks
  private TextService textServiceTest;

  private final List<String> mockedText = new ArrayList<>();

  @BeforeEach
  void setUp() {
    mockedText.add(FIRST);
  }

  @Test
  void textServiceIsNotNull() {
    assertNotNull(textServiceTest);
  }

  @Test
  void addFirstSentence() {
    assertEquals(1, mockedText.size());
  }

  @Test
  void firstSentenceIsInSwedish() {
    assertEquals(FIRST, mockedText.get(0));
  }

  @Test
  void addSecondSentence() {
    mockedText.add(SECOND);
    assertEquals(2, mockedText.size());
  }

  @Test
  void addThirdSentence() {
    when(textServiceTest.getPreviousSentences()).thenReturn(List.of(FIRST, SECOND, THIRD));
    mockedText.add("Det visar en undersökning som Swedbank har gjort bland inköpschefer och i tjänstesektorn.");
    assertEquals(3, textServiceTest.getPreviousSentences().size());
  }

  @Test
  void addAllSentencesInText() {
    List<String> singleton = TextSingleton.getInstance();
    when(textServiceTest.getPreviousSentences()).thenReturn(singleton);
    assertEquals(singleton.size(), textServiceTest.getPreviousSentences().size());
  }
}
