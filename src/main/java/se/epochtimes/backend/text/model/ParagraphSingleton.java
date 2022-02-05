package se.epochtimes.backend.text.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParagraphSingleton {

  private static final String FIRST =
    "  Många företag planerar att höja priserna. Två av tre företag i industrin och drygt " +
      "hälften av tjänsteföretagen räknar med att ta ut högre priser av sina kunder. Det visar " +
      "en undersökning som Swedbank har gjort bland inköpschefer och i tjänstesektorn.";
  private static final String SECOND =
    "  - Det tyder på att företagen kompenserar sig för de stigande kostnaderna och det innebär " +
      "att pristrycket i producentledet kommer att vara fortsatt utmanande under inledningen av " +
      "2022, säger Jörgen Kennemar, analytiker på Swedbank, i ett pressmeddelande.";
  private static final String THIRD = "  Enligt honom är det längre leveranstider, brist på " +
    "insats varor och stigande energipriser som driver upp produktionskostnaderna i näringslivet.";
  private static final String FOURTH = "  Nio av tio företag i tillverkningsindustrin uppger i " +
    "undersökningen att de har förändrat sina inköp det senaste året. Hälften av dem har byggt " +
    "upp större lager för att på så vis säkra tillgången på insatsvaror. En fjärdedel av " +
    "tillverkningsföretagen och en femtedel av tjänsteföretagen använder fler alternativa " +
    "leverantörer för att minska sårbarheten vid leveransstörningar.";
  private static final String FIFTH =
    "  - Att se över företagets inköp har kommit allt mer i fokus under pandemin i och med " +
      "störningarna i de globala handelsflödena. Det gäller inte minst för att säkra tillgången " +
      "på insatsvaror så att produktionen kan upprätthållas, säger Jörgen Kennemar.";


  private final static List<String> paragraphs;

  private ParagraphSingleton(){}

  static{
    paragraphs = new ArrayList<>();
    paragraphs.add(FIRST);
    paragraphs.add(SECOND);
    paragraphs.add(THIRD);
    paragraphs.add(FOURTH);
    paragraphs.add(FIFTH);
  }

  public static List<String> getInstance(){
    return Collections.unmodifiableList(paragraphs);
  }

}
