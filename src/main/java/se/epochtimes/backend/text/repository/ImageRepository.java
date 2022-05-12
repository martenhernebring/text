package se.epochtimes.backend.text.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.epochtimes.backend.text.model.Image;

import java.util.List;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Image, Long> {
  @Query(value = "SELECT * FROM imagetext it WHERE it.image_text = ?1", nativeQuery = true)
  List<Image> findByImageText(@Param(value = "imageText") String imageText);
}
