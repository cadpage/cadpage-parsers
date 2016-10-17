package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Hancock County, IN
 */
public class INHancockCountyAParser extends FieldProgramParser {
  
  public INHancockCountyAParser() {
    super(CITY_LIST, "HANCOCK COUNTY", "IN",
           "CALL ( MUTADDR INFO | CALL+? ADDR/S5XP CITY? X/Z+? MAP ) UNIT! INFO+");
    setupSaintNames("MICHAELS");
    removeWords("CIRCLE");
  }
  
  @Override
  public String getFilter() {
    return "mplus@hancockcoingov.org,777";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Eliminate any INHancockCountyB (INShelbyCounty) messages
    if (subject.startsWith("Unit ") || subject.startsWith("Incident ")) return false;

    body = body.replace("\n", "");
    if (! parseFields(body.split("/"), data)) return false;
    if (data.strCity.equals("FORTVIL")) data.strCity = "FORTVILLE";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MUTADDR")) return new MutualAidAddressField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private static final Pattern MUTAID_PTN = Pattern.compile("(.*)-(.*) CO");
  private class MutualAidAddressField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MUTAID_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1).trim(), data);
      data.strCity = match.group(2).trim() + " COUNTY";
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  // Address class, special case if field after address starts with &
  // make it a cross road rather than an place name
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)[-/&] *(.*)");
  private static final Pattern BOX_PLACE_PTN = Pattern.compile("(R\\d+B\\d+[A-Z]?(?:-\\d+)?)\\b *(.*)");
  private class MyAddressField extends AddressField {
    
    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace(".", " ").trim().replaceAll("  +", " ");
      
      // Sometimes odd delimiter is used to identify city
      String city = null;
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        city = match.group(2);
        if (isCity(city)) {
          field = match.group(1).trim();
        } else {
          city = null;
        }
      }
      
      if (!super.checkParse(field, data)) {
        
        // OK, this wasn't recognized as an address.  Let's check the next field
        // to see if it looks like something that should follow an address
        String next = getRelativeField(+1);
        do {
          
          // Check for empty field
          if (next.length() == 0) break;
          
          // Or a map field
          if (MAP_PTN.matcher(next).matches()) break;
          
          // No go, this is not an address
          return false;
          
        } while (false);
        
        // If we found something there, parse this field as an address
        super.parse(field, data);
      }
      if (data.strPlace.startsWith("&")) {
        data.strCross = data.strPlace.substring(2).trim();
        data.strPlace = "";
      }
      match = BOX_PLACE_PTN.matcher(data.strPlace);
      if (match.matches()) {
        if (match.group(2).equals(data.strCity)) {
          data.strBox = match.group(1);
        } else {
          data.strBox = data.strPlace;
        }
        data.strPlace = "";
      }
      
      data.strPlace = stripFieldStart(data.strPlace, "-");
      
      if (city != null) data.strCity = city;
      data.strCity = convertCodes(data.strCity.toUpperCase(), MISSPELLED_CITY_TABLE);
      if (data.strCity.endsWith(" CO")) data.strCity += "UNTY"; 
      return true;
    }
    
    @Override 
    public String getFieldNames() {
      return "ADDR APT CITY BOX PLACE X";
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("\\d\\d?(?:00\\d\\d)?|[A-Z]{2}\\d+[NESW]?|");
  private class MyMapField extends MapField {
    public MyMapField() {
      setPattern(MAP_PTN, true);
    }
  }
  
  @Override
  public String postAdjustMapAddress(String addr) {
    return DIR_US_40_PTN.matcher(addr).replaceAll("$1");
  }
  private static final Pattern DIR_US_40_PTN = Pattern.compile("\\b[NSEW] +(US +40)\\b", Pattern.CASE_INSENSITIVE);
  
  private static final String[] CITY_LIST = new String[]{
    "FORTVIL",
    "FORTVILLE",
    "GREENFIELD",
    "MAXWELL",
    "MCCORDSVILLE",
    "NEW PALESTINE",
    "SHIRLEY",
    "SPRING LAKE",
    "WILKINSON",

    "BLUE RIVER TWP",
    "BRANDYWINE TWP",
    "BROWN TWP",
    "BUCK CREEK TWP",
    "CENTER TWP",
    "GREEN TWP",
    "JACKSON TWP",
    "SUGAR CREEK TWP",
    "VERNON TWP",
    
    "CHARLOTTESVILLE",
    "NEW PALESTINE",
    "WILLOW BRANCH",
    
    // Indianapolis
    "INDPLS",
    "INDIANAPOLIS",
    
    // Madison County
    "MADISON COUNTY",
    "MADISON CO",
    "INGALS",   // (Misspelled INGALLS)
    "INGALLS",
    "PENDLETON",
    
    // Marion County
    "MARION COUNTY",
    "MARION CO",
    "CUMBERLAND",
    
    // Shelby County
    "SHELBY COUNTY",
    "SHELBY CO",
    "SHELBY",
    "GWENVILLE",
    "GWYNNEVILLE",
    "FOUNTAINTOWN",
    "MORRISTOWN"
  };
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "INDPLS",     "INDIANAPOLIS",
      "INGALS",     "INGALLS"
  });
}
