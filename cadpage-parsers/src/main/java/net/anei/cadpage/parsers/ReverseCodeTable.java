package net.anei.cadpage.parsers;

/**
 * CodeTable class that looks for matching keys at the end of the target string
 * rather then the beginning
 */
public class ReverseCodeTable extends CodeTable {

  public ReverseCodeTable(String... table) {
    super(table);
  }

  @Override
  public void put(String key, String value) {
    super.put(reverse(key), value);
  }

  @Override
  public Result getResult(String code, boolean reqSpace) {
    Result res = super.getResult(reverse(code), reqSpace);
    if (res != null) {
      res.setCode(reverse(res.getCode()));
      res.setRemainder(reverse(res.getRemainder()));
    }
    return res;
  }

  private String reverse(String line) {
    return new StringBuilder(line).reverse().toString();
  }
}
