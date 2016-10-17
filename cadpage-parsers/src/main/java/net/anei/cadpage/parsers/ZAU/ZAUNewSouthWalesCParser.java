package net.anei.cadpage.parsers.ZAU;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZAUNewSouthWalesCParser extends FieldProgramParser {
  
  public ZAUNewSouthWalesCParser() {
    super("", "NSW", CountryCode.AU, 
          "CALL:CALL! ADDR:ADDR! CROSS_ROAD:X? ( Latitude:GPS1! Longitude:GPS2! | Latitude_/_Longitude:GPS! | LAT:GPS1! LONG:GPS2 | ) RV:INFO? CITY:CITY! ID:ID! PHONE:PHONE! CALLER:NAME? UNIT:UNIT? ( FURTHER_INFO:INFO/N! RV:INFO/N | EESP:INFO/N? INFO:INFO/N! )");
  }
  
  @Override
  public String getFilter() {
    return "usgwhy@gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MARKER = Pattern.compile("[A-Z]*:(?=CALL:)");
  private static final Pattern DELIM = Pattern.compile("\\s*[;\n]\\s*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    
    body = stripFieldStart(body, ":");
    body = stripFieldEnd(body, ";");
    body = body.replace(": Latitude", "; Latitude");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
    }
  }
  
  private static final Pattern MAP_GPS_PTN = Pattern.compile("(.*), *([-+]?[.0-9]*, *[-+]?[.0-9]*)");
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(2).trim(), data);
        field = match.group(1).trim();
      }
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }

}
