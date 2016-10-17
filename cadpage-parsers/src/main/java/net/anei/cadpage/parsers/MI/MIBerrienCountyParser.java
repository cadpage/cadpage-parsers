package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class MIBerrienCountyParser extends FieldProgramParser {

  public MIBerrienCountyParser() { 
    super("BERRIEN COUNTY", "MI", 
          "Unit:UNIT? Status:DISPATCHED? Location:ADDR! Common_Name:PLACE? Call_Type:CALL! Call_Time:DATETIME! Call_Number:ID! Quadrant:MAP? District:SRC? Narrative:INFO?!" );
    setupMultiWordStreets(MULTI_WORD_STREET_LIST);
    setupSaintNames("JOSEPH");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Incident")) return false;
    body = body.replace("Call Type:", " Call Type:");
    if (parseMsg(body, data)) return true;
    setFieldList("INFO");
    data.parseGeneralAlert(this, body);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DISPATCHED")) return new SkipField("Dispatched|Enroute", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  //city pattern excludes city code and sector. could add logic to capture and check these if false positives arise
  
  private static Pattern SECTOR_PTN = Pattern.compile("(.*) - [NS]?[EW]? SECTOR", Pattern.CASE_INSENSITIVE);
  private static Pattern GPS_PTN = Pattern.compile("(.*) (?:(\\d{2}.\\d{6,} -\\d{2}\\.\\d{6,})|-361 -361)", Pattern.CASE_INSENSITIVE);
  private static Pattern CITY_PTN = Pattern.compile("(.*)(?<! US) \\d{2} (.*[a-z].*)");
  private static Pattern APT_PTN = Pattern.compile("([a-z](?:-?\\d+)?|\\d+(?:-\\d+)?[a-z]?)(?: (.*))?");
  private static Pattern NOT_APT_PTN = Pattern.compile("\\d*(?:1ST|2ND|3RD|[04-9]TH|1[1-3]TH)");
  private static Pattern TRAIL_DIR_PTN = Pattern.compile("(.*) ([NSEW])");
  private static Pattern MID_DIR_PTN = Pattern.compile(".*[A-Z]( +)[NSEW] .*");
  private static Pattern CLEAN_ADDR_STREET_NAME_PTN = Pattern.compile("(?:\\d+ +|.*& *)?(?:[NSEW] +)?(?:(?:NEW|OLD) )?(.*?)(?: +(?:AVE|BLVD|CIR|CT|DR|HWY|LN|PATH|PKW?Y|PL|RD|ST|TRL|WAY))?");
  private static Pattern SINGLE_BLANK_PTN = Pattern.compile("(?!(?:US|USHY|ST|HWY|RT) )[^ ]+(?<!^M)( +)[^ ]+");
  private static Pattern MIXED_CASE_PTN = Pattern.compile("\\b(?:[A-Z]?[a-z]+ *)+\\b");
  private static Pattern NOT_CITY_PTN = Pattern.compile(".* (?:FIRE|FD)", Pattern.CASE_INSENSITIVE);
  private static Pattern COUNTY_PTN = Pattern.compile("cass|.* co|.* county", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Parse trailing sector
      Matcher mat = SECTOR_PTN.matcher(field);
      if (mat.matches()) field = mat.group(1);
      
      // Parse trailing GPS
      mat = GPS_PTN.matcher(field);
      if (mat.matches()) {
        field = mat.group(1);
        String gps = mat.group(2);
        if (gps != null) setGPSLoc(gps.trim(), data);
      }
      
      //parse trailing CITY
      mat = CITY_PTN.matcher(field);
      if (mat.matches()) {
        field = mat.group(1).trim();
        data.strCity = mat.group(2).trim();
        field = stripFieldEnd(field, ",");
      }
      
      // field contained a combination of the original address, a possible apt, followed
      // by possible cross street information.  All of which gets all mixed together because
      // the street names do not always have the proper street suffix.  Sorting that all
      // out is going to take a lot of work.
      parseAddress(StartType.START_ADDR, FLAG_OPT_STREET_SFX, field, data);
      String left = getLeft();
      boolean comma = isCommaLeft();
      
      // Did I mention that cross street information might contain commma before and/or after
      // the slash?  
      
      // Coming out of the parseAddress call anything after the first comma ends up in left.  Anything
      // before that starting with the first slash goes into the cross street.
      
      // Lets see what we can do with the leftover stuff
      if (left.length() > 0) {

        // If there was a comma in front of it, replace it
        if (comma) left = ", " + left;
        
        // If we found a cross street, append the leftover stuff to that
        if (data.strCross.length() > 0) {
          if (!comma) left = " " + left;
          data.strCross = data.strCross + left;
          left = "";
        }
        
        // If no slash or comma was found, see if we can find an apt at the beginning of the leftovers
        else {
          if (!comma && data.strApt.length() == 0) {
            mat = APT_PTN.matcher(left); 
            if (mat.matches()) {
              data.strApt = mat.group(1);
              left = getOptGroup(mat.group(2));
            }
          }
          
          if (!left.equals("No Cross Streets Found")) data.strCross = left;
        }
        
        // Check for state name ending cross street
        if (data.strCross.endsWith(" IN") || data.strCross.endsWith(" IL")) {
          data.strState = data.strCross.substring(data.strCross.length()-2);
          data.strCross = data.strCross.substring(0,data.strCross.length()-3).trim();
        }
        
        // Any lower/mixed case words in the cross steet are trouble
        Matcher match = MIXED_CASE_PTN.matcher(data.strCross);
        if (match.find()) {
          
          // If found at end of field, move to city
          // if found at start fo field, move to apt
          if (match.end() == data.strCross.length()) {
            String city = match.group().trim();
            if (NOT_CITY_PTN.matcher(city).matches()) {
              data.strPlace = city;
            } else if (data.strCity.length() == 0 || !COUNTY_PTN.matcher(city).matches()){
              data.strCity = city;
            }
            data.strCross = data.strCross.substring(0,match.start()).trim();
            data.strCross = stripFieldEnd(data.strCross, "/");
          }
          
          else if (match.start() == 0) {
            data.strApt = append(data.strApt, " ", match.group().trim());
            data.strCross = data.strCross.substring(match.end()).trim();
            
            match = MIXED_CASE_PTN.matcher(data.strCross);
            if (match.find() && match.end() == data.strCross.length()) {
              data.strCity = match.group().trim();
              data.strCross = data.strCross.substring(0,match.start()).trim();
            }
            
            match = APT_PTN.matcher(data.strCross);
            if (match.matches()) {
              data.strApt = append(data.strApt, " ", match.group(1));
              data.strCross = getOptGroup(match.group(2));
            }
          }
        }
      }
      
      // That takes care of the leftovers.  Now all we have is the address and the 
      // apt and cross street.  Which still need some fiddling with

      // Let's look at what the parser put in the apt field.  If it does not look
      // like a a real apartment, move it to the cross street
      if (NOT_APT_PTN.matcher(data.strApt).matches()) {
        if (data.strCross.startsWith(",")) {
          data.strCross = data.strApt + data.strCross;
        } else if (data.strCross.contains("/")) {
          data.strCross = data.strApt + ' ' + data.strCross;
        } else {
          data.strCross = append(data.strApt, " / ", data.strCross);
        }
        data.strApt = "";
      }
      
      // If we have any cross street information, and the address ends with a direction
      // move the direction to the cross street
      if (data.strCross.length() > 0 && !data.strCross.startsWith(",")) {
        mat = TRAIL_DIR_PTN.matcher(data.strAddress);
        if (mat.matches()) {
          data.strAddress = mat.group(1).trim();
          data.strCross = mat.group(2) + ' ' + data.strCross;
        }
      }

      // Almost there.  But if what we have in the cross street looks incomplete, 
      if ((data.strCross.length() == 0 && !data.strAddress.contains("&")) || 
          data.strCross.startsWith(",") || 
          (data.strCross.length() > 0 && !data.strCross.contains("/"))) {
        int pt = breakAddress(data.strAddress);
        if (pt >= 0) {
          String cross = data.strAddress.substring(pt+1).trim();
          data.strAddress = data.strAddress.substring(0,pt);
          if (!data.strCross.startsWith(",") && data.strCross.length() > 0) cross += " / ";
          data.strCross =  cross + data.strCross;
        } 
      }
      
      // Check for cities in Indiana
      String state = CITY_ST_TABLE.getCodeDescription(data.strCity.toUpperCase(), true);
      if (state != null) data.strState = state;
    }
    
    /**
     * Try to break up an address that is believed to be a combination of the real address
     * lacking a street suffix followed by a single cross street name
     * @param address combined address/cross street field
     * @return the identified end of the real address if found, -1 otherwise
     */
    private int breakAddress(String address) {
      
      // Clean off leading street number, direction, and trailing street suffix 
      Matcher mat = CLEAN_ADDR_STREET_NAME_PTN.matcher(address);
      int start = 0;
      if (mat.matches()) {
        start = mat.start(1);
        address = mat.group(1);
      }
      
      // See if there is an embedded direction in what is left
      mat = MID_DIR_PTN.matcher(address);
      if (mat.matches()) return start+mat.start(1);
      
      // See if we can use the multi-word street list to identify the break
      // This also checks to see if the entire address is on the list
      String street = FWD_MULTI_WORD_STREET_LIST.getCode(address, true);
      if (street != null) {
        if (street.length() == address.length()) return -1;
        return start+street.length();
      }
      
      // If what is left contains single blank, that is where we break things
      mat = SINGLE_BLANK_PTN.matcher(address);
      if (mat.matches()) return start+mat.start(1);
      
      // Try looking for a known multi-word list at end of address
      street = REV_MULTI_WORD_STREET_LIST.getCode(address, true);
      if (street != null) {
        int pt = address.length()-street.length()-1;
        while (address.charAt(pt-1) == ' ') pt--;
        return start+pt;
      }

      // no go
      return -1;
    }
    
    @Override public String getFieldNames() {
      return super.getFieldNames() + " X PLACE CITY ST GPS";
    }
  }
  
  private static final Pattern CALL_CROSS_PTN = Pattern.compile("(.*) X\\b *(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CROSS_PTN.matcher(field);
      if (match.matches()) {
        data.strAddress = append(data.strAddress, " ", data.strCross);
        data.strCall = match.group(1).trim();
        data.strCross = match.group(2);
      }
    }
  }
  
  private static final CodeTable CITY_ST_TABLE = new CodeTable(
      "COOK",         "IL",
      "LA PORTE",     "IN",
      "LAKE CO",      "IL",
      "LAKE COUNTY",  "IL",
      "SPRINGFIELD",  "IN",
      "ST JOSEPH CO", "IN"
  );
  
  @Override
  public String adjustMapCity(String city) {
    String tmp = city.toUpperCase();
    if (tmp.endsWith(" TOWNSHIP")) {
      tmp = tmp.substring(0,tmp.length()-9)+" TWP";
    }
    tmp = MAP_CITY_TABLE.getProperty(tmp);
    if (tmp != null) return tmp;
    
    if (city.toUpperCase().endsWith(" CO")) city += "unty";
    return city;
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "COLOMA TWP",         "Coloma Charter Twp",
      "LAKE TWP",           "Lake Charter Twp",
      "LINCOLN TWP",        "Lincoln Charter Twp",
      "ORONOKO TWP",        "Oronoko Charter Twp",
      "ST JOSEPH TWP",      "St Joseph Charter Twp"
  });
  
  private static String[] MULTI_WORD_STREET_LIST = new String[]{
    "BLACK LAKE",
    "BROWN SCHOOL",
    "CAMPUS CIRCLE",
    "CLEAR LAKE",
    "COMMUNITY HALL",
    "COUNTRY AIRE",
    "COUNTRY KNOLL",
    "COUNTY LINE",
    "DAN SMITH",
    "DANIEL BOONE",
    "DEANS HILL",
    "DEER PARK",
    "DIAMOND POINT",
    "EAU CLAIRE",
    "ELM VALLEY",
    "FOREST BEACH",
    "FOREST HILLS",
    "FOREST LAWN",
    "GOLDEN CREST",
    "HAGAR SHORE",
    "HARBOR COUNTRY",
    "HICKORY CREEK",
    "HIGH BRIDGE",
    "HIGH VIEW",
    "HIPPS HOLLOW",
    "INDIAN TRAIL",
    "JOHN BEERS",
    "LAKE BLUFF",
    "LAKE CHAPIN",
    "LAKE SHORE",
    "LAKE VIEW",
    "LEMON CREEK",
    "LINCOLN WOOD",
    "LITTLE PAW PAW LAKE",
    "LIVINGSTON HILLS",
    "LONG LAKE",
    "LONGMEADOW VILLAGE",
    "MARQUETTE WOODS",
    "MEDICAL PARK",
    "MT ZION",
    "NORTH BRANCH",
    "NOTRE DAME",
    "OAK LANE",
    "OLIVE BRANCH",
    "PAW PAW LAKE",
    "PAW PAW",
    "PUCKER STREET",
    "RANGE LINE",
    "RED ARROW",
    "RED BUD",
    "RIVERSIDE POINTE",
    "ROCKY WEED",
    "ROSE HILL",
    "ROYAL CURVE",
    "SHAKER FARM",
    "SINGER LAKE",
    "SPRING CREEK",
    "SPRING HILL",
    "SPRING PARK",
    "ST JOSEPH RIVER",
    "ST JOSEPH VAL",
    "ST JOSEPH",
    "STATE LINE",
    "STELTER FARM",
    "STRAWBERRY ROW",
    "THREE OAKS",
    "TOWER HILL",
    "TOWN HALL",
    "UNION PIER",
    "VALLEY VIEW",
    "WALTON E RIVERSIDE",
    "WARREN WOODS",
    "WILD DUNES",
    "WOODS EDGE"
 
  };
  
  private static CodeSet FWD_MULTI_WORD_STREET_LIST = new CodeSet(MULTI_WORD_STREET_LIST);
  private static ReverseCodeSet REV_MULTI_WORD_STREET_LIST = new ReverseCodeSet(MULTI_WORD_STREET_LIST);

}
