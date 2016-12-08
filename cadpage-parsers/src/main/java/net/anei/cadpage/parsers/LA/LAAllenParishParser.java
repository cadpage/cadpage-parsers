package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class LAAllenParishParser extends DispatchA64Parser{

  public LAAllenParishParser() {
    this("ALLEN PARISH", "LA");
  }

  protected LAAllenParishParser(String defCity, String defState) {
    super(defCity, defState);
  }

  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}