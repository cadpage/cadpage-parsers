
package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORMarionCountyBParser extends FieldProgramParser {

  public ORMarionCountyBParser() {
    this("MARION COUNTY", "OR");
  }
  
  protected ORMarionCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState, 
          "CALL SRC DATETIME ADDR/ZiS? UNIT/Z ID! INFO/N+");
  }

  @Override
  public String getAliasCode() {
    return "ORMarionCountyB";
  }
  
  @Override
  public String getFilter() {
    return "@cityofsalem.net,@ccschaplain.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_CR;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("[A-Z]{3,4}\\d{12}", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*?)\\d*");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      String code = field;
      Matcher match = CALL_CODE_PTN.matcher(code);
      if (match.matches()) code = match.group(1);
      String call = CALL_CODES.getProperty(code);
      if (call != null) {
        data.strCode = field;
        data.strCall = call;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static Pattern PHONE_PATTERN = Pattern.compile("(?:[A-Z]{1,3}:)?(?:\\d{3}-)?\\d{3}-?\\d{4}");
  private static Pattern FIELD_CLEANUP_PATTERN = Pattern.compile(".*?\\(([^/]*,[^/]*)\\)");
  private static Pattern REMOVE_MAPBOOK_PATTERN = Pattern.compile("(.*?)(?:\\(MapBook:(\\d{4})\\))(.*?)");
  private static Pattern REMOVE_ZIP_PATTERN = Pattern.compile("(.+?)(?:[, ]+\\d{5}|,? 0)$");
  private static Pattern INTERSECTION_CLEANUP_PATTERN = Pattern.compile("(.*),.*(/.*)");

  public class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      Parser p = new Parser(field);
      String thing = p.getLastOptional(";");
      if (thing.length() > 0) {
        if (PHONE_PATTERN.matcher(thing).matches()) {
          data.strPhone = thing;
          thing = p.getLastOptional(';');
        }
        data.strName = thing;
        field = p.get();
      }

      // remove mapbook and store its #
      Matcher mapbookMat = REMOVE_MAPBOOK_PATTERN.matcher(field);
      if (mapbookMat.matches()) {
        field = mapbookMat.group(1) + mapbookMat.group(3);
        data.strMap = mapbookMat.group(2);
      }

      // check and act if this string is in the format "blah(partweactuallywant)" and act on it
      Matcher fieldMat = FIELD_CLEANUP_PATTERN.matcher(field);
      if (fieldMat.matches()) {
        field = fieldMat.group(1);
      }
      
      // parse remaining string
      p = new Parser(field);
      if (field.endsWith(")")) {
        p.getLast(")");
        field = p.getOptional("(");
        String cross = p.get();
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
        p = new Parser(field);
      }
      
      data.strCity = p.getLastOptional(",");
      if (NUMERIC.matcher(data.strCity).matches()) data.strCity = p.getLastOptional(","); 
      data.strPlace = p.getLastOptional(", @");

      // remove extra city from intersection address
      String addr = p.get();
      Matcher intMat = INTERSECTION_CLEANUP_PATTERN.matcher(addr);
      if (intMat.matches()) addr = intMat.group(1).trim() + " " + intMat.group(2);
      
      // If the city we found is not a city, then it is probably parser of a name, 
      // so put it back together
      if (data.strCity.length() > 0 && !isCity(data.strCity)) {
        data.strAddress = addr + ", " + data.strCity;
        data.strCity = "";
      } else {
        super.parse(addr, data);
      }
      
      // remove zipcode from city
      Matcher zipMat = REMOVE_ZIP_PATTERN.matcher(data.strCity);
      if (zipMat.matches()) data.strCity = zipMat.group(1);
    }

    @Override
    public String getFieldNames() {
      return ("ADDR APT MAP PLACE CITY X NAME PHONE");
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ABDOM",   "Abdominal Pain",
      "ACC",     "Motor vehicle accident w/injury",
      "ACCHR",   "Hit and run accident w/injury",
      "AIREM",   "Air Emergency via phone",
      "ALERT",   "Air Emergency via tower",
      "ALREAC",  "Allergic Reaction",
      "ALRMCO",  "Carbon monoxide alarm",
      "ALRMF",   "Fire alarm",
      "ALRMMED", "Medical alarm",
      "AOAP",    "Assist Police",
      "APT",     "Apartment fire",
      "ASLT",    "Assault with injury",
      "BACK",    "Back pain",
      "BITE",    "Animal Bite",
      "BLEED",   "Bleeding problem",
      "BREATH",  "Difficulty breathing",
      "BURN",    "Burn injury",
      "BOAT",    "Boat accident",
      "BOMBF",   "Bomb device",
      "BURNCO",  "Burning complaint",
      "BURNIN",  "Daily Burning information",
      "CAR",     "Car fire",
      "CHEST",   "Chest pain",
      "CHIM",    "Chimney/flue fire",
      "CHOKE",   "Choking",
      "COLD",    "Cold fire investigation",
      "COMML",   "Commercial fire",
      "DIAB",    "Diabetic issue",
      "DOMDIS",  "Domestic disturbanc w/injury",
      "DROWN",   "Drowning",
      "EXPOSE",  "Cold or heat exposure",
      "EXPLOD",  "Explosion",
      "EYE",     "Eye injury",
      "FALL",    "Fall",
      "FIGHT",   "Fight with injury",
      "FIRENL",  "Non-structure low risk fire",
      "FIRENH",  "Non-structure high risk fire",
      "FIRES",   "Structure fire",
      "GRASSH",  "High risk grass fire",
      "GRASSL",  "Low risk grass fire",
      "GSW",     "Gun shot wound",
      "HA",      "Headache",
      "HAZBDS",  "Biological Detection System (Salem Post Office)",
      "HAZMAT",  "Hazardous Materials Incident",
      "HOUSE",   "House fire",
      "INDUST",  "Industrial accidentLAB",
      "LAB",     "Drug lab standby",
      "MCI",     "Mass casualty incident",
      "MPI",     "Multi patient incident",
      "MARINE",  "Boat/marine fire",
      "MEDVAC",  "Air ambulance transport",
      "MENTAL",  "Mental subject w/injury",
      "MUTUAL",  "Mutual Aid",
      "NATGAS",  "Natural gas leak",
      "OB",      "Obstetric Distress or Childbirth",
      "OD",      "Overdose",
      "ODORNS",  "Odor of Smoke - not seen (in or outside)",
      "OTTRAN",  "Out of town transport",
      "POISON",  "Poisoning",
      "PUBLIC",  "Public Assist",
      "RESCUE",  "Land based rescue",
      "ROB",     "Robbery w/injury",
      "SEXOFF",  "Sex offense w/injury",
      "SIREN",   "TEST        TEST Tsunami Siren",
      "SMOKEN",  "Visable smoke non-structure",
      "SMOKES",  "Visable smoke structure",
      "SPILL",   "Fuel spill or leak",
      "STAB",    "Stabbing",
      "STNDBY",  "Aircraft standby",
      "STROKE",  "Stroke/CVA",
      "SURF",    "Surf Rescue",
      "TESTAIR", "Training - Aircraft Emergency",
      "TESTF",   "Test or information - Fire",
      "TESTM",   "Test or information - Fire",
      "TIME",    "Transfer from health care facility",
      "TRAIN",   "Train derailment w/injury",
      "TRAP",    "Accident with entrapment",
      "TREE",    "Tree fire",
      "TOXIC",   "Toxic exposure",
      "TRANS",   "Medical transfer",
      "TRAUMA",  "Bicycle crash/trauma/fracture",
      "UNCON",   "Unconscous person",
      "UNKMED",  "Unknown medical",
      "WATER",   "Water rescue",
      "WCTRAN",  "Wheelchair transport",
      "WIRE",    "Wire down"
  });
  
  private static final String[] CITY_LIST = new String[]{
    
    // Marion County
    "MARION COUNTY",
    
    // Cities
    "AUMSVILLE",
    "AURORA",
    "DETROIT",
    "DONALD",
    "GATES",
    "GERVAIS",
    "HUBBARD",
    "IDANHA",
    "JEFFERSON",
    "KEIZER",
    "MILL CITY",
    "MT ANGEL",
    "ST PAUL",
    "SALEM",
    "SCOTTS MILLS",
    "SILVERTON",
    "STAYTON",
    "SUBLIMITY",
    "TURNER",
    "WOODBURN",

    // Census-designated places
    "BROOKS",
    "BUTTEVILLE",
    "FOUR CORNERS",
    "HAYESVILLE",
    "LABISH VILLAGE",
    "MARION",
    "MEHAMA",

    // Unincorporated communities
    "BREITENBUSH",
    "CHAMPOEG",
    "CHEMAWA",
    "CLEAR LAKE",
    "MACLEAY",
    "MCKEE",
    "MIDDLE GROVE",
    "MONITOR",
    "NIAGARA",
    "NORTH HOWELL",
    "PRATUM",
    "ROSEDALE",
    "SAINT BENEDICT",
    "SAINT LOUIS",
    "SHAW",
    "TALBOT",
    "WACONDA",
    "WEST STAYTON",
    
    // Lincoln County
    "LINCOLN COUNTY",

    // Cities
    "DEPOE BAY",
    "LINCOLN CITY",
    "NEWPORT",
    "SILETZ",
    "TOLEDO",
    "WALDPORT",
    "YACHATS",

    // Census-designated places
    "LINCOLN BEACH",
    "ROSE LODGE",

    // Unincorporated communities
    "AGATE BEACH",
    "BAYVIEW",
    "BEVERLY BEACH",
    "BURNT WOODS",
    "CHITWOOD",
    "EDDYVILLE",
    "ELK CITY",
    "FISHER",
    "GLENEDEN BEACH",
    "HARLAN",
    "KERNVILLE",
    "LITTLE ALBANY",
    "LOGSDEN",
    "NASHVILLE",
    "NEOTSU",
    "NEWPORT HEIGHTS",
    "NORTONS",
    "OTIS",
    "OTIS JUNCTION",
    "OTTER ROCK",
    "ROADS END",
    "SAN MARINE",
    "SEAL ROCK",
    "SOUTH BEACH",
    "TIDEWATER",
    "YAQUINA",
    
    // Benton County
    "BENTON COUNTY",

    // Cities
    "ADAIR",
    "ALBANY",
    "CORVALLIS",
    "MONROE",
    "PHILOMATH",

    // Census-designated places
    "ALPINE",
    "ALSEA",
    "BELLFOUNTAIN",
    "BLODGETT",
    "KINGS VALLEY",
    "SUMMIT",

    // Unincorporated communities
    "ALDER",
    "DAWSON",
    "DRY CREEK",
    "FLYNN",
    "GLENBROOK",
    "GREENBERRY",
    "HARRIS",
    "HOSKINS",
    "LEWISBURG",
    "NOON",
    "WREN",

    // Polk County
    "POLK COUNTY",

    // Cities
    "DALLAS",
    "FALLS CITY",
    "INDEPENDENCE",
    "MONMOUTH",
    "WILLAMINA",

    // Census-designated places
    "EOLA",
    "FORT HILL",
    "GRAND RONDE",
    "RICKREALL",

    // Unincorporated communities
    "AIRLIE",
    "BALLSTON",
    "BETHEL",
    "BLACK ROCK",
    "BRIDGEPORT",
    "BRUNKS CORNER",
    "BUENA VISTA",
    "CROWLEY",
    "ELLENDALE",
    "LEWISVILLE",
    "MCCOY",
    "MODEVILLE",
    "PEDEE",
    "PERRYDALE",
    "SALT CREEK",
    "SUVER",
    "VALLEY JUNCTION",
    "VALSETZ",
    "ZENA",
    
    // Lane County
    "LANE COUNTY"
    
  };

}
