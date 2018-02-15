package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.Message;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

/**
 * Gloucester County, NJ (version A)
 */
public class NJGloucesterCountyAParser extends DispatchProphoenixParser {
  
  private static final Pattern FROM_ADDR_PTN = Pattern.compile("GC RSAN #(\\d+)");
  private static final Pattern SPECIAL_TRAIL_PTN = Pattern.compile(";.{2}$");
  
  private String fromAddress;
  
  public NJGloucesterCountyAParser() {
    super(CITY_CODES, CITY_LIST, "GLOUCESTER COUNTY", "NJ");
  }
 
  @Override
  public String getFilter() {
    return "gccad@co.gloucester.nj.us,777,@private.gloucesteralert.com,12101,411912";
  }
  
  @Override
  protected Data parseMsg(Message msg, int parseFlags) {
    fromAddress = msg.getFromAddress();
    return super.parseMsg(msg, parseFlags);
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // THere are some weird text options presumably introduced by forwarding services
    body = stripFieldStart(body, "Fwd:");
    body = stripFieldEnd(body, "=");
    body = stripFieldEnd(body, " STOP");
    
    Matcher match = FROM_ADDR_PTN.matcher(fromAddress);
    if (match.matches()) {
      body = "GC ALERT (#" + match.group(1) + ") " + body;
      match = SPECIAL_TRAIL_PTN.matcher(body);
      if (match.find()) body = body.substring(0,match.start()).trim();
    }
    
    if (!body.contains("\n")) body = body.replace("} ", "}\n");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("ROWAN")) city = "GLASSBORO";
    return city;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "A", "CLAYTON",
      "B", "DEPTFORD TWP",
      "C", "EAST GREENWICH TWP",
      "CF","WEST DEPFORD TWP",
      "D", "ELK TWP",
      "E", "FRANKLIN TWP",
      "F", "GLASSBORO",
      "G", "GIBBSTOWN",
      "H", "HARRISON TWP",
      "I", "LOGAN TWP",
      "J", "MANTUA TWP",
      "K", "MONROE TWP",
      "L", "NATIONAL PARK",
      "M", "NEWFIELD",
      "N", "PAULSBORO",
      "O", "PITMAN",
      "P", "SOUTH HARRISON TWP",
      "Q", "SWEDESBORO",
      "R", "WASHINGTON TWP",
      "S", "WENONAH",
      "T", "WEST DEPTFORD TWP",
      "Q", "SWEDESBORO",
      "U", "WESTVILLE",
      "V", "WOODBURY",
      "W", "WOODBURY HEIGHTS",
      "X", "WOOLWICH TWP",
      "Y", "ROWAN",
      "04", "BUENA",
      "05", "BUENA VISTA TWP",
      "07", "EGG HARBOR CITY",
      "09", "ESTELL MANOR",
      "10", "FOLSOM",
      "23", "WEYMOUTH TWP",
      "AC", "ATLANTIC COUNTY",
      "CU", "CUMBERLAND COUNTY",
      "SA", "SALEM COUNTY"
  });
  
  private static final String[] CITY_LIST = new String[]{
    
    // Boroughs
    "CLAYTON",
    "GLASSBORO",
    "NATIONAL PARK",
    "NEWFIELD",
    "PAULSBORO",
    "PITMAN",
    "SWEDESBORO",
    "WENONAH",
    "WESTVILLE",
    "WOODBURY",
    "WOODBURY HEIGHTS",
    
    // Townships
    "DEPTFORD TWP",
    "EAST GREENWICH TWP",
    "ELK TWP",
    "FRANKLIN TWP",
    "GREENWICH TWP",
    "HARRISON TWP",
    "LOGAN TWP",
    "MANTUA TWP",
    "MONROE TWP",
    "SOUTH HARRISON TWP",
    "WASHINGTON TWP",
    "WEST DEPTFORD TWP",
    "WOOLWICH TWP",
    
    // Communities
    "ALMONESSON",
    "AURA",
    "BARNSBORO",
    "BECKETT",
    "BILLINGSPORT",
    "BRIDGEPORT",
    "CROSS KEYS",
    "EWAN",
    "FRANKLINVILLE",
    "GENLOCH",
    "GIBBSTOWN",
    "GOOD INTENT",
    "GREENFIELDS VILLAGE",
    "HARDINGVILLE",
    "HARRISONVILLE",
    "HURFFVILLE",
    "MALAGA",
    "MICKLETON",
    "MOUNT ROYAL",
    "MULLICA HILL",
    "NEW BROOKLYN",
    "OAK VALLEY",
    "RED BANK",
    "REPAUPO",
    "RICHWOOD",
    "RICHWOOD",
    "SEWELL",
    "THOROFARE",
    "TURNERSVILLE",
    "VICTORY LAKES",
    "WILLIAMSTOWN",
    "WOLFERT",
    
    // Atlantic County
    "BUENA",
    "BUENA VISTA TWP",
    "EGG HARBOR CITY",
    "ESTELL MANOR",
    "FOLSOM",
    "MARYS LANDING",
    "WEYMOUTH TWP"
  };

}
