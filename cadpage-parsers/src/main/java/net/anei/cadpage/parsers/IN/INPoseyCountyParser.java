package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INPoseyCountyParser extends DispatchA19Parser {
  
  public INPoseyCountyParser() {
    super("POSEY COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "PoseyCounty.E911@poseycountyin.gov";
  }
}
