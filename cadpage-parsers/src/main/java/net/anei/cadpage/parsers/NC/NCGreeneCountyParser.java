
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCGreeneCountyParser extends DispatchSouthernParser {

  public NCGreeneCountyParser() {
    super(CALL_SET, CITY_LIST, "GREENE COUNTY", "NC", DSFLAG_OPT_DISPATCH_ID | DSFLAG_LEAD_PLACE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@Greenecountync.com,";
  }
  
  private static final Pattern CALL_CODE_PTN2 = Pattern.compile("(\\d\\d(?:0\\d\\d)?) +(.*)");
  private static final Pattern MAP_PTN = Pattern.compile("(.*?)((\\bCELL .* )?\\b(?:[NSEW]|[NS][EW]) SECTOR)");
  private static final Pattern ADDR_DIR_PTN = Pattern.compile("(.*?) +([NSEW]|[NS][EW])");
  private static final Pattern START_DIGIT_PTN = Pattern.compile("(\\d+) +(.*)");
  private static final Pattern APT_PTN = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]");
  private static final Pattern HWY_264_PTN = Pattern.compile(".*\\b264$");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;

    // See if a  mile marker ended up in the name
    if (data.strName.startsWith("MM ") || data.strName.startsWith("mm ")) {
      data.strAddress = append(data.strAddress, " ", data.strName);
      data.strName = "";
    }
    
    // If there is a call code in front of the call description, strip it off
    Matcher match = CALL_CODE_PTN2.matcher(data.strCall);
    if (match.matches()) {
      if (data.strCode.length() == 0) data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    
    // There is often a map code between the address and city fields, which
    // generally ends up in the APT field
    if (data.strApt.startsWith("- ")) {
      data.strMap = data.strApt.substring(2).trim();
      data.strApt = "";
    }
    else if ((match = MAP_PTN.matcher(data.strApt)).matches()) {
      data.strApt = match.group(1).trim();
      data.strMap = match.group(2);
    } else if  (data.strApt.equals("SECTOR")) {
      match = ADDR_DIR_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strAddress = match.group(1);
        data.strMap = match.group(2) + ' ' + data.strApt;
        data.strApt = "";
      }
    }
    
    if (data.strPlace.startsWith("-")) data.strPlace = data.strPlace.substring(1).trim();
    if (data.strPlace.endsWith("#")) {
      match = START_DIGIT_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strPlace = data.strPlace + match.group(1);
        data.strAddress = match.group(2);
      }
    }
    if (data.strPlace.equals("Not Entered")) data.strPlace = "";
    
    if (data.strCity.length() == 0) {
      int pt = data.strName.indexOf("SECTOR");
      if (pt >= 0) {
        data.strMap = data.strName.substring(0,pt+6);
        data.strName = data.strName.substring(pt+6).trim();
      }
      if (data.strApt.length() == 0 && APT_PTN.matcher(data.strName).matches()) {
        data.strApt = data.strName;
        data.strName = "";
      }
    }
    data.strPlace = append(data.strPlace, " - ", data.strName);
    data.strName = "";
    
    if (data.strApt.equals("A") && HWY_264_PTN.matcher(data.strAddress).matches()) {
      data.strAddress += " ALT";
      data.strApt = "";
    }
    
    if (data.strCity.equals("WALTONSBURG")) data.strCity = "WALSTONBURG";
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("APT", "APT MAP");
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d-\\S+) *(.*)");

  @Override
  protected void parseExtra(String sExtra, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(sExtra);
    if (match.matches()) {
      data.strCall = match.group(1).replace('-', ' ');
      data.strSupp = match.group(2);
      return;
    }
    super.parseExtra(sExtra, data);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ARBA JASON",
    "AUTUMN FLAME",
    "BB FIRST",
    "BB SECOND",
    "BEAMAN OLD CREEK",
    "BEDDARD FARM",
    "BENNIE ALBRITTON",
    "BILLY RON",
    "BRICK KITCHEN",
    "BROOK MEADOW",
    "BURNSIDE VILLAGE",
    "CARSON EDWARDS",
    "CHICKEN SHACK",
    "CORBETT TOWN",
    "CUTTER CREEK",
    "DEER POINTE",
    "DIXIE GREENE",
    "DIXON FARM",
    "DUNN COBB",
    "EDEN CHURCH",
    "EDWARDS BRIDGE",
    "EVERMAY FARM",
    "EXUM POND",
    "FAIR GROVE",
    "FIRE STATION",
    "FIRE TOWER",
    "FORT RUN",
    "FOUR WAY", 
    "FOX CROFT",
    "FRANKLIN JONES",
    "FRED HARRISON",
    "FREE GOSPEL", 
    "FRIENDSHIP CHURCH", 
    "GRANT SAND HOLE",
    "GRAY POND",
    "GRAY TURNAGE",
    "GRAYS MILL",
    "GUTHRIE TURNER",
    "HAPPY VALLEY",
    "HARRIS COURT",
    "HOOT HOLLOW",
    "JAMAL SARSOUR",
    "JESSE HILL",
    "JIM GRANT",
    "JOSHUA MEWBORN",
    "JUDGE HARDY",
    "KEARNEY CEMETERY",
    "KNOX SCHOOL",
    "LAKE SHORE",
    "LAKES SHORE",
    "LEWIS STORE",
    "LILLY PAD",
    "LLOYD HARRISON",
    "MARTIN L KING JR",
    "MAURY BALLPARK",
    "MEWBORN CHURCH",
    "MIDDLE SCHOOL",
    "MIKE STOCKS",
    "MOORE ROUSE",
    "MOORE TOWN",
    "MORRIS BBQ",
    "MT HERMAN CHURCH",
    "OAK GROVE",
    "ORMOND FARM",
    "PARKER FARM",
    "PAULS PATH",
    "PEAK BRANCH",
    "PETER GRANT",
    "PINE CONE",
    "PINE SHOAL",
    "RABBIT HOLLOW",
    "ROUSE CHAPEL",
    "ROY HOOKER",
    "SAND PIT",
    "SCHOOL HOUSE",
    "SHADY GROVE CHURCH",
    "SNOW HILL RIDGE",
    "SPEIGHTS BRIDGE",
    "SPRING BRANCH CHURCH",
    "STOCKS MCLAWHORN",
    "THOMAS SUGG",
    "TITUS MEWBORN",
    "TURNAGE FARM",
    "TYSON CHAPEL CHURCH",
    "TYSON LOOP",
    "VANDIFORD THOMAS",
    "VILLAGE PARK",
    "WASHINGTON BRANCH CHURCH",
    "WEST MARLBORO",
    "WHEAT SWAMP",
    "WILLIAM HOOKER",
    "WILLOW GREEN",
    "WOODROW CORBETT"
  };

  private static final CodeSet CALL_SET = new CodeSet(
      "31 Alarm",
      "40 Fight In Progress",
      "50 P.D. Vehicle Accident - Property Damage",
      "50 P.I. Vehicle Accident - Personal Injury",
      "50 P.I. Vehicle Accident - Property Damage",
      "52 Ambulance Needed",
      "56 Drunk Pedestrian",
      "63 Investigate",
      "73 Mental Subject",
      "79 Domestic Dispute",
      "90 Shots Fired",
      "98 Fire Call"
  );

  private static final String[] CITY_LIST = new String[]{
    "HOOKERTON",
    "SNOW HILL",
    "WALSTONBURG",
    "WALTONSBURG",   // Misspelled by dispatch
    
    //Townships
    "BULL HEAD",
    "CARRS",
    "HOOKERTON",
    "JASON",
    "OLDS",
    "ORMONDSVILLE",
    "SHINE",
    "SNOW HILL",
    "SPEIGHTS BRIDGE",
    
    // Other
    "MAURY",
    
    // Lenoir County
    "CONTENTNEA NECK",
    "FALLING CREEK",
    "INSTITUTE",
    "LAGRANGE",
    "LA GRANGE",
    "VANCE",
    
    // Pitt County
    "AUTHUR",
    "AYDEN",
    "FARMVILLE",
    "FOUNTAIN",
    "GRIFTON",
    "WINTERVILLE",
    
    // Wayne County
    "EUREKA",
    "GOLDSBORO",
    "NEW HOPE",
    "PIKEVILLE",
    "SAULSTON",
    "STONEY CREEK",
    
    // Wilson County
    "GARDNERS",
    "SARATOGA",
    "STANTONSBURG",
    "WILSON"
    
  };
  
}
