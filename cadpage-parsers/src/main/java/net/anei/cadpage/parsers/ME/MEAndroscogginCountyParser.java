package net.anei.cadpage.parsers.ME;


import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class MEAndroscogginCountyParser extends DispatchA32Parser {
  
  public MEAndroscogginCountyParser() {
    super(CITY_LIST, "ANDROSCOGGIN COUNTY","ME");
  }
  
  @Override
  public String getFilter() {
    return "lisbondispatch@lisbonpd.com";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "AUBURN",
    "DURHAM",
    "GREENE",
    "LEEDS",
    "LEWISTON",
    "LISBON",
    "LIVERMORE",
    "LIVERMORE FALLS",
    "MECHANIC FALLS",
    "MINOT",
    "POLAND",
    "SABATTUS",
    "TURNER",
    "WALES"
  };
}
