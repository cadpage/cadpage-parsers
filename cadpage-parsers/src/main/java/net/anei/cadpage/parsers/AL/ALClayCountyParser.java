package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALClayCountyParser extends FieldProgramParser {
  
  public ALClayCountyParser() {
    super("CLAY COUNTY", "AL", 
          "CALL ADDR/iSXP INFO GPS! END");
  }
  
  @Override
  public String getFilter() {
    return "claye911@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CLAY E911")) return false;
    
    body = body.replace('\n', ' ');
    if (body.startsWith("// ")) body = ' ' + body;
    if (body.endsWith(" //")) body += ' ';
    if (!parseFields(body.split(" // "), data)) return false;
    if (data.strPlace.startsWith("MM ")) {
      data.strAddress = append(data.strAddress, " ", data.strPlace);
      data.strPlace = "";
    }
    return true;
  }

}
