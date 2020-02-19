package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.Cadpage2Parser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCClarendonCountyBParser extends Cadpage2Parser {
  
  public SCClarendonCountyBParser() {
    super("CLARENDON COUNTY", "SC");
    setFieldList("CALL PLACE ADDR APT CITY ID PRI DATE TIME UNIT X INFO");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCross = data.strCross.replace('@', '/');
    return data.strCall.length() > 0 && data.strAddress.length() > 0;
  }
}
