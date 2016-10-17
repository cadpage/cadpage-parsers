
package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Marion County, AL
 */
public class ALMarionCountyParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile(">>> ?MARION COUNTY 911 (?:NOTIFICATION|UPDATE) ?<<< +={20,} +");

  public ALMarionCountyParser() {
    super(CITY_LIST, "MARION COUNTY", "AL",
          "Unit:UNIT? Event:ID! Location:ADDR/S! Event_Type:CALL! Event_SubType:CALL/SDS! Complainant_Name:NAME! Complainant_Phone:PHONE INFO+");
  }
    
  @Override
  public String getFilter() {
    return "911call@marion911.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    
    int pt = body.indexOf("  ----------");
    if (pt >= 0) body = body.substring(0,pt);
    
    body = body.replace(" Changed to:", ":");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Nearest:");
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        parseAddress(field.substring(0,pt).trim(), data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
      
      // Check for duplicated city
      if (data.strCity.length() > 0) {
        data.strAddress = stripFieldEnd(data.strAddress, " " + data.strCity);
      }
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "GUIN",
    "HALEYVILLE",
    "HAMILTON",
    "WINFIELD",

    // Towns
    "BEAR CREEK",
    "BRILLIANT",
    "GLEN ALLEN",
    "GU-WIN",
    "HACKLEBURG",
    "TWIN",
    "YAMPERTOWN",

    // Unincorporated communities
    "BARNESVILLE",
    "BEXAR",
    "PIGEYE",
    "PULL TIGHT",
    "SOUTH HALEYVILLE",

    // Ghost town
    "PIKEVILLE",
    
    //missed places
    "SHOTTSVILLE",
    "SHILOH",
    "BYRD", 
    "PEA RIDGE"
  };
}
