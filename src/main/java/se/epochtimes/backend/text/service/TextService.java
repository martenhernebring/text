package se.epochtimes.backend.text.service;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.deprecated.TextDTO;
import se.epochtimes.backend.text.deprecated.text.Text;
import se.epochtimes.backend.text.deprecated.TextRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextService {

  private final TextRepository textRepository;

  public TextService(TextRepository textRepository) {
    this.textRepository = textRepository;
  }

  public List<TextDTO> getAllPreviousTexts() {
    return textRepository.findAll().stream().map(TextDTO::new).collect(Collectors.toList());
  }

  public TextDTO process(TextDTO draft) {
    Text processed = textRepository.save(new Text(draft));
    return new TextDTO(processed);
  }

  public TextDTO get(long id) {
    return new TextDTO(textRepository.getById(id));
  }
}
