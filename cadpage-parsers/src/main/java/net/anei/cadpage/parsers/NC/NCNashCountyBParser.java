package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class NCNashCountyBParser extends DispatchA3Parser {

  public NCNashCountyBParser() {
    super("", "NASH COUNTY", "NC", 
          "Line2:ADDR! Line3:APT! Line4:APT! Line5:CITY! Line6:X! Line7:X! Line8:MAP! Line9:INFO! Line10:CODE! Line11:CALL! Line12:NAME Line13:PHONE Line14:UNIT Line15:INFO2 Line16:INFO2 Line17:INFO2 Line18:INFO");
    setBreakChar('=');
  }
  
  @Override
  public String getFilter() {
    return "NASH911@NASHCOUNTYNC.GOV";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    if (!body.startsWith("NASH911: *\n")) return false;
    body = body.substring(11).trim();

    body = stripFieldEnd(body, "*");
    return parseFields(body.split("\\*\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO2")) return new InfoField();
    return super.getField(name);
  }
}
