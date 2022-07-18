package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXChambersCountyParser extends FieldProgramParser {
  
  public TXChambersCountyParser() {
    super("CHAMBERS COUNTY", "TX", 
          "( ADDR/ZSCI END " +
          "| CALL ADDR/ZSXI END" +
          "| CALL PLACE? ADDR/Z INFO/Z! END )");
  }

  @Override
  public String getFilter() {
    return "Spok-Messenger@chamberstx.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Chambers County Paging System")) return false;
    String callPfx = null;
    if (body.startsWith("*STAGE*")) {
      callPfx = body.substring(0,7);
      body = body.substring(7).trim();
    }
    body = body.replace('\n', ' ');
    if (!parseFields(body.split("\\*", -1), data)) return false;
    if (callPfx != null) data.strCall = callPfx + data.strCall;
    return true;
  }
}
