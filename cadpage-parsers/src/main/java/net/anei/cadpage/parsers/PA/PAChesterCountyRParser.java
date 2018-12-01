package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyRParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyRParser() {
    super("CALL ADDRCITY! END");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch02008@prs.FDCMS.info";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
}
