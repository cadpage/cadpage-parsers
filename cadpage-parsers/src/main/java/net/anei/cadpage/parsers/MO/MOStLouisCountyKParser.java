package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStLouisCountyKParser extends FieldProgramParser {
  
  public MOStLouisCountyKParser() {
    super("ST LOUIS COUNTY", "MO", 
          "ID CALL ADDR CITY! Description:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "glendalecad@glendalemo.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CALL")) return false;
    return parseFields(body.split("\n"), data);
  }

}
