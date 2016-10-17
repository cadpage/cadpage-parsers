package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;

/**
 * Fayette County, Pennsylvania
 */
public class PAFayetteCountyParser extends DispatchBParser {

  public PAFayetteCountyParser() {
    super(CITY_LIST, "FAYETTE COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "FAYETTE-911@fcema.org";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return body.startsWith("FAYETTE-911:");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Dispatch doesn't usually add a TWP to townships, but Google insists on it
    if (TOWNSHIPS.contains(data.strCity.toUpperCase())) data.strCity += " TWP";
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CONNELLSVILLE",
    "UNIONTOWN",

    "BELLE VERNON",
    "BROWNSVILLE",
    "DAWSON",
    "DUNBAR",
    "EVERSON",
    "FAIRCHANCE",
    "FAYETTE CITY",
    "MARKLEYSBURG",
    "MASONTOWN",
    "NEWELL",
    "OHIOPYLE",
    "PERRYOPOLIS",
    "POINT MARION",
    "SEVEN SPRINGS",
    "SMITHFIELD",
    "SOUTH CONNELLSVILLE",
    "VANDERBILT",
  
    "BROWNSVILLE TWP",
    "BULLSKIN",
    "CONNELLSVILLE TWP",
    "DUNBAR TWP",
    "FRANKLIN",
    "GEORGES",
    "GERMAN",
    "HENRY CLAY",
    "JEFFERSON",
    "LOWER TYRONE",
    "LUZERNE",
    "MENALLEN",
    "NICHOLSON",
    "NORTH UNION",
    "PERRY",
    "REDSTONE",
    "SALTLICK",
    "SOUTH UNION",
    "SPRINGFIELD",
    "SPRINGHILL",
    "STEWART",
    "UPPER TYRONE",
    "WASHINGTON",
    "WHARTON"
  };
  
  private static Set<String> TOWNSHIPS = new HashSet<String>(Arrays.asList(new String[]{
      "BULLSKIN",
      "FRANKLIN",
      "GEORGES",
      "GERMAN",
      "HENRY CLAY",
      "JEFFERSON",
      "LOWER TYRONE",
      "LUZERNE",
      "MENALLEN",
      "NICHOLSON",
      "NORTH UNION",
      "PERRY",
      "REDSTONE",
      "SALTLICK",
      "SOUTH UNION",
      "SPRINGFIELD",
      "SPRINGHILL",
      "STEWART",
      "UPPER TYRONE",
      "WASHINGTON",
      "WHARTON"
  })); 
}
