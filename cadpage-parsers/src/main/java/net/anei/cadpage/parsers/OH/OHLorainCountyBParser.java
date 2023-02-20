package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchInfoSysParser;



public class OHLorainCountyBParser extends DispatchInfoSysParser {
  
  public OHLorainCountyBParser() {
    super(CITY_LIST, "LORAIN COUNTY", "OH");
    addExtendedDirections();
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com,sunsrv@sundance-sys.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From: MapUser")) return false;
    if (!super.parseMsg(body, data)) return false;
    data.strCall = stripFieldStart(data.strCall, data.strCode+'-');
    return true;
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities    
    "AMHERST",
    "AVON",
    "AVON LAKE",
    "ELYRIA",
    "LORAIN",
    "N RIDGEVILLE",
    "OBERLIN",
    "SHEFFIELD LAKE",
    "VERMILION",

    // Villages
    "GRAFTON",
    "KIPTON",
    "LAGRANGE",
    "ROCHESTER",
    "SHEFFIELD",
    "SOUTH AMHERST",
    "WELLINGTON",

    // Townships
    "AMHERST",
    "BRIGHTON",
    "BROWNHELM",
    "CAMDEN",
    "CARLISLE",
    "COLUMBIA",
    "EATON",
    "ELYRIA",
    "GRAFTON",
    "HENRIETTA",
    "HUNTINGTON",
    "LAGRANGE",
    "NEW RUSSIA",
    "PENFIELD",
    "PITTSFIELD",
    "ROCHESTER",
    "SHEFFIELD",
    "WELLINGTON",

    // Census-designated place
    "EATON ESTATES",

    // Other communities
    "BELDEN",
    "BRENTWOOD LAKE",
    "BRIGHTON",
    "BROWNHELM",
    "BROWNHELM STATION",
    "COLUMBIA HILLS CORNERS",
    "HENRIETTA",
    "HUNTINGTON",
    "NORTH EATON",
    "PENFIELD",
    "PITTSFIELD",
    
    // Cayahoga County
    "BAY VILLAGE",
    "FAIRVIEW PARK",
    "NORTH OLMSTED",
    "ROCKY RIVER",
    "WESTLAKE"
  };
}
