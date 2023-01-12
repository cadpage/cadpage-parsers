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
          "ID:ID_CODE_CALL_ADDR! APT:APT! BUS:PLACE? X_ST:X? MAP:MAP? CHL:CH_CITY! GPS:GPS? Units:UNIT!");
    removeWords("TERRACE");
  }

  @Override
  public String getFilter() {
    return "dispatch@sccda.org,dispatch@sccmo.org,SCCEC_info@sccmo.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern PRENOTE_PTN = Pattern.compile("\\b\\d+\\) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d)-\\[\\d+\\] (?:\\[(?:Notification|Page)\\])? *(.*)");
  private static final Pattern PRENOTE_PREFIX_PTN = Pattern.compile("(\\d{6}-\\d{5}) New Notification:");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=APT:|X ST:|MAP:|CHL:|GPS:)");

  @Override
  public boolean parseMsg(String body, Data data) {

    // Parser preceding comment
    String prefix = "";
    if (body.startsWith("Comment:")) {
      int pt = body.indexOf(",ID:", 8);
      if (pt < 0) pt = body.indexOf("ID:");
      if (pt < 0) return false;
      data.strSupp = prefix = body.substring(8, pt).trim();
      if (body.charAt(pt) == ',') pt++;
      body = body.substring(pt).trim();
    }

    // Dispatch sends a fixed length field format and a variable length field format.
    // Things get easier if this is a fixed format alert
    if (parseFixedMsg(body, data)) return true;

    // Otherwise reset things and try the variable field format
    data.initialize(this);
    data.strSupp = prefix;

    // OK, First look for prenotification variant
    Matcher match = PRENOTE_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      setFieldList("ID CODE CALL ADDR PLACE DATE TIME INFO");
      match = PRENOTE_PTN.matcher(body);
      if (!match.find()) return false;
      if (!parseIdCodeCallAddress(body.substring(0,match.start()).trim(), data)) return false;
      data.strDate =  match.group(1);
      data.strTime = match.group(2);
      data.strSupp = append(data.strSupp, "\n", match.group(3));
      return true;


    }

    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }

  private boolean parseFixedMsg(String body, Data data) {
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
        data.strCity = cleanCity(p.get(30));
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

    int fLen = p.checkAhead("APT:", 101, 100);
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
      data.strCity = cleanCity(p.get(30));
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
      data.strCity = cleanCity(p.get(30));
      if (!p.check("Units:")) return false;
      data.strUnit = p.get();
      return true;
    }
  }

  @Override
  public String getProgram() {
    return "INFO? " + super.getProgram();
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

  private static String cleanCity(String city) {
    int pt = city.indexOf("(Unin");
    if (pt >= 0)  city = city.substring(0,pt).trim();
    if (city.equalsIgnoreCase("Unincorporated")) city = "";
    else if (city.equals("Unincor STL Co")) city = "St Louis County";
    return city;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_CODE_CALL_ADDR")) return new MyIdCodeCallAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CH_CITY")) return new MyChannelCityField();
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

    // Some variable formats have an AT marker, which we will use to mark the start
    // of the address.  Trusting their consistent use of camel case for real address
    // fields to guarantee this won't find a street name starting with AT.

    // But, trailing place names are upper case and can start with AT.  So add a sanity check
    // if the terminator is more than 30 characters out
    int pt = field.indexOf(" AT", callStart);
    if (pt >= 0 && pt <= 30) {
      data.strCall = field.substring(callStart,pt).trim();
      parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, field.substring(pt+3).trim(), data);
      data.strPlace = getLeft();
      return true;
    }

    // See if we find field starts with a known call description
    // Only considered valid if it is the right length or is
    // terminated with a blank.  Or is the one known call description
    // that includes double blanks that get sequenced down to a 25 character
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

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }

  private static final Pattern CH_CITY_PTN = Pattern.compile("([A-Z]*TAC\\d+)\\b *(.*)");
  private class MyChannelCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  CH_CITY_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1);
        field = match.group(2);
      }
      field = cleanCity(field);
      if (!field.isEmpty()) super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH CITY";
    }
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
      "AB Pain Not Specific",
      "AB Pn Above Navel +45",
      "AB Pn Near Faint 12-5",
      "Abdominal Pain 12-50",
      "Acute Heart Prob/MI",
      "Acute onset Pri Symp",
      "Acute onset Pri Symptom",
      "Advised Incident",
      "Alarm - Carbon Monoxide",
      "Alarm - Commercial",
      "Allergy Difficult Br",
      "Allergy Difficult Bre",
      "Alert Diff Breathing",
      "Animal Bite Unk Statu",
      "Appliance Fire Conta",
      "Appliance Fire Contai",
      "Appliance Fire Contained",
      "Arrest Stab/Gunshot",
      "Assist - EMS (Fire)",
      "Assist - MIH",
      "Assault Not Alert",
      "Assault Unknown Stat",
      "Assist - Citizen (EMS)",
      "Assist - Citizen (Fire)",
      "Assist - Citizen (Fire)",
      "Assist - Police (EMS)",
      "Assist - Police (Fire)",
      "Back Pain Near Faint",
      "Back Pain Non Recent",
      "Back Pain Non Traumat",
      "Back Pain Not Alert",
      "Back Pain (No Trauma)",
      "Boat Fire Docked",
      "Born No Complication",
      "BP Diff Breathing",
      "BRSH FRE bravo",
      "BRUSH FIRE unknown",
      "BSH FRE small",
      "Building Collapse",
      "Carbon dioxide",
      "Cardiac Arrest Delta",
      "Cardiac Arrest Not Br",
      "Cardiac Arrest Not Breath",
      "Cardiac Hx and alert",
      "CA Uncertain Breathin",
      "CC Transfer (CCT)",
      "Chest Pain Alpha",
      "Chest Pain Diff Breat",
      "Chest Pain Normal +35",
      "Chest Pain Not Alert",
      "Chest Pain QD",
      "Chest Pain/Discomfort",
      "Chest Pn Abnormal Bre",
      "Chest Pn Abnormal Breath",
      "Choking Ineffect Bre",
      "Choking Not Alert",
      "Choking Partial Bloc",
      "Citizen Assist",
      "Citizen Assist Unk",
      "Commercial Alarm",
      "Commercial Alrm",
      "Commercial Fire",
      "Commercial Gen Alrm",
      "Commercial Heat Alrm",
      "Commercial/IND Fire",
      "COMMERCIAL/INDUSTRI",
      "Commercial Pull Alrm",
      "Commercial Smoke Al",
      "Commercial Smoke Alr",
      "Commercial Vehicle Fi",
      "Commercial Vehicle Fire",
      "Commercial WtrFlow",
      "Commercial WtrFlow A",
      "Commercial WtrFlow Alrm",
      "CP Clammy or cold swe",
      "CP Clammy or cold sweats",
      "CP Heart Attack/Angin",
      "CP Heart Attack/Angina HX",
      "DB clammy or cold sw",
      "DB clammy or cold swe",
      "Diabetic Not Alert",
      "Diabetic Seizures",
      "Diabetic Unconscious",
      "Diff Breath Abnormal",
      "Diff Breath Color Cha",
      "Diff Breathing Abnor",
      "Diff Breathing Abnorm",
      "Diff Breathing Not A",
      "Diff Breathing Not Al",
      "Difficulty Breathing",
      "Downed Trees/Objects",
      "Drowning Not Alert",
      "Electrical ARCING",
      "Electrical Hazard W H",
      "Electrical HZ Near Wa",
      "Electrical Odor",
      "ENTRPMT Peripheral On",
      "Entrapped No Inj / Tr",
      "Evaluation",
      "Explosion w/INJ UNK",
      "Fainting Abnormal Br",
      "Fainting  Abnormal Br",
      "Fainting Not Breath",
      "Fall",
      "Fall Bravo",
      "Fall Non-Recent +6 h",
      "Fall Non-Recent +6 hr",
      "Fall Non-Recent +6 hrs",
      "Fall Not Alert",
      "Fall Not Dangerous",
      "Fall Possibly Danger",
      "Fall Possibly Dangero",
      "Fall Possibly Dangerous",
      "Fall Unconscious",
      "Fall Unknown",
      "Fire Reported OUT",
      "Focal/Absence Not Al",
      "Focal/Absence Not Ale",
      "Focal/Absence Not Alert",
      "Fuel Spill",
      "Fuel Spill Outdoor",
      "Gas Leak COMM OUTSD",
      "Gas Leak OUTSD Tank",
      "Gas Leak RES",
      "Gas Leak RES OUTSD",
      "Gas Odor",
      "Gas Odor COMM OUTSD",
      "Gas Odor OUTSD",
      "Gas Odor RES",
      "General Sick Case",
      "UNCONTAINED HAZMAT",
      "Headache Abnormal Bre",
      "Headache Not Alert",
      "Heart Problem Cardiac",
      "Heart Problem Diff Br",
      "Heart Problems Not Al",
      "Heart Problem Unk Sta",
      "Heat EXP Not Alert",
      "Heavy Smoke In The Ar",
      "Hemorrhage DANGEROUS",
      "Hemo NOT DANGEROUS",
      "Hemo Poss Dangerous",
      "Hemorrhage Diff Brea",
      "Hemorrhage Not Alert",
      "HI LIFE HZ Keypad A",
      "HI LIFE HZ Pull Alr",
      "High Angle Rescue",
      "High Life Hazard Fir",
      "High Life Hazard Fire",
      "HIGH LIFE HZ Gen Al",
      "HIGH LIFE HZ Gen Alr",
      "HIGH LIFE HZ Keypad",
      "HIGH LIFE HZ Pull Al",
      "HIGH LIFE WtrFlow Al",
      "HIGH LIFE HZ Smoke",
      "HIGH LIFE HZ Smoke A",
      "HIGH LIFE WtrFlow A",
      "HIGH LIFE WtrFlow Alrm",
      "HIGH RISE Smoke Alrm",
      "HP Clammy/Cold Sweats",
      "Hydrant Problem",
      "Illegal Burn",
      "Ingestion Antidepress",
      "Ingestion Not Alert",
      "Ingestion Unconsciou",
      "Ingest/OD Unk Status",
      "LARGE NON-DWELLING b",
      "Large Outside Fire",
      "Level III Transfer",
      "Level I Transfer",
      "Light Smoke In The Ar",
      "LG Fire w/Sturctures",
      "LONG FALL",
      "LRG BRSH FIRE",
      "LRG Non-Dwelling Fi",
      "LRG Non-Dwelling Fire",
      "Machinery Fire",
      "Medical Alarm No PT I",
      "Medical Alarm No PT Info",
      "Mobile home, house",
      "Mobile Home/Trailer",
      "Mobile Home/Trailr Fire",
      "Motor Vehicle Accident",
      "Motor Vehicle Accident",
      "Move Up (EMS)",
      "Move Up (Fire)",
      "Move Up (Fire) Pumper",
      "Mutual Aid to STAGNG",
      "MVA All Terrain Vehic",
      "MVA Bicycle / Motorcy",
      "MVA High Mechanism",
      "MVA HIGH VELOCITY",
      "MVA Injuries",
      "MVA Multi Inj Pts 1 U",
      "MVA Multiple Patients",
      "MVA No Injuries",
      "MVA Not Alert",
      "MVA Other Hazards",
      "MVA Pedestrian Struck",
      "MVA Possible Fatality",
      "Residental Fire",
      "MVA Uncons",
      "MVA Unknown Status",
      "MVA Unk Pts w/ Injuri",
      "MVA Unk Status Unk P",
      "MVA with Ejection",
      "MVA with Rollover",
      "No Cardiac Hx / alert",
      "No Cardiac Hx / alert +35",
      "No Cardiac Hx and -35",
      "MVA No Dangerous Inju",
      "NonDwell Smoke Alrm",
      "Not Alert",
      "OBVS Death Cold / Sti",
      "OBVS Death Cold / Stiff",
      "OBVS Death Questiona",
      "OBVS Death Questionab",
      "OCC Veh Lockout",
      "OD Arrest",
      "OD Dif Brth Fntnyl",
      "OD Fentanyl",
      "OD Ingestion",
      "OD Ingestion Fentany",
      "OD Uncon Fentanyl",
      "OD Unk Fentanyl",
      "Outside Fire",
      "Outside Fire Unknown",
      "Overdose Antidepress",
      "Overdose Diff Breathi",
      "Overdose Not Alert",
      "Overdose QD",
      "Overdose Unconscious",
      "Overdose Unknown Stat",
      "PD BUILDING CHECK",
      "Penetrating Trauma Br",
      "Penetrating Trauma De",
      "Poisoning No Symptom",
      "PR Assignment",
      "Pregnancy Unknown Sta",
      "Priority Transfer",
      "Psych Case Jumper",
      "Psych Case Minor Ble",
      "Psych Case Unk Statu",
      "Psych Case Unk Status",
      "Psych Non - Suicidal",
      "Psych Non-Suicidal",
      "Psych Not Alert",
      "Psych Suicidal Threa",
      "Public Assist No Inj",
      "Public Assist No Inju",
      "Res CO Alrm",
      "Res CO Multi Sick",
      "Res Gen Alrm",
      "Resi Multi Fire OUT",
      "Residental",
      "Residential Fire Mul",
      "Residental Fire Multi",
      "Residential Alarm",
      "Residential Alrm",
      "Residential Fire",
      "Residental Fire Mul",
      "Residential Fire Mult",
      "Residential Fire Multi",
      "Residential (multip",
      "Res Alrm",
      "Res Lightning Strike",
      "Res Smoke Alrm",
      "Res Unk Alrm",
      "Res WtrFlow Alrm",
      "Resp Arrest Ineffecti",
      "Scheduled Transfer",
      "Seizures Continue/Mul",
      "Seizures Continuous/M",
      "Seizures Pregnancy",
      "Seizure Stopped No Hx",
      "Seizure Stopped Seiz",
      "Seizure Stopped Unk H",
      "Seizure Stopped Unk Hx",
      "Seizure Stroke Hx",
      "Seizure Stroke/Seiz H",
      "Seizures Unk Breath <",
      "Seizures Unk Breath -",
      "Seizures Unk Breath",
      "SELECT INCIDENT TYPE",
      "Service Call",
      "Sewer/Drain",
      "Sick Case Altered LOC",
      "Sick Case BP Problem",
      "Sick Case Can't Urina",
      "Sick Case Cramps/Spas",
      "Sick Case Cut off Rin",
      "Sick Case Diff Breath",
      "Sick Case Dizziness",
      "Sick Case Fever or Ch",
      "Sick Case Generally W",
      "Sick Case Immobility",
      "Sick Case Nausea",
      "Sick Case Nervous",
      "Sick Case No Symptoms",
      "Sick Case Not Alert",
      "Sick Case Other Pain",
      "Sick Case Unk Status",
      "Sick Case Vomiting",
      "Sinking Vehicle",
      "Small Brush/Grass Fi",
      "Small Brush/Grass Fir",
      "SMALL NON-DWELLING",
      "Small Outside Fire",
      "Small Outside Fire",
      "Smoke Investigation",
      "Stabbing Unknown Sta",
      "Stroke Ab Breathing",
      "Stroke Breath Norm",
      "Stroke Not Alert",
      "Stroke Headache",
      "Stroke Loss Balance",
      "Stroke Numbness",
      "Stroke Paralysis",
      "Stroke Speech Probs",
      "Stroke Vision Proble",
      "Structure Fire Overri",
      "Structure Fire Override",
      "Suspected STROKE",
      "Threatening Suicide",
      "TI Chest or Neck Inju",
      "Trach obv distress",
      "Transformer Fire",
      "Transformer Hazard",
      "Transformer wire po",
      "Trauma Injury Not Dan",
      "Trauma Injury Poss Da",
      "Traumatic Injury Seri",
      "Traumatic Injury Serious",
      "Trauma Injury Poss Danger",
      "Trauma Unknown",
      "Unconscious Abn Breat",
      "Unconscious Breathing",
      "Unconscious Not Breat",
      "Unk EMS Life Question",
      "Unk EMS Stand/Sit/Mov",
      "Unk EMS Stand/Sit/Move",
      "Unknown building",
      "Unknown EMS Delta",
      "Unknown EMS Problem",
      "Unknown EMS Problem QD",
      "Unk Prob Language Bar",
      "Unk Sit Gen Alrm",
      "Vehicle Fire",
      "Vehicle fire near g",
      "Vehicle Fire Reported",
      "Water Main Break",
      "Water Problem",
      "Water Problem Elec/H",
      "Water Problem Elec/Hz",
      "Water Rescue",
      "Water Rescue Override",
      "Watercraft Welfare C",
      "Wires Arcing",
      "Wires down",
      "Wires Down",
      "Wires down ARCING"
    );
}