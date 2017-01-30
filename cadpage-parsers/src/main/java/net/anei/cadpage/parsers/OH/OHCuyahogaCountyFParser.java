package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHCuyahogaCountyFParser extends FieldProgramParser {
  
  public OHCuyahogaCountyFParser() {
    super("CUYAHOGA COUNTY", "OH", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strApt.equals("0")) data.strApt = "";
    return true;
  }

}
