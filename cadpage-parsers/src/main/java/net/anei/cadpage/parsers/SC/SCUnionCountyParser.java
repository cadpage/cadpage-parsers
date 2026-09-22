package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class SCUnionCountyParser extends SmartAddressParser {

  public SCUnionCountyParser() {
    super(CITY_LIST, "UNION COUNTY", "SC");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setFieldList("CALL CODE ADDR CITY ST APT INFO X GPS");
  }

  @Override
  public String getFilter() {
    return "ledsreport@countyofunion.com";
  }

  private static final Pattern TRAIL_GPS_PTN = Pattern.compile("(.*?) ([-+]?\\d{2,3}\\.\\d{6,} +[-+]?\\d{2,3}\\.\\d{6,})");
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("([^,]*), *([A-Z ]+), *([A-Z]{2}) *(?:\\d{5} *)?(.*?)");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("([^,]*), *([A-Z ]+)\\b *(.*)");
  private static final Pattern CALL_CODE_ADDR_PTN = Pattern.compile("(.*) (S\\d+) +(.*)");
  private static final Pattern NO_INFO_PTN = Pattern.compile("(.+?) None (.*)");
  private static final Pattern TRAIL_X_MARK_PTN = Pattern.compile(" +\\([\\.0-9]+ (?:feet|miles)\\) ");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Please respond to")) return false;

    Matcher match = TRAIL_GPS_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      setGPSLoc(match.group(2), data);
    }

    String callAddr, extra;
    match = ADDR_CITY_ST_PTN.matcher(body);
    if (match.matches()) {
      callAddr = match.group(1).trim();
      data.strCity = match.group(2).trim();
      data.strState = match.group(3);
      extra = match.group(4).trim();
    }
    else if ((match = ADDR_CITY_PTN.matcher(body)).matches()) {
      callAddr = match.group(1).trim();
      String city = match.group(2);
      extra = match.group(3);
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
      if (!data.strCity.isEmpty()) {
        extra = append(city.substring(data.strCity.length()).trim(), " ", extra);
      } else {
        data.strCity = city;
      }
    }
    else return false;

    // Parse call code and address
    match = CALL_CODE_ADDR_PTN.matcher(callAddr);
    int pt;
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strCode = match.group(2).trim();
      parseAddress(stripFieldStart(match.group(3).trim(), "Intersection of "), data);
    } else if ((pt = callAddr.indexOf(" Intersection of ")) > 0) {
      data.strCall = callAddr.substring(0,pt).trim();
      parseAddress(callAddr.substring(pt+17), data);
    } else {
      parseAddress(StartType.START_CALL, FLAG_NO_CITY | FLAG_ANCHOR_END, callAddr, data);
    }

    // Extra contains an apartment, info, and cross street info seperated by blanks.
    // The first two may (or may not) be "None"

    match = NO_INFO_PTN.matcher(extra);
    if (match.matches()) {
      data.strApt = cleanApt(match.group(1).trim());
      data.strCross = match.group(2).trim();
    }

    else {
      match = TRAIL_X_MARK_PTN.matcher(extra);
      if (match.find()) {
        pt = match.start();
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, extra.substring(0,pt), data);
        if (!data.strCross.isEmpty()) {
          data.strCross = data.strCross + extra.substring(pt);
          extra = getStart();
        }
      }

      match = INFO_BRK_PTN.matcher(extra);
      if (!match.find()) return false;
      data.strApt = cleanApt(extra.substring(0,match.start()));
      extra = extra.substring(match.end());
      data.strSupp = INFO_BRK_PTN.matcher(extra).replaceAll("\n");
    }
    return true;
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|UNIT|SUITE|LOT) +(\\S+)", Pattern.CASE_INSENSITIVE);
  private String cleanApt(String apt) {
    if (apt.equals("None")) return "";
    Matcher match = APT_PTN.matcher(apt);
    if (match.matches()) apt = match.group(1);
    return apt;
  }

  private static final String[] MWORD_STREET_LIST = new String[] {
      "BLUE RIDGE",
      "BOB LITTLE",
      "BROWNS CREEK CHURCH",
      "BRUCE O WILSON",
      "BUFFALO WEST SPRINGS",
      "HOPE CHURCH",
      "INDUSTRIAL PARK",
      "JONESVILLE LOCKHART",
      "MAID MARION",
      "MT TABOR CHURCH",
      "MT VERNON",
      "MUD BRIDGE",
      "PEACH ORCHARD",
      "PEACH SHED",
      "PHILIPPI CHURCH",
      "PINE FOREST",
      "PUMP STATION",
      "SANTUC CARLISLE",
      "SULPHUR SPRINGS",
      "VERNON FOSTER",
      "WHITLOCK LAKE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ALLERGIC/ANAPHYLACTIC",
      "ALTERED MENTAL STATUS",
      "ARGUMENT/DISTURBANCE 1049",
      "ASSAULT",
      "CHEST PAIN/INJURY",
      "COMMERCIAL FIRE ALARM",
      "CRUSHING",
      "DEAD ON ARRIVAL",
      "DIABETIC REACTION",
      "DIZZINESS/WEAKNESS",
      "DRUG OVERDOSE",
      "ELECTRICAL FIRE",
      "FAINT",
      "FALL",
      "FRACTURE",
      "GAS ODOR COMPLAINT",
      "GRASS/WOODS FIRE",
      "HEAD INJURY",
      "HEART PROBLEM",
      "HEMORRHAGE",
      "HIT & RUN",
      "HYPERTENSION",
      "LANDING ZONE",
      "LIFTING ASSISTANCE",
      "MEDICAL ALARM",
      "MVA/INJURY",
      "OB/EMERG",
      "PAIN",
      "POST OP COMPLICATIONS",
      "POWER LINES DOWN",
      "PSYCHIATRIC/BEHAVORIAL",
      "RESIDENTIAL FIRE ALARM",
      "RESPIRATORY DISTRESS",
      "SEIZURE",
      "SHOOTING INCIDENT",
      "STOLEN VEHICLE",
      "STOMACH/DIGESTIVE",
      "STROKE/CVA/TIA",
      "SUSPICIOUS PERSON",
      "TREES IN ROADWAY",
      "UNKNOWN PROBLEM",
      "UNRESPONSIVE",
      "URINARY PROBLEM",
      "VEHICLE FIRE",
      "WRECK/MVA"
  );

  private static final String[] CITY_LIST = new String[] {

      // City
      "UNION",

      // Towns
      "CARLISLE",
      "JONESVILLE",
      "LOCKHART",

      // Census-designated places
      "BUFFALO",
      "MONARCH MILL",

      // Unincorporated communities
      "CROSS KEYS",
      "SANTUC",
      "FISH DAM",
      "BOGANSVILLE",
      "GOSHEN HILL",
      "PINCKNEY",

      // Newberry County
      "ENOREE",
      "WHITMIRE",

      // Spartenburg County
      "PACOLET"
  };
}
