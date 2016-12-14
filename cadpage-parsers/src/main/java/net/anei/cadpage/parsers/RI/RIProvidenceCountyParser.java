package net.anei.cadpage.parsers.RI;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class RIProvidenceCountyParser extends DispatchA32Parser {
  
  public RIProvidenceCountyParser() {
    super(CITY_LIST, "PROVIDENCE COUNTY", "RI");
  }
  
  @Override
  public String getFilter() {
    return "woonsocketdispatch@gmail.com,dispatch@lincolnpoliceri.com";
  }
  
  private static final String[] CITY_LIST = new String[]{

      //Cities

      "CENTRAL FALLS",
      "CRANSTON",
      "EAST PROVIDENCE",
      "PAWTUCKET",
      "PROVIDENCE",
      "WOONSOCKET",

      //Towns

      "BURRILLVILLE",
      "CUMBERLAND",
      "FOSTER",
      "GLOCESTER",
      "JOHNSTON",
      "LINCOLN",
      "NORTH PROVIDENCE",
      "NORTH SMITHFIELD",
      "SCITUATE",
      "SMITHFIELD",

      //Villages

      "ALBION",
      "ARNOLD MILLS",
      "BRANCH VILLAGE",
      "CHEPACHET",
      "CLAYVILLE",
      "CUMBERLAND HILL",
      "ESMOND",
      "FORESTDALE",
      "FOSTER CENTER",
      "GEORGIAVILLE",
      "GLENDALE",
      "GREENVILLE",
      "HARMONY",
      "HARRISVILLE",
      "LIME ROCK",
      "LONSDALE",
      "MANVILLE",
      "OAKLAND",
      "PASCOAG",
      "PRIMROSE",
      "QUINNVILLE",
      "RIVERSIDE",
      "RUMFORD",
      "SAYLESVILLE",
      "SMITHVILLE-NORTH SCITUATE",
      "SLATERSVILLE",
      "UNION VILLAGE",
      "VALLEY FALLS",
      "WATERFORD"
  };

}
