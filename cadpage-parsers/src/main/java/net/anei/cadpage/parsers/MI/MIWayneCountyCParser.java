package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIWayneCountyCParser extends FieldProgramParser {

  public MIWayneCountyCParser() {
    super("WAYNE COUNTY", "MI",
          "Call_Type:CALL! Address:ADDRCITY! Additional_Location_Information:INFO! City:CITY! CAD_number:ID! District:SKIP! Common_Name:PLACE! Narritive_Info:INFO! END");
    setBreakChar(']');
  }

  @Override
  public String getFilter() {
    return "dispatch@brownstownpolice-mi.org,sincservice@downrivermutualaid.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (!body.startsWith("{")) return false;
    body = '[' + body.substring(1);
    return super.parseMsg(body,  data);
  }
}
