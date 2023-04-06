package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXDallasCountyHParser extends DispatchA82Parser {

  public TXDallasCountyHParser() {
    super("DALLAS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "rowlettgateway@rowlett.com";
  }

}
