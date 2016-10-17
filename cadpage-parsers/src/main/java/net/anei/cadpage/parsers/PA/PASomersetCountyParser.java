package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;



public class PASomersetCountyParser extends DispatchBParser {
  
  private static final Pattern MASTER = Pattern.compile("Code: ([^\n]*)\nCode Detail: .*\n\nUnformatted Message: 9-1-1 CENTER:(.*)");

  public PASomersetCountyParser() {
    super(CITY_LIST, "SOMERSET COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "alert@ecm2.com,911CENTER@co.somerset.pa.us";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    do {
      Matcher match = MASTER.matcher(body);
      if (match.matches()) {
        body = match.group(1) + " @ " + match.group(2);
        break;
      }
      
      if (body.startsWith("911CENTER:") && subject.length() > 0) {
        body = subject + " " + body.substring(10);
        break;
      }
      
      if (subject.contains(">")) {
        body = subject + " @ " + body;
        break;
      }
      return false;
    } while (false);
    
    body = body.replace(" AT ", " & ");
    return super.parseMsg(body, data);
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
    "DAVIDSVILLE",
    "FRIEDENS",
    "JEROME",
    // Unincorporated communities
    "JENNERS",
    "SPRINGS",
  };
}
