package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA14Parser;


public class NYNassauCountyJParser extends DispatchA14Parser {

  public NYNassauCountyJParser() {
    super("NASSAU COUNTY", "NY", true);
  }

  @Override
  public String getFilter() {
    return "paging1@firerescuesystems.xohost.com,8559495933";
  }
}
