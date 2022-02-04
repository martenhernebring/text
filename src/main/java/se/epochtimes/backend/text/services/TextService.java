package se.epochtimes.backend.text.services;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.dto.TextDTO;
import se.epochtimes.backend.text.model.Text;
import se.epochtimes.backend.text.repository.TextRepository;

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

  public void add(TextDTO text) {
    textRepository.save(new Text(text));
  }

  public TextDTO get(long id) {
    return new TextDTO(textRepository.getById(id));
  }
}