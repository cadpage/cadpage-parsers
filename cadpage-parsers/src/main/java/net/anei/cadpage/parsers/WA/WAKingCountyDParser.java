package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAKingCountyDParser extends FieldProgramParser {

  public WAKingCountyDParser() {
    super(CITY_CODES, "KING COUNTY", "WA", 
          "ADDRCITY APT CITY CALL CH UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "CAD@norcom.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("NORCOM Dispatch Page")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "KE", "KENMORE",
      "LF", "LAKE FOREST PARK",
      "MI", "MERCER ISLAND",
      
      "LK FOREST PK",   "LAKE FOREST PARK"
  });
}
