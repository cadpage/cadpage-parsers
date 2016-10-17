package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class IAScottCountyParser extends SmartAddressParser {
  
  private static final Pattern SRC_PTN = Pattern.compile("^([A-Z]{4}) +");
  private static final Pattern TERMINATE_PTN = Pattern.compile(" +(?:E911 Info -|\\[)");
  
  public IAScottCountyParser() {
    super(CITY_LIST, "SCOTT COUNTY", "IA");
    setFieldList("SRC ADDR CITY CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "secc@scott.ia.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident")) return false;
    
    Matcher match = SRC_PTN.matcher(body);
    if (!match.find()) return false;
    data.strSource = match.group(1);
    body = body.substring(match.end());
    
    match = TERMINATE_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, body, data);
    if (data.strCity.length() == 0) return false;
    
    Parser p = new Parser(body);
    p.get(data.strCity);
    data.strCall = p.get("  ");
    data.strSupp = p.get();
    
    if (data.strCity.equalsIgnoreCase("SCOTT COUNTY")) data.strCity = "";
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    "SCOTT COUNTY",
    
    // Cities
    "BETTENDORF",
    "BLUE GRASS",
    "BUFFALO",
    "DAVENPORT",
    "DIXON",
    "DONAHUE",
    "DURANT",
    "ELDRIDGE",
    "LE CLAIRE",
    "LONG GROVE",
    "MAYSVILLE",
    "MCCAUSLAND",
    "NEW LIBERTY",
    "PANORAMA PARK",
    "PRINCETON",
    "RIVERDALE",
    "WALCOTT",

    // Unincorporated communities
    "BIG ROCK",
    "PARK VIEW",

    // Townships
    "LIBERTY TWP",
    "CLEONA TWP",
    "ALLENS GROVE TWP",
    "HICKORY GROVE TWP",
    "BLUE GRASS TWP",
    "BUFFALO TWP",
    "WINFIELD TWP",
    "SHERIDAN TWP",
    "DAVENPORT",
    "BUTLER TWP",
    "LINCOLN TWP",
    "PLEASANT VALLEY TWP",
    "PRINCETON TWP",
    "LE CLAIRE TWP"
  };
}
