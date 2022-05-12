package se.epochtimes.backend.text.service;

import org.springframework.stereotype.Service;
import se.epochtimes.backend.text.dto.ImageDTO;
import se.epochtimes.backend.text.exception.ConflictException;
import se.epochtimes.backend.text.model.Image;
import se.epochtimes.backend.text.repository.ImageRepository;

import java.util.List;

@Service("imageService")
public class ImageService {

  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public ImageDTO add(ImageDTO dto) {
    List<Image> existing = imageRepository.findByImageText(dto.getImageText());
    if(existing.size() > 0) {
      throw new ConflictException(
        "The article has already been posted. Please get by following header: "
          + existing.get(0).getHeader()
      );
    }
    Image image = new Image();
    image.setHeader(dto.getHeader());
    image.setImageText(dto.getImageText());
    image.setImageCredit(dto.getImageCredit());
    imageRepository.save(image);
    return new ImageDTO(image);
  }
}
