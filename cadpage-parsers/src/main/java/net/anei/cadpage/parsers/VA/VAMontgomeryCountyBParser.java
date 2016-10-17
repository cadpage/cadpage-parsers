package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAMontgomeryCountyBParser extends FieldProgramParser {
  
  public VAMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "VA", 
          "ADDRCITY CALL PLACE INFO X UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@nrv911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    body = stripFieldEnd(body, " <end>");
    return parseFields(body.split(";"), data);
  }

}
