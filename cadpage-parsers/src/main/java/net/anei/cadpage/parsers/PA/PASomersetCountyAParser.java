package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class PASomersetCountyAParser extends DispatchB3Parser {
  
  private static final Pattern MASTER = Pattern.compile("Code: ([^\n]*)\nCode Detail: .*\n\nUnformatted Message: 9-1-1 CENTER:(.*)");

  public PASomersetCountyAParser() {
    super(CITY_LIST, "SOMERSET COUNTY", "PA");
    setupCallList((CodeSet)null);
  }

  @Override
  public String getFilter() {
    return "alert@ecm2.com,911CENTER@co.somerset.pa.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MASTER.matcher(body);
    do {
      if (match.matches()) {
        body = match.group(1) + " @ " + match.group(2);
        break;
      }
      
      if (body.startsWith("911CENTER:")) break;
      
      if (subject.contains(">")) break;
      return false;
    } while(false);
    
    body = stripFieldStart(body, "911CENTER:");
    body = body.replace(" AT ", " & ");
    return super.parseMsg(subject, body, data);
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Boroughs
    "ADDISON",
    "BENSON",
    "BERLIN",
    "BOSWELL",
    "CALLIMONT",
    "CASSELMAN",
    "CENTRAL CITY",
    "CHAMPION",
    "CONFLUENCE",
    "GARRETT",
    "HOOVERSVILLE",
    "INDIAN LAKE",
    "JENNERSTOWN",
    "MEYERSDALE",
    "NEW BALTIMORE",
    "NEW CENTERVILLE",
    "PAINT",
    "ROCKWOOD",
    "SALISBURY",
    "SEVEN SPRINGS",
    "SHANKSVILLE",
    "SOMERSET",
    "STOYSTOWN",
    "URSINA",
    "WELLERSBURG",
    "WINDBER",
    
    // Townships
    "ADDISON TWP",
    "ALLEGHENY TWP",
    "BLACK TWP",
    "BROTHERSVALLEY TWP",
    "CONEMAUGH TWP",
    "ELK LICK TWP",
    "FAIRHOPE",
    "FAIRHOPE TWP",
    "GREENVILLE TWP",
    "JEFFERSON TWP",
    "JENNER TWP",
    "LARIMER TWP",
    "LINCOLN TWP",
    "LOWER TURKEYFOOT TWP",
    "MIDDLECREEK TWP",
    "MILFORD TWP",
    "NORTHAMPTON TWP",
    "OGLE TWP",
    "PAINT TWP",
    "QUEMAHONING TWP",
    "SHADE TWP",
    "SOMERSET TWP",
    "SOUTHAMPTON TWP",
    "STONYCREEK TWP",
    "SUMMIT TWP",
    "UPPER TURKEYFOOT TWP",
    
    // Census designated places
    "CAIRNBROOK",
    "DAVIDSVILLE",
    "EDIE",
    "FRIEDENS",
    "JEROME",
    
    // Unincorporated communities
    "ACOSTA",
    "DEAL",
    "FORT HILL",
    "GRAY",
    "HIDDEN VALLEY",
    "JENNERS",
    "MARKLETON",
    "QUECREEK",
    "SPRINGS",
    
    // Fayette County
    "CHAMPION",
    
    // Bedford County
    "SCHELLSBURG"
  };
}
