package se.epochtimes.backend.text.model;

import org.junit.jupiter.api.Test;
import se.epochtimes.backend.text.model.header.Category;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryElementTest {
  @Test
  void addCategory() {
    Category ekonomi = Category.INRIKES;
    assertNotNull(ekonomi);
  }

  @Test
  void illegalShouldThrow() {
    assertThrows(IllegalArgumentException.class, () -> Category.valueOf(-1));
  }
}
