package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Grantham, PA  (Messiah College EMS)
 */
public class PAGranthamParser extends MsgParser {
  
  public PAGranthamParser() {
    super("GRANTHAM", "PA");
    setFieldList("CALL PLACE");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (!body.endsWith("; _")) return false;
    body = body.substring(0,body.length()-3).trim();
    String[] flds = body.split(";");
    if (flds.length != 2) return false;
    data.strCall = flds[0].trim();
    data.strPlace = flds[1].trim();
    return true;
  }
}
