package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Wadena County, MN
 */

public class MNWadenaCountyParser extends DispatchA27Parser {
  
  public MNWadenaCountyParser() {
    super(CITY_LIST, "WADENA COUNTY", "MN", "\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.wadena.mn.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("County")) data.strCity = "";
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // City    
    "ALDRICH",
    "MENAHGA",
    "NIMROD",
    "SEBEKA",
    "STAPLES",
    "VERNDALE",
    "WADENA",

    // Townships    
    "ALDRICH TWP",
    "BLUEBERRY TWP",
    "BULLARD TWP",
    "HUNTERSVILLE TWP",
    "LEAF RIVER TWP",
    "LYONS TWP",
    "MEADOW TWP",
    "NORTH GERMANY TWP",
    "ORTON TWP",
    "RED EYE TWP",
    "ROCKWOOD TWP",
    "SHELL RIVER TWP",
    "THOMASTOWN TWP",
    "WADENA TWP",
    "WING RIVER TWP",

    // Unincorporated    
    "BLUEGRASS",
    "HUNTERSVILLE",
    "LEAF RIVER",
    "SHELL CITY"
  };
  
}
