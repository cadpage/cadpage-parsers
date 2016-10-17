package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;


public class TNHamiltonCountyCParser extends DispatchH01Parser {
  public TNHamiltonCountyCParser() {
    super("HAMILTON COUNTY", "TN",
          "MARK! Workstation:SKIP! Print_Time:SKIP! User:SKIP! Location:ADDR! Response_Type:CALL! Zone_Name:MAP! Priority_Name:PRI! Creation_Time:SKIP! Sequence_Number:ID! Status_Name:SKIP! Status_Time:DATETIME! Handling_Unit:UNIT! Agency:SRC! NOTES+");
  }
  
  @Override
  public String getFilter() {
    return "911NOTIF@HC911.ORG";
  }
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    if (! super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strApt.startsWith("[")) {
      int pt = data.strApt.lastIndexOf(']');
      if (pt >= 0) data.strApt = data.strApt.substring(pt+1).trim();
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField("Response Email Report lite", true);
    if (name.equals("PRI")) return new PriorityField("PRI *(.*)");
    return super.getField(name);
  }
 }