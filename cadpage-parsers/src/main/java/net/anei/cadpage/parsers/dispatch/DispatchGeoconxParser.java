package net.anei.cadpage.parsers.dispatch;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchGeoconxParser extends FieldProgramParser {
  
  /**
   *  Flag indicating an empty subject is acceptable
   */
  public static final int GCX_FLG_EMPTY_SUBJECT_OK = 1;
  
  /**
   * Flag indicating a name or place and phone number may
   * precede the address field
   */
  public static final int GCX_FLG_NAME_PHONE = 2;
  
  /**
   * Flag indicating address field mah contain a leading place name
   */
  public static final int GCX_FLG_LEAD_PLACE = 4;
  
  /**
   * Flag indicating address field may contain a trailing place name
   * instead of a map field
   */
  public static final int GCX_FLG_TRAIL_PLACE = 8;
  
  private Set<String> citySet;
  private boolean noSubject; 
  private boolean nameField;
  private boolean leadPlace;
  private boolean trailPlace;
  
  public DispatchGeoconxParser(String defCity, String defState) {
    this(null, defCity, defState, 0);
  }
  
  public DispatchGeoconxParser(Set<String> citySet, String defCity, String defState) {
    this(citySet, defCity, defState, 0);
  }
  
  public DispatchGeoconxParser(String defCity, String defState, int flags) {
    this(null, defCity, defState, flags);
  }
  
  public DispatchGeoconxParser(Set<String> citySet, String defCity, String defState, int flags) {
    super(defCity, defState,
          "( CFS:ID! MSG:CALL! PLACE CITY ADDR! " +
          "| " + ((flags & GCX_FLG_NAME_PHONE) == 0
                  ? "CALL ADDR "
                  : "CALL ( EMPTY ADDR | NAMEPH/Z ADDR | NAMEPH? ADDR ) ") +
          ") INFO+");
    this.citySet = citySet;
    this.noSubject = (flags & GCX_FLG_EMPTY_SUBJECT_OK) != 0;
    this.nameField = (flags & GCX_FLG_NAME_PHONE) != 0;
    this.leadPlace = (flags & GCX_FLG_LEAD_PLACE) != 0;
    this.trailPlace = (flags & GCX_FLG_TRAIL_PLACE) != 0;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String flds[] = body.split("\n");
    if (!subject.equals("E911") &&
        !subject.equals("E911-Page") && 
        !subject.equals("E911 Incident Auto-Page")) {
      if (!noSubject || flds.length < 3) return false;
    }
    return parseFields(flds, data);
  }

  
  @Override
  public Field getField(String name) {
    if (name.equals("NAMEPH")) return new NamePhoneField();
    if (name.equals("ADDR")) return new BaseAddressField();
    return super.getField(name);
  }
  
  private static final Pattern PHONE_PTN = Pattern.compile("\\b\\d{10}$");
  private class NamePhoneField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = PHONE_PTN.matcher(field);
      if (!match.find()) return false;
      data.strPhone = match.group();
      field = field.substring(0,match.start()).trim();

      if (checkPlace(field)) {
        data.strPlace = field;
      } else {
        data.strName = field;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = PHONE_PTN.matcher(field);
      if (match.find()) {
        data.strPhone = match.group();
        field = field.substring(0,match.start()).trim();
      }
      if (checkPlace(field)) {
        data.strPlace = field;
      } else {
        data.strName = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PLACE PHONE";
    }
  }
  
  private static final Pattern CITY_PTN = Pattern.compile(", *([A-Z]+ ?[A-Z]*)$", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile(", *(?:APT +)?((?:\\w+ *)?[-\\w]+)$");
  private static final Pattern AT_PTN = Pattern.compile("(?:@| AT )(?! *M[MP]\\b)", Pattern.CASE_INSENSITIVE);
  private class BaseAddressField extends AddressField {
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isAddress(field)) return false;
      parse(field, data, true);
      return true;
    }
    
    
    @Override
    public void parse(String field, Data data) {
      parse(field, data, false);
    }
    
    private void parse(String field, Data data, boolean noNameHere) {
      field = field.replace("&&", "&");
      
      Matcher match = CITY_PTN.matcher(field);
      if (match.find()) {
        data.strCity = match.group(1).trim(); 
        field = field.substring(0,match.start()).trim();
      }
      
      int pt = field.indexOf("- ");
      if (pt >= 0) {
        String  map = field.substring(pt+2).trim();
        if (trailPlace) data.strPlace = map;
        else data.strMap = map;
        field = field.substring(0,pt).trim();
      }
      
      match = APT_PTN.matcher(field);
      if (match.find()) {
        data.strApt = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      
      field = AT_PTN.matcher(field).replaceAll("&");
      
      
      StartType st = ((!nameField && !leadPlace) || noNameHere || data.strName.length() > 0 || data.strPlace.length() > 0 
                      ? StartType.START_ADDR : StartType.START_PLACE);
      parseAddress(st, FLAG_IGNORE_AT | FLAG_ANCHOR_END, field, data);
      if (st == StartType.START_PLACE){ 
        if (data.strAddress.length() == 0) {
          data.strAddress = data.strPlace;
          data.strPlace = "";
        } else if (!leadPlace && !checkPlace(data.strPlace)){
          data.strName = data.strPlace;
          data.strPlace = "";
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE NAME ADDR APT MAP CITY";
    }
  }
  
  /**
   * method invoked by address parser to determine if field is an address
   * or not.  Moved here so it can be overridden by subclasses that need
   * to add additional conditions
   * @param field
   * @return
   */
  protected boolean isAddress(String field) {

    // If we find a comma, assume an address.  Unless we have a city list
    // in which case confirm valid city.  If not, reject immediately
    int pt = field.lastIndexOf(',');
    if (pt >= 0) {
      String sCity = field.substring(pt+1).trim().toUpperCase();
      if (citySet == null) return true;
      if (citySet.contains(sCity)) return true;;
      return false;
    }
    
    // Anything containing an @ is OK.
    if (field.indexOf('@') >= 0) return true;
    
    // Does it look like it starts with a street number
    // that is not followed by YO
    if (STREET_NO.matcher(field).matches()) return true;
    
    // OK, check for a legitimate street name
    if (isValidAddress(field)) return true;
    
    // well, we certainly tried...
    return false;
  }
  private static final Pattern STREET_NO = Pattern.compile("\\d+ +(?!YO|CAR).*", Pattern.CASE_INSENSITIVE);
}
