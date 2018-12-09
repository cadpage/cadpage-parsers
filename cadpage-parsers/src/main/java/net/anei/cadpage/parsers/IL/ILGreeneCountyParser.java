package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class ILGreeneCountyParser extends DispatchA29Parser {
 
  public   ILGreeneCountyParser() {
    super(CITY_LIST, "GREENE COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@jacksonvilleil.com.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity.toUpperCase(), MISSPELLED_CITY_TABLE);
    return true;
  }

  private static final Pattern STATE_PTN = Pattern.compile("\\bSTATE +(?:RTE +)?(\\d+)\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = super.adjustMapAddress(addr);
    addr = STATE_PTN.matcher(addr).replaceAll("IL $1");
    return addr;
  }
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "CAROLLTON",      "CARROLLTON",
      "CARROLTON",      "CARROLLTON",
      "HWITE HALL",     "WHITE HALL"
  });


  private static final String[] CITY_LIST = new String[]{
      
  // CITIES

      "CARROLLTON",
      "CAROLLTON",  // Misspelled
      "CARROLTON",  // Misspelled
      "GREENFIELD",
      "ROODHOUSE",
      "WHITE HALL",

  // VILLAGES

      "ELDRED",
      "HILLVIEW",
      "KANE",
      "ROCKBRIDGE",
      "WILMINGTON",

  // UNINCORPORATED COMMUNITIES

      "BARROW",
      "BELLTOWN",
      "BERDAN",
      "DRAKE",
      "EAST HARDIN",
      "LAKE CENTRALIA",
      "OLD KANE",

 // TOWNSHIPS

      "ATHENSVILLE",
      "BLUFFDALE",
      "CARROLLTON",
      "KANE",
      "LINDER",
      "PATTERSON",
      "ROCKBRIDGE",
      "ROODHOUSE",
      "RUBICON",
      "WALKERVILLE",
      "WHITE HALL",
      "HWITE HALL",   // Misspelled
      "WOODVILLE",
      "WRIGHTS",
      
      // Calhoun County
      "KAMPSVILLE",
      
      // Jersey County
      "JERSEYVILLE",
      
      // ?????
      "ELDRIDGE"
  };
}
