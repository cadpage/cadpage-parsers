package net.anei.cadpage.parsers.FL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class FLManateeCountyParser extends FieldProgramParser {
  
  public FLManateeCountyParser() {
    super(CITY_CODES, "MANATEE COUNTY", "FL",
        "Location:ADDR/S? ( Inside_location:LOC! Event_type:CALL! Subtype:INFO! Map_grid:MAP! Time:TIME! BEAT:BOX " + 
                         "| Estimated_Address:PLACE? INSIDE_LOCATION:LOC? TYPE_CODE:CALL! INSIDE_LOCATION:LOC? SUB_TYPE:INFO TIME:TIME% MAP_GRID:MAP TIME:TIME ) END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "93001,777,888,@txt.voice.google.com,americanopportunityfla@gmail.com,2513877788";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern FROM_PREFIX_PTN = Pattern.compile("(?:\\*\\*\\[from \\d+\\]|FWD:) +");
  private static final Pattern EST_PREFIX_PTN = Pattern.compile("\\d+ : EST |EA : |\\d+ (?=Inside location:)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf("\n\n--\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = FROM_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    match = EST_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = "Location: " + body.substring(match.end());

    body = body.replaceAll("\\s+", " ").replace("SUB TYPE:", " SUB TYPE:");
    body = body.replace("Estimated Address ", "Estimated Address:");
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.length() == 0) {
      if (data.strPlace.length() == 0) return false;
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("LOC")) return new MyLocField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    return super.getField(name);
  }
  
  private static final Pattern ADDR_DELIM_PTN = Pattern.compile(" *:[ @]*");
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      String[] parts = ADDR_DELIM_PTN.split(field);
      field = parts[0];
      for (int ndx = 1; ndx < parts.length; ndx++) {
        data.strPlace = append(data.strPlace, " - ", parts[ndx]);
      }
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      field = stripFieldEnd(field,  " EA");
      Parser p = new Parser(field);
      String apt = p.getLastOptional(',');
      if (apt.length() == 0) apt = p.getLastOptional(';');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern PLACE_APT_PTN = Pattern.compile("UNIT *([^:]+): *(.*)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      
      // Remove leading EST
      if (field.startsWith("EST ")) field = field.substring(4).trim();
      if (field.equals("EST")) return;
      if (field.startsWith("@")) field = field.substring(1).trim();
      
      // If there was no location field, use this
      if (data.strAddress.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
      
      // Otherwise this is a normal place field
      else {
        Matcher match = PLACE_APT_PTN.matcher(field);
        if (match.matches()) {
          data.strApt = match.group(1).trim();
          field = match.group(2).trim();
        }
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }

  private static final Pattern LOC_APT_PTN = Pattern.compile("(?:APT|RM|SUITE|LOT|#|UNIT) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern LOC_PLACE_PTN = Pattern.compile("\\*+ *(.*)");
  private class MyLocField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      String place = null;
      String apt = null;
      do {
        Matcher match = LOC_APT_PTN.matcher(field);
        if (match.matches()) {
          apt = match.group(1);
          if (apt == null) apt = field;
          break;
        }
        
        match = LOC_PLACE_PTN.matcher(field);
        if (match.matches()) {
          place = match.group(1);
          break;
        }
        
        int chk = NUMERIC.matcher(field).find() ? 20 : 2;
        if (field.length() <= chk) {
          apt = field;
        } else {
          place = field;
        }
      } while (false);
      
      if (apt != null) {
        if (!data.strApt.contains(apt)) data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, " - ", place);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }
  
  private static final Pattern ST_62_PTN = Pattern.compile("\\b(?:ST|SR|FL) +62\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String address, String city, boolean cross) {
    return ST_62_PTN.matcher(address).replaceAll("WAUCHULA RD");
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "2303 1ST ST E",                        "+27.479214,-82.562549"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AM",   "Anna Maria",
      "BB",   "Bradenton Beach",
      "BR",   "Bradenton",
      "CZ",   "Cortez",
      "DE",   "Desoto County",
      "EL",   "Ellenton",
      "HA",   "Hardee County",
      "HB",   "Holmes Beach",
      "HC",   "Hillsborough County",
      "LBK",  "Longboat Key",
      "LK",   "Longboat Key",
      "MY",   "Myakka City",
      "PA",   "Palmetto",
      "PI",   "Pinellas County",
      "PR",   "Parrish",
      "SARA", "Sarasota County",
      "SM",   "Sarasota",
      "UB",   "Bradenton",
      "UP",   "Palmetto",
      "WM",   "Wimauma"
  });
}
