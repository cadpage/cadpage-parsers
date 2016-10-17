package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXSeguinParser extends DispatchA18Parser {
  
  public TXSeguinParser() {
    super(CITY_LIST, "GUADALUPE COUNTY","TX");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  @Override
  public String getFilter() {
    return "crimespaging@seguintexas.gov";
  }
  
  private static String[] CITY_LIST = new String[]{
    "CIBOLO",
    "GERONIMO",
    "KINGSBURY",
    "MARION",
    "MCQUEENEY",
    "NEW BERLIN",
    "NEW BRAUNFELS",
    "NORTHCLIFF",
    "REDWOOD",
    "SANTA CLARA",
    "SCHERTZ",
    "SELMA",
    "SEGUIN",
    "STAPLES",
    "ZUEHL"
  };
}
