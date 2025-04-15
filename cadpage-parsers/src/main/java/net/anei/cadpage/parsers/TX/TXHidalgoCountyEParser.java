package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXHidalgoCountyEParser extends FieldProgramParser {
  
  public TXHidalgoCountyEParser() {
    super("HIDALGO COUNTY", "TX", 
          "CREATE_DATE/TIME:DATETIME! LOCATION:ADDRCITY! GPS! CLOSEST_INTERSECTION:X! RADIO_CHANNEL:CH! " + 
          "FIRE_CALL_TYPE:CALL! NATURE_OF_CALL:CALL/SDS! INCIDENT_#:ID! NARRATIVE:INFO! INFO/N+ DISPATCHED_UNITS:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@mcallenpd.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("paging")) return false;
    int pt = body.indexOf("\nDisclaimer:");
    body = body.replace(" NATURE OF CALL:", "\nNATURE OF CALL:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("LATITUDE/LONGITUDE[: ]*(.*)", true);
    return super.getField(name);
  }

}
