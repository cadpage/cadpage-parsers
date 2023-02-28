package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class TXCookeCountyCParser extends DispatchA78Parser {
  
  public TXCookeCountyCParser() {
    super("COOKE COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@CookeCountySheriffsOfficealerts.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
