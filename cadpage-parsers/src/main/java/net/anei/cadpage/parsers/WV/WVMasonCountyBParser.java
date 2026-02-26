package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WVMasonCountyBParser extends SmartAddressParser {

  public WVMasonCountyBParser() {
    super(CITY_LIST, "MASON COUNTY", "WV");
    setFieldList("ID CALL ADDR APT CITY ST PLACE INFO");
    setupCallList(CALL_SET);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "CAD@MASONCOUNTYOESWV.GOV";
  }

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(CFS\\d\\d-\\d{6}) +");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern CITY_ST_PLACE_PTN = Pattern.compile("([A-Z ]+), ([A-Z]{2})(?: \\d{5})?\\b(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = LEAD_ID_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end());

    int pt = body.indexOf("\nLegal / Privacy Notice");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (body.endsWith(" None")) {
      body = body.substring(0,body.length()-5).trim();
    } else {
      boolean first = true;
      for (String part : INFO_BRK_PTN.split(body)) {
        if (first) {
          body = part;
          first = false;
        } else {
          data.strSupp = append(data.strSupp, "\n", part);
        }
      }
    }

    pt = body.indexOf(',');
    if (pt < 0) {
      parseAddress(StartType.START_CALL, FLAG_NO_CITY, body, data);
      data.strPlace = getLeft();
    } else {
      parseAddress(StartType.START_CALL, FLAG_NO_CITY | FLAG_ANCHOR_END, body.substring(0,pt).trim(), data);
      body = body.substring(pt+1).trim();
      match = CITY_ST_PLACE_PTN.matcher(body);
      if (match.matches()) {
        data.strCity =  match.group(1).trim();
        data.strState = match.group(2);
        data.strPlace = match.group(3);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
        data.strPlace = getLeft();
      }
    }
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[] {
      "ARCHERY CLUB",
      "ASHTON UPLAND",
      "BEAGLE CLUB",
      "BEAR WALLOW",
      "BURDETTE ADDITION",
      "CEDAR HOLLOW",
      "CHANDLER RIDGE",
      "CHESTNUT RIDGE",
      "CRAB CREEK",
      "FIVE MILE",
      "GILL RIDGE",
      "GRAHAM STATION",
      "GUN CLUB",
      "GUNVILLE RIDGE",
      "HANG UP 565 ESSEX",
      "HANGING ROCK",
      "HANNAN TRACE",
      "HENDERSON 4",
      "IVA DURST",
      "IVY DURST",
      "JERRYS RUN",
      "JIM HILL",
      "JOE ROUSH",
      "JORDAN LANDING",
      "KANAWHA VALLEY",
      "KRODEL PARK",
      "LEON BADEN",
      "LONE OAK",
      "MASON EIGHTY",
      "MOUNT CARMEL",
      "MOUNT ZION",
      "MUD RUN",
      "OHIO RIVER",
      "PINE GROVE",
      "PINE GROVE CHAPE",
      "PLAIN VALLEY",
      "POND BRANCH",
      "REBEL RIDGE",
      "REDMOND RIDGE",
      "ROUSH FERREL",
      "SALT CREEK",
      "SALT CREK",
      "SAND FORK",
      "SIXTEEN MILE",
      "SLEEPY HOLLOW",
      "SLIDING HILL CRE",
      "SLIDING HILL CREEK",
      "SUPPER CLUB",
      "T CUPP",
      "THOMAS RIDGE",
      "THREE MILE",
      "UNION CAMPGROUND",
      "WALDON ROUSH WAY",
      "WATERLOO SMITH CHURCH",
      "WHITE FALCON",
      "WHITE RIDGE",
      "WHITTEN RIDGE",
      "WILD CAT",
      "WOOD SCHOOL",
      "ZID CAMP"
  };


  private static final CodeSet CALL_SET = new CodeSet(
      "324",
      "ABANDONED VEHICLE",
      "ALARM-FIRE",
      "ALARM-MEDICAL",
      "ALSTX",
      "AMS",
      "ARM PAIN",
      "ASSAULT",
      "BLSTX",
      "BRUSH FIRE",
      "CAMP",
      "CO ALARM",
      "CODE",
      "CP",
      "CVA",
      "DEER",
      "DIB",
      "DIZZY",
      "DM",
      "EXPLOSION",
      "FALL",
      "FIRE MUTUAL AID",
      "FIRE UTILITY",
      "FIRE-X",
      "FLU SYMPTOMS",
      "GAS LEAK",
      "GAS ODOR",
      "GENERAL MEDICAL",
      "HARASSMENT",
      "HEAD INJURY",
      "ILLEGAL BURN",
      "LAC",
      "LEG PAIN",
      "LEON",
      "LIFT ASST",
      "LINE DOWN",
      "MEDICAL-X",
      "MVA UNK INJURY",
      "MVA - WITH INJURY",
      "MVA W/O INJURY",
      "NV",
      "ODOR INVESTIGATION",
      "SEIZURE",
      "STRANDED MOTORIST",
      "STRUCTURE FIRE - RESIDENTIAL",
      "SYNCOPE",
      "TEST",
      "TREE",
      "TRESPASS",
      "UNRESPONSIVE",
      "VEHICLE FIRE - COMMERCIAL",
      "VEHICLE FIRE - PASSENGER",
      "WARRANT",
      "WATER LEAK",
      "WATER RESCUE",
      "WEAKNESS",
      "WELFARE",
      "XXXXXXX"
  );

  private static final String[] CITY_LIST = new String[] {

      // City
      "POINT PLEASANT",

      // Towns
      "HARTFORD",
      "LEON",
      "MASON",
      "NEW HAVEN",

      // Magisterial districts
      "ARBUCKLE",
      "CLENDENIN",
      "COLOGNE",
      "COOPER",
      "GRAHAM",
      "HANNAN",
      "LEWIS",
      "ROBINSON",
      "UNION",
      "WAGGENER",

      // Census-designated places
      "APPLE GROVE",
      "CLIFTON",
      "GALLIPOLIS",
      "GALLIPOLIS FERRY",

      // Unincorporated communities
      "AMBROSIA",
      "ARBUCKLE",
      "ARLEE",
      "ASH",
      "ASHTON",
      "BADEN",
      "BEALE",
      "BEECH HILL",
      "BEN LOMOND",
      "CAPEHART",
      "CONDEE",
      "COUCH",
      "DEERLICK",
      "ELMWOOD",
      "FAIRVIEW",
      "FLAT ROCK",
      "GLENWOOD",
      "GRAHAM STATION",
      "GREER",
      "GRIMMS LANDING",
      "GUNVILLE",
      "HENDERSON",
      "HOGSETT",
      "LAKIN",
      "LETART",
      "MAGGIE",
      "MERCERS BOTTOM",
      "MOUNT OLIVE",
      "NAT",
      "PONDLICK",
      "RAYBURN",
      "SASSAFRAS",
      "SOUTHSIDE",
      "SPILMAN",
      "TRIBBLE",
      "UPLAND",
      "WATERLOO",
      "WEST COLUMBIA",
      "WOOD",
      "WYOMA",

      // Cabell County
      "MILTON",

      // Jackson County
      "COTTAGEVILLE",

      // Putnam County
      "FRAZIERS BOTTOM"
  };
}
