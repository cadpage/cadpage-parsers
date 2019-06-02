package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOClayCountyCParser extends DispatchA33Parser {
  
  
  public MOClayCountyCParser() {
    super("CLAY COUNTY", "MO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "ITI@NKC.ORG";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    String cross = data.strCross;
    if (cross.endsWith(", MO")) {
      data.strCity = append(data.strCity, " ", cross.substring(0, cross.length()-4).trim());
      data.strState = "MO";
      data.strCross = "";
    } else if (cross.endsWith("CITY")) {
      data.strCity = append(data.strCity, " ", cross);
      data.strCross = "";
    } else if (cross.equals("MO")) {
      data.strState = "MO";
      data.strCross = "";
    }
    return true;
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1603 NE CLUBHOUSE DR",                 "+39.150603,-94.562522"
  });
  
}