package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXCollinCountyEParser extends DispatchA19Parser {

  public TXCollinCountyEParser() {
    super("COLLIN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "FRN-murphytx@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
