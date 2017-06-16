package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAGalaxBParser extends DispatchSouthernParser {
  
  public VAGalaxBParser() {
    super(CITY_LIST, "GALAX", "VA", DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_UNIT1 | DSFLG_ID | DSFLG_TIME);
  }
  
  private static final String[] CITY_LIST = new String[]{
      "CANA",
      "ELK CREEK",
      "FRIES",
      "GALAX",
      "HILLSVILLE",
      "INDEPENDENCE",
      "LAMBSBURG",
      "LAUREL FORK",
      "TROUTDALE",
      "WINSTON",
      "WOODLAWN"
  };

}
