package net.anei.cadpage.parsers.UT;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

public class UTWeberCountyBParser extends DispatchA11Parser {
  
  public UTWeberCountyBParser() {
    super(CITY_CODES, "WEBER COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "spillman@weber911.org";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FW",  "FARR WEST",
      "HAR", "HARRISVILLE",
      "HOO", "HOOPER",
      "MS",  "MARRIOTT-SLATERVILLE",
      "NOG", "NORTH OGDEN",
      "OGD", "OGDEN",
      "PV",  "PLEASANT VIEW",
      "RIV", "RIVERDALE",
      "ROY", "ROY",
      "SOG", "SOUTH OGDEN",
      "WCO", "WOODS CROSS",
      "WH",  "WEST HAVEN",
      
  });
}
