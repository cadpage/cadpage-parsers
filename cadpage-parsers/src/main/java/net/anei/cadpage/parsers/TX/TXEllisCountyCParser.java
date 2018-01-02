package net.anei.cadpage.parsers.TX;


import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXEllisCountyCParser extends DispatchA18Parser {
  
  public TXEllisCountyCParser() {
    super(CITY_LIST, "ELLIS COUNTY","TX");
    for (String city : CITY_LIST) {
      setupCities(city + " TX", city + " TEXAS");
    }
  }

  @Override
  public String getFilter() {
    return "alert@waxahachiepd.org";
  }

  private static String[] CITY_LIST = new String[]{

      //cities

      "BARDWELL",
      "CEDAR HILL",
      "ENNIS",
      "FERRIS",
      "GLENN HEIGHTS",
      "GRAND PRAIRIE",
      "MANSFIELD",
      "MAYPEARL",
      "MIDLOTHIAN",
      "OAK LEAF",
      "OVILLA",
      "PECAN HILL",
      "RED OAK",
      "WAXAHACHIE",

      //towns
      
      "ALMA",
      "GARRETT",
      "ITALY",
      "MILFORD",
      "PALMER",
      "VENUS",
      
      //Unincorporated community

      "AUBURN",
      "AVALON",
      "BRISTOL",
      "CRISP",
      "FORRESTON",
      "IKE",
      "INDIA",
      "RANKIN",
      "ROCKETT",
      "TELICO"
  };
}
