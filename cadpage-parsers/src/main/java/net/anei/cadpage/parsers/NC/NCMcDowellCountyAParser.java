
package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCMcDowellCountyAParser extends DispatchSouthernParser {

  public NCMcDowellCountyAParser() {
    super(null, "MCDOWELL COUNTY", "NC", 
          DSFLG_OPT_DISP_ID | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_TIME);
    setupSaintNames("JOSEPH");
    removeWords("COVE");
    setupCities(CITY_LIST);
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (! super.parseMsg(body, data)) return false;
    data.strCall = stripFieldEnd(data.strCall, "-");
    data.strCity = stripFieldEnd(data.strCity, " CITY");
    data.strCity = stripFieldEnd(data.strCity, " AREA");
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PK_PTN.matcher(addr).replaceAll("PARK");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapCity(String city) {
    return NCMcDowellCountyParser.doAdjustMapCity(city);
  }
  
  private static final String[] CITY_LIST = new String[]{
      
    //cities
    "MARION",
    "MARION AREA",
    "MARION CITY",
    "HANKINS NORTH FORK",
    "WOODLAWN",
    "NORTH WOODLAWN",
    
    //towns
    "OLD FORT",
    "OLD FORT AREA",
    "OLD FORT TOWN",
    
    //Census-designated place
    "WEST MARION",
    "WEST MARION CITY",
    
    //Unincorporated communities
    "ASHFORD",
    "GLENWOOD",
    "LINVILLE FALLS",
    "LITTLE SWITZERLAND",
    "NEBO",
    "NORTH ASHFORD",
    "NORTH COVE",
    "PG",
    "PLEASANT GARDENS",
    "PROVIDENCE",

    //townships
    "CROOKED CREEK",
    "DYSARTSVILLE",
    "MONTFORD COVE",
    "PLEASANT GARDENS",
    "WOODLAWN-SEVIER",
    "SUGAR HILL",
    
    // Buncombe County
    "ASHEVILLE",
    "BLACK MOUNTAIN",
    
    // Rutherford County
    "UNION MILLS",
    
    // Mitchell County
    "SPRUCE PINE"
  };
}
