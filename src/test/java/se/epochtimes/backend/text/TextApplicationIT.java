package se.epochtimes.backend.text;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
class TextApplicationIT {

  @Test
  void main() {
    TextApplication.main(new String[0]);
  }
}