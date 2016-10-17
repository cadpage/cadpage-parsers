package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 Ramsey County, MN
 **/



public class MNRamseyCountyParser extends FieldProgramParser {
  
  public MNRamseyCountyParser() {
    super("RAMSEY COUNTY", "MN", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR/SXa! CITY:CITY? ID:ID! PRI:PRI? INFO:INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";"), data)) return false;
    if (data.strApt.equalsIgnoreCase("None")) data.strApt = "";
    return true;
  }
}
