package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;


public class VAPatrickCountyParser extends DispatchSouthernPlusParser {
  
  public VAPatrickCountyParser() {
    super(CITY_LIST, "PATRICK COUNTY", "VA", DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_X | DSFLG_NAME | DSFLG_PHONE |
        DSFLG_CODE | DSFLG_ID | DSFLG_TIME);
  }

  private static final String[] CITY_LIST = new String[]{

      //TOWNS
      "STUART",

      //CENSUS-DESIGNATED PLACE

      "PATRICK SPRINGS",

      //OTHER UNINCORPORATED COMMUNITIES

      "ARARAT",
      "CLAUDVILLE",
      "CRITZ",
      "FAIRYSTONE",
      "MAYBERRY",
      "MEADOWS OF DAN",
      "PENNS STORE",
      "RUSSELL CREEK",
      "VESTA",
      "WOOLWINE"


  }; 
}
