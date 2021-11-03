package net.anei.cadpage.parsers.CT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CTTollandCountyAParser extends SmartAddressParser {

  public CTTollandCountyAParser() {
    super(CTTollandCountyParser.CITY_LIST, "TOLLAND COUNTY", "CT");
    removeWords("COURT", "KNOLL", "ROAD", "STREET", "TERRACE");
    addRoadSuffixTerms("CMNS", "COMMONS", "PARK");
    setupSaintNames("PHILIPS");
    setupMultiWordStreets(
        "WHITNEY T FERGUSON III"
    );
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@TollandCounty911.org,@TollandCounty911.com,messaging@iamresponding.com,noreply@everbridge.net,777";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]+");
  private static final Pattern BAD_PTN = Pattern.compile("\\d{10} .*", Pattern.DOTALL);

  private static final Pattern MASTER1 = Pattern.compile("(.*?) Cross Street (?:(.*?) )?(?:(Station \\d+) )?(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)(?: (\\d{4}-\\d{8}\\b.*))?");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(.*?) ((?:(?:[A-Z]+\\d+|\\d+[A-Z]+\\d*|RGH|Lifeflight|Lifestar|Sta\\d+)(?:-RIT)?\\b,?)+)");
  private static final Pattern TRAIL_CH_PTN = Pattern.compile("(.*?)[-/ ]*\\b(\\d\\d\\.\\d\\d(?:[-/., ]+(?:(?:[A-Z]+ )?F/?G|VFG|TAC[- ]*\\S+))?)\\b *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  private static final Pattern MASTER2 = Pattern.compile("(.*?) (\\d\\d:\\d\\d)(?: (.*?))?(?: (\\d{4}-\\d{8}))?");
  private static final Pattern FLR_PTN = Pattern.compile("(\\d+)(?:ST|ND|RD|TH) *(?:FLOOR|FLR?)");
  private static final Pattern APT_PTN = Pattern.compile("(?:UNIT|TRLR|TRAILER|APT|LOT|FLR?)[- ]*(.*)|[A-Z] *\\d*|\\d+[A-Z]?|\\d+FL");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    do {

      if (subject.equals("TN Alert")) break;

      if (SUBJECT_PTN.matcher(subject).matches()) {
        data.strSource = subject;
        break;
      }

      if (body.startsWith("TN Alert / ")) {
        body = body.substring(11);
        break;
      }

      // But if we don't have anything, accept that too
    } while (false);

    // Rule out variant of CTTollandCountyB
    if (BAD_PTN.matcher(body).matches()) return false;

    int pt = body.indexOf("\nText STOP");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = body.replace('\n', ' ');

    // Check for variant 1 format
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT CITY PLACE CALL CH X UNIT DATE TIME ID");
      body = match.group(1).trim();
      String cross = getOptGroup(match.group(2));
      if (!cross.equals("No Cross Streets Found")) data.strCross = cross;
      data.strUnit = cvtUnitCodes(getOptGroup(match.group(3)));
      data.strDate = match.group(4);
      String time = match.group(5);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      data.strCallId = getOptGroup(match.group(6));

      //  Parse trailing unit and channel in either order

      boolean foundUnit = false;
      match = TRAIL_UNIT_PTN.matcher(body);
      if (match.matches()) {
        foundUnit = true;
        body = match.group(1).trim();
        data.strUnit = append(cvtUnitCodes(match.group(2).trim()), ",", data.strUnit);
      }

      String postCall = "";
      match = TRAIL_CH_PTN.matcher(body);
      if (match.matches()) {
        String tmp = match.group(3);
        if (tmp.length() == 0 || tmp.startsWith("*") || tmp.startsWith("-")) {
          body = match.group(1).trim();
          data.strChannel = match.group(2);
          if (tmp.length() > 0) postCall = tmp.substring(1).trim();

          if (!foundUnit) {
            match = TRAIL_UNIT_PTN.matcher(body);
            if (match.matches()) {
              body = match.group(1).trim();
              data.strUnit = append(cvtUnitCodes(match.group(2).trim()), ",", data.strUnit);
            }
          }
        }
      }

      pt = body.indexOf(',');
      if (pt >= 0) {
        parseAddress(body.substring(0,pt).trim(), data);
        body = body.substring(pt+1).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
        body = getLeft();
        body = stripFieldStart(body, "/");
        String left = parseCallDesc(body, data);
        if (left != null) {
          parsePlace(left, data);
        } else {
          data.strCall = body;
        }
      }

      else {
        String left = parseCallDesc(body, data);
        if (left != null) {
          left = stripFieldEnd(left, "/");
          parseAddress(StartType.START_ADDR, FLAG_NO_CITY, left, data);
          parsePlace(getLeft(), data);
        } else {
          parseAddress(StartType.START_ADDR, FLAG_NO_CITY, body, data);
          data.strCall = getLeft();
        }
      }
      data.strCall = append(data.strCall, " - ", postCall);

      if (data.strChannel.length() == 0) {
        match = TRAIL_CH_PTN.matcher(data.strCross);
        if (match.matches()) {
          data.strCross = match.group(1);
          data.strChannel = match.group(2);
          parsePlace(match.group(3), data);
        }
      }
      return true;
    }

    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ADDR APT CITY PLACE CALL TIME X ID");
      body = match.group(1).trim();
      data.strTime = match.group(2);
      String cross = getOptGroup(match.group(3));
      cross = stripFieldStart(cross, "Cross Street");
      if (!cross.equals("No Cross Streets Found")) data.strCross = cross;
      data.strCallId = getOptGroup(match.group(4));

      // We are invoking the smart address parser strictly to find city, it
      // shouldn't have to do much parsing.  If it doesn't find a city, bail out.
      // The slash confuses the parse logic, so switch it to something unusual
      // Ditto for parrens
      body = FLR_PTN.matcher(body).replaceAll("FLR $1");
      body = escape(body);
      parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK, body, data);
      String sAddr = unescape(data.strAddress);
      data.strApt = unescape(data.strApt);
      body = unescape(getLeft());

      // Address always has a slash, which the address parser turned to an ampersand
      // What is in front of that becomes the address
      pt = sAddr.indexOf('/');
      if (pt >= 0) {

        // Use smart address parser to extract trailing apt
        parseAddress(StartType.START_ADDR, FLAG_NO_CITY, sAddr.substring(0,pt).trim(), data);
        data.strApt = append(data.strApt, " - ", getLeft());

        sAddr = sAddr.substring(pt+1).trim();
        sAddr = stripFieldEnd(sAddr, "/");

        // if what comes after the slash is a street name
        // If not, put it in the apt field
        match = APT_PTN.matcher(sAddr);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = sAddr;
          if (!data.strApt.equals(apt)) data.strApt = append(apt, "-", data.strApt);
        }
        else if (isValidAddress(sAddr)) {
          data.strAddress = append(data.strAddress, " & ", sAddr);
        } else {
          data.strPlace = append(data.strPlace, " - ", sAddr);
        }
      }

      // Once in a blue moon, the slash ends up in the apartment field
      else if (data.strApt.endsWith("/")) {
        data.strApt = data.strApt.substring(0,data.strApt.length()-1).trim();
      } else {
        pt = data.strApt.indexOf('/');
        if (pt >= 0) {
          data.strApt = append(data.strApt.substring(0,pt).trim(), " - ", data.strApt.substring(pt+1).trim());
        }
        else body = stripFieldStart(body, "/");
      }

      // Everything from city to time field is the call description
      body = stripFieldStart(body, "*");

      String left = parseCallDesc(body, data);
      if (left != null) {
        parsePlace(left, data);
      }  else {
        data.strCall = body;
      }
      return true;
    }

    return false;
  }

  /**
   * Search message text for known call description
   * @param body message text
   * @param data data object
   * @return remainder of text string if call found, null otherwise
   */
  private String parseCallDesc(String body, Data data) {
    int pt = body.indexOf("<New Call>");
    if (pt >= 0) {
      data.strCall = body.substring(pt+10).trim();
      return body.substring(0, pt).trim();
    }

    for (int j = 0; j<body.length()-2; j++) {
      if (Character.isLetter(body.charAt(j))) {
        String call = CALL_LIST.getCode(body.substring(j), true);
        if (call != null) {
          data.strCall = body.substring(j);
          return body.substring(0, j).trim();
        }
      }
    }
    return null;
  }


  private void parsePlace(String place, Data data) {
    if (data.strChannel.length() == 0) {
      Matcher match = TRAIL_CH_PTN.matcher(place);
      if (match.matches()) {
        parsePlace2(match.group(1), data);
        data.strChannel = match.group(2);
        parsePlace2(match.group(3), data);
        return;
      }
    }
    parsePlace2(place, data);
  }

  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*?)\\b(?:UNIT|APT|(?=BLDG)) *(.*)");

  private void parsePlace2(String place, Data data) {
    Matcher match = PLACE_APT_PTN.matcher(place);
    if (match.matches()) {
      place = match.group(1).trim();
      data.strApt = append(data.strApt, "-", match.group(2));
    }
    data.strPlace = append(data.strPlace, " - ", place);
  }

  private static final String[] ESCAPE_CODES = new String[]{
    "/", "\\s",
    "(", "\\lp",
    ")", "\\rp",
    "[", "\\lb",
    "]", "\\rb"
  };

  private static String escape(String fld) {
    for (int j = 0; j<ESCAPE_CODES.length; j+=2) {
      fld = fld.replace(ESCAPE_CODES[j], ESCAPE_CODES[j+1]);
    }
    return fld;
  }

  private static String unescape(String fld) {
    for (int j = 0; j<ESCAPE_CODES.length; j+=2) {
      fld = fld.replace(ESCAPE_CODES[j+1], ESCAPE_CODES[j]);
    }
    return fld;
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "EXIT 64",  "41.823335, -72.499277",
      "EXIT 66",  "41.833940, -72.463705",
      "EXIT 67",  "41.854601, -72.429307",
      "EXIT 65",  "41.826197, -72.487915"

  });

  private String cvtUnitCodes(String units) {
    StringBuilder sb = new StringBuilder();
    for (String unit : units.split(",")) {
      unit = convertCodes(unit.trim(), UNIT_CODES);
      if (sb.length() > 0) sb.append(',');
      sb.append(unit);
    }
    return sb.toString();
  }

  private static final Properties UNIT_CODES = buildCodeTable(new String[]{
      "41CT",     "Chief_Tone",
      "41GT123",  "Stations_123",
      "41GT45",   "Stations_45",
      "41OT",     "Officers",
      "41FP",     "Fire_Police"
   });

  private static final CodeSet CALL_LIST = new CodeSet(
      "<New Call>",
      "Active Violence/Shooter",
      "Aircraft Accident",
      "ALS - DIAL",
      "ALS",
      "Appliance Fire",
      "Appliance Malfunction",
      "AREA OF DOT GARAGE",
      "BLS",
      "BIOLER ROOM Smoke In Building-Commercial",
      "Bomb Threat",
      "Brush Fire",
      "CARDIAC ARREST",
      "Cardiac Arrest",
      "Chimney Fire",
      "CO No Symptoms",
      "CO With Symptoms",
      "Cover Assignment",
      "Diesel Fuel Spill",
      "Dumpster/Debris Fire",
      "Electrical Fire",
      "Fire Alarm - Commercial",
      "Fire Alarm - Residential",
      "Fire Alarm",
      "Fire Alarm-Commercial",
      "Fire Alarm-Residential",
      "Fuel Spill",
      "Gasoline Spill",
      "Hazardous Materials",
      "Lift Assist",
      "LPG Natural Gas/Propane Leak Exterior",
      "LPG / Natural Gas Leak Interior",
      "LPG / Natural Gas Leak Exterior",
      "Machinery Entrapment",
      "Mutual Aid Fire",
      "Natural Gas/Propane Leak",
      "OFFICER CALL TN.",
      "Officer Call",
      "Outside Fire",
      "Search & Rescue",
      "Search and Rescue",
      "Service Call",
      "Smoke Detector Activation",
      "Smoke In Building",
      "Smoke In Building-Commercial",
      "Smoke In Building-Residential",
      "Smoke in the Building - Commercial",
      "Smoke in the Building - Residential",
      "Smoke/Odor Investigation",
      "Standby",
      "Structure Fire",
      "Structure Fire - Commercial",
      "Structure Fire - Residential",
      "test call only",
      "THIS IS ONLY A TEST",
      "Tree/Wires Down",
      "Unknown Type Fire",
      "Vehicle Accident",
      "Vehicle Accident - DIAL",
      "Vehicle Accident / Head On",
      "Vehicle Accident W/ ALS",
      "Vehicle Accident W/ ALS HEAD INJ",
      "Vehicle Accident W/ Extrication",
      "Vehicle Accident W/O Injuries",
      "Vehicle Accident w/o Injury",
      "Vehicle Accident/HeadOn",
      "Vehicle Fire",
      "Water Rescue",
      "Wires Burning/Arcing"
  );
}
