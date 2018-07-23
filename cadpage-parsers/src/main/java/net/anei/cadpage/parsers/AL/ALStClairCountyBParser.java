package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * St Clair County, AL
 */
public class ALStClairCountyBParser extends DispatchSouthernParser {

  public ALStClairCountyBParser() {
    super(CITY_LIST, "ST CLAIR COUNTY", "AL", 
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_BAD_PLACE | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_ID | DSFLG_TIME);
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITY_TABLE);
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "DAVIS LAKE",    "SPRINGFIELD"
  });
 
  static final String[] CITY_LIST = new String[]{
    "ARGO",
    "ASHVILLE",
    "BRANCHVILLE",
    "COOL SPRINGS",
    "DAVIS LAKE",
    "LEEDS",
    "MARGARET",
    "MOODY",
    "ODENVILLE",
    "PELL CITY",
    "PINEDALE SHORES",
    "RAGLAND",
    "RIVERSIDE",
    "SPRINGIVLLE",   // Misspelled
    "SPRINGVILLE",
    "STEELE",
    "TRUSSVILLE",
    "VINCENT",

    "CROPWELL"

  };
  
  static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "SPRINGIVLLE",    "SPRINGVILLE"
  });
}
