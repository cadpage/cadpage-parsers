package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class ILWoodfordCountyParser extends DispatchA47Parser {
  
  public ILWoodfordCountyParser() {
    super("Woodford Comm", CITY_LIST, "WOODFORD COUNTY", "IL", "\\d{0,2}[A-Z]{1,2}\\d{1,2}|[A-Z]{1,3}FD");
  }
  
  @Override
  public String getFilter() {
    return "woodford911@mchsi.com,woodcom@woodford-county.org";
  }

  private static final String[] CITY_LIST =new String[]{
    
      //Cities
      
      "EL PASO",
      "EUREKA",
      "GERMANTOWN HILLS",
      "METAMORA",
      "MINONK",
      "ROANOKE",
      "WASHBURN",

      //Villages 

      "BAY VIEW GARDENS",
      "BENSON",
      "CONGERVILLE",
      "DEER CREEK",
      "GOODFIELD",
      "KAPPA",
      "PANOLA",
      "SECOR",
      "SPRING BAY",

      //Unincorporated communities

      "LOWPOINT",
      "OAK RIDGE",
      "SPIRES",
      "WOODFORD",
      
      // Marshall County
      "MARSHALL COUNTY"
  };
}
