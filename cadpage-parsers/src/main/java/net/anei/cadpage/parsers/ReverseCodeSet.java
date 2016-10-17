package net.anei.cadpage.parsers;

/**
 * Codeset class that looks for matching codes at the end of the target string
 * rather then the beginning
 */
public class ReverseCodeSet extends CodeSet {

  public ReverseCodeSet(String... table) {
    super(table);
  }

  @Override
  protected void add(String code) {
    super.add(reverse(code));
  }

  @Override
  public String getCode(String code, boolean reqSpace) {
    String result =  super.getCode(reverse(code), reqSpace);
    if (result != null) result = reverse(result);
    return result;
  }

  private String reverse(String line) {
    return new StringBuilder(line).reverse().toString();
  }
}
