package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Wythe County, VA
 */
public class VAWytheCountyParser extends DispatchDAPROParser {
  
  public VAWytheCountyParser() {
    super(CITY_CODE_TABLE, "WYTHE COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@wytheco.org";
  }
   
  private static final CodeSet CALL_SET = new CodeSet(
    "ACCIDENT NONREPORTABLE",
    "ACCIDENT REPORTABLE",
    "ASSIST OTHER AGENCY",
    "DIABETIC",
    "FIRE ALARM",
    "GENERAL ILLNESS",
    "VEHICLE FIRE"
    
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "MAX", "MAX MEADOWS",
        "WYT", "WYTHEVILLE"
    });
}