package net.anei.cadpage.parsers.OH;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHJeffersonCountyAParser extends DispatchEmergitechParser {

  public OHJeffersonCountyAParser() {
    super("jcomm:", CITY_LIST, "JEFFERSON COUNTY", "OH");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    else if (data.strCity.endsWith(" Co")) data.strCity += "unty";
    if (WV_CITIES.contains(data.strCity.toUpperCase())) data.strState = "WV";
    return true;
  }

  @Override
  public String adjustMapAddress(String address, boolean cross) {
    if (cross) {
      int pt = address.indexOf('@');
      if (pt >= 0) address = address.substring(0, pt).trim();
    }
    return super.adjustMapAddress(address, cross);
  }

  private static final String[] CITY_LIST = new String[] { 

    "ADENA",
    "AMSTERDAM",
    "BERGHOLZ",
    "BLOOMINGDALE",
    "DILLONVALE",
    "EMPIRE",
    "IRONDALE",
    "MOUNT PLEASANT",
    "NEW ALEXANDRIA",
    "RAYLAND",
    "RICHMOND",
    "SMITHFIELD",
    "STEUBENVILLE",
    "STRATTON",
    "TILTONSVILLE",
    "TORONTO",
    "WINTERSVILLE",
    "YORKVILLE", 

    // Townships
    "KNOX TWP",
    "MINGO JUNCTION",
    "BRUSH CREEK TWP",
    "CROSS CREEK TWP",
    "ISLAND CREEK TWP",
    "MOUNT PLEASANT TWP",
    "SMITHFIELD TWP",
    "SPRINGFIELD TWP",
    "STEUBENVILLE TWP",
    "ROSS TWP",
    "SALEM TWP",
    "SALINE TWP",
    "WARREN TWP",
    "WAYNE TWP",
    "WELLS TWP",
    
    // Unincorporated communities
    "UNIONPORT",
    "BRILLIANT",
    "EAST SPRINGFIELD",
    "GREENTOWN",
    "HAMMONDSVILLE",
    "HOPEWELL",
    "NEW SOMERSET",
    "PINEY FORK",
    "RUSH RUN",
    "WEEMS",
    "WOLF RUN",


    "BELMONT CO",
    "BROOKE CO",
    "COLUMBIANA CO",
    "CARROLL CO",
    "HANCOCK CO",
    "HARRISON CO",
    "OHIO CO"
  };
  
  private static final Set<String> WV_CITIES = new HashSet<String>(Arrays.asList(new String[]{
      "BROOKE COUNTY",
      "HANCOCK COUNTY",
      "OHIO COUNTY"
  }));
}
