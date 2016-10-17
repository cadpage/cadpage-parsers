package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyNParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyNParser() {
    super("CALL ADDR CITY! INFO/N+ Dispatch%EMPTY TIME ( PLACE_DASH | PLACE ) NAME PHONE! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "station41@verizon.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    body = body.replace('\n', ' ');
    if (body.endsWith(";")) body += ' ';
    return parseFields(body.split(" ; ", -1), data);
  }
}
