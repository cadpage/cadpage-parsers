package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILKankakeeCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER_PTN3 =
      Pattern.compile("(.*)(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)(?: +(\\d{4}-\\d{8}))?(?: +(.*))?");
  private static final Pattern LEAD_DIR_PTN = Pattern.compile("([NSEW])\\b *(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(.*?) *\\b(?:APT|RM|LOT) +([^ ]+) *(.*)", Pattern.CASE_INSENSITIVE);
  
  private static final Pattern MASTER_PTN2 = 
      Pattern.compile("(.+) Location: (.+) \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d Incident #: *(.*)");
  private static final Pattern HOUSE_SLASH_PTN = Pattern.compile("^\\d+(/)[^ ]");
  private static final Pattern ADDR_CALLBK_PTN = Pattern.compile("(.*?)[/ ]+CALLBK.*");
  
  private static final Pattern MASTER_PTN1 = Pattern.compile("([A-Z0-9 ]+?)  +(.*?)(?: +(\\d{4}-\\d{8}))?");
  private static final Pattern PLACE_CITY_BRK_PTN = Pattern.compile("\\b([A-Z0-9]+)([A-Z][a-z]+)\\b");
  private static final Pattern SRC_PTN = Pattern.compile("(Aroma Fire|Bourbonnais Fire|Herscher Fire|K3 Twp Fire|Manteno Fire|Momence Fire|Saline/Limestone Fire|Otto Fire|Saline/Limestone Fire|St.Anne Fire|Station #\\d+) +(.*)");
  
  public ILKankakeeCountyParser() {
    super(CITY_LIST, "KANKAKEE COUNTY", "IL");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "dispatchmessage@nwsmessage.net,noreply@k3county.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Check for message signature
    if (!subject.equals("NWS Message")) return false;
    
    // There are now three formats, Wish they would make up their minds
    Matcher match = MASTER_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL PLACE ADDR APT CITY ST ID");
      data.strCall = match.group(1).trim();
      String sAddr = match.group(2);
      Matcher match2 = HOUSE_SLASH_PTN.matcher(sAddr);
      if (match2.find()) sAddr = sAddr.substring(0,match2.start(1)) + " " + sAddr.substring(match2.end(1));
      match2 = ADDR_CALLBK_PTN.matcher(sAddr);
      if (match2.matches()) sAddr = match2.group(1);
      parseAddress(StartType.START_PLACE, FLAG_START_FLD_NO_DELIM | FLAG_ANCHOR_END, sAddr, data);
      data.strCallId = match.group(3);
      
      // If we didn't find an address, transfer place to address
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      
      // If we did find an address
      // try to parse apt from end of parsed address
      else { 
        String addr = data.strAddress;
        String apt = data.strApt;
        data.strAddress = data.strApt = "";
        parseAddress(StartType.START_ADDR, FLAG_NO_CITY, addr, data);
        if (apt.length() == 0) apt = data.strApt;
        data.strApt = append(apt, "-", getLeft());
      }
      setState(data);
      return true;
    }
    
    match = MASTER_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT CITY ST PLACE DATE TIME ID X");
      body = match.group(1).trim();
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strCallId = getOptGroup(match.group(4));
      data.strCross = getOptGroup(match.group(5));
      
      // Sometimes there is no blank separating the place name
      // from the city name.  We will try to identify these by looking
      // for multiple upper case letters followed by a lower case leter
      body = PLACE_CITY_BRK_PTN.matcher(body).replaceFirst("$1 $2");
      
      parseAddress(StartType.START_CALL, FLAG_PAD_FIELD_EXCL_CITY | FLAG_CROSS_FOLLOWS, body, data);
      if (data.strAddress.length() == 0) return false;
      
      String pad = getPadField();
      match = LEAD_DIR_PTN.matcher(pad);
      if (match.matches()) {
        data.strAddress = append(data.strAddress, " ", match.group(1));
        pad = match.group(2);
      }
      if (pad.startsWith("/")) {
        data.strAddress = append(data.strAddress, " & ", pad.substring(2).trim());
      } else if (pad.length() < 5) {
        data.strApt = append(data.strApt, "-", pad);
      } else {
        data.strPlace = pad;
      }

      String place = getLeft();
      if (data.strCity.length() == 0 && place.startsWith("/")) {
        data.strAddress = append(data.strAddress, " & ", place.substring(2).trim());
      }
      else {
        match = APT_PTN.matcher(place);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(2));
          place = append(match.group(1), " ", match.group(3));
        }
        if (isValidAddress(place) && !place.toUpperCase().endsWith(" TERRACE")) {
          data.strCross = append(data.strCross, " / ", place);
        } else if (data.strApt.length() == 0 && place.length() < 5) {
          data.strApt = append(data.strApt, "-", place);
        } else {
          data.strPlace = append(data.strPlace, " - ", place);
        }
      }
      setState(data);
      return true;
    }
    
    match = MASTER_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT SRC ADDR APT PLACE CITY ST CALL ID");
      data.strUnit = match.group(1).trim();
      body = match.group(2).trim();
      data.strCallId = getOptGroup(match.group(3));
      
      // Sometimes there is no blank separating the place name
      // from the city name.  We will try to identify these by looking
      // for multiple upper case letters followed by a lower case leter
      body = PLACE_CITY_BRK_PTN.matcher(body).replaceFirst("$1 $2");
      
      // compress multiple blanks
      body = body.replaceAll("  +", " ");
      
      // See if we can identify source field from pattern search.  If not
      // we will have to trust the SAP
      StartType st = StartType.START_OTHER;
      match = SRC_PTN.matcher(body);
      if (match.matches()) {
        st = StartType.START_ADDR;
        data.strSource = match.group(1);
        body = match.group(2);
      }
      parseAddress(st, FLAG_IMPLIED_INTERSECT | FLAG_PAD_FIELD, body, data);
      if (data.strSource.length() == 0) data.strSource = getStart();
      String pad = getPadField();
      if (pad.length() < 5) {
        data.strApt = append(data.strApt, " ", pad);
      } else {
        data.strPlace = pad;
      }
      data.strCall = getLeft();
      setState(data);
      return data.strCity.length() > 0;
    }
    
    return false;
  }
  
  private static void setState(Data data) {
    String state = CITY_STATE_TABLE.getProperty(data.strCity);
    if (state != null) data.strState = state;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "1ST NORTH",
      "1ST SOUTH",
      "2ND SOUTH",
      "3RD SOUTH",
      "3300 NORTH",
      "4TH SOUTH",
      "ARTHUR BURCH",
      "BELLE AIRE",
      "BROTHER ALPHONSUS",
      "BULL CREEK",
      "BUR OAK",
      "CAREER CENTER",
      "DOWN E COURT",
      "EDGE WATER",
      "EL PASO",
      "ESSON FARM",
      "EXLINE CLUB",
      "FAMILY DOLLAR",
      "FOX RUN",
      "HARVEST VIEW",
      "INDUSTRIAL PARK",
      "LAKE METONGA",
      "MILL POND",
      "OAK CREEK",
      "OAK RIDGE",
      "OAK RUN",
      "PARK LANE",
      "RIVER BEND",
      "SAINT FRANCIS",
      "SAINT JOSEPH",
      "SAINT PETERS",
      "SPORTSMAN CLUB",
      "STEEPLE CHASE",
      "STOCKTON HEIGHTS",
      "TOWN LINE",
      "VAN BUREN",
      "VAN METER",
      "WESTERN HILLS",
      "WILL PEOTONE",
      "WILLIAM LATHAM"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911:UNKNOWN",
      "911:ABANDONED",
      "ABANDONED",
      "ACCIDENT",
      "AGENCY ASSIST",
      "ALARM CALL",
      "ALARM:AUTOMATIC",
      "ALARM:AUTOMATIC/SHAPIRO",
      "ALARM:BOX",
      "ALARM:CO DET",
      "ALARM:FIRE",
      "ALARM:STILL",
      "AMB:MUTUAL AID",
      "AMBULANCE",
      "AMBULANCE:ASSIST",
      "ANIMAL CASE",
      "BATTERY",
      "BURGLARY",
      "CONTROL BURN",
      "DEATH INVESTIGATION",
      "DECEASED SUBJ",
      "DIS CONDUCT",
      "DISABLED VEH",
      "DISTURBANCE",
      "DOM DIST",
      "DUI",
      "FIGHT",
      "FIRE:BRUSH",
      "FIRE:MUTAL AID",
      "FIRE:STRUCTURE",
      "FIRE:VEHICLE",
      "HAZMAT",
      "HIT/RUN",
      "HOME INVASION",
      "ILLEGAL BURNING",
      "INDECENT EXPOSURE",
      "JUV/RUNAWAY",
      "JUVENILE CALL",
      "KEEP THE PEACE",
      "LIFT ASSIST",
      "MABAS RADIO DRILL",
      "MENTAL CASE",
      "MISSING PERSON",
      "NEW",
      "OTHER DUTIES",
      "PERSON UNK",
      "REMOVAL",
      "RIVERSIDE AMB",
      "ROBBERY",
      "SERVICE",
      "SHOTS FIRED",                                                                                                                                                                                                 
      "SMOKE/ODOR",
      "SUSPICIOUS PERSON",
      "TRAFFIC COMPLAINT",
      "UNKNOWN PROBLEM",
      "WALK THRU",
      "WEAPONS",
      "WELFARE CHECK",
      "WIRES DOWN"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "AROMA PARK",
    "BONFIELD",
    "BOURBONNAIS",
    "BRADLEY",
    "BUCKINGHAM",
    "CABERY",
    "CHEBANSE",
    "ESSEX",
    "GRANT PARK",
    "HERSCHER",
    "HOPKINS PARK",
    "IRWIN",
    "KANKAKEE",
    "LIMESTONE",
    "MANTENO",
    "MOMENCE",
    "PEWING",
    "SOLLITT",
    "ST ANNE",
    "REDDICK",
    "SAMMONS POINT",
    "SUN RIVER TERRACE",
    "UNION HILL",
    "WILMINGTON",
    "YEAGER",

    "AROMA TOWNSHIP",
    "BOURBONNAIS TOWNSHIP",
    "ESSEX TOWNSHIP",
    "GANEER TOWNSHIP",
    "KANKAKEE TOWNSHIP",
    "LIMESTONE TOWNSHIP",
    "MANTENO TOWNSHIP",
    "NORTON TOWNSHIP",
    "OTTO TOWNSHIP",
    "PEMBROKE TOWNSHIP",
    "PILOT TOWNSHIP",
    "ROCKVILLE TOWNSHIP",
    "SALINA TOWNSHIP",
    "ST ANNE TOWNSHIP",
    "SUMMER TOWNSHIP",
    "YELLOWHEAD TOWNSHIP",
    
    "KANKAKEE COUNTY",
    
    // Ford County
    "FORD COUNTY",
    "ELLIOTT",
    "KEMPTON",
    "LIMESTONE",
    "MELVIN",
    "PAXTON",
    "PIPER CITY",
    "ROBERTS",
    "ROSSVILLE",
    
    
    // Grundy County
    "GARDNER",
    "GRUNDY COUNTY",
    "BRACEVILLE",
    "PEOTONE",
    
    // Iroquois County
    "IROQUOIS COUNTY",
    "ASHKUM",
    "BEAVERVILLE",
    "PAPINEAU",
    "THAWVILLE",
    
    // Vermillion County
    "VERMILLION COUNTY",
    "EAST LYNN",
    "HOOPESTON",
    "RANKIN",
    
    // Will County
    "WILL COUNTY",
    "BEECHER",
    "CRETE",
    "BRAIDWOOD",
    "CUSTER PARK",
    
    // Newton County, IN
    "NEWTON COUNTY",
    "BROOK",
    "GOODLAND",
    "KENTLAND",
    "MOROCCO"
  };
  
  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{
      "NEWTON COUNTY", "IN",
      "BROOK",         "IN",
      "GOODLAND",      "IN",
      "KENTLAND",      "IN",
      "MOROCCO",       "IN"
      
  });
}
