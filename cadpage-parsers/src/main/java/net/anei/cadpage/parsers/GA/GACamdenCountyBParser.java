package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Camden County, GA
 */
public class GACamdenCountyBParser extends FieldProgramParser {
  
  public GACamdenCountyBParser() {
    super(CITY_LIST, "CAMDEN COUNTY", "GA",
           "CALL ADDR/S! Units:UNIT! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "4046926092";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (! parseFields(body.split("\n"), 4, data)) return false;
    if (data.strCity.equals("CAMDEN COUNTY")) data.strCity = "";
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CAMDEN COUNTY",
    
    "HOPEWELL",
    "KINGS BAY",
    "KINGS BAY BASE",
    "KINGSLAND",
    "ST MARYS",
    "SEALS",
    "SPRING BLUFF",
    "WAVERLY",
    "WHITE OAK",
    "WOODBINE"
  };
}
