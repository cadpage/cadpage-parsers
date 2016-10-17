package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA26Parser;

/**
 * Madison County (Alexandria), IN
 */

public class INMadisonCountyAParser extends DispatchA26Parser {
  
  public INMadisonCountyAParser() {
    super(CITY_CODES, "MADISON COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "Mplus@madisoncty.com";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHESTERFIE", "CHESTERFIELD",
      "CHESTERFIE STATION 50", "CHESTERFIELD",
      "COUNTRY CL", "COUNTRY CLUB HEIGHTS",
      "MARKLEVILL", "MARKLEVILLE",
      "RIVER FORE", "RIVER FOREST",
      "WOODLAWN H", "WOODLAWN HEIGHTS",
      "ANDERS TWP", "ANDERSON TWP",
      "DUCK C TWP", "DUCK CREEK TWP",
      "FALL C TWP", "FALL CREEK TWP",
      "JACKSO TWP", "JACKSON TWP",
      "LAFAYE TWP", "LAFAYETTE TWP",
      "PIPE C TWP", "PIPE CREEK TWP",
      "RICHLA TWP", "RICHLAND TWP",
      "STONEY TWP", "STONEY CREEK TWP",
      "VAN BU TWP", "VAN BUREN TWP"
  });
}
