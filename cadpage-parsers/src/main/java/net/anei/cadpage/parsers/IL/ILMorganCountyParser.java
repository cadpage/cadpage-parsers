package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

public class ILMorganCountyParser extends DispatchA80Parser {

  public ILMorganCountyParser() {
    super("MORGAN COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@jacksonvilleil.com";
  }
}
