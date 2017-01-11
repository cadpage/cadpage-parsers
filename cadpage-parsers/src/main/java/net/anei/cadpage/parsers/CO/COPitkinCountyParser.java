package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class COPitkinCountyParser extends DispatchA19Parser {
  
  public COPitkinCountyParser() {
    super("PITKIN COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "PCREDC@pitkin911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
