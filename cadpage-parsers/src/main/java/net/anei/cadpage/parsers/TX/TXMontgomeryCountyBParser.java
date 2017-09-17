package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Montgomery County, TX (MCHD EMS)
 */
public class TXMontgomeryCountyBParser extends DispatchProQAParser {
  
  public TXMontgomeryCountyBParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "TX",
          "( Comment:INFO/G! ID:ID2! UNIT:UNIT2! " +
          "| ID:ID! PRI:PRI? UNIT:UNIT! PRI:PRI? CALL:CALL! ( NOTES:INFO/R! INFO/N+" + 
                                                           "| PLACE:PLACE! APT:APT? ADDR:ADDR! ( X-STREETS:X! MAP:MAP! CITY:CITY! CROSS_STREETS:SKIP? | CROSS_STREETS:X! MAP:MAP! CITY:CITY! CHANNEL:CH! | CITY:CITY! ( MAP:MAP! | ) | ) ( INFO:INFO! | GPS! GPS! ) ) )");
  }
  
  @Override
  public String getFilter() {
    return "tritechcad@mchd-tx.org,ALARMSQLServer";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_REMOVE_EXT | MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_SET;
  }
  
  private static final Pattern MASTER1 = Pattern.compile("(?:(\\d{2,4}-\\d{6,7}) +)?(?:(\\d{8} \\d{8}|0 0) +)?(.*?)(?: *(\\d\\d[A-Z]-?[A-Z]\\d?))? *(?:NOT FOU|((?:WOODLA|\\d\\d&\\d\\d-)?\\d{2,4}[A-Za-z]))(?: +(F[DG] ?\\d+(?: +F[DG] ?\\d+)*)(?: +(?:North|East|South|West))?)?(?: +((?:[A-Z]+\\d+|Lake Rescue)(?: +[A-Z]+\\d+)*))?(?: +(TAC +\\d+))?");
  private static final Pattern MASTER2 = Pattern.compile("(?:([A-Z0-9]+)-)?(.*?) *(\\d\\d[A-Z]-[A-Z]) +(.*?) +(F[DG]\\d+(?: +F[DG]\\d+)*) +(\\d{2,3}[A-Za-z])(?: +(.*))?");
  private static final Pattern MASTER3 = Pattern.compile("([^\\d].*?)(0BH \\d|\\d\\d[A-Z]{2}\\b)(.*?)(?: (F[DG]\\d))? (\\d{2,4}[A-Z]) (.*)");
  
  private static final Pattern ADDRESS_RANGE_PTN = Pattern.compile("\\b(\\d+) *- *(\\d+)\\b");
  
  private static final Pattern RUN_REPORT_PTN1 = Pattern.compile("ID#:(\\d{2}-\\d{6}) *; *Unit:([^ ]+) *;[ ;]*(AC - Assignment Complete *; *.*|Disp:.*)");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("ID[#:](\\d{4}-\\d{6}) *[-;]([A-Z][ A-Za-z]+:\\d\\d:\\d\\d:\\d\\d\\b.*)");
  private static final Pattern RUN_REPORT_PTN3 = Pattern.compile("(\\d{4}-\\d{6}) (Time at Destination:\\d\\d:\\d\\d:\\d\\d)(.*?)-Destination Address:(.*) -");
  private static final Pattern RUN_REPORT_PTN4 = Pattern.compile("ID: *(\\S+) +; *(\\S+) +(Call Complete: \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d) *(.*)");
  private static final Pattern RUN_REPORT_PTN5 = Pattern.compile("ID:(\\S+) +; *(Time Cancelled:.*)");
  private static final Pattern NOTIFICATION_PTN1 = Pattern.compile("(\\d\\d-\\d{6}) - \\d+\\) (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) [\\d:]+\\.000-\\[\\d+\\] \\[Notification\\] +(.*?)(?: +\\[Shared\\])?");
  private static final Pattern NOTIFICATION_PTN2 = Pattern.compile("ID#:(\\d\\d-\\d{6}) +; +\\d+\\) (.*)");
  
  private static final Pattern MISSING_SEMI_PTN = Pattern.compile("(?<=CITY:)(?= *\\d{8}\\b)| (?=NOTES:)");
  private static final Pattern COMMA_ID_PTN = Pattern.compile(", *ID:");
  private static final Pattern DELIM = Pattern.compile("[ ,]*; *");

  private static final Pattern ADDR_CODE_CALL_PTN = Pattern.compile("(.*?)([#\\*]\\d+|\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) *-[- ]*(.*)");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=[a-z])(?=[A-Z])|(?<= [A-Z])(?=[A-Z][a-z])");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Message")) return false;
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    body = stripFieldStart(body, ",");
    
    Matcher match = RUN_REPORT_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      for (String time : match.group(3).split(";")) {
        data.strSupp = append(data.strSupp, "\n", time.trim());
      }
      return true;
    }
    
    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      for (String time : match.group(2).split("[-;]")) {
        data.strSupp = append(data.strSupp, "\n", time.trim());
      }
      return true;
    }
    
    match = RUN_REPORT_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO PLACE ADDR");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).trim();
      data.strPlace = match.group(3).trim();
      data.strAddress = match.group(4).trim();
      return true;
    }
    
    match = RUN_REPORT_PTN4.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strSupp = match.group(3) + ' ' + match.group(4);
      return true;
    }
    
    match = RUN_REPORT_PTN5.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }
    
    match = NOTIFICATION_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("ID DATE TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCallId = match.group(1);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strSupp = match.group(4);
      return true;
    }
    
    match = NOTIFICATION_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).trim();
      return true;
    }
    
    // See if we can use the regular semicolon delimited form
    String body2 = MISSING_SEMI_PTN.matcher(body).replaceAll(";");
    boolean comment = body2.startsWith("Comment:");
    if (comment) body2 = COMMA_ID_PTN.matcher(body2).replaceFirst(";ID:");
    String[] flds = DELIM.split(body2);
    if (comment || flds.length >= 4) {
      return parseFields(flds, data);
    }
    
    // Foo.  Now we have to do this the hard way
    match = MASTER3.matcher(body);
    if (match.matches()) {
      setFieldList("CALL BOX ADDR APT SRC MAP UNIT");
      data.strCall = match.group(1).trim();
      data.strBox = match.group(2).trim();
      parseAddress(match.group(3).trim(), data);
      data.strSource = getOptGroup(match.group(4));
      data.strMap = match.group(5);
      data.strUnit = match.group(6);
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL BOX ADDR APT CITY SRC MAP UNIT");
      data.strCode = getOptGroup(match.group(1));
      data.strCall = match.group(2).trim();
      fixOutOfCountyResponse(data);
      data.strBox = match.group(3);
      parseAddress(match.group(4).trim(), data);
      data.strSource = match.group(5);
      data.strMap = match.group(6);
      data.strUnit = getOptGroup(match.group(7));
      return true;
    }

    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("ID GPS ADDR APT CITY CODE CALL BOX MAP SRC UNIT CH");
      data.strCallId = getOptGroup(match.group(1));
      String gps = match.group(2);
      if (gps != null) parseGPSField(gps, data);
      String addr = match.group(3).trim();
      addr = ADDRESS_RANGE_PTN.matcher(addr).replaceAll("$1-$2");
      data.strBox = getOptGroup(match.group(4));
      data.strMap = getOptGroup(match.group(5));
      data.strSource = getOptGroup(match.group(6));
      data.strUnit = append(data.strUnit, " ", getOptGroup(match.group(7)));
      data.strChannel = getOptGroup(match.group(8));
      
      // What we have is an address followed by a call, with no consistent
      // way to identify the break.
      // Start by looking for a call code 
      boolean found = false;
      String call;
      match = ADDR_CODE_CALL_PTN.matcher(addr);
      if (match.matches()) {
        found = true;
        addr = match.group(1).trim();
        data.strCode = match.group(2);
        data.strCall = match.group(3);
      }
      
      // Try looking it up in a reverse call list
      if ((call = CALL_SET.getCode(addr)) != null) {
        found = true;
        data.strCall = call;
        addr = addr.substring(0,addr.length()-call.length());
      }
      
      // No luck, try looking for a signature lower case letter followed by
      // an upper case character
      else if ((match = MISSING_BLANK_PTN.matcher(addr)).find()) {
        found = true;
        pt = match.start();
        data.strCall = addr.substring(pt);
        addr = addr.substring(0,pt);
      }
      
      // If either of those worked, the address may be truncated, in which 
      // case we will try to complete common road suffixes
      if (found) {
        if (addr.endsWith(" ")) {
          addr = addr.trim();
        } else {
          pt = addr.lastIndexOf(' ');
          if (pt >= 0) {
            pt++;
            String lastWord = addr.substring(pt);
            if ("Av".startsWith(lastWord)) addr = addr.substring(0,pt) + "Ave";
            if ("Ci".startsWith(lastWord)) addr = addr.substring(0,pt) + "Cir";
            if ("D".startsWith(lastWord)) addr = addr.substring(0,pt) + "Dr";
            if ("Loo".startsWith(lastWord)) addr = addr.substring(0,pt) + "Loop";
            if ("R".startsWith(lastWord)) addr = addr.substring(0,pt) + "Rd";
          }
        }
        parseAddress(addr, data);
      }
      
      // If neither approach found the break, we will have to count on the
      // smart address parser, which is none to reliable in this county.  There
      // are too many street names that really lack an identifiable suffix
      else {
        parseAddress(StartType.START_ADDR, addr, data);
        data.strCall = getLeft();
        if (data.strCall.length() == 0) return false;
      }
      
      // If call is out of county response, zap the default county
      fixOutOfCountyResponse(data);
      if (data.strCall.startsWith("Out of County Respon")) data.defCity = "";
      
      return true;
    }

    return false;
  }

  public void fixOutOfCountyResponse(Data data) {
    if (data.strCall.startsWith("Out of County Respon")) {
      data.defCity = "";
      if (data.strCall.length() > 20) {
        data.strCity = data.strCall.substring(20);
        data.strCall = "Out of County Respon";
      }
    }
  }

  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField(false);
    if (name.equals("ID2")) return new MyIdField(true);
    if (name.equals("UNIT2")) return new MyUnit2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGpsField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(\\d{2}-\\d{6}|\\d{4}-\\d{6})|(POST-\\d{7}/\\d{2})(\\d\\d:\\d\\d:\\d\\d)");
  private class MyIdField extends IdField {
    
    boolean permissive;
    
    public MyIdField(boolean permissive) {
      this.permissive = permissive;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) {
        if (permissive) return;
        abort();
      }
      String id = match.group(1);
      if (id != null) {
        data.strCallId = id;
      } else {
        data.strCallId = match.group(2);
        data.strTime = match.group(3);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ID TIME";
    }
  }
  
  private class MyUnit2Field extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt < 0) pt = 0;
      field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }
  
  
  private static final Pattern CALL_PTN = Pattern.compile("([#\\*][A-Z]*\\d+[A-Z]?|\\d{1,3}[A-Z]\\d{0,2}[A-Z]?) *-[- ]*(.*)");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      
      else {
        field = MBLANK_PTN.matcher(field).replaceAll(" ");
        field = field.replace("( ", "(").replace(" )", ")");
      }
      super.parse(field, data);
    } 
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Not Found", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyGpsField extends GPSField {
    
    public MyGpsField() {
      super("\\d{8}|0", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strGPSLoc.length() == 0) {
        data.strGPSLoc = field;
        return;
      }
      
      else {
        field = data.strGPSLoc + ' ' + field;
        data.strGPSLoc = "";
        parseGPSField(field, data);
      }
    }
  }

  private void parseGPSField(String gps, Data data) {
    if (gps.length() != 17) return;
    gps = gps.substring(0,2) + '.' + gps.substring(2,8) + ',' + gps.substring(9,11) + '.' + gps.substring(11); 
    setGPSLoc(gps, data);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }
  
  private static final ReverseCodeSet CALL_SET = new ReverseCodeSet(
      "Abd. Pain -Pre-Alert",
      "Abdominal Pain",
      "Aircraft Emergency",
      "Alarm",
      "Alarm - Carbon Monox",
      "Alarm - Carbon Monoxide",
      "Alarm - Fire",
      "Alarm - Pull Station",
      "Alarm - Water Flow",
      "Allergic Reaction",
      "Animal Bite",
      "Assault",
      "Assist Law Enforceme",
      "Back Pain",
      "Bomb Threat",
      "Breathing Problems",
      "Burn Patient",
      "Cardiac Arrest",
      "Chest Pain",
      "Chest Pain Pre-Alert",
      "Child Locked in a Ve",
      "Child Locked in a Vehicle",
      "Choking",
      "Choking Pre-Alert",
      "Dedicated Fire Standby",
      "Dedicated Standby",
      "Diabetic Problems",
      "Diabetic",
      "Diff. Breath. Pre-Alert",
      "Difficulty Breathing",
      "Drowning",
      "Electrical Hazard",
      "Electrical Hazard -",
      "Electrical Hazard - Live Wires",
      "Electrical Hazard - Trans Fire",
      "Electrocution",
      "Emotional Crisis",
      "Eval Emerg Resp Req",
      "Explosion",
      "Eye Problems",
      "Eye Problems/Injury",
      "Fall",
      "Fall Pre-Alert",
      "Fluid Spill",
      "Gas",
      "Gas - Cut Commercial",
      "Gas - Cut Line Outsi",
      "Gas - Cut Line Outside",
      "Gas - In a Residence",
      "Gas - Smell in Area",
      "Gas - Smell in a Bui",
      "Gas - Smell in a Building Comm",
      "Gas - Smell in a Res",
      "Gas - Smell in a Residence",
      "General Weakness",
      "Hazmat",
      "Hazmat - Large Fuel",
      "Headache",
      "Heart Problems",
      "Heat/Cold Exposure",
      "Hemorrhage",
      "Hemorrhage/Laceratio",
      "Hemorrhage/Laceration",
      "Inhalation Pre-Alert",
      "Inhalation/Hazmat",
      "Lake Rescue",
      "Lake Rescue - Conroe",
      "Life Flight Landing",
      "Lightning - To a Str",
      "MVA",
      "MVA - Entrapment",
      "MVA - Fire",
      "MVC",
      "MVC Pre-Alert",
      "Medical Alarm",
      "Medical Alarm Pre-Alert",
      "Medical Priority 1",
      "Medical Priority 2",
      "Mutual Aid Assist Agency",
      "Obvious/Expected Death",
      "Odor",
      "Out of County Respon",
      "Out of County ResponSPRING",
      "Outside - Check For",
      "Outside - Check For Fire",
      "Outside - Controlled Burn",
      "Outside - Controlled",
      "Outside - Dumpster F",
      "Outside - Dumpster Fire",
      "Outside - Extinguish",
      "Outside - Grass/Wood",
      "Outside - Grass/Woods Fire",
      "Outside - Illegal Bu",
      "Outside - Illegal Burn",
      "Outside - Small Fire",
      "Outside - Trash Fire",
      "Outside - Unknown Ty",
      "Outside - Unknown Type Fire",
      "Outside Fire",
      "Overdose Pre-Alert",
      "Overdose Ingestion",
      "Overdose/Ingestion",
      "Overdose/Poisoning",
      "Penetrating Trauma",
      "Pregnancy/Miscarriag",
      "Pregnancy/Miscarriage",
      "Psychiatric Pre-Alert",
      "Psychiatric/Suicide",
      "Rescue",
      "Rescue - Elevator",
      "Seizures",
      "Service Call",
      "Service Call - Alert",
      "Sick Person",
      "Smoke - In a Bldg Co",
      "Smoke - In a Bldg Commercial",
      "Smoke - In a Residen",
      "Smoke - In a Residence",
      "Smoke - In the Area",
      "Smoke - Smell in a R",
      "Smoke - Smell in Comm Bldg",
      "Smoke - Smell in the Area",
      "Smoke - Smell in the",
      "SSM Level -",
      "Stab/GSW/Penetrating Trauma",
      "Stab/GSW/Penetrating",
      "Stroke",
      "Structure - Apartmen",
      "Structure - Apartment",
      "Structure - Commerci",
      "Structure - Extingui",
      "Structure - Large Bu",
      "Structure - High Lif",
      "Structure - High Life Hazard",
      "Structure - Resident",
      "Structure - Residential",
      "Structure - Residential/Oven",
      "Structure - Small Bu",
      "Structure - Small Building",
      "Structure Fire",
      "TEST",
      "Transfer/Evaluation",
      "Traumatic Injuries",
      "Traumatic Injury",
      "Traumatic Inj. Pre-Alert",
      "Unconscious Party",
      "Unconscious Pre-Alert",
      "Unconscious/Fainting",
      "Unknown Prob. Pre-Alert",
      "Unknown Problem",
      "Unknown Problem/Man",
      "Unknown Problem/Man Down",
      "Vehicle Fire - Comme",
      "Vehicle Fire - Commercial",
      "Vehicle Fire",
      "Water Rescue",
      "Water Rescue - Motor",
      "Water Rescue - Sinki",
      "Water Rescue - Still",
      "WFD PA Child Locked",
      
      "#CFD AIRCRAFT EMERGENCY PA",
      "#CFD STRUCTURE FIRE PA",
      "ALERT III ON L",
      "ASSIST LOCKED IN VEHICLE",
      "COMMERCIAL ALARM",
      "COMMERCIAL GAS LEAK",
      "DRILL ONLY - TEST",
      "GAS LEAK/ODOR PA",
      "SMALL OUTSIDE FIRE",
      "SMOKE INVESTIGATION PA",
      "STRUCTURE FIRE PA",
      "ODOR OF SMOKE OUTSIDE",
      "OUTSIDE FIRE PA",
      "UNKNOWN FUEL SPILL",
      "VEHICLE FIRE PA"

  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "CONROE",
    "CUT AND SHOOT",
    "HOUSTON",
    "MAGNOLIA",
    "MONTGOMERY",
    "OAK RIDGE NORTH",
    "PANORAMA VILLAGE",
    "PATTON VILLAGE",
    "ROMAN FOREST",
    "SHENANDOAH",
    "SPLENDORA",
    "STAGECOACH",
    "WOODBRANCH",
    "WOODLOCH",
    "WILLIS",

    // Census designated places
    "PINEHURST",
    "PORTER HEIGHTS",
    "THE WOODLANDS",

    // Unincorporated areas
    "DOBBIN",
    "DECKER PRAIRIE",
    "IMPERIAL OAKS",
    "NEW CANEY",
    "PORTER",
    "RIVER PLANTATION",
    "TAMINA",
    "THE WOODLANDS",
    
    // Harris County
    "SPRING"
  };
}
