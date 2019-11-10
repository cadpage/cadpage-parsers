package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VARockinghamCountyBParser extends DispatchH05Parser {
  
  public VARockinghamCountyBParser() {
    super("ROCKINGHAM COUNTY", "VA", 
        "CFS_#:SKIP! DATE_TIME:DATETIME CALL_TYPE:CALL! TAC_Channel:CH! ADDR:ADDRCITY! X_STREET:X! LAT:GPS1! LON:GPS2! UNITS:UNIT! INCIDENT_#:ID! NARRATIVE:EMPTY! INFO_BLK/N+");
  }
  
  @Override
  public String getFilter() {
    return "caddmsmailbox@hrecc.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
