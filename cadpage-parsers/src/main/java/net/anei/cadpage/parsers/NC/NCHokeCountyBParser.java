package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCHokeCountyBParser extends FieldProgramParser {
  
  public NCHokeCountyBParser() {
    super("HOKE COUNTY", "NC", 
          "CALL ADDRCITYST X PLACE INFO! END");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,74121";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (body.startsWith("HOKE COUNTY 911:")) {
        body = body.substring(16).trim();
        break;
      }
      
      if (subject.equals("HOKE COUNTY 911")) break;
      
      return false;
    } while (false);
    
    return parseFields(body.split("\\*\\*", -1), data);
  }
}
