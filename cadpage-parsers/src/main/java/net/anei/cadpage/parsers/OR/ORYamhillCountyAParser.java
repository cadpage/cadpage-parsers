package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class ORYamhillCountyAParser extends DispatchA3Parser {

  public ORYamhillCountyAParser() {
    super("CAD:", CITY_LIST, "YAMHILL COUNTY", "OR", 
         "INFO", FA3_NBH_CODE);
  }
  
  @Override
  public String getFilter() {
    return "CAD@newbergoregon.gov";
  }

  private static Pattern MASTER = Pattern.compile("CAD:(.*?)(?: Line\\d+=)*(\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2} : pos\\d .*?)?");
  private static Pattern END_UNIT_PTN = Pattern.compile("(.*) ([^ ]*\\b(?:MAPGR|[A-Z]+\\d+))");
  private static Pattern END_DIGIT_PTN = Pattern.compile("(.*)(\\d)");
  private static Pattern UNK_CALL_PTN = Pattern.compile("(.*?) ((?:(?:FIRE|RES|MVC) )?[^ ]+)");

  @Override
  public boolean parseMsg(String body, Data data) {

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1).trim();
    String extra = match.group(2);
    
    // Cross street information is duplicated in both sections, but the second section is more reliable
    // so we will do it first.
    if (extra != null) {
      if (!super.parseFields(new String[] { extra }, data)) return false;
    }

    // We will work the address field from the end.  Last token should be a comma separated unit field
    match = END_UNIT_PTN.matcher(addr);
    if (!match.matches()) return false;
    addr = match.group(1);
    data.strUnit = match.group(2);
    
    // Call code is now at the end.  If it is in our table we have it down.  
    match = END_DIGIT_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1);
      data.strPriority = match.group(2);
    }
    CodeTable.Result res = CODE_TABLE.getResult(addr);
    if (res != null) {
      data.strCode = res.getCode() + data.strPriority;
      data.strCall = res.getDescription();
      addr = res.getRemainder();
    }
    
    // If not we assume a single word token unless we can identify a common two word prefix.
    else {
      match = UNK_CALL_PTN.matcher(addr);
      if (!match.matches()) return false;
      addr = match.group(1).trim();
      data.strCall = match.group(2) + data.strPriority;
    }
    
    // There may, or may not, be a source code at the new end of   text
    match = END_UNIT_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      data.strSource = match.group(2);
    }
    
    // Parse address from start of line
    
    parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS | FLAG_RECHECK_APT, addr.replace("//", "&"), data);
    String left = getLeft();
    
    // If there was not apt and no city, but we have a cross street from the second half, used the second half
    // cross street to identify a possible apt in the leftovers stuff
    if (data.strApt.length() ==  0 && data.strCity.length() == 0 && data.strCross.length() > 0) {
      String word = new Parser(data.strCross).get(' ');
      if (!left.startsWith(word)) {
        int pt = left.indexOf(' ' + word);
        if (pt >= 0) data.strApt = left.substring(0,pt).trim();
      }
    }
    
    // If we do not have a cross street from the second half, parser the leftovers stuff
    // as a cross street
    if (left.length() > 0 && data.strCross.length() == 0) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, left, data);
    }

    // That's all folks
    return true;
  }

  @Override
  public String getProgram() {
    return "ADDR APT CITY SRC CODE CALL PRI UNIT " + super.getProgram();
  }
  
  private static CodeTable CODE_TABLE = new ReverseCodeTable(
      "FIRE AIRCRFT",         "Aircraft Crash",
      "FIRE ALARM",           "Fire Alarm",
      "FIRE AL-CO",           "CO Alarm",
      "FIRE AL-ELEV",         "Elevator Alarm",
      "FIRE AL-MED",          "Medical Alarm",
      "FIRE ASSIST",          "Public Assist",
      "FIRE BARK",            "Barkdust Fire",
      "FIRE BARN",            "Barn Fire",
      "FIRE BOAT",            "Boat Fire",
      "FIRE CAR",             "Vehicle Fire",
      "FIRE CHIMNEY",         "Chimney Fire",
      "FIRE COLAPSE",         "Building Collapse",
      "FIRE COMM",            "Commercial Fire",
      "FIRE ELEC",            "Electrical Fire",
      "FIRE EXPLOS",          "Explosion",
      "FIRE GAS",             "Gas Fire",
      "FIRE GASLEAK",         "Natural Gas/LPG Leak",
      "FIRE GRASS",           "Grass Fire",
      "FIRE HAZMAT",          "Spill",
      "FIRE ILLEGAL",         "Burn Complaint",
      "FIRE INVEST",          "Fire Investigation",
      "FIRE LRG TK",          "Large Truck Fire",
      "FIRE MOVEUP",          "Fire Move-UP",
      "FIRE MUTUAL",          "Fire Mutual Aid",
      "FIRE OTHER",           "MISC/Unknown Fire",
      "FIRE POLE",            "Pole Fire",
      "FIRE RES",             "Residential Fire",
      "FIRE SMLBLDG",         "Small Building Fire",
      "FIRE SMOKE",           "Smoke/Odor Investigation",
      "FIRE SPILL",           "Small HazMat Spill",
      "FIRE STANDBY",         "NFD Personnel Station Standby",
      "FIRE TRASH",           "Dumpster/Trash Fire",
      "FIRE TREE",            "Tree Down",
      "FIRE WATER",           "Water Problem",
      "FIRE WILDLAN",         "Wildland Fire",
      "FIRE WIRES",           "Wires Down",
      "MVC BOAT",             "Boat Crash",
      "MVC TRAIN",            "Train Crash",
      "MVCI",                 "Motor Vehicle Crash",
      "RES ELEV",             "Elevator Entrapment",
      "RES OTHER MISC",       "Rescue",
      "RES ROPE",             "Rope Rescue",
      "RES SEARCH",           "Search and Rescue",
      "RES TRENCH",           "Confined Space/Trench Rescue",
      "RES WATER MA",         "Water Rescue Mutual Aid",
      "ABDOM",                "Abdominal Pain",
      "ALREAC",               "Allergic Reaction",
      "ANIMAL",               "Animal Bite",
      "ASSAULT",              "Assault",
      "BACK",                 "Back Pain",
      "BLEED",                "Bleeding Problem",
      "BREATH",               "Breathing Problem",
      "BURN",                 "Burn Patient",
      "CHEST",                "Chest Pain",
      "CHOKE",                "Choking",
      "DIAB",                 "Diabetic Problem",
      "DROWN",                "Drowning",
      "EXPOSE",               "Environmental Exposure",
      "FALL",                 "FALL",
      "GSW",                  "Gun Shot Wound",
      "HA",                   "Headache",
      "INDUSTACC",            "Industrial Accident",
      "MENTAL",               "Mental Problem",
      "OB",                   "OB Emergency",
      "OD",                   "Overdose",
      "SEIZURE",              "Seizure",
      "SICKPERS",             "Sick Person",
      "STROKE",               "Stroke",
      "TOXIC",                "Toxic",
      "TRAUMA",               "Traumatic Injury",
      "UNCON",                "Unconscious",
      "UNKMED",               "Unknown Medical",
      "WATER RESCU",          "Water Rescue",
      "MED MA",               "Medical Mutual Aid",
      "TRANSFER",             "Interfacility Hospital Transfer",
      "MOVE UP",              "Medical Move Up",
      "LOCK OUT",             "Lock out");

  private static String[] CITY_LIST = {
    "AMITY",
    "CARLTON",
    "DAYTON",
    "DUNDEE",
    "LAFAYETTE",
    "HILLSBORO",
    "MCMINVILLE",
    "NEWBERG",
    "SALEM",
    "SHERIDAN",
    "SHERWOOD",
    "WILLAMINA",
    "WILSONVILLE",
    "YAMHILL"};
}
