package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNScottCountyParser extends FieldProgramParser {
  
  public MNScottCountyParser() {
    super(CITY_LIST, "SCOTT COUNTY", "MN",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR/S! CITY:CITY? ID:ID! PRI:PRI INFO:INFO/N+");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";"), data)) return false;
    data.strAddress = stripFieldEnd(data.strAddress, data.strCity);
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "BELLE PLAINE",
    "ELKO NEW MARKET",
    "JORDAN",
    "NEW PRAGUE",
    "PRIOR LAKE",
    "SAVAGE",
    "SHAKOPEE",

    // Townships
    "BELLE PLAINE TOWNSHIP",
    "BLAKELEY TOWNSHIP",
    "CEDAR LAKE TOWNSHIP",
    "CREDIT RIVER TOWNSHIP",
    "HELENA TOWNSHIP",
    "JACKSON TOWNSHIP",
    "LOUISVILLE TOWNSHIP",
    "NEW MARKET TOWNSHIP",
    "SAND CREEK TOWNSHIP",
    "SPRING LAKE TOWNSHIP",
    "ST LAWRENCE TOWNSHIP",

    // Unincorporated communities
    "BLAKELEY",
    "CEDAR LAKE",
    "HELENA",
    "LYDIA",
    "MARYSTOWN",
    "MUDBADEN",
    "SPRING LAKE",
    "ST BENEDICT",
    "ST PATRICK",
    "UNION HILL",
    
    // Le Sueur County
    "LANESBURGH",
    
    "DAKOTA COUNTY",
    "CARVER COUNTY",
    "HENNEPIN COUNTY",
    "LE SUEUR COUNTY",
    "RICE COUNTY",
    "RAMSEY COUNTY",
    "SIBLEY COUNTY"
  };
}
