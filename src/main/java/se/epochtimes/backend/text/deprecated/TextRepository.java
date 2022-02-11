package se.epochtimes.backend.text.deprecated;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.epochtimes.backend.text.deprecated.text.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
}
