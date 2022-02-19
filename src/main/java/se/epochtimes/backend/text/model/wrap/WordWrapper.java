package se.epochtimes.backend.text.model.wrap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import se.epochtimes.backend.text.model.main.HeadlineComponent;

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
  private Word currentWord;
  private boolean bisect;

  private WordWrapper(String raw) {
    String[] w = raw.trim().split("\\s+");
    int l = w.length;
    words = new Word[l];
    for(int i = 0; i < l; i++)
      words[i] = new Word(w[i], i);
  }

  public WordWrapper(String raw, Format format) {
    this(raw);
    switch (format) {
      case HEADLINE -> this.max = 12;
      case LEAD -> this.max = 30;
      case PARAGRAPH -> this.max = 33;
      case DEFAULT -> this.max = 32;
    }
  }

  public WordWrapper(String raw, Format format, int startingSpaces) {
    this(raw, format);
    this.startingSpaces = startingSpaces;
  }

  public static HeadlineComponent format(HeadlineComponent main) {
    WordWrapper ww = new WordWrapper(main.getHeadline(), Format.HEADLINE);
    main.setHeadline(join(ww.wrapWords()));
    ww = new WordWrapper(main.getLead(), Format.LEAD);
    main.setLead(join(ww.wrapWords()));
    return main;
  }

  public static String formatBody(String body) {
    String[] paragraphs = body.trim().split("\\R+");
    WordWrapper ww = new WordWrapper(paragraphs[0], Format.PARAGRAPH, 3);
    paragraphs[0] = join(ww.wrapWordsWithBisect());
    for(int i = 1; i < paragraphs.length; i++) {
      ww = new WordWrapper(paragraphs[i], Format.PARAGRAPH, 2);
      paragraphs[i] = join(ww.wrapWordsWithBisect());
    }
    return String.join("", paragraphs);
  }

  public static String join(List<String> lines) {
    StringJoiner joiner = new StringJoiner(NL, "", NL);
    for (String el : lines) {
      joiner.add(el);
    }
    return joiner.toString();
  }

  public List<String> wrapWordsWithBisect() {
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

  public List<String> wrapWords() {
    lines = new ArrayList<>();
    lineWidth = 0;
    for(Word word: words)
      append(word);
    addLine();
    ImmutablePair<Integer, String> l = findLongest();
    if (l.getLeft() < (lines.size() - 1))
      updateIfNecessary(l);
    return lines;
  }

  private void append(Word word) {
    if (word.isEmpty())
      return;
    this.currentWord = word;
    this.lineWidth += word.getLength();
    addLeadingWhiteSpace();
    addWord();
  }

  private void addLeadingWhiteSpace() {
    if(currentWord.getIndex() > 0)
      addWhiteSpaceDistance();
    else
      addParagraphStartingSpace();
  }

  private void addParagraphStartingSpace() {
    for(int sp = 0; sp < startingSpaces; sp++) {
      sb.append(" ");
      lineWidth++;
    }
  }

  private void addWhiteSpaceDistance() {
    lineWidth++;
    if(currentWord.isNotBig(max))
      addWhiteSpaceSmallWord();
    else
      addWhiteSpaceBigWord();
  }

  private void addWhiteSpaceSmallWord() {
    if (((!bisect || isNotBisectable()) || (lineWidth - currentWord.getLength()/2 >= max)) && isLong()) {
      addLine();
      lineWidth = currentWord.getLength();
    } else
      sb.append(" ");
  }

  private void addWhiteSpaceBigWord() {
    if(!isHuge())
      sb.append(" ");
    else {
      addLine();
    }
  }

  private void addWord() {
    if (currentWord.isNotBig(max) && (!bisect || isNotBisectable()))
      sb.append(currentWord);
    else {
      bisect();
    }
  }

  private void bisect() {
    sb.append(currentWord.bisectFirstHalf());
    addLine();
    String leftOver = currentWord.getSecondHalf();
    sb.append(leftOver);
    lineWidth = leftOver.length();
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

  private void updateIfNecessary(ImmutablePair<Integer, String> longest) {
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

  private void addLine() {
    lines.add(sb.toString());
    sb.setLength(0);
  }

  private boolean isLong() {
    return lineWidth > max;
  }

  private boolean isHuge() {
    return lineWidth - currentWord.getLength()/2 > max;
  }

  private boolean isNotBisectable() {
    return !isLong() || currentWord.getLength() <= 14 || currentWord.getIndex() >= (words.length - 1);
  }
}
