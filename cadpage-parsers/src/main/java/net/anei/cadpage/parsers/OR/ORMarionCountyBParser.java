
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
    super(defCity, defState, "CALL SRC DATETIME ADDR/ZiS? UNIT/Z ID!");
  }

  @Override
  public String getAliasCode() {
    return "ORMarionCountyB";
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

  public static Pattern FIELD_CLEANUP_PATTERN = Pattern.compile(".*?\\(([^/]*,[^/]*)\\)");
  public static Pattern REMOVE_MAPBOOK_PATTERN = Pattern.compile("(.*?)(?:\\(MapBook:(\\d{4})\\))(.*?)");
  public static Pattern REMOVE_ZIP_PATTERN = Pattern.compile("(.+?)(?:[, ]+\\d{5}|,? 0)$");
  public static Pattern INTERSECTION_CLEANUP_PATTERN = Pattern.compile("(.*),.*(/.*)");

  public class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

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
      Parser p = new Parser(field);
      if (field.endsWith(")")) {
        p.getLast(")");
        field = p.getOptional("(");
        data.strCross = p.get();
        //remove preceding and trailing slashes
        int si = data.strCross.indexOf("/");
        if (si == 0) data.strCross = data.strCross.substring(1);
        else if (si == data.strCross.length() - 1) data.strCross = data.strCross.substring(0, data.strCross.length() - 1);
        p = new Parser(field);
      }

      data.strCity = p.getLastOptional(",");
      if (NUMERIC.matcher(data.strCity).matches()) data.strCity = p.getLastOptional(","); 
      data.strPlace = p.getLastOptional(", @");

      // remove extra city from intersection address
      String addr = p.get();
      Matcher intMat = INTERSECTION_CLEANUP_PATTERN.matcher(addr);
      if (intMat.matches()) addr = intMat.group(1).trim() + " " + intMat.group(2);
      super.parse(addr, data);
      
      // remove zipcode from city
      Matcher zipMat = REMOVE_ZIP_PATTERN.matcher(data.strCity);
      if (zipMat.matches()) data.strCity = zipMat.group(1);
      
      //  Remove anything following a comma that is left in the address
      int pt = data.strAddress.indexOf(',');
      if (pt >= 0) data.strAddress = data.strAddress.substring(0,pt).trim();
    }

    @Override
    public String getFieldNames() {
      return ("ADDR APT MAP PLACE CITY X");
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

}
