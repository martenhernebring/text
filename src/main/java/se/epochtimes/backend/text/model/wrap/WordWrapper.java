package se.epochtimes.backend.text.model.wrap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import se.epochtimes.backend.text.model.headline.HeadlineComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class WordWrapper {

  public static final String NL = "\n";
  private int maxLettersPerLine;

  private final Word[] words;
  private List<String> lines;

  private int lineWidth, startingSpaces;
  private final StringBuilder sb = new StringBuilder();
  private boolean bisect;

  private WordWrapper(String raw) {
    String[] w = raw.trim().split("\\s+");
    int l = w.length;
    words = new Word[l];
    words[0] = new Word(w[0], Order.FIRST);
    if(l > 1){
      for(int i = 1; i < l - 1; i++)
        words[i] = new Word(w[i], Order.MIDDLE);
      words[l - 1] = new Word(w[l - 1], Order.LAST);
    }
  }

  private WordWrapper(String raw, Format format) {
    this(raw);
    this.maxLettersPerLine = format.getMax();
  }

  private WordWrapper(String raw, Format format, int startingSpaces) {
    this(raw, format);
    this.startingSpaces = startingSpaces;
  }

  public static String format(String text, Format format) {
    WordWrapper ww = new WordWrapper(text, format);
    return join(ww.wrapWordsWithLines());
  }

  public static HeadlineComponent format(HeadlineComponent main) {
    main.setHeadline(format(main.getHeadline(), Format.HEADLINE));
    main.setLeader(format(main.getLeader(), Format.LEAD));
    return main;
  }

  static String formatParagraph(String text, int startingSpaces) {
    WordWrapper ww = new WordWrapper(text, Format.PARAGRAPH, startingSpaces);
    return join(ww.wrapWordsWithBisect());
  }

  public static String formatBody(String body) {
    String[] paragraphs = body.trim().split("\\R+");
    paragraphs[0] = formatParagraph(paragraphs[0], 3);
    for(int i = 1; i < paragraphs.length; i++)
      paragraphs[i] = formatParagraph(paragraphs[i], 2);
    return String.join("", paragraphs);
  }

  private static String join(List<String> lines) {
    StringJoiner joiner = new StringJoiner(NL, "", NL);
    for (String el : lines) {
      joiner.add(el);
    }
    return joiner.toString();
  }

  private List<String> wrapWordsWithBisect() {
    List<String> normal = wrapWordsWithLines();
    int normalNewLines = normal.size();
    bisect = true;
    List<String> bisected  = wrapWordsWithLines();
    bisect = false;
    if(bisected.size() < normalNewLines)
      return bisected;
    else
      return normal;
  }

  private List<String> wrapWordsWithLines() {
    lines = new ArrayList<>();
    lineWidth = 0;
    wrapWords();
    addLine();
    updateIfNecessary();
    return lines;
  }

  private void wrapWords() {
    for(Word word: words)
      append(word);
  }

  private void append(Word word) {
    if (word.isEmpty())
      return;
    this.lineWidth += word.length();
    addLeadingWhiteSpace(word);
    addWord(word);
  }

  private void addLeadingWhiteSpace(Word word) {
    if(word.order() == Order.FIRST)
      addParagraphStartingSpace();
    else
      addWhiteSpaceDistance(word);
  }

  private void addParagraphStartingSpace() {
    for(int sp = 0; sp < startingSpaces; sp++) {
      sb.append(" ");
      lineWidth++;
    }
  }

  private void addWhiteSpaceDistance(Word word) {
    lineWidth++;
    if(isSpace(word)) {
      sb.append(" ");
    } else {
      addLine();
      if(isNotBig(word.length()) && isLongLineWithoutBisect(word)) {
        lineWidth = word.length();
      }
    }
  }

  private void addWord(Word toBeAdded) {
    if (isNotBisectable(toBeAdded))
      sb.append(toBeAdded);
    else
      bisect(toBeAdded);
  }

  private void bisect(Word word) {
    sb.append(word.bisectFirstHalf());
    addLine();
    String leftOver = word.secondHalf();
    sb.append(leftOver);
    lineWidth = leftOver.length();
  }

  private boolean isSpace(Word word) {
    if(isNotBig(word.length()))
      return !isLongLineWithoutBisect(word);
    else
      return !isTooLongForBisect(word.length());
  }

  private boolean isLongLineWithoutBisect(Word word) {
    return isLineTooLong() &&
      (isTooLongForBisect(word.length()) || isNotCandidateForBisect(word));
  }

  private boolean isNotBisectable(Word word) {
    return isNotBig(word.length()) &&
      (!isLineTooLong() || isNotCandidateForBisect(word));
  }

  private boolean isNotCandidateForBisect(Word word) {
    return !bisect || word.length() <= 14 || word.order() == Order.LAST;
  }

  private void addLine() {
    lines.add(sb.toString());
    sb.setLength(0);
  }

  private boolean isNotBig(int wordLength) {
    return wordLength <= maxLettersPerLine;
  }

  private boolean isTooLongForBisect(int wordLength) {
    return lineWidth - wordLength/2 > maxLettersPerLine;
  }

  private boolean isLineTooLong() {
    return lineWidth > maxLettersPerLine;
  }

  private void updateIfNecessary() {
    ImmutablePair<Integer, String> l = findLongest();
    if (l.getLeft() < (lines.size() - 1))
      updateIfNecessaryNotLast(l);
  }

  private ImmutablePair<Integer, String> findLongest() {
    int index = 0;
    String longest = lines.get(0);
    for (int i = 1; i < lines.size(); ++i) {
      if (lines.get(i).length() > longest.length()) {
        longest = lines.get(i);
        index = i;
      }
    }
    return new ImmutablePair<>(index, longest);
  }

  private void updateIfNecessaryNotLast(ImmutablePair<Integer, String> ip) {
    int li = ip.getLeft();
    String next = lines.get(li + 1);
    String[] w = ip.getRight().split(" ");
    int lstl = w[w.length - 1].length();
    int proposal = next.length() + lstl + 1;
    String lngst = ip.getRight();
    int lngstl = lngst.length();
    if(proposal <= maxLettersPerLine && proposal < lngstl) {
      lines.set(li, lngst.substring(0, lngstl - lstl - 1));
      lines.set(li + 1, lngst.substring(lngstl - lstl) + " " + next);
    }
  }
}
