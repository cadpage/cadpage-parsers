package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchBParser;


/**
 * Salem County, NJ
 */
public class NJSalemCountyAParser extends DispatchBParser {
  
  private static final String[] CITY_LIST = new String[]{
    "ALLOWAY TWP",
    "ALLOWAY",
    "CARNEYS POINT TWP",
    "CARNEYS POINT",
    "ELMER",
    "ELSINBORO TWP",
    "LOWER ALLOWAYS CREEK TWP",
    "MANNINGTON TWP",
    "OLDMANS TWP",
    "PENNS GROVE",
    "PENNSVILLE TWP",
    "PENNSVILLE",
    "PILESGROVE TWP",
    "PITTSGROVE TWP",
    "OLIVET",
    "QUINTON TWP",
    "SALEM CITY",
    "UPPER PITTSGROVE TWP",
    "WOODSTOWN"
    
  };
  
  public NJSalemCountyAParser() {
    super(CITY_LIST, "SALEM COUNTY", "NJ");
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return body.startsWith("911-CENTER:");
  }
}
