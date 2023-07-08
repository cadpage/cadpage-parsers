package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class ORJacksonCountyParser extends FieldProgramParser {

  private static final Pattern SRC_DATE_TIME_PTN = Pattern.compile(" - From +([A-Z0-9]+) +(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)$");

  public ORJacksonCountyParser() {
    super(CITY_CODES, "JACKSON COUNTY", "OR",
          "EMPTY? CODE CALL ( GPS1 GPS2 | ) ( AT | PLACE SKIP AT | ADDR ) CITY X? PRI:PRI! Unit:UNIT! UNIT+");
  }

  @Override
  public String getFilter() {
    return "Messaging@ecso911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! subject.equals("CAD Page") &&
        !subject.equals("Cell Phone Paging system") &&
        !subject.equals("Message from ECSO")) return false;
    if (!body.startsWith("DISPATCH:")) return false;
    body = body.substring(9).trim();

    Matcher match = SRC_DATE_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      body = body.substring(0,match.start()).trim();
    }

    // Look for truncated source date time
    else {
      int pt = body.indexOf(" - From");
      if (pt >= 0) {
        String src = body.substring(pt+7).trim();
        pt = src.indexOf(' ');
        if (pt >= 0) src = src.substring(0,pt);
        data.strSource = src;
      }
    }

    body = body.replace("Units:", "Unit:");
    return parseFields(body.split(","), 5, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " SRC DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("AT")) return new AtField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      String call = CALL_CODES.getProperty(field);
      if (call != null) data.strCall = call;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.equals(field)) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getOptional(" AT ");
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE";
    }
  }

  private class AtField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("at ")) return false;
      super.parse(field.substring(3).trim(), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('<');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("btwn ")) return false;
      field = field.substring(5).trim();
      field = field.replace(" and ", " / ");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "AB0",      "Abdominal Pain",
      "AB1",      "Abdominal Pain",
      "ABDOM-A4", "Abdominal Pain",
      "ABDOM-C2", "Abdominal Pain",
      "ABDOM-D2", "Abdominal Pain",
      "ADMIN",    "Administrative",
      "ADMINU",   "Admin Unavailable",
      "AIRCRFT-DE",                  "Aircraft Emergency",
      "AIRCRFTF", "Aircraft Emergency",
      "AL0",      "Allergic Reaction",
      "AL1",      "Allergic Reaction",
      "ALARMC",   "Carbon Monoxide Alrm",
      "ALARMF",   "Fire Alarm",
      "ALARMM-B3","Medical Alarm",
      "ALERT1",   "Minor Aircraft Issue",
      "ALERT2",   "Major Aircraft Issue",
      "ALERT3",   "Aircraft Crash",
      "ALLER-A4", "Allergic Reaction",
      "ALLER-B3", "Allergic Reaction",
      "ALLER-C1", "Allergic Reaction",
      "ALLER-C2", "Allergic Reaction",
      "ALLER-D1", "Allergic Reaction",
      "ALLER-D2", "Allergic Reaction",
      "ALLER-EE", "Allergic Reaction",
      "AN0",      "Animal Bite",
      "AN1",      "Animal Bite",
      "ARMSF",    "ARMS Activation",
      "AS0",      "Assault",
      "AS1",      "Assault",
      "ASLT-A3",  "Assault",
      "ASLT-A4",  "Assault",
      "ASLT-B2",  "Assault",
      "ASLT-B3",  "Assault",
      "ASLT-D1",  "Assault",
      "ASLT-D3",  "Assault",
      "ASLT-DE",  "Assault",
      "ASLTW-EE", "Assault w/a Weapon",
      "ASLTWF",   "Assault w/a Weapon",
      "ASSTAF",   "Assist to Agency",
      "ASSTPF",   "Assist to Public",
      "BACK-A4",  "Back Pain",
      "BACK-C1",  "Back Pain",
      "BACK-C2",  "Back Pain",
      "BACK-D1",  "Back Pain",
      "BARKF",    "Bark Fire",
      "BE0",      "Psyc Transport",
      "BE1",      "Psyc Transport",
      "BITE-A4",  "Bite Injury",
      "BITE-B2",  "Bite Injury",
      "BITE-B3",  "Bite Injury",
      "BITE-D1",  "Bite Injury",
      "BITE-D2",  "Bite Injury",
      "BITE-DE",  "Bite Injury",
      "BK0",      "Back Pain",
      "BK1",      "Back Pain",
      "BL0",      "Bleeding",
      "BL1",      "Bleeding",
      "BP-C1",    "Breathing Problems",
      "BP-C3",    "Breathing Problems",
      "BP-D1",    "Breathing Problems",
      "BP-EE",    "Breathing Problems",
      "BR0",      "Breathing Problem",
      "BR1",      "Breathing Problem",
      "BR3",      "Breathing Problem",
      "BU0",      "Burn",
      "BU1",      "Burn",
      "BURN-A4",  "Burns",
      "BURN-B3",  "Burns",
      "BURN-C1",  "Burns",
      "BURN-CE",  "Burns",
      "BURN-D1",  "Burns",
      "BURN-D3",  "Burns",
      "BURN-DE",  "Burns",
      "BURN-EE",  "Burns",
      "CARB-B4",  "Carbon Monoxide",
      "CARB-C1",  "Carbon Monoxide",
      "CARB-D1",  "Carbon Monoxide",
      "CARB-D3",  "Carbon Monoxide",
      "CARB-DE",  "Carbon Monoxide",
      "CB-B4",    "Childbirth",
      "CB-C2",    "Childbirth",
      "CB-D1",    "Childbirth",
      "CB-D2",    "Childbirth",
      "CH0",      "Chest Pain",
      "CH1",      "Chest Pain",
      "CH3",      "Chest Pain",
      "CHOKE-A4", "Choking",
      "CHOKE-D1", "Choking",
      "CHOKE-EE", "Choking",
      "COLLAP-D1","Collapse",
      "COMMCARE", "Community Care",
      "CP-A3",    "Chest Pains",
      "CP-C1",    "Chest Pains",
      "CP-C3",    "Chest Pains",
      "CP-D1",    "Chest Pains",
      "CP-D3",    "Chest Pains",
      "CPR-B4",   "Cardiac Arrest",
      "CPR-D1",   "Cardiac Arrest",
      "CPR-DE",   "Cardiac Arrest",
      "CPR-EE",   "Cardiac Arrest",
      "DAMF",     "Dam Emergency",
      "DEATH-B4", "Death Investigation",
      "DEATH-D1", "Death Investigation",
      "DEATH-EE", "Death Investigation",
      "DEATH-O6", "Death Investigation",
      "DI0",      "Diabetic Problem",
      "DI1",      "Diabetic Problem",
      "DIAB-A4",  "Diabetic Problem",
      "DIAB-C1",  "Diabetic Problem",
      "DIAB-C3",  "Diabetic Problem",
      "DIAB-D1",  "Diabetic Problem",
      "DIVERT",   "HOSPITAL DIVERSION",
      "DR0",      "Drowning",
      "DR1",      "Drowning",
      "DR1R",     "Drowning",
      "DR3",      "Drowning",
      "DROWN-A4", "Drowning",
      "DROWN-B2", "Drowning",
      "DROWN-B3", "Drowning",
      "DROWN-B4", "Drowning",
      "DROWN-C1", "Drowning",
      "DROWN-C2", "Drowning",
      "DROWN-D1", "Drowning",
      "DROWN-D2", "Drowning",
      "DROWN-EE", "Drowning",
      "EL0",      "Electrocution",
      "EL1",      "Electrocution",
      "EL3",      "Electrocution",
      "ELEC-C3",  "Electrocution",
      "ELEC-D1",  "Electrocution",
      "ELEC-D2",  "Electrocution",
      "ELEC-D3",  "Electrocution",
      "ELEC-EE",  "Electrocution",
      "ENTRAP-B2","Entrapment",
      "ENTRAP-D1","Entrapment",
      "ENTRAPA-B2",                  "Entrapment",
      "ENTRAPA-D1",                  "Entrapment",
      "ETF",      "Emergent Transport",
      "EXPLO-A4", "Explosion",
      "EXPLO-B3", "Explosion",
      "EXPLO-C1", "Explosion",
      "EXPLO-D1", "Explosion",
      "EXPLO-D3", "Explosion",
      "EXPLO-DE", "Explosion",
      "EXPLO-EE", "Explosion",
      "EXPLOF",   "Explosion",
      "EXPO-A4",  "Exposure",
      "EXPO-B3",  "Exposure",
      "EXPO-B4",  "Exposure",
      "EXPO-C1",  "Exposure",
      "EXPO-C3",  "Exposure",
      "EXPO-D1",  "Exposure",
      "EXPO-D3",  "Exposure",
      "EY0",      "Eye Problem",
      "EY1",      "Eye Problem",
      "EYE-A4",   "Eye Injury",
      "EYE-B2",   "Eye Injury",
      "EYE-D1",   "Eye Injury",
      "FA0",      "Fall",
      "FA1",      "Fall",
      "FA3",      "Fall",
      "FALL-A3",  "Fall",
      "FALL-A4",  "Fall",
      "FALL-B2",  "Fall",
      "FALL-B3",  "Fall",
      "FALL-D1",  "Fall",
      "FINV",     "Fire Investigation",
      "FLUEF",    "Flue Fire",
      "FUELLK",   "Fuel Leak",
      "GAS",      "Gas",
      "GRASSF",   "Grass Fire",
      "GSW-A4",   "Gunshot Wound",
      "GSW-B2",   "Gunshot Wound",
      "GSW-B3",   "Gunshot Wound",
      "GSW-B4",   "Gunshot Wound",
      "GSW-D1",   "Gunshot Wound",
      "GSW-D2",   "Gunshot Wound",
      "GSW-D3",   "Gunshot Wound",
      "GSW-DE",   "Gunshot Wound",
      "HAZM",     "Comb Hazardous Mat",
      "HAZM-B4",  "Hazardous Material",
      "HAZM-C1",  "Hazardous Material",
      "HAZM-D1",  "Hazardous Material",
      "HAZM-D3",  "Hazardous Material",
      "HAZM-DE",  "Hazardous Material",
      "HAZMF",    "Hazardous Material",
      "HC0",      "Hypothermia",
      "HC1",      "Hypothermia",
      "HC3",      "Hypothermia",
      "HE0",      "Headache",
      "HE1",      "Headache",
      "HEAD-A4",  "Headache",
      "HEAD-B3",  "Headache",
      "HEAD-C1",  "Headache",
      "HEAD-C3",  "Headache",
      "HEART-A3", "Heart Problems",
      "HEART-C1", "Heart Problems",
      "HEART-C3", "Heart Problems",
      "HEART-D1", "Heart Problems",
      "HEM-A4",   "Hemorrhage",
      "HEM-B2",   "Hemorrhage",
      "HEM-B3",   "Hemorrhage",
      "HEM-C2",   "Hemorrhage",
      "HEM-C3",   "Hemorrhage",
      "HEM-D1",   "Hemorrhage",
      "HH0",      "Hyperthermia",
      "HH1",      "Hyperthermia",
      "HH3",      "Hyperthermia",
      "HM1",      "Hazardous Material",
      "ILBURN",   "Illegal Burn",
      "IN0",      "Poisoning",
      "IN1",      "Poisoning",
      "INFOF",    "Fire Info",
      "INSPEC",   "Fire Inspection",
      "LIFT-A6",  "Lift Assist",
      "LIFTF",    "Lift Assist",
      "MAINT",    "Vehicle Maintenance",
      "MAINTU",   "Vehicle Maintenance",
      "MEDICAL",  "Medical Aid Request",
      "MESSF",    "Message",
      "MISCF",    "Miscellaneous",
      "MISSF",    "Missing Person",
      "MVCBL-D1", "MVC w/ Building",
      "MVCE-D1",  "MVC w/ Entrapment",
      "MVCI-A4",  "MVC w/ Injury",
      "MVCI-B2",  "MVC w/ Injury",
      "MVCI-D1",  "MVC w/ Injury",
      "MVCI-DE",  "MVC w/ Injury",
      "MVCP-O6",  "MVC w/ Prop Damage",
      "MVCPD-D1", "MVC w/ Ped or Bike",
      "MVCR-D1",  "MVC w/ Rollover",
      "MVCU-A3",  "MVC Unknown Injury",
      "MVCU-A4",  "MVC Unknown Injury",
      "MVCU-B2",  "MVC Unknown Injury",
      "MVCU-B3",  "MVC Unknown Injury",
      "MVCU-D1",  "MVC Unknown Injury",
      "NATGAS",   "Natural Gas Leak",
      "OB0",      "Pregnancy Problems",
      "OB1",      "Pregnancy Problems",
      "OB3",      "Pregnancy Problems",
      "OD-B3",    "Overdose",
      "OD-C1",    "Overdose",
      "OD-C3",    "Overdose",
      "OD-D1",    "Overdose",
      "OD-DE",    "Overdose",
      "OD-EE",    "Overdose",
      "OD0",      "Overdose",
      "OD1",      "Overdose",
      "OD3",      "Overdose",
      "ODOREF",   "Electrical Odor",
      "ODORG-B4", "Odor of gas",
      "ODORG-C1", "Odor of gas",
      "ODORG-D1", "Odor of gas",
      "ODORG-D3", "Odor of gas",
      "ODORG-DE", "Odor of gas",
      "ODORGF",   "Odor of gas",
      "OVEN",     "Res Oven Fire",
      "PERDN-D1", "Person Down",
      "PO1",      "Poisoning",
      "POISN-B3", "Poisoning",
      "POISN-C1", "Poisoning",
      "POISN-C3", "Poisoning",
      "POISN-D1", "Poisoning",
      "POISN-DE", "Poisoning",
      "POISN-O6", "Poisoning",
      "POWER",    "Power Line Problem",
      "PREG-A3",  "Pregnancy Problems",
      "PREG-A4",  "Pregnancy Problems",
      "PREG-B3",  "Pregnancy Problems",
      "PREG-C2",  "Pregnancy Problems",
      "PREG-C3",  "Pregnancy Problems",
      "PREG-D1",  "Pregnancy Problems",
      "PREG-O4",  "Pregnancy Problems",
      "PSYCH-A6", "Psych Transport",
      "RCAD",     "Remote CAD Entry",
      "REMOTE-D1","Inaccessible Terrain",
      "RESCUE-A3","Rescue",
      "RESCUE-A4","Rescue",
      "RESCUE-A6","Rescue",
      "RESCUE-B2","Rescue",
      "RESCUE-B3","Rescue",
      "RESCUE-D1","Rescue",
      "RESCUE-DE","Rescue",
      "RR",       "Comb RR Accident",
      "RR-D1",    "Railroad Accident",
      "RRF",      "Railroad Accident",
      "RVSST",    "Struct Strike Team",
      "RVTST",    "Tender Strike Team",
      "RVWST",    "Wildland Strike Team",
      "RVWTF",    "Wildland Task Force",
      "SAFEF",    "Public Safety Hazard",
      "SAWF",     "Sawdust Fire",
      "SEIZ-A4",  "Seizure",
      "SEIZ-B1",  "Seizure",
      "SEIZ-C1",  "Seizure",
      "SEIZ-C3",  "Seizure",
      "SEIZ-D1",  "Seizure",
      "SF0",      "Seasonal Flu",
      "SF1",      "Seasonal Flu",
      "SF3",      "Seasonal Flu",
      "SICK-A3",  "Sick Person",
      "SICK-A4",  "Sick Person",
      "SICK-B3",  "Sick Person",
      "SICK-C1",  "Sick Person",
      "SICK-C3",  "Sick Person",
      "SICK-D1",  "Sick Person",
      "SICK-O4",  "Sick Person",
      "SICK-O6",  "Sick Person",
      "SK0",      "Sick Person",
      "SK1",      "Sick Person",
      "SLIDE-D1", "Landslide",
      "SMOKE",    "Smoke Investigation",
      "ST0",      "Stroke",
      "ST1",      "Stroke",
      "STAB-A4",  "Stabbing",
      "STAB-B2",  "Stabbing",
      "STAB-B3",  "Stabbing",
      "STAB-B4",  "Stabbing",
      "STAB-D1",  "Stabbing",
      "STAB-D2",  "Stabbing",
      "STAB-D3",  "Stabbing",
      "STAB-DE",  "Stabbing",
      "STF",      "Scheduled Transport",
      "STRAP",    "Comb Struc Fire Trap",
      "STRAPF",   "Struct Fire Trapped",
      "STROKE-A3","Stroke",
      "STROKE-C1","Stroke",
      "STROKE-C3","Stroke",
      "STRUCF",   "Structure Fire",
      "SUIC-A3",  "Suicidal",
      "SUIC-A4",  "Suicidal",
      "SUIC-A6",  "Suicidal",
      "SUIC-B2",  "Suicidal",
      "SUIC-B3",  "Suicidal",
      "SUIC-B4",  "Suicidal",
      "SUIC-C1",  "Suicidal",
      "SUIC-C3",  "Suicidal",
      "SUIC-D1",  "Suicidal",
      "SUIC-D2",  "Suicidal",
      "SUIC-D3",  "Suicidal",
      "SUIC-DE",  "Suicidal",
      "SUIC-EE",  "Suicidal",
      "SUIC-O3",  "Suicidal",
      "SYSMF",    "System Maintenance",
      "SZ0",      "Seizures",
      "SZ1",      "Seizures",
      "SZ3",      "Seizures",
      "TA0",      "MVC Unknown Injury",
      "TA1",      "MVC w/ Injury",
      "TA1C",     "MVC w/ Ped or Bike",
      "TA1P",     "MVC w/ Entrapment",
      "TA1R",     "MVC w/ Rollover",
      "TA2",      "MVC w/ Prop Damage",
      "TEST",     "Comb Test Incident",
      "TESTF",    "Test Incident",
      "TR0",      "Traumatic Injury",
      "TR1",      "Traumatic Injury",
      "TR1P",     "Traumatic Entrapment",
      "TR3",      "Traumatic Injury",
      "TRAIN",    "Training",
      "TRAINU",   "Training Unavailable",
      "TRASHF",   "Trash Fire",
      "TRAUM-A3", "Traumatic Injury",
      "TRAUM-A4", "Traumatic Injury",
      "TRAUM-B1", "Traumatic Injury",
      "TRAUM-B2", "Traumatic Injury",
      "TRAUM-B3", "Traumatic Injury",
      "TRAUM-B4", "Traumatic Injury",
      "TRAUM-C2", "Traumatic Injury",
      "TRAUM-C3", "Traumatic Injury",
      "TRAUM-D1", "Traumatic Injury",
      "TRAUM-D2", "Traumatic Injury",
      "TRAUM-D3", "Traumatic Injury",
      "TRAUM-DE", "Traumatic Injury",
      "TREEF",    "Tree Fire",
      "UK0",      "Unknown Problem",
      "UK1",      "Unknown Problem",
      "UN0",      "Unconscious",
      "UN1",      "Unconscious",
      "UN3",      "Unconscious",
      "UNC-A3",   "Unconscious",
      "UNC-C1",   "Unconscious",
      "UNC-C2",   "Unconscious",
      "UNC-C3",   "Unconscious",
      "UNC-D1",   "Unconscious",
      "UNC-EE",   "Unconscious",
      "UNK-B3",   "Unknown Problem",
      "UNK-B4",   "Unknown Problem",
      "VEHF",     "Vehicle Fire",
      "WA",       "Comb Water Assist",
      "WAF",      "Water Assist",
      "WI",       "Comb Walk-In Medical",
      "WIF",      "Walk-in Medical",
      "WR-B2",    "Water Rescue",
      "WR-C1",    "Water Rescue",
      "WR-D1",    "Water Rescue",
      "WR-D2",    "Water Rescue",
      "WR-D4",    "Water Rescue",
      "WR-EE",    "Water Rescue"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AS", "ASHLAND",
      "BF", "BUTTE FALLS",
      "CJ", "CAVE JUNCTION",
      "CL", "CRATER LAKE",
      "CP", "CENTRAL POINT",
      "DL", "DIAMOND LAKE",
      "EP", "EAGLE POINT",
      "GH", "GOLD HILL",
      "GP", "GRANTS PASS",
      "JV", "JACKSONVILLE",
      "KE", "KERBY",
      "KF", "KLAMATH FALLS",
      "ME", "MERLIN",
      "MF", "MEDFORD",
      "OB", "O'BRIEN",
      "PH", "PHOENIX",
      "PR", "PROSPECT",
      "RR", "ROGUE RIVER",
      "SC", "SHADY COVE",
      "SE", "SELMA",
      "TA", "TALENT",
      "TR", "TRAIL",
      "WC", "WHITE CITY",
      "WI", "WILLIAMS",
      "WO", "WOLF CREEK",
      "WV", "WILDERVILLE"
  });
}
