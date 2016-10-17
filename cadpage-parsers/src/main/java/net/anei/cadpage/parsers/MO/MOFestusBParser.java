package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOFestusBParser extends DispatchA33Parser {
    
  public MOFestusBParser() {
    super("FESTUS", "MO");
  }
  
  @Override
  public String getFilter() {
    return "JBITTER@CITYOFFESTUS.ORG";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // They do some strange things with some data fields
    data.strCall = append(data.strMap, " - ", data.strCall);
    data.strMap = data.strCity;
    data.strCity = "";
    data.strSupp = append(data.strCross, " / ", data.strSupp);
    data.strCross = "";
    
    // See if we can derive the city name from the map code
    String city = data.strMap;
    int pt = city.lastIndexOf('/');
    if (pt >= 0) city = city.substring(pt+1).trim();
    data.strCity = convertCodes(city, CITY_CODES);
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CRYSTAL",         "CRYSTAL CITY",
      "JEFF",            "JEFFERSON COUNTY",
      "FESTUS PD &",     "FESTUS",
      "FESTUS PD & FD",  "FESTUS",
      "HERKY",           "HERCULANEUM",
      "JPAD",            "",
      "JPAD1",           "",
      "R-7 FD",          "FESTUS",
      "S OF FLUCOM-PAPIN R", "DESOTO"
      
  });
}
