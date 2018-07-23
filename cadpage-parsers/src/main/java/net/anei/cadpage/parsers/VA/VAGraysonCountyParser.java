package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAGraysonCountyParser extends DispatchSouthernParser {
  
  public VAGraysonCountyParser() {
    super(CITY_LIST, "GRAYSON COUNTY", "VA", 
          DSFLG_PROC_EMPTY_FLDS|DSFLG_ADDR|DSFLG_BAD_PLACE|DSFLG_X|DSFLG_NAME|DSFLG_PHONE|DSFLG_CODE|DSFLG_UNIT1|DSFLG_ID |DSFLG_TIME);
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Towns
      "FRIES",
      "INDEPENDENCE",
      "TROUTDALE",

      // Unincorporated communities
      "BAYWOOD",
      "CARSONVILLE",
      "COMERS ROCK",
      "ELK CREEK",
      "FAIRVIEW",
      "FLAT RIDGE",
      "GRANT",
      "MOUTH OF WILSON",
      "RUGBY",
      "VOLNEY",
      "WHITETOP",
      
      // Carroll County
      "HILLSVILLE"
  };
}
