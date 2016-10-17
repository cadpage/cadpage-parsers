package net.anei.cadpage.parsers.OH;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHGalliaCountyParser extends DispatchEmergitechParser {
  
  public OHGalliaCountyParser() {
    super("Gallia911:", 60, CITY_LIST, "GALLIA COUNTY", "OH");
    addSpecialWords("LINCOLN");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // If there is no city, see if we can find it in the info section
    if (data.strCity.length() == 0) {
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, data.strSupp);
      if (res.isValid()) res.getData(data);
    }
    
    // If city ends with " CO", expand it
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    
    // ANd finally, see if this is in West Virginia
    if (WV_CITY_TABLE.contains(data.strCity)) data.strState = "WV";
    
    // See if there is a callback phone number in the APT field
    if (data.strApt.startsWith("CALLBK=")) {
      data.strPhone = data.strApt.substring(7).trim();
      data.strApt = "";
    }
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST").replace("APT", "APT PHONE");
  }

  private static final String[] CITY_LIST = new String[]{
    // Villages
    "CENTERVILLE",
    "CHESHIRE",
    "CROWN CITY",
    "GALLIPOLIS",
    "RIO GRANDE",
    "VINTON",

    // Townships
    "ADDISON TWP",
    "CHESHIRE TWP",
    "CLAY TWP",
    "GALLIPOLIS TWP",
    "GREEN TWP",
    "GREENFIELD TWP",
    "GUYAN TWP",
    "HARRISON TWP",
    "HUNTINGTON TWP",
    "MORGAN TWP",
    "OHIO TWP",
    "PERRY TWP",
    "RACCOON TWP",
    "SPRINGFIELD TWP",
    "WALNUT TWP",

    // Unincorporated communities
    "BIDWELL",
    "KERR",
    "PATRIOT",
    
    // Mason County, WV
    "MASON CO",
    "POINT PLEASANT"
  };
  
  private static Set<String> WV_CITY_TABLE = new HashSet<String>(Arrays.asList(
      "MASON COUNTY",
      "POINT PLEASANT"
  ));
}
