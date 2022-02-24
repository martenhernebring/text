package se.epochtimes.backend.text.dto;

import java.io.Serializable;

public record EditDTO(String headline, String lead, String support) implements Serializable {
}
