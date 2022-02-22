package se.epochtimes.backend.text.model.wrap;

record Word(String word, Order order) {

  boolean isEmpty() {
    return word.isEmpty();
  }

  int length() {
    return word.length();
  }

  private int mid() {
    return word.length() / 2;
  }

  private String firstHalf() {
    return word.substring(0, mid());
  }

  String secondHalf() {
    return word.substring(mid());
  }

  String bisectFirstHalf() {
    String firstHalf = firstHalf();
    StringBuilder sb = new StringBuilder(firstHalf());
    if (firstHalf.charAt(firstHalf.length() - 1) != '-')
      sb.append("-");
    return sb.toString();
  }

  @Override
  public String toString() {
    return word;
  }
}
