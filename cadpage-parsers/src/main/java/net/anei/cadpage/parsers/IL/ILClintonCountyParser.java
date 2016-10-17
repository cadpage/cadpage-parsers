package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class ILClintonCountyParser extends DispatchA29Parser {
 
  public   ILClintonCountyParser() {
    super(CITY_LIST, "CLINTON COUNTY", "IL");
    setupProtectedNames("ROD AND GUN");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@clintonco.illinois.gov>";
  }
  
  private static final String[] CITY_LIST = new String[]{
      //Cities

      "BREESE",
      "CARLYLE",
      "CENTRALIA",
      "TRENTON",

      //Villages

      "ALBERS",
      "AVISTON",
      "BARTELSO",
      "BECKEMEYER",
      "DAMIANSVILLE",
      "GERMANTOWN",
      "HOFFMAN",
      "HUEY",
      "KEYESPORT",
      "NEW BADEN",
      "ST. ROSE",

      //Townships

      "BREESE",
      "BROOKSIDE",
      "CARLYLE",
      "CLEMENT",
      "EAST FORK",
      "GERMANTOWN",
      "IRISHTOWN",
      "LAKE",
      "LOOKING GLASS",
      "MERIDIAN",
      "SAINT ROSE",
      "SANTA FE",
      "SUGAR CREEK",
      "WADE",
      "WHEATFIELD"


  
  };
}
