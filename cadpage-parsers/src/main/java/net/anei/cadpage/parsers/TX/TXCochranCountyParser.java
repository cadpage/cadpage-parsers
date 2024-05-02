package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class TXCochranCountyParser extends DispatchA78Parser {

  public TXCochranCountyParser() {
    super("COCHRAN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@COCHRANCOUNTYSHERIFFSOFFICEalerts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
