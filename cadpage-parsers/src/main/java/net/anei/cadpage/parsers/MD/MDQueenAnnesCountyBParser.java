package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MDQueenAnnesCountyBParser extends FieldProgramParser {

  public MDQueenAnnesCountyBParser() {
    super(CITY_CODES, "QUEEN ANNES COUNTY", "MD",
          "( CT:ADDR/S0L! BOX:BOX! DUE:UNIT! END " +
          "| Inc:ID! Call_Type:CODE! Call_Desc:CALL! Date:DATE! Time_Recv:TIME! Time_Clear:TIME_CLR! Box_Area:BOX! Station:SRC! Priority:PRI! Lat:GPS1! Long:GPS2! City:CITY! Location:ADDR/S0! Units:UNIT! UNIT/S+ Rmk:INFO/N+ From_CAD_User:SKIP )");
    setupCallList(CALL_LIST);
    addNauticalTerms();
    removeWords("NEW");
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupProtectedNames("4-H PARK");
  }

  @Override
  public String getFilter() {
    return "qac911@gmail.com,@c-msg.net";
  }

  private static final Pattern DELIM = Pattern.compile("[\t\n]+");
  private static final Pattern MARKER = Pattern.compile("(?:QA911com:|)(\\d{8}) +(?=CT:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("From QAC DES")) {
      if (!body.startsWith("Inc:")) return false;
      if (!parseFields(DELIM.split(body), data)) return false;
    }

    else {
      Matcher match = MARKER.matcher(body);
      if (!match.lookingAt()) return false;

      data.strCallId = match.group(1);
      body = body.substring(match.end());
      if (!super.parseMsg(body, data)) return false;
    }

    fixMutualAidCalls(data);
    return true;
  }

  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }

  private static final Pattern MA_BOX_PTN = Pattern.compile("[A-Z]{4}");
  private static final Pattern MA_TO_CITY_PTN = Pattern.compile("MUTUAL AID.* TO ([A-Z]+)");

  static void fixMutualAidCalls(Data data) {
    Matcher match;
    // See if this is a mutual aid call that we can extract a city name from
    match = MA_BOX_PTN.matcher(data.strBox);
    if (match.matches()) {
      String city = MA_CITY_TABLE.getProperty(data.strBox);
      if (city != null) {
        data.strCity = city;
      }
      if (data.strCall.startsWith("MUTUAL AID") && !data.strCall.contains(" TO ")) {
        data.strCall = data.strCall + " TO " + data.strBox;
      }
    }
    if (data.strCity.length() == 0) {
      match = MA_TO_CITY_PTN.matcher(data.strCall);
      if (match.matches()) {
        String city = MA_CITY_TABLE.getProperty(match.group(1));
        if (city != null) data.strCity = city;
      }
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME_CLR")) return new MyTimeClearedField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyTimeClearedField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0) {
        data.msgType = MsgType.RUN_REPORT;
        data.strSupp = "Time Recv: " + data.strTime + "\nTime Clear: " + field;
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO?";
    }
  }

  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
    }

    @Override
    public void parse(String field, Data data) {
      super.parse(field+"000", data);
    }
  }

  static String[] MWORD_STREET_LIST = new String[]{
      "4-H PARK",
      "ANDREW FARM",
      "ANNA CAROL",
      "BATTS NECK",
      "BAY BRIDGE",
      "BAY CITY",
      "BAY MEADOWS",
      "BELL CHESTER",
      "BENNET POINT",
      "BENNETT POINT",
      "BENTON CORNERS",
      "BENTON PLEASURE",
      "BOOKERS WHARF",
      "BRICK HOUSE",
      "BURCHARD SAWMILL",
      "BURTON AIR",
      "BUSCHS FRONTAGE",
      "CABIN CREEK",
      "CALLAHAN FARM",
      "CARRS WHARF",
      "CASTLE HARBOR",
      "CASTLE MARINA",
      "CEE JAY",
      "CHANNEL MARKER",
      "CHAR NOR MANOR",
      "CHESTER RIVER BEACH",
      "CHESTER RIVER",
      "CHESTER STATION",
      "CHESTERVILLE BRIDGE",
      "CHESTNUT MANOR FARM",
      "CHEWS MANOR",
      "CHURCH HILL",
      "CLABBER HILL",
      "CLAIBORNE FIELDS",
      "CLANNIHAN SHOP",
      "CLARK CORNERS",
      "COOPER FARM",
      "COX NECK",
      "COX SAWMILL",
      "CRAB ALLEY",
      "CRANEY CREEK",
      "CREEK POINT",
      "CREEKS END",
      "CREEKSIDE COMMONS",
      "CROUSE MILL",
      "DEL RHODES",
      "DELL FOXX",
      "DIXON STABLE",
      "DOUBLE CREEK POINT",
      "DUCK PUDDLE",
      "DUDLEY CORNERS",
      "DUHAMEL CORNER",
      "DULIN CLARK",
      "EAST CAMPUS",
      "EAST HILL",
      "ELL DOWNES",
      "EMORY FARM",
      "FALLEN HORSE",
      "FIVE FARMS",
      "FLAT IRON SQUARE",
      "GOLDEN EYE",
      "GOODHAND CREEK",
      "GRANGE HALL",
      "GRANNY BRANCH",
      "GRASONVILLE CEMETERY",
      "GRAVEL RUN",
      "GREAT NECK",
      "GREEN ACRES",
      "GREEN SPRING",
      "HESS FRONTAGE",
      "HICKORY RIDGE",
      "HIGH BRIDGE",
      "HIGH POINT",
      "HOLDEN FARM",
      "HOUGHTON HOUSE",
      "HOUSE POINT",
      "INDIAN PLANTATION",
      "ISLAND CREEK",
      "JAMES K HORNE",
      "JOHN BROWN",
      "JOHN PATRICK",
      "JOHN POWELL",
      "JOSEPH BOYLES",
      "KEENE FARM",
      "KENT MANOR",
      "KENT NARROWS",
      "KENT POINT",
      "KING GEORGE",
      "KING STORE",
      "LENTLY FARM",
      "LIME LANDING",
      "LITTLE CREEK",
      "LITTLE EAGLE",
      "LITTLE HUT",
      "LITTLE KIDWELL",
      "LITTLE NECK",
      "LOG CANOE",
      "LONG CREEK",
      "LONG POINT",
      "LOVE POINT",
      "MACUM CREEK",
      "MARION QUIMBY",
      "MERRICK CORNER",
      "MONROE MANOR",
      "NICHOLS MANOR",
      "NORTHWEST CREEK",
      "OLD LOVE POINT",
      "OLDE POINT",
      "OUTLET CENTER",
      "OYSTER COVE",
      "PEACOCK CORNER",
      "PERRYS CORNER",
      "PETERS CORNER",
      "PINE COVE",
      "PINE TREE",
      "PINEY CREEK",
      "PINEY NARROWS",
      "POPLAR SCHOOL",
      "PRICE STATION",
      "PRINCESS ANNE",
      "PROSPECT BAY",
      "QUAKER NECK",
      "QUARTER CREEK",
      "QUEEN ANNE CIRCLE",
      "QUEEN ANNE",
      "QUEEN LANDING",
      "QUEEN MARY",
      "QUEEN NEVA",
      "QUEEN VICTORIA",
      "QUEENS COLONY HIGH",
      "RABBIT HILL",
      "RED LION BRANCH",
      "RIVER VIEW",
      "ROBERTS STATION",
      "ROCK HALL",
      "ROE INGLESIDE",
      "ROLLING BRIDGE",
      "ROLPHS WHARF",
      "ROUND TOP",
      "RUSTIC ACRES",
      "SAYERS FOREST",
      "SCHOOL HOUSE",
      "SHIPPING CREEK",
      "SHOPPING CENTER",
      "SKIP JACK",
      "SOUTH CAROLINA",
      "SOUTH LAKE",
      "SPANIARD NECK",
      "SPARKS MILL",
      "SPORTSMAN HALL",
      "SPORTSMAN NECK",
      "STORM HAVEN",
      "STRONG FARM",
      "SULLIVAN FARM",
      "SWAN COVE",
      "THOMPSON CREEK",
      "TOWN POINT",
      "TRINITY FARM",
      "UNION CHURCH",
      "WATERSIDE FARM",
      "WEB FOOT",
      "WELCOME CENTER",
      "WELLS COVE",
      "WHITE HOUSE",
      "WHITE MARSH",
      "WILLARD POINT",
      "WIND DRIFT",
      "WINDSWEPT FARM",
      "WOODS EDGE",
      "WOODS WHARF",
      "WYE HARBOR",
      "WYE KNOT",
      "WYE MILLS",
      "YACHT CLUB"
  };

  static CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAINS",
      "ACCIDENTAL OVERDOSE",
      "ACCIDENTAL POISONING",
      "ALERT",
      "ALLERGIC/REACTION",
      "ALS INTERFACILITY",
      "ANIMAL BITE/ATTACK",
      "ANIMAL RESCUE",
      "APPLIANCE FIRE",
      "ASSAULT",
      "BACK PAIN-NONTRAUMA",
      "BOAT IN DISTRESS",
      "BREATHING PROBLEMS",
      "BRUSH/GRASS FIRE",
      "BUILDING FIRE",
      "CARDIAC ARREST",
      "CHEST PAINS",
      "CHIMNEY FIRE",
      "CHIMNEY FIRE",
      "CHOKING",
      "CITIZEN ASSIST",
      "CO ALARM",
      "COLD EXPOSURE",
      "COMMERCIAL BLDG FIRE",
      "COMMERCIAL VEH FIRE",
      "CONTROL BURNING",
      "DIABETIC PROBLEMS",
      "DROWNING/DIVE ACCDNT",
      "DWELLING FIRE",
      "ELECTRICAL HAZARD",
      "ELEVATOR MALFUNCTION",
      "EYE PROBLEM/INJURY",
      "EXPECTED DEATH",
      "FAINTING",
      "FALLS",
      "FARM VEHICLE FIRE",
      "FIRE ALARM",
      "FIRE TEST CALL",
      "FIRE UNIT TRANSFER",
      "FUEL SPILL",
      "FUEL SPILL/WATERWAY",
      "FUEL SPILL IN DRAIN",
      "GENERAL FIRE ALARM",
      "GRASS FIRE W/EXP",
      "HAZMAT",
      "HAZMAT/SMALL SPILL",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT EXPOSURE",
      "HEAT DETECTOR",
      "HEM/LACERATION",
      "HEMORRHAGE/LACS",
      "INTENTIONAL OVERDOSE",
      "INTERFACILITY TRANSP",
      "INSIDE GAS LEAK",
      "LARGE FUEL SPILL",
      "LARGE OUTSIDE FIRE",
      "LIFT ASSIST",
      "LG BRUSH/GRASS FIRE",
      "LOCK IN/OUT DWELLING",
      "LOCK OUT OF VEHICLE",
      "LOCK OUT",
      "MINOR MVC",
      "MLTPL DWELLING FIRE",
      "MULTIPLE VEH MVC",
      "MUTUAL AID MEDICAL TO KENT",
      "MVC/NOT ALERT",
      "MVC COMMERCIAL VEH",
      "MVC W/FUEL LEAK",
      "MVC HIGH MECHANISM",
      "MVC HIGH OCCUPANCY",
      "MVC INJ/HAZARD",
      "MVC INVOLVING A BUS",
      "MVC UNK INJURY",
      "MVC UNKNOWN INJURIES",
      "MVC VEH VS BLDG",
      "MVC W/BIKE/MOTORCYCL",
      "MVC W/ENTRAPMENT",
      "MVC W/FUEL LEAK",
      "MVC W/HAZARDS",
      "MVC W/INJURIES",
      "MVC W/MINOR INJURIES",
      "MVC W/MOTORCYCLE",
      "MVC PED/BIKE/MC",
      "MVC W/PEDESTRIAN",
      "MVC W/ROLLOVER",
      "NEAR FAINTING",
      "OBVIOUS DEATH",
      "ODOR OF GAS INSIDE",
      "ODOR OF SMOKE INSIDE",
      "ODOR OF GAS OUTSIDE",
      "OTHER ALARM",
      "OUTSIDE FIRE",
      "OUTSIDE GAS LEAK",
      "OUTSIDE ODOR OF GAS",
      "OVERDOSE",
      "PENETRATING TRAUMA",
      "PREG/CHILDBIRTH/MATR",
      "PSYCHIATRIC",
      "PSYCHIATRIC/SUICIDE",
      "PULL STATION ALARM",
      "SCUBA DIVE ACCIDENT",
      "SEIZURE",
      "SEIZURES",
      "SERVICE CALL",
      "SICK PERSON",
      "SMALL FUEL SPILL",
      "SMALL GRASS FIRE",
      "SMALL OUTSIDE FIRE",
      "SMALL STRUCTURE FIRE",
      "SMOKE DETECTOR",
      "SMOKE INVESTIGATION",
      "STABBING",
      "STROKE",
      "STROKE(CVA)",
      "STROKE (CVA)",
      "STROKE(CVA)<2HRS",
      "STROKE(CVA)>2HRS",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE/OUT",
      "TALB MUTUAL AID MEDICAL",
      "TANK FARM FIRE",
      "TRAILER FIRE",
      "TRANSFORMER FIRE",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS",
      "UNCONSCIOUS/FAINTING",
      "UNK BRUSH FIRE",
      "UNK STRUCTURE FIRE",
      "UNKNOWN PROBLEM",
      "UNKNOWN TYPE ALARM",
      "VEH FIRE W/EXPOSURE",
      "VESSEL TAKING WATER",
      "WATERFLOW ALARM",
      "WATER PROBLEMS",
      "WATER RESCUE",
      "WILDLAND FIRE",
      "WIRES DOWN",
      "VEHICLE FIRE",

      "MUTUAL AID",
      "MUTUAL AID TO AACO",
      "MUTUAL AID TO CARO",
      "MUTUAL AID TO KENT",
      "MUTUAL AID TO TALBOT",
      "MUTUAL AID MEDICAL"
  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BARC", "BARCLAY",
      "CENT", "CENTREVILLE",
      "CHES", "CHESTER",
      "CHTN", "CHESTERTOWN",
      "CHUR", "CHURCH HILL",
      "CRUM", "CRUMPTON",
      "GRAS", "GRASONVILLE",
      "HEND", "HENDERSON",
      "INGL", "INGLESIDE",
      "MARY", "MARYDEL",
      "MILL", "MILLINGTON",
      "PRCE", "PRICE",
      "QANN", "QUEEN ANNE",
      "QUEE", "QUEENSTOWN",
      "STEV", "STEVENSVILLE",
      "SUDL", "SUDLERSVILLE",
      "WYE",  "WYE MILLS"

  });

  private static final Properties MA_CITY_TABLE = buildCodeTable(new String[]{
      "AACO", "ANNE ARUNDEL COUNTY",
      "CARO", "CAROLINE COUNTY",
      "KENT", "KENT",
      "TALB", "TALBOT COUNTY",
      "TALBOT", "TALBOT COUNTY"
  });

}
