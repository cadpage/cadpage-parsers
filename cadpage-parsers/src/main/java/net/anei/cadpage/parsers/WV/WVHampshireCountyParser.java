package net.anei.cadpage.parsers.WV;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Hampshire County, WV
 */
public class WVHampshireCountyParser extends DispatchA48Parser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+|MEDIC", Pattern.CASE_INSENSITIVE);
  private static final Pattern COUNTY_PTN = Pattern.compile("^(HARDY|FRED|FREDERICK|MINERAL|ALLEGANY|MORGAN) ", Pattern.CASE_INSENSITIVE);

  public WVHampshireCountyParser() {
    super(CITY_LIST, "HAMPSHIRE COUNTY", "WV", FieldType.NAME, A48_NO_CODE, UNIT_PTN);
    setupCallList(CALL_LIST);
    setupSpecialStreets(
        "HERITAGE SQUARE APTS",
        "WAPOCOMA CAMP GROUND"
    );
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@frontier.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Get rid of some garbage constructs before they up the semicolon count
    // to the point where we might think this is a semicolon delimited format
    body = body.replace("Name:;", "");
    body = body.replace("Address:null null;", "");
    body = body.replace("Phone number:000000000;", "");
    
    if (!super.parseMsg(subject, body, data)) return false;
    
    // SO far, so good
    // Now for some special corrective measures to take if the
    // address parser did not find a city
    if (data.strCity.length() == 0) {
      
      // If there was no name, then see if the address includes
      // a STA word.  If found, this marks what should be the
      // end of the address and everything else goes in the name
      if (data.strName.length() == 0) {
        int pt = data.strAddress.indexOf(" STA ");
        if (pt >= 0) {
          pt += 4;
          data.strName = data.strAddress.substring(pt+1).trim();
          data.strAddress = data.strAddress.substring(0,pt).trim();
        }
      }
      
      // If the name looks like a neighboring county 911 center
      // leave it alone but use it to set the city & possible state
      Matcher match = COUNTY_PTN.matcher(data.strName);
      if (match.find()) {
        String city = match.group(1).toUpperCase();
        if (city.equals("FRED")) city = "FREDERICK";
        data.strCity = city + " COUNTY";
        if (city.equals("FREDERICK")) data.strState = "VA";
        else if (city.equals("ALLEGANY")) data.strState = "MD";
      }
    }
    
    // If we did find a city, see if it is one of our misspelled entries
    else {
      String city = MISTYPED_CITIES.getProperty(data.strCity.toUpperCase());
      if (city !=  null) data.strCity = city;
    }
    
    if (data.strCallId.endsWith("`")) {
      data.strCallId = data.strCallId.substring(0,data.strCallId.length()-1).trim();
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY ", "CITY ST ");
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ALLERGY",
      "ALTERED MENTAL STATUS",
      "ASSAULT",
      "ASSISTANCE",
      "BLEED",
      "BLOOD PRESSURE",
      "BREATHING DIFFICULTY",
      "BROKE",
      "BS",
      "CAR",
      "CARDIAC",
      "CHOKE",
      "COMMERCIAL FIRE",
      "CONTROL BURN",
      "DIABETIC",
      "DRUNK",
      "E-ALARM",
      "FALL",
      "F-COMM-ALARM",
      "FLOOD",
      "GENERAL ILLNESS",
      "JUVENILE",
      "MVA",
      "OB",
      "OD",
      "PAIN",
      "PSYCHIATRIC",
      "RESCUE",
      "SEIZURES",
      "SMOKE INVESTIGATION",
      "STROKE",
      "STRUCTURE",
      "TEST",
      "TRAUMA",
      "TREE",
      "UNCONCIOUS",
      "UNRESPONSIVE",
      "WELL"
  );
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "APPLE RIDGE",
    "BEN SAVILLE",
    "BLACKS HILL",
    "BREEZY COVE",
    "CAMP RIM ROCK",
    "CAPE COD",
    "CAPON RIVER",
    "CAPON SCHOOL",
    "CAPON SPRINGS",
    "CHRISTIAN CHURCH",
    "CHURCH HOLLOW",
    "COLD STREAM",
    "COOPER MOUNTAIN VIEW",
    "COTTAGE CASTLE",
    "CREST HAVEN",
    "DILLONS RUN",
    "DOCTOR RANDOLPH SPENCER",
    "DON MCCAULEY",
    "DUNMORE RIDGE",
    "DYNO NOBEL",
    "FALCON TURN",
    "FORD HILL",
    "FROG HOLLOW",
    "GRANNY P",
    "GRASSY LICK",
    "GREEN LANTERN",
    "HAMPSHIRE PARK",
    "HEIDE COOPER",
    "HENRY W MILLER",
    "HICKORY CORNER",
    "HNERY W MILLER",
    "IRON PILE",
    "JACK RUSSELL RIDGE",
    "JERSEY MOUNTAIN",
    "KEARNS SCHOOL HOUSE",
    "LITTLE CACAPON",
    "LITTLE CACAPON LEVELS",
    "MARTINSBURG GRADE",
    "MUIRWOOD GREENE",
    "MYSTIC MOUNTAIN",
    "PAINTER HOLLOW",
    "PATTERSON CREEK",
    "PAW PAW",
    "PEACH TREE FARMS",
    "PIN OAK",
    "POTOMAC OVERLOOK",
    "RIDGE LOOP",
    "ROCK OAK",
    "SAND FIELD",
    "SMOKEY HOLLOW",
    "SOL SHANHOLTZ",
    "STONEY MOUNTAIN OVERLOOK",
    "TIMBER RIDGE",
    "TIMBER RIDGE CAMP",
    "TROUT RUN",
    "VALLEY VIEW",
    "WAPACOMA CAMPGROUND",
    "WATSON SCHOOL"
  };
  
  private static final String[] CITY_LIST = new String[]{

    // Incorporated Communities
    "ROMNEY",
    "CAPON BRIDGE",
    
    // Unincorporated Communities
    "AUGUSTA",
    "BARNES MILL",
    "BLOOMERY",
    "BLUES BEACH",
    "BUBBLING SPRING",
    "CAPON LAKE",
    "CAPON SPRING",
    "CAPON SPRINGS",
    "CAPON SPRINGS STATION",
    "COLD STREAM",
    "CREEKVALE",
    "DAVIS FORD",
    "DELRAY",
    "DILLONS RUN",
    "DONALDSON",
    "FORKS OF CACAPON",
    "FRENCHBURG",
    "GLEBE",
    "GOOD",
    "GRACE",
    "GREEN SPRING",
        "GREENSPRING",   // Mistyped
    "HAINESVILLE",
    "HANGING ROCK",
    "HIGGINSVILLE",
    "HIGH VIEW",
    "HOOKS MILLS",
    "HOY",
    "INTERMONT",
    "JERICHO",
    "JUNCTION",
    "KIRBY",
    "LARGENT",
    "LEHEW",
    "LEVELS",
    "LITTLE CACAPON",
    "LOOM",
    "MECHANICSBURG",
    "MILLBROOK",
    "MILLEN",
    "MILLESONS MILL",
    "NEALS RUN",
    "NERO",
    "NORTH RIVER MILLS",
    "OKONOKO",
    "PANCAKE",
    "PIN OAK",
    "PLEASANT DALE",
    "POINTS",
    "PURGITSVILLE",
        "PURGITVILLE",    // Mistyped
    "RADA",
    "RAVEN ROCKS",
    "RIDGEDALE",
    "RIO",
    "RUCKMAN",
    "SECTOR",
    "SEDAN",
    "SHANKS",
    "SHILOH",
    "SLANESVILLE",
    "SOUTH BRANCH DEPOT",
    "SPRINGFIELD",
        "SPARINGFIELD",  // Mistyped
    "THREE CHURCHES",
    "VANCE",
    "VANDERLIP",
    "WAPPOCOMO",
    "WOODROW",
    "YELLOW SPRING",
        "YELLOW SPRIN",
        "YELLOW SPRINGS",
    
    // Hardy County
    "WARDENSVILLE",
        "WARDENSVILE",
        
    // Morgan County
    "PAW PAW",
    
    // Mineral County
    "BURLINGTON",
  };
  
  private static final Properties MISTYPED_CITIES = buildCodeTable(new String[]{
      "GREENSPRING",        "GREEN SPRING",
      "PURGITVILLE",        "PURGITSVILLE",
      "SPARINGFIELD",       "SPRINGFIELD",
      "WARDENSVILE",        "WARDENSVILLE",
      "YELLOW SPRIN",       "YELLOW SPRING",
      "YELLOW SPRINGS",     "YELLOW SPRING"
      
  });
}
