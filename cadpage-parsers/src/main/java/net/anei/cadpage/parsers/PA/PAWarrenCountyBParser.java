package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAWarrenCountyBParser extends FieldProgramParser {
  
  public PAWarrenCountyBParser() {
    super("WARREN COUNTY", "PA", 
          "Inc_Code:CALL! Address:ADDRCITY! Common_Name:PLACE! Units:UNIT! Cross_Streets:X! END");
  }
  
  @Override
  public String getFilter() {
    return "alerts@warrencounty.ealertgov.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Warren County Alert")) return false;
    return parseFields(body.split("\n"), data);
  }

}
