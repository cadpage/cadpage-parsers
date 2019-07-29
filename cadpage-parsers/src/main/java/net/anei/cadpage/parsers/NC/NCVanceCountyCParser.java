package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCVanceCountyCParser extends DispatchSouthernParser {
  
  public NCVanceCountyCParser() {
    super(CITY_LIST, "VANCE COUNTY", "NC", 
        DSFLG_ADDR|DSFLG_OPT_BAD_PLACE|DSFLG_OPT_X|DSFLG_OPT_CODE|DSFLG_ID);
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.endsWith(" VA")) {
      data.strCity = data.strCity.substring(0, data.strCity.length()-3);
      data.strState = "VA";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new String[]{
      
      // City
      "HENDERSON",
      
      // Town
      "KITTRELL",
      "MIDDLEBURG",
      
      // Census designated place
      "SOUTH HENDERSON",
      
      
      // Unincorporated communities
      "ADCOCK CROSSROADS",
      "BEARPOND",
      "BOBBITT",
      "BROOKSTON",
      "BULLOCKSVILLE",
      "CARLTON",
      "COKESBURY",
      "DABNEY",
      "DREWRY",
      "EPSOM",
      "FAULKNER CROSSROADS",
      "FLINT HILL",
      "FLOYTAN CROSSROADS",
      "GILL",
      "GILLBURG",
      "GREENWAY",
      "GREYSTONE",
      "HARRIS CROSSROADS",
      "HICKS CROSSROADS",
      "KNOTTS CROSSROADS",
      "MOBILE",
      "STEEDSVILLE",
      "TOWNSVILLE",
      "TUNGSTEN",
      "WATKINS",
      "WELDONS MILL",
      "WEST END",
      "WILLIAMSBORO",
      "WOODWORTHS",
      "VICKSBORO",
      
      // Granville County
      "BULLOCK",
      "OXFORD",
      
      // Warren County
      "MANSON",
      "NORLINA",
      
      // Mecklenburg County
      "MECKLENBURG VA"

  };
}
