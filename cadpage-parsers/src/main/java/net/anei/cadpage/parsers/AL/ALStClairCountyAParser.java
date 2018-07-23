package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

/**
 * St Clair County, AL
 */
public class ALStClairCountyAParser extends DispatchB3Parser {

  public ALStClairCountyAParser() {
    super("911CENTRAL:", ALStClairCountyBParser.CITY_LIST, "ST CLAIR COUNTY", "AL");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MSTREET_LIST);
    setupSpecialStreets("COLVIN SPRIN");
    setupSaintNames("CLAIR");
    addRoadSuffixTerms("PRKWY");
    removeWords("PIKE");
  }
  
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])/B\\b");
  private static final Pattern SHORT_ADDR_PTN = Pattern.compile("(\\d+ +(?:&|AND) +\\d+) +(.*)", Pattern.CASE_INSENSITIVE);
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("9-1-1CENTRAL:")) {
      subject = "911CENTRAL:" + subject.substring(13);
    }
    if (body.startsWith("9-1-1CENTRAL:")) {
      body = "911CENTRAL:" + body.substring(13);
    }
    body = body.replace('@', '&');
    body = DIR_BOUND_PTN.matcher(body).replaceAll("$1B");
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCall.endsWith(" END OF")) {
      data.strCall = data.strCall.substring(0,data.strCall.length()-7).trim();
      data.strAddress = "END OF " + data.strAddress;
    }
    data.strCity = convertCodes(data.strCity, ALStClairCountyBParser.MISSPELLED_CITY_TABLE);
    
    if (data.strName.length() == 0 && data.strCity.length() == 0) {
      Matcher match = SHORT_ADDR_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strAddress = match.group(1);
        data.strName = match.group(2);
      }
    }
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "DAVIS LAKE",    "SPRINGFIELD"
  });
  
  private static final String[] MSTREET_LIST = new String[]{
    "ANDERSON HILL",
    "ARGO MARGARET",
    "BEASON COVE",
    "BEAVER CREEK",
    "BEAVER RIDGE",
    "BERRY HILL",
    "BILL SMITH",
    "BLACK JACK",
    "BLAIR FARM",
    "BROWN HILL",
    "BUCKS VALLEY",
    "CAMP CREEK",
    "CAMP WINNATASKA",
    "CANOE CREEK",
    "CANOE LAKE",
    "CEDAR CREEK",
    "COOK SPRINGS",
    "COOK SPRINGS CUT OFF",
    "COPPER SPRINGS",
    "COUNTRY LIVING",
    "COUNTRY VIEW",
    "CRAWFORDS COVE",
    "CROOKED CREEK",
    "CURT JARRETT",
    "DOUBLE BRIDGE",
    "EARLY SPRINGS",
    "EDDIE HOUTS",
    "FERN CREEK",
    "FOUR SEASONS",
    "FREEZE MOUNTAIN",
    "FREEZE MT",
    "FROST HOLLOW",
    "G B SANDERS",
    "GENE GLENN",
    "GEORGE CROWE",
    "GREEN BRIAR",
    "HAPPY KNOLL",
    "HAWKS BEND",
    "HICKORY VALLEY",
    "HILL TOP",
    "HONOR KEITH",
    "HOYT HILL",
    "IRA HANKS MEMORIAL",
    "JEAN RIDGE",
    "JOHN OWENS",
    "JOHN RANDALL",
    "KELLY CREEK",
    "LYNCH LAKE",
    "MADDOS FARM",
    "MAJESTIC PINES",
    "MEADOW RIDGE",
    "MINERAL SPRINGS",
    "MISTY PINES",
    "MOUNTAIN VIEW",
    "MT OAKS",
    "MURPHREES VALLEY",
    "NEAL HOUSTON",
    "NELSIE ANN",
    "OAK GROVE",
    "OAK HILL",
    "PARK HILL",
    "PEACEFUL VALLEY",
    "PEARL LAKE",
    "PEBBLE BEACH",
    "PIN OAK",
    "PINEY WOODS",
    "PLEASANT HILLS",
    "PLEASANT VALLEY",
    "RAY WYATT",
    "ROBERTS MILL PND",
    "ROBERTS MILL POND",
    "ROCK CRUSHER",
    "ROCK HAVEN",
    "ROCKY TOP",
    "SHOAL CREEK",
    "SMITH GLEN",
    "SMITH RIDGE",
    "SOUTHERN OAKS",
    "SPORTSMAN LAKE",
    "SPRINGVILLE STATION",
    "SULFUR SPRINGS",
    "SWEET APPLE",
    "TIMBER RIDGE",
    "TOM L SMITH",
    "TOOT HOLDER",
    "VALLEY VIEW",
    "VILLAGE SPRINGS",
    "WASHINGTON VALLEY",
    "WEST AFTER SHANGHAI",
    "WEST JONES VILLAGE",
    "WHITE OAK",
    "WILL KEITH",
    "WOLF CREEK",
    "WOODEN TRACE",
    "WOODY ACRES"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
    "911 HANGUP/OPEN LINE",
    "ALARM",
    "ASSAULT",
    "ASSIST MOTORIST",
    "ASSIST OCCUPANT",
    "ATTEMPTED SUICIDE",
    "CALL BY PHONE",
    "CALL BY PHONE FOR",
    "CHECK WELFARE",
    "CONTROLLED BURN",
    "DEATH",
    "DISTURBANCE",
    "DOMESTIC DISTURBANCE PHYSICAL",
    "DOG COMPLAINT",
    "FIRE ALARM",
    "FIRE CALL",
    "FIRE, OTHER",
    "FYI FOR",
    "HAZARDOUS MATERIAL",
    "HIT AND RUN",
    "INTOXICATED DRIVER",
    "INVESTIGATE SMOKE",
    "KEYS IN VEHICLE",
    "MEDICAL CALL",
    "MEET COMPLAINANT",
    "MENTAL PATIENT",
    "MISSING PERSON",
    "OVERDOSE",
    "PASS ALONG INFORMATION",
    "PATIENT TRANSPORT",
    "PURSUIT",
    "PUBLIC SERVICE",
    "RECKLESS DRIVING",
    "SMELL OF GAS/OTHER ODOR",
    "STRUCTURE FIRE",
    "SUSPICIOUS VEHICLE",
    "TEST CAD EVENT",
    "TRAFFIC STOP",
    "TRAIN WRECK/DERAILMENT",
    "TREE DOWN",
    "VEHICLE FIRE",
    "VERBAL DOMESTIC DISTURBANCE",
    "WATER OUTAGE/SEWER PROBLEM",
    "WILD FIRE",
    "WIRE DOWN",
    "WRECK"
  );
}
