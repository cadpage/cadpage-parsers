package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Page County, VA
 */
public class VAPageCountyBParser extends DispatchSouthernParser {
    
  public VAPageCountyBParser() {
    super(CITY_LIST, "PAGE COUNTY", "VA", 
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_OPT_X | DSFLG_OPT_NAME | DSFLG_OPT_PHONE | DSFLG_OPT_CODE | DSFLG_UNIT1 | DSFLG_ID );
  }
  
  @Override
  public String getFilter() {
    return "ecc@pagesheriff.com";
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      //Towns

      "LURAY",
      "SHENANDOAH",
      "STANLEY",

      //Unincorporated communities

      "ALMA",
      "BATTLE CREEK",
      "BLAINESVILLE",
      "CAVETOWN",
      "COMERTOWN",
      "COMPTON",
      "FLEEBURG",
      "FURNACE",
      "HONEYVILLE",
      "IDA",
      "INGHAM",
      "LEAKSVILLE",
      "MARKSVILLE",
      "NEWPORT",
      "OVERALL",
      "RILEYVILLE",
      "STONY MAN",
      "GROVE HILL"

  };
}