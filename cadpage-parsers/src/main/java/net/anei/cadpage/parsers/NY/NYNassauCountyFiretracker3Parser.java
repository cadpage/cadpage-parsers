package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class NYNassauCountyFiretracker3Parser extends DispatchH05Parser {

  public NYNassauCountyFiretracker3Parser() {
    super("NASSAU COUNTY", "NY",
          "FDID:SRC! DATETIME! Call_Type:CALL! Address:ADDRCITY! bet:X! Additional_Information:INFO! Alerts:ALERT! " +
              "Narrative:EMPTY! INFO_BLK+ CFS_Number:SKIP! 1st_Unit:SKIP! ID! TIMES+ https:GPS! END");
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "NassauFireCall@nassaucountyny.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("CONFIDENTIALITY NOTICE:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
