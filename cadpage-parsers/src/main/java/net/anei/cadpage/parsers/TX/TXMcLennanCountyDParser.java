package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXMcLennanCountyDParser extends DispatchA19Parser {

  public TXMcLennanCountyDParser() {
    super("MCLENNAN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "noreply@responsemaster.net";
  }
}
