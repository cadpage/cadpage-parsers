package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCHokeCountyBParser extends FieldProgramParser {
  
  public NCHokeCountyBParser() {
    super("HOKE COUNTY", "NC", 
          "CALL ADDRCITYST X PLACE EMPTY! END");
  }
  
  @Override
  public String getFilter() {
    return "74121";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("HOKE COUNTY 911:")) return false;
    body = body.substring(16).trim();
    return parseFields(body.split("\\*\\*", -1), data);
  }
}
