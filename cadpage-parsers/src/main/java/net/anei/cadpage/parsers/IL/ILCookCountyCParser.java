package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ILCookCountyCParser extends SmartAddressParser {
  
  private static final Pattern CANCEL = Pattern.compile("CANCEL (.*?); ([ A-Z]+)(?:\\(S\\) +\\(N\\) *(.*))?");
  private static final Pattern MASTER = Pattern.compile("(?:FYI|Update): (?:([A-Z]{1,3}FD) )?(.*?)(?: ([A-Z0-9][A-Z0-9,]*\\d))?");
  
  public ILCookCountyCParser() {
    super(CITY_CODES, "COOK COUNTY", "IL");
    setupCities(CITY_LIST);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setFieldList("SRC CALL PLACE ADDR APT CITY UNIT");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = CANCEL.matcher(body);
    if (match.matches()) {
      data.strCall = "CANCEL";
      parseAddress(match.group(1).trim(), data);
      data.strCity = convertCodes(match.group(2).trim(), CITY_CODES);
      data.strPlace = getOptGroup(match.group(3));
      return true;
    }
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = getOptGroup(match.group(1));
    String addr = match.group(2).trim();
    data.strUnit = getOptGroup(match.group(3));
    parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
    return true;
  }
  
  @Override
  protected boolean isHouseNumber(String token) {
    if (NUMBER_CODE_PTN1.matcher(token).matches()) return true;
    return super.isHouseNumber(token);
  }
  private static final Pattern NUMBER_CODE_PTN1 = Pattern.compile("(\\d+)-[A-Z]{1,4}");
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = NUMBER_CODE_PTN2.matcher(addr).replaceFirst("$1 ");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern NUMBER_CODE_PTN2 = Pattern.compile("^(\\d+)-[A-Z]{1,4} ");

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT WITH INJURY",
      "AMBULANCE ABDOMINAL PROBLEM",
      "AMBULANCE ALLERGIC REACTION",
      "AMBULANCE ANIMAL BITE",
      "AMBULANCE ARREST",
      "AMBULANCE ASSIST",
      "AMBULANCE BATTERY VICTIM",
      "AMBULANCE BLEEDING",
      "AMBULANCE BREATHING PROBLEMS",
      "AMBULANCE CHEST PAINS",
      "AMBULANCE FAINTING",
      "AMBULANCE FALL VICTIM",
      "AMBULANCE HEADACHE",
      "AMBULANCE INDUSTRIAL ACCIDENT",
      "AMBULANCE INTOXICATED PATIENT",
      "AMBULANCE NON SPECIFIC ILLNESS",
      "AMBULANCE PREGNANCY",
      "AMBULANCE PSYCHIATRIC PATIENT",
      "AMBULANCE SEIZURE/CONVULSIONS",
      "AMBULANCE STROKE",
      "AMBULANCE TRAUMATIC INJURY",
      "AMBULANCE UNCONSCIOUS",
      "AMBULANCE UNKNOWN PROBLEM",
      "AUTOMATIC MUTUAL AID",
      "CANCEL",
      "CO INVESTIGATION",
      "DUMPSTER FIRE",
      "FIRE ALARM",
      "FIRE STRUCTURE",
      "LIFT ASSIST",
      "LIFT ASSIST RESIDENCE",
      "MA21 AUTOMATIC MUTUAL AID",
      "MEDICAL ALARM",
      "NATURAL GAS LEAK INSIDE",
      "SPECIAL SERVICE",
      "TROUBLE ALARM",
      "VEHICLE FIRE",
      "WIRES DOWN OR ARCING"
  );
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "CENTRAL PARK",
    "CLIFTON PARK",
    "OAK PARK",
    "ST LOUIS",
    "WILLOW SPRINGS"
  };
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "BERWYN",
      "BLUE ISLAND",
      "BURBANK",
      "CALUMET CITY",
      "CHICAGO",
      "CHICAGO HEIGHTS",
      "COUNTRY CLUB HILLS",
      "COUNTRYSIDE",
      "DES PLAINES",
      "ELGIN",
      "ELMHURST",
      "EVANSTON",
      "HARVEY",
      "HICKORY HILLS",
      "HOMETOWN",
      "MARKHAM",
      "NORTHLAKE",
      "OAK FOREST",
      "PALOS HEIGHTS",
      "PALOS HILLS",
      "PARK RIDGE",
      "PROSPECT HEIGHTS",
      "ROLLING MEADOWS",
  
      // Town
      "CICERO",
  
      // Villages
      "ALSIP",
      "ARLINGTON HEIGHTS",
      "BARRINGTON",
      "BARRINGTON HILLS",
      "BARTLETT",
      "BEDFORD PARK",
      "BELLWOOD",
      "BENSENVILLE",
      "BERKELEY",
      "BRIDGEVIEW",
      "BROADVIEW",
      "BROOKFIELD",
      "BUFFALO GROVE",
      "BURNHAM",
      "BURR RIDGE",
      "CALUMET PARK",
      "CHICAGO RIDGE",
      "CRESTWOOD",
      "DEER PARK",
      "DEERFIELD",
      "DIXMOOR",
      "DOLTON",
      "EAST DUNDEE",
      "EAST HAZEL CREST",
      "ELK GROVE VILLAGE",
      "ELMWOOD PARK",
      "EVERGREEN PARK",
      "FLOSSMOOR",
      "FORD HEIGHTS",
      "FOREST PARK",
      "FOREST VIEW",
      "FRANKFORT",
      "FRANKLIN PARK",
      "GLENCOE",
      "GLENVIEW",
      "GLENWOOD",
      "GOLF",
      "HANOVER PARK",
      "HARWOOD HEIGHTS",
      "HAZEL CREST",
      "HILLSIDE",
      "HINSDALE",
      "HODGKINS",
      "HOFFMAN ESTATES",
      "HOMEWOOD",
      "INDIAN HEAD PARK",
      "INVERNESS",
      "JUSTICE",
      "KENILWORTH",
      "LA GRANGE",
      "LA GRANGE PARK",
      "LANSING",
      "LEMONT",
      "LINCOLNWOOD",
      "LYNWOOD",
      "LYONS",
      "MATTESON",
      "MAYWOOD",
      "MCCOOK",
      "MELROSE PARK",
      "MERRIONETTE PARK",
      "MIDLOTHIAN",
      "MORTON GROVE",
      "MOUNT PROSPECT",
      "NILES",
      "NORRIDGE",
      "NORTH RIVERSIDE",
      "NORTHBROOK",
      "NORTHFIELD",
      "OAK BROOK",
      "OAK LAWN",
      "OAK PARK",
      "OLYMPIA FIELDS",
      "ORLAND HILLS",
      "ORLAND PARK",
      "PALATINE",
      "PALOS PARK",
      "PARK FOREST",
      "PHOENIX",
      "POSEN",
      "RICHTON PARK",
      "RIVER FOREST",
      "RIVER GROVE",
      "RIVERDALE",
      "RIVERSIDE",
      "ROBBINS",
      "ROSELLE",
      "ROSEMONT",
      "SAUK VILLAGE",
      "SCHAUMBURG",
      "SCHILLER PARK",
      "SKOKIE",
      "SOUTH BARRINGTON",
      "SOUTH CHICAGO HEIGHTS",
      "SOUTH HOLLAND",
      "STEGER",
      "STICKNEY",
      "STONE PARK",
      "STREAMWOOD",
      "SUMMIT",
      "THORNTON",
      "TINLEY PARK",
      "UNIVERSITY PARK",
      "WESTCHESTER",
      "WESTERN SPRINGS",
      "WHEELING",
      "WILLOW SPRINGS",
      "WILMETTE",
      "WINNETKA",
      "WOODRIDGE",
      "WORTH"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEDF", "BEDFORD PARK",
      "BRID", "BRIDGEVIEW",
      "BURB", "BURBANK",
      "EVER", "EVERGREEN PARK",
      "HOME", "HOMETOWN",
      "OAK",  "OAK LAWN",
      "PALO", "PALOS HEIGHTS",
      "SUMM", "SUMMIT",
      "UNIN", "",
      "UNINCORPORATED COOK", "",
      "WORT", "WORTH"
  });
}
