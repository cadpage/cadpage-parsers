package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class TNMorganCountyAParser extends FieldProgramParser {
  
  public TNMorganCountyAParser() {
    super("MORGAN COUNTY", "TN",
          "CALL! ADDRCITY INFO");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911 Incident Auto-Page")) return false;
    return parseFields(body.split("\n"), data);
  };
  
  @Override
  public String adjustMapAddress(String addr) {
    int pt = addr.indexOf(" - ");
    if (pt >= 0) addr = addr.substring(0,pt).trim();
    return super.adjustMapAddress(addr);
  }
}
