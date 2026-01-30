package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class TNCampbellCountyCParser extends DispatchC04Parser {

  public TNCampbellCountyCParser() {
    super("CAMPBELL COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@LAFOLLETTE911.INFO";
  }
}
