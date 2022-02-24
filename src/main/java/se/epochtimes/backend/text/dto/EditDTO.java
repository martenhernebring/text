package se.epochtimes.backend.text.dto;

import java.io.Serializable;

public record EditDTO(String headline, String leader, String support)
  implements Serializable {}
