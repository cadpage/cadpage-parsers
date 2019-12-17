

package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Chattam County, NC
 */
public class NCChathamCountyAParser extends DispatchSouthernParser {

  public NCChathamCountyAParser() {
    super(CITY_LIST, "CHATHAM COUNTY", "NC", 
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_OPT_CODE|DSFLG_ID|DSFLG_TIME);
  }
  
  @Override
  public String getFilter() {
    return "@chathamnc.org";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CARY",
    "FEARRINGTON",
    "GOLDSTON",
    "PITTSBORO",
    "SILER CITY",
    "CHAPEL HILL",
    
    "BEAR CREEK",
    "BENNETT",
    "BONLEE",
    "BRICKHAVEN",
    "BYNUM",
    "CARBONTON",
    "CORINTH",
    "CRUTCHFIELD CROSSROADS",
    "GULF",
    "HAYWOOD",
    "MONCURE",
    "SILK HOPE",
    "WILSONVILLE",
    
    // Alamance County
    "GRAHAM",
    "SNOW CAMP",
    
    // Durham County
    "DURHAM",
    
    // Lee County
    "SANFORD",
    
    // Orange County
    "CARRBORO",
    
    // Randolph County
    "LIBERTY",
    "STALEY",
    
    // Wake County
    "APEX",
    "NEW HILL"
  };
}
