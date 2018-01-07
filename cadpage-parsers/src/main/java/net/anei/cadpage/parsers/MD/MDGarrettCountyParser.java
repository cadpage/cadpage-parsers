package net.anei.cadpage.parsers.MD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MDGarrettCountyParser extends FieldProgramParser {
  
  public MDGarrettCountyParser() {
    super(CITY_LIST, "GARRETT COUNTY", "MD", 
          "ADDR CITY? ( X | X1 X2 | ) CALL CALL2/SDS+? INFO/SDS+? UNIT_TIME! END");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MULTI_WORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "garrettco911@garrettcounty.org,garrettcounty911@gc911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Check for old times report
    if (subject.startsWith("Times - ")) {
      return parseRunReport(body, data);
    }
    
    // Try to parse new dash delimited field format
    String[] flds = body.replace(" - - ", " - ").split(" - ");
    if (flds.length >= 3) {
      String prefix = null;
      int pt = flds[0].indexOf('\n');
      if (pt >= 0) { 
        prefix = flds[0].substring(0,pt).trim();
        flds[0] = flds[0].substring(pt+1).trim();
      }
      if (!parseFields(flds, data)) return false;
      if (prefix != null) {
        data.msgType = MsgType.RUN_REPORT;
        data.strCall = append(prefix, " - ", data.strCall);
      }
      return true;
    }
    return false;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField("\\[ *(.*?) *\\]", true);
    if (name.equals("X1")) return new MyCrossField("\\[ *(.*?)", true);
    if (name.equals("X2")) return new MyCrossField("(.*?) *\\]", true);
    if (name.equals("CALL2")) return new CallField("HOT|COLD|[AB]LS", true);
    if (name.equals("UNIT_TIME")) return new MyUnitTimeField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      while (field.startsWith("*")) field = field.substring(1).trim();
      Parser p = new Parser(field);
      String apt = p.getLastOptional('#');
      data.strPlace = p.getLastOptional(',');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    
    public MyCrossField(String pattern, boolean required) {
      super(pattern, required);
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return false;
    }
  }

  private static final Pattern UNIT_TIME_PTN = Pattern.compile("([ A-Z0-9]+) (\\d\\d:\\d\\d)");
  private class MyUnitTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1).trim();
      data.strTime = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "UNIT TIME";
    }
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("CAD:(\\S+) +([^,]+), *(.*?)  Unit: *(\\S+) *(.*)");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile(" (?>=[A-Z]+:)");

  private boolean parseRunReport(String body, Data data) {
    setFieldList("UNIT ID CALL ADDR APT CITY ST PLACE UNIT INFO");
    data.msgType = MsgType.RUN_REPORT;
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (!match.matches()) {
      data.strSupp = body;
      return true;
    }
    data.strCallId = match.group(1);
    data.strCall = match.group(2).trim();
    parseCallAddress(false, match.group(3).trim(), data);
    data.strUnit = match.group(4);
    data.strSupp = TIMES_BRK_PTN.matcher(match.group(5).trim()).replaceAll("\n");
    return true;
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:FLOOR|BLDG|(APT|RM)) *(.*)");
  private static final Pattern CALL_ADDR_PTN = Pattern.compile("(.*?) (?:@!|[^\\p{ASCII}]+ |\\*|(\\d{2,3}[A-Z]\\d{2}[A-Z]?) )[\\* ]*(.*)");

  private void parseCallAddress(boolean inclCall, String sCallAddr, Data data) {
    sCallAddr = stripFieldEnd(sCallAddr, "*");
    Parser p = new Parser(sCallAddr);
    String place;
    while ((place = p.getLastOptional("[@")).length() > 0) {
      data.strPlace = append(place, " - ", data.strPlace);
    }
    String apt = p.getLastOptional("[APT ");
    if (apt.length() == 0) apt = p.getLastOptional("[#");
    if (apt.length() == 0) apt = p.getLastOptional('[');
    apt = stripFieldEnd(apt, "]");
    
    String city = p.getLastOptional(',');
    if (city.equals("PA")) {
      data.strState = data.strCity;
      city = p.getLastOptional(',');
    }
    if (city.endsWith(" PA")) {
      data.strState = "PA";
      city = city.substring(0,city.length()-3).trim();
    }
    if (city.length() > 0) {
      Matcher match = APT_PTN.matcher(city);
      if (match.matches()) {
        if (match.group(1) != null) city = match.group(2);
        apt = append(city, "-", apt);
      } else {
        data.strCity = city;
      }
    }
        
    sCallAddr = p.get();
    
    if (sCallAddr.endsWith(" PA")) {
      data.strState = "PA";
      sCallAddr = sCallAddr.substring(0,sCallAddr.length()-3).trim();
    }

    StartType st;
    int flags = FLAG_ANCHOR_END;
    if (!inclCall) {
      st = StartType.START_ADDR;
    } else {
      Matcher match = CALL_ADDR_PTN.matcher(sCallAddr);
      if (match.matches()) {
        data.strCall = match.group(1);
        data.strCode =  append(getOptGroup(match.group(2)), "/", data.strCode);
        sCallAddr =  match.group(3);
        st = StartType.START_ADDR;
      }
      else {
        st = StartType.START_CALL;
        flags |= FLAG_START_FLD_REQ;
      }
    }
    sCallAddr = stripFieldStart(sCallAddr, "@");
    sCallAddr = sCallAddr.replace('@', '&');
    if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
    parseAddress(st, flags, sCallAddr, data);
    data.strAddress = stripFieldEnd(data.strAddress, "-");
    data.strApt = append(data.strApt, "-", apt);
    
    city = data.strCity;
    if (city.length() > 0) {
      city = stripFieldEnd(city, " TWP");
      city = stripFieldEnd(city, " TOWNSHIP");
      if (PA_CITY_SET.contains(city)) data.strState = "PA";
    }
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 TRANSFER AGC",
      "ABDOMINAL PAIN",
      "ALARM PD",
      "ALLERGIC REACTION",
      "AMBULANCE ASSIST",
      "AMBULANCE ASSIST LIFTING",
      "ASSAULT",
      "BACK PAIN",
      "BEHAVIORAL PROBLEM",
      "BEHAVIORAL PROBLEM PD",
      "BLEEDING",
      "BOAT FIRE",
      "BOATING ACCIDENT",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "BURNS",
      "CARBON MONOXIDE",
      "CARDIAC ARREST",
      "CARDIAC EMERGENCY",
      "CHOKING",
      "CITIZEN ASSIST/SERVICE CALL",
      "CHEST PAIN",
      "CO ALARM",
      "CONVULSIONS",
      "DECEASED PERSON",
      "DIABETIC EMERGENCY",
      "DIABETIC PROBLEMS",
      "DIFFICULTY BREATHING",
      "DISTURBANCE / NUISANCE",
      "DOMESTIC DISTURBANCE",
      "ELECTRICAL FIRE",
      "EMERG PUBLIC SERV",
      "FALL INJURY",
      "FALLS",
      "FIRE ALARM",
      "FIRE ALARM/COMMERCIAL",
      "FIRE ALARM/RESIDENTIAL",
      "FLUE FIRE",
      "FUEL SPILL",
      "FUEL SPILL SMALL",
      "FUEL SPILL UNKNOWN AMOUNT",
      "GAS LEAK / GAS ODOR",
      "HAZMAT",
      "HEMORRHAGE",
      "INTERFACILITY CARE",
      "INVESTIGATION",
      "LANDING ZONE",
      "LIFT ASSIST NON EMERG",
      "MACHINERY FIRE",
      "MEDIC ASSIST",
      "MEDICAL ALARM",
      "MEDICAL EMERGENCY",
      "MOTOR VEHICLE COLLISION",
      "MVC",
      "MVC ADVISED",
      "MVC ENTRAPMENT",
      "MVC INJURIES",
      "MVC MOTORCYCLE",
      "MVC NO INJURIES",
      "MVC ROLLOVER",
      "MVC UNK INJURIES",
      "ODOR INVESTIGATION",
      "OUTBUILDING FIRE",
      "OUTSIDE FIRE",
      "OVERDOSE",
      "PARADE",
      "PEDESTRIAN STRUCK ALS",
      "PHYSICAL RESCUE",
      "POWER LINES DOWN",
      "PROPANE ALARM",
      "PROPANE TANK LEAK",
      "PSYCHIATRIC EMERGENCY",
      "PUBLIC SERVICE",
      "SEARCH DETAIL",
      "SEIZURES",
      "SICK PERSON",
      "SMOKE IN BUILDING",
      "SMOKE INVESTIGATION",
      "SMOKE INVESTIGATION / OUTSIDE",
      "STAND BY",
      "STROKE / CVA",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE BARN",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE RESIDENTIAL",
      "STRUCTURE FIRE WORKING",
      "TEST CALL",
      "TRACTOR TRAILER FIRE",
      "TRAINING",
      "TRAFFIC ACCIDENT/HIT-RUN",
      "TRAFFIC CONTROL",
      "TANKER ASSIST",
      "TRANSPORT",
      "TRAUMA / INJURIES",
      "TRAUMATIC INJURY",
      "TREE DOWN",
      "UNATTENDED CONT BURN",
      "UNCONSCIOUS",
      "UNCONSCIOUS/FAINTING",
      "UNK TYPE FIRE",
      "UNKNOWN PROBLEM",
      "UNKNOWN PROBLEM (MAN DOWN)",
      "UTILITY LINES DOWN",
      "VEHICLE FIRE",
      "WATER RESCUE"
  );
  private static final String[] MULTI_WORD_STREET_LIST = new String[]{
    "ACCIDENT BITTINGER",
    "ADVENTURE SPORTS",
    "AIKEN MILLER",
    "ANN KAHL",
    "AVILTON LONACONING",
    "BEALL SCHOOL",
    "BEAR HILL",
    "BIG RUN",
    "BILL BEITZEL",
    "BLOOMING ROSE",
    "CHESNUT RIDGE",
    "CHESTNUT RIDGE",
    "COON HOLLOW",
    "CRANBERRY SWAMP",
    "DORSEY HOTEL",
    "DRY RUN",
    "EAST OAK",
    "HARE HOLLOW",
    "HARVEY WINTERS",
    "HOYES SANG RUN",
    "LAKE POINTE",
    "LITTLE POND",
    "MAPLE GROVE",
    "MARSH HILL",
    "MAYHEW INN",
    "MAYNARDIER RIDGE",
    "NOAH FRAZEE",
    "OAKLAND SANG RUN",
    "PEAT MOSS",
    "PERGIN FARM",
    "PINEY RUN",
    "PRIMO VISTA",
    "ROCK LODGE",
    "ROCK LODGE",
    "SAINT JOHNS ROCK",
    "SAM CROW",
    "SAND FLAT",
    "SAND SPRING",
    "SANG RUN",
    "SHORT MILE",
    "SKY VALLEY",
    "SMITH POINTE",
    "SQUIRE FIKE",
    "THOUSAND ACRES",
    "TRAP RUN",
    "WHITE FAWN",
    "WHITE OAK",
    "WHITE ROCK",
    "WILDWOOD ESTATES",
    "WOLF ACRES"
  };
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "ACCIDENT",
    "DEER PARK",
    "FRIENDSVILLE",
    "GRANTSVILLE",
    "KITZMILLER",
    "LOCH LYNN HEIGHTS",
    "MOUNTAIN LAKE PARK",
    "OAKLAND",

    // Census-designated places
    "BLOOMINGTON",
    "CRELLIN",
    "FINZEL",
    "GORMAN",
    "HUTTON",
    "JENNINGS",
    "SWANTON",

    // Unincorporated communities
    "ALTAMONT",
    "ASHER GLADE",
    "AVILTON",
    "BETHEL",
    "BEVANSVILLE",
    "BITTINGER",
    "BLOOMING ROSE",
    "CASSELMAN",
    "EAST VINDEX",
    "ELDER HILL",
    "ENGLE MILL",
    "FAIRVIEW",
    "FLOYD",
    "FORT PENDLETON",
    "FOXTOWN",
    "FRICKS CROSSING",
    "FROSTBURG",
    "GORTNER",
    "GRAVEL HILL",
    "GREEN GLADE",
    "HAZELHURST",
    "HERRINGTON MANOR",
    "HI-POINT",
    "HIGH POINT",
    "HOYES",
    "HOYES RUN",
    "KAESE MILL",
    "KEARNEY",
    "KEMPTON",
    
    // Parks
    "DEEP CREEK",
    "MT LAKE PARK",

    // Somerset county, PA
    "ADDISON",
    "BENSON",
    "BERLIN",
    "BOSWELL",
    "CALLIMONT",
    "CASSELMAN",
    "CENTRAL CITY",
    "CONFLUENCE",
    "GARRETT",
    "HOOVERSVILLE",
    "INDIAN LAKE",
    "JENNERSTOWN",
    "MEYERSDALE",
    "NEW BALTIMORE",
    "NEW CENTERVILLE",
    "PAINT",
    "ROCKWOOD",
    "SALISBURY",
    "SEVEN SPRINGS",
    "SHANKSVILLE",
    "SOMERSET",
    "STOYSTOWN",
    "URSINA",
    "WELLERSBURG",
    "WINDBER",

    "ADDISON TWP",
    "ALLEGHENY TWP",
    "BLACK TWP",
    "BROTHERSVALLEY TWP",
    "CONEMAUGH TWP",
    "ELK LICK TWP",
    "FAIRHOPE TWP",
    "GREENVILLE TWP",
    "JEFFERSON TWP",
    "JENNER TWP",
    "LARIMER TWP",
    "LINCOLN TWP",
    "LOWER TURKEYFOOT TWP",
    "MIDDLECREEK TWP",
    "MILFORD TWP",
    "NORTHAMPTON TWP",
    "OGLE TWP",
    "PAINT TWP",
    "QUEMAHONING TWP",
    "SHADE TWP",
    "SOMERSET TWP",
    "SOUTHAMPTON TWP",
    "STONYCREEK TWP",
    "SUMMIT TWP",
    "UPPER TURKEYFOOT TWP",
    
    "ADDISON TOWNSHIP",
    "ALLEGHENY TOWNSHIP",
    "BLACK TOWNSHIP",
    "BROTHERSVALLEY TOWNSHIP",
    "CONEMAUGH TOWNSHIP",
    "ELK LICK TOWNSHIP",
    "FAIRHOPE TOWNSHIP",
    "GREENVILLE TOWNSHIP",
    "JEFFERSON TOWNSHIP",
    "JENNER TOWNSHIP",
    "LARIMER TOWNSHIP",
    "LINCOLN TOWNSHIP",
    "LOWER TURKEYFOOT TOWNSHIP",
    "MIDDLECREEK TOWNSHIP",
    "MILFORD TOWNSHIP",
    "NORTHAMPTON TOWNSHIP",
    "OGLE TOWNSHIP",
    "PAINT TOWNSHIP",
    "QUEMAHONING TOWNSHIP",
    "SHADE TOWNSHIP",
    "SOMERSET TOWNSHIP",
    "SOUTHAMPTON TOWNSHIP",
    "STONYCREEK TOWNSHIP",
    "SUMMIT TOWNSHIP",
    "UPPER TURKEYFOOT TOWNSHIP"

  };
  
  private static final Set<String> PA_CITY_SET = new HashSet<String>(Arrays.asList(
      "ADDISON",
      "BENSON",
      "BERLIN",
      "BOSWELL",
      "CALLIMONT",
      "CASSELMAN",
      "CENTRAL CITY",
      "CONFLUENCE",
      "GARRETT",
      "HOOVERSVILLE",
      "INDIAN LAKE",
      "JENNERSTOWN",
      "MEYERSDALE",
      "NEW BALTIMORE",
      "NEW CENTERVILLE",
      "PAINT",
      "ROCKWOOD",
      "SALISBURY",
      "SEVEN SPRINGS",
      "SHANKSVILLE",
      "SOMERSET",
      "STOYSTOWN",
      "URSINA",
      "WELLERSBURG",
      "WINDBER",

      "ADDISON",
      "ALLEGHENY",
      "BLACK",
      "BROTHERSVALLEY",
      "CONEMAUGH",
      "ELK LICK",
      "FAIRHOPE",
      "GREENVILLE",
      "JEFFERSON",
      "JENNER",
      "LARIMER",
      "LINCOLN",
      "LOWER TURKEYFOOT",
      "MIDDLECREEK",
      "MILFORD",
      "NORTHAMPTON",
      "OGLE",
      "PAINT",
      "QUEMAHONING",
      "SHADE",
      "SOMERSET",
      "SOUTHAMPTON",
      "STONYCREEK",
      "SUMMIT",
      "UPPER TURKEYFOOT"
  ));
}
