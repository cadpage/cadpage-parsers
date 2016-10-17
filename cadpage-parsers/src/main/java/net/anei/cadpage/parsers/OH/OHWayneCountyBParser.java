package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Wayne County, OH (variant B)
 */
public class OHWayneCountyBParser extends FieldProgramParser {
  
  public OHWayneCountyBParser() {
    super("WAYNE COUNTY", "OH",
           "CALL ADDR/i INFO+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@ohiomuni.net,dispatch@orrville.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Message")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    String info = data.strSupp.toUpperCase();
    for (String city : CITY_LIST) {
      if (info.contains(city)) {
        data.strCity = city;
        break;
      }
    }
    return true;
  }
  
  private static final String[] CITY_LIST = new String[] {
    "NORTH LAWRENCE"
  };
}
