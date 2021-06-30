package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStCharlesCountyParser extends FieldProgramParser {

  private static final Pattern ID_PTN = Pattern.compile("\\d{6}-\\d{5}");

  public MOStCharlesCountyParser() {
    super("ST CHARLES COUNTY", "MO",
          "ID:ID_CODE_CALL_ADDR! APT:APT! X_ST:X! MAP:MAP! CHL:CITY! GPS:GPS? Units:UNIT!");
    removeWords("TERRACE");
  }

  @Override
  public String getFilter() {
    return "dispatch@sccda.org,dispatch@sccmo.org,SCCEC_info@sccmo.org";
  }

  private static final Pattern PRENOTE_PTN = Pattern.compile("\\b\\d+\\) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d)-\\[\\d+\\] (?:\\[(?:Notification|Page)\\])? *(.*)");
  private static final Pattern PRENOTE_PREFIX_PTN = Pattern.compile("(\\d{6}-\\d{5}) New Notification:");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=APT:|X ST:|MAP:|CHL:|GPS:)");

  @Override
  public boolean parseMsg(String body, Data data) {

    // Dispatch sends a fixed length field format and a variable length field format.
    // Things get easier if this is a fixed format alert
    if (parseFixedMsg(body, data)) return true;

    // Otherwise reset things and try the variable field format
    data.initialize(this);

    // OK, First look for prenotification variant
    Matcher match = PRENOTE_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      setFieldList("ID CODE CALL ADDR PLACE DATE TIME INFO");
      match = PRENOTE_PTN.matcher(body);
      if (!match.find()) return false;
      if (!parseIdCodeCallAddress(body.substring(0,match.start()).trim(), data)) return false;
      data.strDate =  match.group(1);
      data.strTime = match.group(2);
      data.strSupp = match.group(3);
      return true;


    }

    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }

  private boolean parseFixedMsg(String body, Data data) {
    if (body.startsWith("Comment:")) {
      int pt = body.indexOf(",ID:", 8);
      if (pt < 0) pt = body.indexOf("ID:");
      if (pt < 0) return false;
      data.strSupp = body.substring(8, pt).trim();
      if (body.charAt(pt) == ',') pt++;
      body = body.substring(pt).trim();
    }

    FParser p = new FParser(body);

    // Check for special New Notification format
    if (p.check("New Notification:")) {
      setFieldList("CODE CALL ADDR APT INFO");
      parseCodeCall(p.get(26), data);
      parseAddress(p.get(31), data);
      if (p.getOptional(" [Notification] ", 26, 28) == null) return false;
      data.strSupp = append(data.strSupp, "\n", p.get());
      return true;
    }

    // Skip optional ID: label
    p.check("ID:");

    data.strCallId = p.get(12);
    if (! ID_PTN.matcher(data.strCallId).matches()) return false;

    if (p.check("        New Notification:")) {
      setFieldList("ID CODE CALL ADDR DATE TIME INFO");
      parseCodeCall(p.get(26), data);
      parseAddress(p.get(31), data);
      String info = p.get();
      Matcher match = PRENOTE_PTN.matcher(info);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strSupp = append(data.strSupp, "\n", match.group(3));
      return true;
    }

    data.strSupp = append(data.strSupp, "\n", p.get(8));

    if (p.checkAhead(30, "AT")) {

      setFieldList("ID INFO CODE CALL ADDR APT PLACE CITY CH GPS UNIT");

      parseCodeCall(p.get(30), data);
      if (!p.check("AT")) return false;
      parseAddress(p.get(35), data);
      if (!p.check("APT:")) return false;
      data.strApt = append(data.strApt, "-", p.get(5));
      if (!p.check("BUS:")) return false;
      data.strPlace =  p.get(40);
      if (p.check("DV:")) {
        data.strPlace = p.get(30);
      } else if (p.check("FD:")) {
        data.strCity = p.get(30);
        if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
      } else return false;
      if (!p.check("CHL:")) return false;
      data.strChannel = p.get(9);
      if (!p.check("GPS:")) return false;
      String gps = p.get(2)+'.'+p.get(6);
      if (!p.check("  -")) return false;
      p.check(" ");
      setGPSLoc(gps+",-"+p.get(2)+'.'+p.get(6), data);
      if (!p.check("  ")) return false;
      data.strPlace = append(data.strPlace, " - ", p.get(30));
      if (!p.check("Units:")) return false;
      data.strUnit = p.get();
      return true;
    }

    setFieldList("ID INFO CODE CALL ADDR PLACE APT X MAP CH CITY GPS UNIT");

    int fLen = p.checkAhead("APT:", 67, 101, 100);
    if (fLen < 0) return false;

    fLen -= 75;
    parseCodeCall(p.get(fLen), data);
    parseAddress(p.get(50), data);
    data.strPlace = p.get(25);
    if (!p.check("APT:")) return false;

    p.setOptional();
    data.strApt = p.getOptional("X ST:", 5, 16);
    if (data.strApt == null) return false;
    data.strCross = p.get(30);

    if (!p.check("MAP:")) return false;
    String map = p.getOptional("CHL:", 10);
    if (map != null) {
      data.strMap = map;
      fLen = p.checkAhead("Units:", 35, 39);
      if (fLen < 0) fLen = p.checkAhead("GPS:", 35, 39);
      if (fLen < 0) return false;
      data.strChannel = p.get(fLen-30);
      data.strCity = p.get(30);
      if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
      if (p.check("GPS:")) {
        p.check(" ");
        String gps = p.get(2)+'.'+p.get(6);
        if (!p.checkBlanks(2)) return false;
        p.check(" ");
        setGPSLoc(gps+','+p.get(3)+'.'+p.get(6), data);
        if (!p.checkBlanks(2)) return false;
      }
      if (!p.check("Units:")) return false;
      data.strUnit = p.get();
      return true;

    } else {
      data.strMap = p.get(15);
      data.strCity = p.get(30);
      if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
      if (!p.check("Units:")) return false;
      data.strUnit = p.get();
      return true;
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Za-z]?|\\d{2}) +(.*)");

  private void parseCodeCall(String call, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = call;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_CODE_CALL_ADDR")) return new MyIdCodeCallAddressField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern ID_ADDR_PTN = Pattern.compile("(\\d{6}-\\d{5}) (.*)");
  private class MyIdCodeCallAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (!parseIdCodeCallAddress(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID CODE CALL ADDR APT? PLACE";
    }
  }

  private boolean parseIdCodeCallAddress(String field, Data data) {

    // Split off leading call ID
    Matcher match = ID_ADDR_PTN.matcher(field);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    field = match.group(2);
    field = stripFieldStart(field, "New Notification:");

    // The call description is tricky.  The code and description
    // cut off at 26 characters with no delimiter.  If they are shorter
    // there will be a blank delimiter
    match = CODE_CALL_PTN.matcher(field);
    int callStart = 0;
    if (match.matches()) {
      data.strCode = match.group(1);
      callStart = match.start(2);
    }

    // Sanity check to keep from aborting on short fields
    if (field.length() < 30) return false;

    // See if we find field starts with a known call description
    // Only considered valid if it is the right length or is
    // terminated with a blank.  Or is the one known call description
    // that includes double blanks that get squenced down to a 25 character
    // field in this variant
    String call = CALL_LIST.getCode(field.substring(callStart));
    if (call != null) {
      int callEnd = callStart + call.length();
      if (callEnd == 26 || field.charAt(callEnd) == ' ' || call.equals("Fainting Abnormal Br")) {
        data.strCall = call;
        parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, field.substring(callEnd).trim(), data);
        data.strPlace = getLeft();
        return true;
      }
    }

    // No such luck.  Try parsing with a hard call cutoff at col 26 and again using the
    // smart address parser to pick one out and select the one that looks better.
    String call1 = field.substring(callStart, 26).trim();
    Result res1 = parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, field.substring(26).trim());

    Result res2 = parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, field.substring(callStart).trim());

    int status = res1.getStatus() - res2.getStatus();
    if (status == 0) {
      if (Character.isDigit(field.charAt(26)) && !Character.isDigit(field.charAt(25))) status = 1;
    }

    if (status > 0) {
      data.strCall = call1;
      res1.getData(data);
      data.strPlace = res1.getLeft();
    } else {
      res2.getData(data);
      data.strPlace = res2.getLeft();
    }
    return true;
  }

  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2})(\\d{6}) +([-+]?\\d{2})(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+'.'+match.group(2)+","+match.group(3)+'.'+match.group(4), data);
      }
    }
  }

  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) city = tmp;
    return super.adjustMapCity(city);
  }
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "CENTRAL COUNTY",     "St Peters",
      "DISPATCH.",          "",
      "RIVERS POINTE",      ""
  });

  // We use the call list ourselves, but do not want the smart address parser to use it, so
  // we don't call setupCallList to set it up.  But overriding getCallList allows the test
  // class to confirm that all identified call are in the list

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "AB Pn Above Navel +45",
      "AB Pn Near Faint 12-5",
      "Acute Heart Prob/MI",
      "Acute onset Pri Symp",
      "Acute onset Pri Symptom",
      "Advised Incident",
      "Alarm - Carbon Monoxide",
      "Alarm - Commercial",
      "Alert Diff Breathing",
      "Appliance Fire Conta",
      "Appliance Fire Contai",
      "Appliance Fire Contained",
      "Assault Not Alert",
      "Assault Unknown Stat",
      "Assist - Citizen (EMS)",
      "Assist - Citizen (Fire)",
      "Assist - Citizen (Fire)",
      "Assist - Police (EMS)",
      "Assist - Police (Fire)",
      "Back Pain Near Faint",
      "Back Pain Non Traumat",
      "Back Pain Not Alert",
      "Back Pain (No Trauma)",
      "Boat Fire Docked",
      "BP Diff Breathing",
      "Cardiac Arrest Delta",
      "Cardiac Arrest Not Br",
      "Cardiac Hx and alert",
      "CA Uncertain Breathin",
      "Chest Pain Diff Breat",
      "Chest Pain Normal +35",
      "Chest Pain QD",
      "Chest Pn Abnormal Bre",
      "Chest Pn Abnormal Breath",
      "Choking Ineffect Bre",
      "Choking Not Alert",
      "Choking Partial Bloc",
      "Citizen Assist",
      "Commercial Alarm",
      "Commercial Alrm",
      "Commercial Fire",
      "Commercial Gen Alrm",
      "Commercial/IND Fire",
      "COMMERCIAL/INDUSTRI",
      "Commercial Pull Alrm",
      "Commercial Smoke Alr",
      "Commercial Vehicle Fi",
      "Commercial WtrFlow A",
      "CP Clammy or cold swe",
      "CP Clammy or cold sweats",
      "CP Heart Attack/Angin",
      "CP Heart Attack/Angina HX",
      "DB clammy or cold sw",
      "DB clammy or cold swe",
      "Diabetic Not Alert",
      "Diabetic Unconscious",
      "Diff Breath Abnormal",
      "Diff Breath Color Cha",
      "Diff Breathing Abnor",
      "Diff Breathing Abnorm",
      "Diff Breathing Not A",
      "Diff Breathing Not Al",
      "Difficulty Breathing",
      "Downed Trees/Objects",
      "Electrical HZ Near Wa",
      "Entrapped No Inj / Tr",
      "Evaluation",
      "Explosion w/INJ UNK",
      "Fainting Abnormal Br",
      "Fainting  Abnormal Br",
      "Fall",
      "Fall Bravo",
      "Fall Non-Recent +6 h",
      "Fall Non-Recent +6 hr",
      "Fall Not Alert",
      "Fall Not Dangerous",
      "Fall Possibly Danger",
      "Fall Possibly Dangero",
      "Fall Possibly Dangerous",
      "Fall Unconscious",
      "Fall Unknown",
      "Focal/Absence Not Al",
      "Focal/Absence Not Alert",
      "Gas Leak OUTSD Tank",
      "Gas Leak RES OUTSD",
      "Gas Odor",
      "Gas Odor OUTSD",
      "Gas Odor RES",
      "General Sick Case",
      "Heart Problem Cardiac",
      "Heart Problem Diff Br",
      "Heart Problems Not Al",
      "Heart Problem Unk Sta",
      "Heat EXP Not Alert",
      "Heavy Smoke In The Ar",
      "Hemo NOT DANGEROUS",
      "Hemo Poss Dangerous",
      "Hemorrhage Diff Brea",
      "Hemorrhage Not Alert",
      "High Life Hazard Fir",
      "High Life Hazard Fire",
      "HIGH LIFE HZ Gen Alr",
      "HIGH LIFE HZ Keypad",
      "HIGH LIFE HZ Pull Al",
      "HIGH LIFE WtrFlow Al",
      "HIGH LIFE HZ Smoke A",
      "HIGH RISE Smoke Alrm",
      "Hydrant Problem",
      "Ingestion Antidepress",
      "Ingestion Not Alert",
      "Ingest/OD Unk Status",
      "Level III Transfer",
      "Level I Transfer",
      "Light Smoke In The Ar",
      "LRG Non-Dwelling Fi",
      "LRG Non-Dwelling Fire",
      "Medical Alarm No PT I",
      "Medical Alarm No PT Info",
      "Mobile Home/Trailer",
      "Mobile Home/Trailr Fire",
      "Motor Vehicle Accident",
      "Motor Vehicle Accident",
      "Move Up (EMS)",
      "Move Up (Fire)",
      "Move Up (Fire) Pumper",
      "MVA All Terrain Vehic",
      "MVA Bicycle / Motorcy",
      "MVA Injuries",
      "MVA Multi Inj Pts 1 U",
      "MVA Not Alert",
      "MVA Other Hazards",
      "MVA Pedestrian Struck",
      "MVA Possible Fatality",
      "MVA Uncons",
      "MVA Unknown Status",
      "MVA Unk Pts w/ Injuri",
      "MVA Unk Status Unk P",
      "MVA with Ejection",
      "MVA with Rollover",
      "No Cardiac Hx / alert",
      "No Cardiac Hx / alert +35",
      "No Cardiac Hx and -35",
      "NonDwell Smoke Alrm",
      "Not Alert",
      "OCC Veh Lockout",
      "OD Arrest",
      "OD Fentanyl",
      "OD Ingestion Fentany",
      "OD Uncon Fentanyl",
      "OD Unk Fentanyl",
      "Outside Fire",
      "Outside Fire Unknown",
      "Overdose Antidepress",
      "Overdose Not Alert",
      "Overdose QD",
      "Overdose Unconscious",
      "Overdose Unknown Stat",
      "PD BUILDING CHECK",
      "Penetrating Trauma Br",
      "Priority Transfer",
      "Psych Case Jumper",
      "Psych Case Minor Ble",
      "Psych Case Unk Statu",
      "Psych Case Unk Status",
      "Psych Non - Suicidal",
      "Psych Not Alert",
      "Public Assist No Inj",
      "Res CO Alrm",
      "Res Gen Alrm",
      "Residental Fire Multi",
      "Residential Alarm",
      "Residential Alrm",
      "Residential Fire",
      "Residential Fire Mult",
      "Residential Fire Multi",
      "Residential (multip",
      "Res Lightning Strike",
      "Res Smoke Alrm",
      "Res Unk Alrm",
      "Scheduled Transfer",
      "Seizures Continue/Mul",
      "Seizures Continuous/M",
      "Seizure Stopped No Hx",
      "Seizure Stroke/Seiz H",
      "Seizures Unk Breath -",
      "Seizures Unk Breath",
      "SELECT INCIDENT TYPE",
      "Service Call",
      "Sick Case Altered LOC",
      "Sick Case BP Problem",
      "Sick Case Cramps/Spas",
      "Sick Case Cut off Rin",
      "Sick Case Diff Breath",
      "Sick Case Dizziness",
      "Sick Case Generally W",
      "Sick Case Immobility",
      "Sick Case Nervous",
      "Sick Case No Symptoms",
      "Sick Case Not Alert",
      "Sick Case Other Pain",
      "Sick Case Unk Status",
      "Sick Case Vomiting",
      "Sinking Vehicle",
      "Small Brush/Grass Fi",
      "Small Brush/Grass Fir",
      "Small Outside Fire",
      "Small Outside Fire",
      "Smoke Investigation",
      "Stroke Not Alert",
      "Stroke Headache",
      "Stroke Numbness",
      "Stroke Paralysis",
      "Stroke Speech Probs",
      "Structure Fire Overri",
      "Suspected STROKE",
      "Threatening Suicide",
      "Transformer Fire",
      "Transformer wire po",
      "Trauma Injury Not Dan",
      "Trauma Injury Poss Da",
      "Traumatic Injury Seri",
      "Traumatic Injury Serious",
      "Trauma Unknown",
      "Unconscious Abn Breat",
      "Unconscious Breathing",
      "Unconscious Not Breat",
      "Unk EMS Life Question",
      "Unk EMS Stand/Sit/Mov",
      "Unk EMS Stand/Sit/Move",
      "Unknown EMS Delta",
      "Unknown EMS Problem",
      "Unknown EMS Problem QD",
      "Unk Sit Gen Alrm",
      "Vehicle Fire",
      "Vehicle Fire Reported",
      "Water Problem Elec/Hz",
      "Water Rescue",
      "Water Rescue Override",
      "Wires Arcing",
      "Wires down",
      "Wires Down"
    );
}