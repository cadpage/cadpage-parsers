package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ORTillamookCountyParser extends SmartAddressParser {
  
  public ORTillamookCountyParser() {
    super(CITY_LIST, "TILLAMOOK COUNTY", "OR");
    setFieldList("UNIT CALL ADDR APT PLACE CITY GPS INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ALDER COVE",
        "BEAVER DAM",
        "BEULAH REED",
        "BEWLEY CREEK",
        "BIG TROUT",
        "CAPE KIWANDA BEACH ACCESS",
        "CAPE KIWANDA",
        "CAPE LOOKOUT",
        "CEDAR CREEK",
        "CEDAR SPRINGS",
        "DEL MONTE",
        "DORY POINTE",
        "EAST BEAVER CREEK",
        "FALCON COVE",
        "FALL CREEK",
        "FARMER CREEK",
        "GODS VALLEY",
        "HAPPY CAMP",
        "HERON VIEW",
        "KILCHIS FOREST",
        "KILCHIS RIVER",
        "LIARS LAIR",
        "LITTLE NESTUCCA RIVER",
        "LONG PRAIRIE",
        "MAROLF LOOP",
        "MAXWELL MOUNTAIN",
        "MCCORMICK LOOP",
        "MOON CREEK",
        "MUESIAL CREEK",
        "NEAHKAHNIE CREEK",
        "NECARNEY CITY",
        "NESKOWIN TRACE",
        "NESTUCCA RIDGE",
        "NESTUCCA RIVER",
        "NETARTS BAY",
        "NETARTS BOAT BASIN",
        "PACIFIC OVERLOOK",
        "PINE RIDGE",
        "SLAB CREEK",
        "SOLLIE SMITH",
        "SOUTH BEACH",
        "VISTA VIEW",
        "WEE WILLIE",
        "WEST END OF CRAB",
        "WHEELER MOHLER",
        "WHISKEY CREEK",
        "WILSON RIVER"
    );
    setupSpecialStreets("5TH STREET LOOP", "5TH STREET LP");
    setupDoctorNames("SOANS");
    addInvalidWords("IN", "_");
  }
  
  @Override
  public String getFilter() {
    return "5038122399@vzwpix.com";
  }
  
  private static final Pattern LEAD_UNIT_PTN = Pattern.compile("(?:\\d{2,4} )+");
  private static final Pattern GPS_PTN = Pattern.compile(" *(?:LAT/LONG?|/)? *\\b([-+]?\\d{2,3}\\.\\d{5,})[,~ ]+([-+]?\\d{2,3}\\.\\d{5,})\\b *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("!")) return false;
    
    Matcher match = GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(1)+','+match.group(2), data);
      body = body.substring(0,match.start()) + ' ' + body.substring(match.end());
    }
  
    String info = null;
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      info = body.substring(pt+1);
      body = body.substring(0,pt).trim();
    }
    
    match = LEAD_UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group().trim();
      body = body.substring(match.end()).trim();
    }
    
    pt = body.indexOf("<UNKNOWN>");
    if (pt >= 0) {
      data.strCall = body.substring(0,pt).trim();
      data.strAddress = "<UNKNOWN>";
      data.strSupp = body.substring(pt+9).trim();
    }
    
    else {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_EMPTY_ADDR_OK | FLAG_RECHECK_APT | FLAG_IGNORE_AT | FLAG_CROSS_FOLLOWS, body, data);
      if (data.strAddress.length() == 0 && data.strCity.length() == 0) return false;
      data.strSupp = getLeft();
    }
    
    if (info != null) {
      for (String line : info.split("\n")) {
        data.strSupp = append(data.strSupp, "\n", line.trim());
      }
    }
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    String city2 = CITY_PLACE_TABLE.getProperty(city.toUpperCase());
    if (city2 != null) city = city2;
    return city;
  }
  private static final Properties CITY_PLACE_TABLE = buildCodeTable(new String[]{
      "BLAINE",           "Beaver",
      "SANDLAKE",         "Cloverdale",
      "CASCADE HEAD",     "Siuslaw National Forest"
  });
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BAY CITY",
    "GARIBALDI",
    "MANZANITA",
    "NEHALEM",
    "ROCKAWAY BEACH",
    "TILLAMOOK",
    "WHEELER",

    // Unincorporated communities and CDPs
    "BARVIEW",
    "BAYOCEAN",
    "BAYSIDE GARDENS",
    "BEAVER",
    "BLAINE",
    "BRIGHTON",
    "CAPE MEARES",
    "CASCADE HEAD",
    "CASTLE ROCK",
    "CLOVERDALE",
    "DOLPH",
    "FAIRVIEW",
    "FOSS",
    "HEBO",
    "HEMLOCK",
    "IDAVILLE",
    "IDIOTVILLE",
    "JORDAN CREEK",
    "LEES CAMP",
    "MANHATTAN BEACH",
    "MEDA",
    "MOHLER",
    "NECARNEY CITY",
    "NEAHKAHNIE",
    "NEAHKAHNIE BEACH",
    "NEDONNA BEACH",
    "NESKOWIN",
    "NETARTS",
    "OCEANSIDE",
    "ORETOWN",
    "PACIFIC CITY",
    "PLEASANT VALLEY",
    "SANDLAKE",
    "TIERRA DEL MAR",
    "TWIN ROCKS",
    "WATSECO",
    "WINEMA",
    "WOODS",
    
    // Clatsop County
    "CLATSOP",
    "CANNON BEACH",
    "HUGG POINT STATE PARK",
    "INDIAN BEACH"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ALARM",
      "ANIMAL",
      "ASSAULT",
      "ASSIST",
      "BURN",
      "BURN COMPLAINTS",
      "CONTACT",
      "DEATH",
      "DISTURBANCE",
      "FIRE",
      "FIRE ALARM",
      "HARASSMENT",
      "HAZ-MAT",
      "HIT AND RUN",
      "INCOMPLETE 911",
      "INFO",
      "JUVENILE",
      "MAN DOWN",
      "MEDICAL",
      "MISSING PERSON",
      "MVA",
      "REC ACCIDENT",
      "RESCUE",
      "ROAD HAZARD",
      "SUICIDAL",
      "SUSPICIOUS",
      "STRUCTURE FIRE",
      "TOW",
      "TRANSPORT",
      "UNCON/NOT BREATHING",
      "UNKNOWN",
      "UTILITY ASSIST",
      "WATER RESCUE"
  );
}
