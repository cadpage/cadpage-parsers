package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyIParser extends FieldProgramParser {
  
  public WASnohomishCountyIParser() {
    super("SNOHOMISH COUNTY", "WA", "");
  }
  
  @Override
  public String getFilter() {
    return "Alert@email.getrave.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return false;
  }
}
