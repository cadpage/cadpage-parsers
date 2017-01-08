package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class VTAddisonCountyBParser extends DispatchA32Parser {
  
  public VTAddisonCountyBParser() {
    super(CITY_LIST, "ADDISON COUNTY", "VT");
  }
  
  @Override
  public String getFilter() {
    return "mremsdispatch911@gmail.com";
  }
  
  private static final String[] CITY_LIST = new String[]{

      //Communities
      
      "ADDISON",
      "BREAD LOAF",
      "BRIDPORT",
      "BRISTOL",
      "CHIMNEY POINT",
      "CORNWALL",
      "FERRISBURGH",
      "GOSHEN",
      "GRANVILLE",
      "HANCOCK",
      "LEICESTER",
      "LINCOLN",
      "MIDDLEBURY",
      "MONKTON",
      "NEW HAVEN",
      "ORWELL",
      "PANTON",
      "RIPTON",
      "SALISBURY",
      "SATANS KINGDOM",
      "SHOREHAM",
      "STARKSBORO",
      "VERGENNES",
      "WALTHAM",
      "WEYBRIDGE",
      "WHITING"
  };

}
