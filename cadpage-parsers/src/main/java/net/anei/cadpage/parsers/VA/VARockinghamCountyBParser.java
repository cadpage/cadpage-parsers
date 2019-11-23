package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VARockinghamCountyBParser extends DispatchH05Parser {
  
  public VARockinghamCountyBParser() {
    super("ROCKINGHAM COUNTY", "VA", 
          "( INCIDENT_#:ID! DATE/TIME:DATETIME CALL_TYPE:CALL! TAC_Channel:CH! ADDR:ADDRCITY! X_STREET:X! LAT:GPS1! LON:GPS2! UNITS:UNIT! ( Narrative:EMPTY! INFO_BLK/N+ | ) UNIT_TIMES:EMPTY! TIMES+ CAD_#:SKIP? END " +
          "| CFS_#:SKIP! DATE_TIME:DATETIME CALL_TYPE:CALL! TAC_Channel:CH! ADDR:ADDRCITY! X_STREET:X! LAT:GPS1! LON:GPS2! UNITS:UNIT! INCIDENT_#:ID! NARRATIVE:EMPTY! INFO_BLK/N+ " + 
          "| Incident_Date:DATETIME? Call:CALL! Tac:CH! Priority:PRI! Address:ADDRCITY! City:CITY? X_Street:X! Units:UNIT! Incident_#:ID! LAT:GPS1! LON:GPS2! CAD_#:SKIP! Fire_Box:BOX INFO/N+ )");
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
    
    if (subject.equals("C30") || subject.equals("!")) {
      if (!subject.equals("!")) data.strSource = subject;
      body = body.replace("<br>", "\n");
      return parseFields(body.split("\n"), data);
    }
    
    else {
      return super.parseHtmlMsg(subject, body, data);
    }
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
