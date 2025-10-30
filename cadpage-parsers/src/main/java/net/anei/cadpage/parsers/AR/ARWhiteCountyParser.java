package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ARWhiteCountyParser extends DispatchH05Parser {

  public ARWhiteCountyParser() {
    super("WHITE COUNTY", "AR",
          "Call_Type:CALL! ( Address:ADDRCITY! | Call_Address:ADDRCITY! ) Common_Name:PLACE! Call_Time:DATETIME! " + 
              "Cross_Street:X! Narrative:EMPTY! INFO_BLK+ Google_Hyperlink:EMPTY! GPS! Incident_Number:ID! Status_Times:EMPTY! TIMES+");
  }

  @Override
  public String getFilter() {
    return "IT@whitecounty.ar.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("Common Name:", "</div><div>Common Name:")
               .replace("Narrative:", "</div><div>Narrative:");
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new GPSField("https://www.google.com/.*query=(.*)", true);
    return super.getField(name);
  }
}
