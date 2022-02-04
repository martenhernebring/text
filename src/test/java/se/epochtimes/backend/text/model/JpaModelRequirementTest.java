package se.epochtimes.backend.text.model;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public abstract class JpaModelRequirementTest {

  protected static void assertModelClassHasTwoConstructors(final Class<?> clazz) {
    assertEquals("There must be two constructors", 2, clazz.getDeclaredConstructors().length);
  }
}
