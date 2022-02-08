package se.epochtimes.backend.text.model;

import static se.epochtimes.backend.text.model.WordWrapper.MAX;

public class Word {
  private final String word;
  private final int index;

  public Word(String word, int paragraphIndex) {
    this.word = word;
    this.index = paragraphIndex;
  }

  boolean isEmpty() {
    return word.isEmpty();
  }

  int getLength() {
    return word.length();
  }

  int getIndex() {
    return index;
  }

  boolean isBig() {
    return getLength() > MAX;
  }

  String getFirstHalf() {
    return word.substring(0, getLength() / 2);
  }

  String getSecondHalf() {
    return word.substring(getLength() / 2);
  }

  @Override
  public String toString() {
    return word;
  }
}
