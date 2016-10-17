package net.anei.cadpage.parsers.IN;


import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

/**
 * Starke County, IN
 */
public class INStarkeCountyParser extends DispatchA29Parser {
  
  private static final Pattern ADDR_HALF_PTN = Pattern.compile("[ /](\\d+ 1/2), *");

  public INStarkeCountyParser() {
    super(CITY_LIST, "STARKE COUNTY", "IN");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "HAMLET INDUSTRIAL",
        "LONG LANE",
        "RANNELLS E",
        "ST LOUIS"
    );
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@co.starke.in.us,messaging@iamresponding.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // The put commas following 1/2 street numbers, which really messes up the address parsing
    body = ADDR_HALF_PTN.matcher(body).replaceFirst(" $1 ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = EXWY_PTN.matcher(addr).replaceAll("").trim();
    addr = DIR_OF_PTN.matcher(addr).replaceAll(" & ");
    return addr;
  }
  private static final Pattern EXWY_PTN = Pattern.compile("\\bEXWY\\b");
  private static final Pattern DIR_OF_PTN = Pattern.compile("[/ ]+((?:N|S|E|W|NO|SO|EA|WE|NORTH|SOUTH|EAST|WEST) OF)[/ ]+");

  private static final CodeSet CALL_LIST = new CodeSet(
      "CO2 DETECTOR ALARM",
      "FOLLOW-UP INVESTIGATION",
      "HARRASSMENT",
      "RESCUE/AMBULANCE",
      "FIRE-BRUSH",
      "FIRE-GENERAL",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "FIRE ALARM",
      "MISSING PERSON",
      "RAPE",
      "SUSPICIOUS CIRCUMSTANCES",
      "TRAFFIC ACCIDENT-INJURIES",
      "TRAFFIC ACCIDENT-PROPERTY DAMAGE",
      "BURN COMPLAINT",
      "911 CELLPHONE CALL",
      "TRAFFIC COMPLAINT",
      "911 HANG UP/ MISDIAL",
      "GAS ODOR",
      "GAS SPILL",
      "MEDICAL",
      "MEDICAL TRANSFER",
      "CONTROL BURN",
      "DECEASED PERSON",
      "TRAFFIC ACCIDENT SLIDE OFF NO DAMAGE",
      "TRAFFIC ACCIDENT UNKNOWN",
      "FIRE PAGER MESSAGE"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities and towns
    "HAMLET",
    "KNOX",
    "NORTH JUDSON",
 
    // Unincorporated towns
    "BASS LAKE",
    "ENGLISH LAKE",
    "GROVERTOWN",
    "KOONTZ LAKE",
    "ORA",
    "SAN PIERRE",
 
    // Townships
    "CALIFORNIA TWP",
    "CENTER TWP",
    "DAVIS TWP",
    "JACKSON TWP",
    "NORTH BEND TWP",
    "OREGON TWP",
    "RAILROAD TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",
    
    // Jasper County
    "WHEATFIELD",
    
    // Joseph County
    "WALKERTON",
    
    // LaPorte County
    "LAPORTE",
    
    // Marshall County
    "CULVER",
    "PLYMOUTH",
    
    // Pulaski County
    "MONTEREY"
      
  };
}
