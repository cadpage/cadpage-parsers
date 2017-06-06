package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHPrebleCountyAParser extends DispatchEmergitechParser {
  
  public OHPrebleCountyAParser() {
    super(new String[]{"PREBLESHERIFF:", "Eaton911:"}, 
          CITY_LIST, "PREBLE COUNTY", "OH", TrailAddrType.PLACE);
  }
  
  @Override
  public String getFilter() {
    return "PREBLESHERIFF@swohio.twcbc.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains(" ADDR:")) return false;
    if (!super.parseMsg(body, data)) return false;
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "4",  "MVA",
      "16", "Dead Body",
      "28", "Fire",
      "29", "EMS Response",
      "C4", "MVA"
  });

  private static final String[] CITY_LIST = new String[]{
    
    // City
    "EATON",

    // Villages
    "COLLEGE CORNER",
    "CAMDEN",
    "ELDORADO",
    "GRATIS",
    "LEWISBURG",
    "NEW PARIS",
    "VERONA",
    "WEST ALEXANDRIA",
    "WEST ELKTON",
    "WEST MANCHESTER",

    // Townships
    "DIXON",
    "GASPER",
    "GRATIS",
    "HARRISON",
    "ISRAEL",
    "JACKSON",
    "JEFFERSON",
    "LANIER",
    "MONROE",
    "SOMERS",
    "TWIN",
    "WASHINGTON",

    // Unincorporated communities

    "BRENNERSVILLE",
    "BRINLEY",
    "BROWNS",
    "CAMPBELLSTOWN",
    "CEDAR SPRINGS",
    "DADSVILLE",
    "EBENEZER",
    "ENTERPRISE",
    "FAIRHAVEN",
    "GETTYSBURG",
    "GREENBUSH",
    "HAMBURG",
    "INGOMAR",
    "MORNING SUN",
    "MUTTONVILLE",
    "NEW HOPE",
    "NEW LEXINGTON",
    "NEW WESTVILLE",
    "SUGAR VALLEY",
    "TALAWANDA SPRINGS",
    "WEST FLORENCE",
    "WEST SONORA",
    "WHEATVILLE",
    
    // Butler County
    "MIDDLETOWN",
    "SOMERVILLE",
    
    // Darke County
    "ARCANUM",
    
    // Montgomery County
    "BROOKVILLE",
    "GERMANTOWN"
  };
}
