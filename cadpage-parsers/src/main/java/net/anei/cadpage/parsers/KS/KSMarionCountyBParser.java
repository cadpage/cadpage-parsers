package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KSMarionCountyBParser extends FieldProgramParser {
  
  public KSMarionCountyBParser() {
    super("MARION COUNTY", "KS", 
          "ID CALL ADDR GPS1 GPS2 CITY ST END");
  }
  
  @Override
  public String getFilter() {
    return "sscsmtp@marioncoks.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatched Unit Email Notification")) return false;
    return parseFields(body.split(";"), data);
  }
}
