package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXSomervellCountyBParser extends DispatchA19Parser {

  public TXSomervellCountyBParser() {
    super("SOMERVELL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "FRN-somervellcountytx@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
