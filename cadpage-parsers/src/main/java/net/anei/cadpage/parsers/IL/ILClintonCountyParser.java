package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class ILClintonCountyParser extends DispatchA29Parser {
 
  public   ILClintonCountyParser() {
    super(CITY_LIST, "CLINTON COUNTY", "IL");
    setupCities(MISSPELLED_CITIES);
    setupProtectedNames("ROD AND GUN");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@clintonco.illinois.gov>";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    return true;
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
      "ST ROSE",

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
      "WHEATFIELD",
      
      // Unincorporated
      "BOULDER",
      "SHATTUC"
  };
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "GERMANTOWM",     "GERMANTOWN"
  });
}
