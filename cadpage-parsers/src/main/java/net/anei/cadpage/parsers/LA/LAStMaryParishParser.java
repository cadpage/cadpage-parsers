package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class LAStMaryParishParser extends DispatchBCParser {

  public LAStMaryParishParser() {
    super("ST MARY PARISH", "LA");
  }

  @Override
  public String getFilter() {
    return "chitcopier@chitimacha.gov";
  }
}
