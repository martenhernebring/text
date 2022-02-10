package se.epochtimes.backend.text.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.epochtimes.backend.text.model.text.Text;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TextRepositoryTest {

  @Autowired
  private TextRepository textRepository;

  @Test
  void idGenerated() {
    textRepository.save(new Text());
    List<Text> texts = textRepository.findAll();
    Text first = texts.get(0);
    assertTrue(first.getId() > - 1);
  }
}
