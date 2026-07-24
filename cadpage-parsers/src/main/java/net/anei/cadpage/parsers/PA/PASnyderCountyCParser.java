package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PASnyderCountyCParser extends FieldProgramParser {

  public PASnyderCountyCParser() {
    super("SNYDER COUNTY", "PA",
          "NEW_DISPATCH_CALL%EMPTY! Call_#:ID! Type:CALL! Priority:PRI! Status:SKIP! Location:ADDRCITY! Map_Page:MAP! " +
              "Units_Dispatched:UNIT? Caller:NAME! Phone:PHONE! Summary:EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-reply@base44-apps.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("[CAD ALERT]")) return false;
    int pt = body.indexOf("\n\nAlert forwarded to:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(body.split("\n+"), data)) return false;
    if (data.strName.equals("-")) data.strName = "";
    if (data.strPhone.equals("-")) data.strPhone = "";
    return true;
  }
}
