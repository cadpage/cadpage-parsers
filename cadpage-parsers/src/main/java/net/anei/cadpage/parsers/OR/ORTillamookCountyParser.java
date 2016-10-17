package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ORTillamookCountyParser extends SmartAddressParser {
  
  public ORTillamookCountyParser() {
    super(CITY_LIST, "TILLAMOOK COUNTY", "OR");
    setFieldList("CALL ADDR APT PLACE CITY INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BIG TROUT",
        "CAPE KIWANDA",
        "CAPE LOOKOUT",
        "CEDAR CREEK",
        "DORY POINTE",
        "EAST BEAVER CREEK",
        "MOON CREEK",
        "NESTUCCA RIDGE",
        "WHISKEY CREEK"
    );
  }
  
  @Override
  public String getFilter() {
    return "5038122399@vzwpix.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace('\n', ' ');
    int pt = body.length();
    while (true) {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_IGNORE_AT, body.substring(0,pt), data);
      if (data.strCity.length() > 0) break;
      if (!isValidAddress()) return false;
      pt = data.strCall.length();
      data.strCall = data.strAddress = data.strApt = data.strCross = "";
    }
    data.strSupp = getLeft() + body.substring(pt);
    return isValidAddress();
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
    "WOODS"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ASSIST",
      "DISTURBANCE",
      "FIRE",
      "FIRE ALARM",
      "HAZ-MAT",
      "MAN DOWN",
      "MEDICAL",
      "MVA",
      "REC ACCIDENT",
      "ROAD HAZARD",
      "SUICIDAL",
      "TOW",
      "UNKNOWN",
      "WATER RESCUE"
  );
}
