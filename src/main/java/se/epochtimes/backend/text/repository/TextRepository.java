package se.epochtimes.backend.text.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.epochtimes.backend.text.model.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
}
