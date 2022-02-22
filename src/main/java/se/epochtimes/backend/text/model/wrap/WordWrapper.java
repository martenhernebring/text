package se.epochtimes.backend.text.model.wrap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import se.epochtimes.backend.text.model.headline.HeadlineComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class WordWrapper {

  public static final String NL = "\n";
  private int max;

  private final Word[] words;
  private List<String> lines;

  private int lineWidth, startingSpaces;
  private final StringBuilder sb = new StringBuilder();
  private boolean bisect;

  public static String formatBody(String body) {
    String[] paragraphs = body.trim().split("\\R+");
    paragraphs[0] = formatParagraph(paragraphs[0], 3);
    for(int i = 1; i < paragraphs.length; i++)
      paragraphs[i] = formatParagraph(paragraphs[i], 2);
    return String.join("", paragraphs);
  }

  static String formatParagraph(String text, int startingSpaces) {
    WordWrapper ww = new WordWrapper(text, Format.PARAGRAPH, startingSpaces);
    return join(ww.wrapWordsWithBisect());
  }

  private static String join(List<String> lines) {
    StringJoiner joiner = new StringJoiner(NL, "", NL);
    for (String el : lines) {
      joiner.add(el);
    }
    return joiner.toString();
  }

  private List<String> wrapWordsWithBisect() {
    List<String> normal = wrapWords();
    int normalNewLines = normal.size();
    bisect = true;
    List<String> bisected  = wrapWords();
    bisect = false;
    if(bisected.size() < normalNewLines)
      return bisected;
    else
      return normal;
  }

  public static HeadlineComponent format(HeadlineComponent main) {
    main.setHeadline(format(main.getHeadline(), Format.HEADLINE));
    main.setLead(format(main.getLead(), Format.LEAD));
    return main;
  }

  private static String format(String text, Format format) {
    WordWrapper ww = new WordWrapper(text, format);
    return join(ww.wrapWords());
  }

  private List<String> wrapWords() {
    lines = new ArrayList<>();
    lineWidth = 0;
    for(Word word: words)
      append(word);
    addLine();
    updateIfNecessary();
    return lines;
  }

  private void addLine() {
    lines.add(sb.toString());
    sb.setLength(0);
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

  private void updateIfNecessaryNotLast(ImmutablePair<Integer, String> longest) {
    int li = longest.getLeft();
    String next = lines.get(li + 1);
    String[] w = longest.getRight().split(" ");
    String last = w[w.length - 1];
    int proposal = next.length() + last.length();
    String ls = longest.getRight();
    if(proposal <= max && proposal < ls.length()) {
      lines.set(li, ls.substring(0, ls.length() - last.length() - 1));
      lines.set(li + 1, ls.substring(ls.length() - last.length()) + " " + next);
    }
  }

  private void addParagraphStartingSpace() {
    for(int sp = 0; sp < startingSpaces; sp++) {
      sb.append(" ");
      lineWidth++;
    }
  }

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
    this.max = format.getMax();
  }

  private WordWrapper(String raw, Format format, int startingSpaces) {
    this(raw, format);
    this.startingSpaces = startingSpaces;
  }

  private void append(Word word) {
    if (word.isEmpty())
      return;
    this.lineWidth += word.getLength();
    addLeadingWhiteSpace(word);
    addWord(word);
  }

  private void addLeadingWhiteSpace(Word word) {
    if(word.getOrder() == Order.FIRST)
      addParagraphStartingSpace();
    else
      addWhiteSpaceDistance(word);
  }

  private void addWhiteSpaceDistance(Word word) {
    lineWidth++;
    if(word.isNotBig(max))
      addWhiteSpaceSmallWord(word);
    else
      addWhiteSpaceBigWord(word);
  }

  private void addWhiteSpaceSmallWord(Word word) {
    if ((lineWidth > max) && (
        lineWidth - word.getLength()/2 >= max
        || !bisect
        || word.getLength() <= 14
        || word.getOrder() == Order.LAST
      )) {
      addLine();
      lineWidth = word.getLength();
    } else
      sb.append(" ");
  }

  private void addWhiteSpaceBigWord(Word word) {
    if(!(lineWidth - word.getLength()/2 > max))
      sb.append(" ");
    else {
      addLine();
    }
  }

  private void addWord(Word word) {
    if (word.isNotBig(max) && (
      !bisect
      || !(lineWidth > max)
      || word.getLength() <= 14
      || word.getOrder() == Order.LAST)
    )
      sb.append(word);
    else {
      bisect(word);
    }
  }

  private void bisect(Word word) {
    sb.append(word.bisectFirstHalf());
    addLine();
    String leftOver = word.getSecondHalf();
    sb.append(leftOver);
    lineWidth = leftOver.length();
  }
}
