package net.anei.cadpage.parsers.MT;


import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class MTHillCountyParser extends DispatchA32Parser {
  
  public MTHillCountyParser() {
    super(CITY_LIST, "HILL COUNTY","MT");
  }
  
  @Override
  public String getFilter() {
    return "havrepolicefire@gmail.com";
  }
  
  private static final String[] CITY_LIST = new String[]{

  //cities
      "HAVRE",
      "HINGHAM",

  //Census-designated places

      "AZURE",
      "BEAVER CREEK",
      "BOX ELDER",
      "GILDFORD",
      "HAVRE NORTH",
      "HERRON",
      "INVERNESS",
      "KREMLIN",
      "PARKER SCHOOL",
      "ROCKY BOY WEST",
      "ROCKY BOY'S AGENCY",
      "RUDYARD",
      "SADDLE BUTTE",
      "ST. PIERRE",
      "SANGREY",
      "WEST HAVRE",

  //Unincorporated communities

      "FOX CROSSING",
      "LAREDO"

  };
}
