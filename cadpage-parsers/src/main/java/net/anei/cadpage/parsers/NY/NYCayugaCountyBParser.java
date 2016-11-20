package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYCayugaCountyBParser extends FieldProgramParser {
  
  public NYCayugaCountyBParser() {
    super("CAYUGA COUNTY", "NY", 
          "CALL ADDRCITY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "FDCMS Dispatch";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
}
