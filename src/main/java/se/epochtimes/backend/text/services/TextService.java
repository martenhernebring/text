package se.epochtimes.backend.text.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.repository.TextRepository;

import java.util.List;

@Service
public class TextService {

  private final TextRepository textRepository;

  public TextService(TextRepository textRepository) {
    this.textRepository = textRepository;
  }

  public List<String> getPreviousSentences() {
    return textRepository.findAll();
  }
}
