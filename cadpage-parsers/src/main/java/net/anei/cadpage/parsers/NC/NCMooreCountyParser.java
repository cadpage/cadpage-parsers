package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;


public class NCMooreCountyParser extends DispatchSouthernPlusParser {
  
  public NCMooreCountyParser() {
    super(CITY_LIST, "MOORE COUNTY", "NC",
          DSFLG_ADDR|DSFLG_ADDR_NO_IMPLIED_APT|DSFLG_OPT_CODE|DSFLG_OPT_ID|DSFLG_TIME);
    setCallCodePtn("[MF]\\d+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch911@moorecountync.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace('@', '&');
    return super.parseMsg(subject, body, data);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ABERDEEN",
    "CAMERON",
    "CARTHAGE",
    "FOXFIRE",
    "PINEBLUFF",
    "PINE BLUFF",
    "PINEHURST",
    "ROBBINS",
    "SEVEN LAKES",
    "SOUTHERN PINES",
    "TAYLORTOWN",
    "VASS",
    "WEST END",
    "WHISPERING PINES",
    
    "EAGLE SPRINGS",
    "HIGHFALLS",
    "JACKSON SPRINGS",
    "WEST END",
    
    "CARTHAGE TWP",
    "BENSALEM TWP",
    "SHEFFIELDS TWP",
    "RITTER TWP",
    "DEEP RIVER TWP",
    "GREENWOOD TWP",
    "MCNEILL TWP",
    "SANDHILL TWP",
    "MINERAL SPRINGS TWP",
    "LITTLE RIVER TWP",
    
    "FT BRAGG",
    
    // Lee County
    "SANFORD",
    
    // Montgomery County
    "BISCOE",
    "STAR",
    
    // Randolph County
    "SEAGROVE"
    
    
  };
}
