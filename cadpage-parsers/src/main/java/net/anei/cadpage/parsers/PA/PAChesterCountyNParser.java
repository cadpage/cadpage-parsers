package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyNParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyNParser() {
    super("CALL ADDR CITY PLACE X PLACE_PHONE! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strSource = subject;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
