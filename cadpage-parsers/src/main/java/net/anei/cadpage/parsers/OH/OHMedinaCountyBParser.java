package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHMedinaCountyBParser extends DispatchEmergitechParser {

  public OHMedinaCountyBParser() {
    super("Medina911:", CITY_LIST, "MEDINA COUNTY", "OH", TrailAddrType.PLACE);
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    String call = data.strCall;
    String callExt = "";
    int pt = call.indexOf(" - ");
    if (pt >= 0) {
      call = call.substring(0, pt);
      callExt = call.substring(pt);
    }
    String call2 = CALL_CODES.getProperty(data.strCall);
    if  (call2 != null) {
      data.strCode = call;
      data.strCall = call2 + callExt;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CALL", "CODE CALL");
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "10",       "Backup Another Unit",
      "12",       "Burglary",
      "12A",      "Burglary in Progress",
      "12B",      "Alarm Activated entry or burglary",
      "13",       "Special Detail",
      "14",       "Bad Check or Forgery",
      "15",       "Warrant Service",
      "15A",      "Search Warrant",
      "15B",      "Civil Paper Service",
      "16",       "Deceased Person DOA Dead",
      "18",       "Animal Complaint/ Barking Dog/ Loose K9",
      "19",       "Drunk Driver Complaint",
      "2",        "MVC no Injuries",
      "20",       "Domestic",
      "21",       "Prisoner Relay",
      "22",       "Drowning - Water Rescue",
      "23",       "Lunch",
      "24",       "Drunk, alcohol complaint, intoxicated",
      "24A",      "Drug Complaint",
      "26",       "Disturbance or Fight",
      "26SR",     "Fire - Residential Structure ",
      "28",       "Fire - Unknown Other",
      "2812BC",   "Fire Alarm - Commercial",
      "2812BR",   "Fire Alarm - Residential",
      "28B",      "Bomb Threat",
      "28D",      "Fire - Dumpster - Trash",
      "28G",      "Fire - Grass - Brush",
      "28SC",     "Fire - Structure - Commercial",
      "28SR",     "Fire - Structure - Residential",
      "28TEST",   "Fire Alarm Testing",
      "28U",      "Fire - Transformer or Wires",
      "28V",      "Fire - Vehicle",
      "28W",      "Fire - Gas Well",
      "29",       "Squad - Ambulance Request",
      "2912B",    "Medical Alarm",
      "2PP",      "Private Property MVC",
      "31",       "Tow Request",
      "32",       "Homicide",
      "34",       "Juvenile Complaint - Unruly",
      "36",       "Theft - Larceny",
      "38",       "Missing Person",
      "3834",     "Missing Juvenile",
      "4",        "MVC w/Injuries",
      "40DIS",    "C40's Being Discharged",
      "41",       "Unit Using Portable Radio ",
      "42",       "Nature Unknown",
      "4PP",      "Private Property Injury Crash ",
      "53",       "Mental ",
      "54",       "Stabbing/Cutting ",
      "55",       "COP / Extra Patrol",
      "55B",      "Bike Patrol",
      "55S",      "Senior Check",
      "55T",      "COP- Traffic Enforcement/ Radar",
      "55V",      "Victor Watch",
      "56",       "Stolen Vehicle/UnAuthorized Use",
      "58",       "Suicide/Attempted Suicide/Suicide",
      "59",       "Oversize Load",
      "6",        "Airplane Crash",
      "60",       "Suspicious Person",
      "6060A",    "Suspicious Person & Vehicle",
      "60A",      "Suspicious Vehicle",
      "61",       "Problem Individual",
      "62",       "Traffic Detail",
      "66",       "Escape/ Inmate Escape",
      "70",       "Harassment- Phone or Text",
      "72",       "Lockout",
      "8",        "Assault, Abuse, Neglect",
      "90",       "Swat Call",
      "911",      "911 Hangup/Abandoned Call",
      "911MIS",   "911 Misdial",
      "911OPEN",  "911 Open Line",
      "911TEST",  "911 Test Call",
      "911TRUNK", "911 Trunk Failure",
      "99",       "Emergency Traffic",
      "ABDUCT",   "Abduction/Kidnapping",
      "ABV",      "Abandoned Vehicle",
      "ADD",      "Additional LST UNIT requested at scene of",
      "AF",       "Cancelled Call - Accidental Form",
      "AGENCY ASSIST", "Assist other Agency",
      "ALLERGY",  "Allergic Reaction",
      "AMBER",    "Amber Alert ",
      "ARSON",    "Arson Investigation",
      "ASSIST",   "ASSIST ",
      "B&E",      "Breaking and Entering",
      "BACK PAIN","Back Pain",
      "BC",       "Building/Business Check",
      "BOLO",     "BOLO/ Attempt to Locate",
      "BREATHE",  "Breathing Problems",
      "BURN",     "Burning Complaint/Open Burn ",
      "BURNS",    "EXPLOSION Burns or Injuries from Explosion",
      "BUSVIOL",  "School Bus Violation/Observation",
      "CARDIAC OR RESPIRATORY ARREST", "Heart Problem/Not Breathing",
      "CAVEIN",   "Cave In / Tech Rescue",
      "CE",       "Consensual Encounter",
      "CHEST PAIN", "Chest Pain",
      "CHOKING",  "Choking",
      "CITASST",  "Citizen Assist/standby/directions",
      "CIVIL",    "Civil Matter",
      "CO",       "CO Detector - No Symptoms",
      "CO2",      "CO Detector w - Symptoms",
      "CO29",     "CO Detector W/Symptoms ",
      "CONVULSIONS",                "/SEIZURES Convulsions Seizures",
      "CRIM DAMAGE/MISCHIEF/VANDALISM", "Criminal Damage Mischief Vandalism",
      "CUST",     "Custody Dispute",
      "DAV",      "Disabled Vehicle",
      "DEBRIS",   "Debris in Roadway/Street Obstruction",
      "DIABETIC", "Diabetic Problem",
      "DITCH",    "Vehicle in Ditch",
      "DRE",      "Drug Recognition Expert Request",
      "ELEC",     "Power Outage/ Electric Problem",
      "ELECTROCUTION", "Electrocution/Struck by Lightning",
      "ESC",      "Escort-Finance",
      "EXPLO",    "Some type of Explosion",
      "EYE PROBLEMS / INJURIES",                "Eye Problems/ Injuries",
      "FALL",     "Fall Victim",
      "FIREINSP", "FIRE INSPECTION",
      "FIREWORK", "Fireworks Complaint",
      "FLOOD",    "Flooded Roadways",
      "Forensic", "Forensic Exam",
      "FRAUD",    "Fraud/ Identity Fraud",
      "FSNE",     "Fire- Non Emergency Service Call",
      "FU",       "Followup",
      "FULL ARREST/ CPR", "Full Arrest/ CPR",
      "GAS WELL", "Fire - Gas Well",
      "GASLEAK",  "Gas Leak - Odor - Natural or Fuel - Fire",
      "GASSMELL", "Smell Of Gas In The Area",
      "GOLF",     "Golf Cart Inspection",
      "HAZMAT",   "Hazmat - All Hazards Team Request",
      "HEADPAIN", "Head Pain/Headache",
      "HEART PROBLEM / AICD", "Heart Problem / automatic implantable",
      "HEAT / COLD", "Heat or Cold Exposure",
      "HELO",     "Helicopter- Medical Tranport",
      "HEMORRHAGE", "Hemorrhage/Laceration/Cut/Bleeding",
      "INACESSIBLEINCIDENT/NON VEHICLE ENTRAPMENT", "Inaccessable Incident/Entrapment/Trench/Structure",
      "INVALID ASSIST", "Invalid Assist Lift Assist",
      "K9",       "K9 Assist/ Request",
      "LITTER",   "Littering",
      "LPR",      "LPR ALERT",
      "MEDICAL",  "Medical Call",
      "MISC",     "Miscellaneous ",
      "MUTUAL AID FIRE/EMS", "MUTUAL AID FIRE EMS",
      "NOISE COMPLAINT", "Loud Party/ Noise Complaint Loud Music",
      "NOTIF",    "Notfication",
      "ODOR",     "Fire - Odor Investigation",
      "OPEN BURN","Open Burn Complaint",
      "OVERDOSE", "Overdose/Poisoning/Ingestion",
      "PANDEMIC", "USED IN CASES OF VIRAL PANDEMICS/EPIDEMIC/OUTBREAK/COVID19/CORONAVIRUS",
      "PARK",     "Parking Complaint/Violation",
      "PENETRAT", "Stab/Gunshot/Penetrating/Cutting Trauma",
      "PREGNANCY","Pregnancy/Childbirth/Miscarriage",
      "PROPERTY", "Property- Lost/Found",
      "PURSUIT",  "Pursuit",
      "QRT",      "Quick Response Team",
      "REC56",    "Recover Stolen Vehicle",
      "REPO",     "Repossession of Vehicle",
      "ROAD",     "Road Rage",
      "RR",       "Railroad Crossing Problems/Train",
      "SALT",     "Salt Request for Roads",
      "SBA",      "Stand By Assist/General Escort",
      "SEX",      "Sex Offender Address Verification",
      "SICK",     "Sick Person",
      "SIGN",     "Sign down or Missing",
      "SIRENTEST","Tornado Siren Monthly Test",
      "SMOKE",    "Smoke Investigation - Smell of Smoke in Structure",
      "STROKE",   "Stroke",
      "SUSP",     "Suspicious Circumstances",
      "TEST",     "Test Calls",
      "THRT",     "Threats",
      "TPOCPO",   "Violation of CPO/TPO",
      "TRAFFIC",  "Traffic Complaint/ Reckless Operation",
      "Train Derailment",                "Train Derailment",
      "TRANSFER", "Medical Transfer/Interfacility",
      "TRAUMA",   "Traumatic Injuries, Injury",
      "TREES",    "Trees or Branches Down",
      "TRES",     "Trespassers",
      "TROT",     "Technical Rescue - ANY Discipline",
      "TS",       "Traffic Stop",
      "UAV",      "UAV Flight",
      "UNCONSCIOUS",                "Unconscious/Fainting/Unresponsive/Not Responding",
      "UNDERAGE COMPLAINT",                "Underage Alcohol/Tobacco/Consumption",
      "UNKMED",   "Unknown Medical Problem",
      "UNW",      "UnWanted Subject",
      "WATER",    "Water Problem/ Shut Off",
      "WEATHER",  "Weather Emergency/ Tornado Activation",
      "WELFARE",  "Welfare Check",
      "Wires",    "Lines Down - No FD",
      "WIRES",    "Wires Down Or Sparking "
  });

  private static final String[] CITY_LIST = new String[]{

      "BRUNSWICK",
      "MEDINA",
      "RITTMAN",
      "WADSWORTH",

      "CHIPPEWA LAKE",
      "CRESTON",
      "GLORIA GLENS PARK",
      "LODI",
      "SEVILLE",
      "SPENCER",
      "SULLIVAN",
      "WESTFIELD CENTER",

      "BRUNSWICK HILLS TWP",
      "CHATHAM TWP",
      "GRANGER TWP",
      "GUILFORD TWP",
      "HARRISVILLE TWP",
      "HINCKLEY TWP",
      "HOMER TWP",
      "LAFAYETTE TWP",
      "LITCHFIELD TWP",
      "LIVERPOOL TWP",
      "MEDINA TWP",
      "MONTVILLE TWP",
      "SHARON TWP",
      "SPENCER TWP",
      "WADSWORTH TWP",
      "WESTFIELD TWP",
      "YORK TWP",

      "BEEBETOWN",
      "HOMERVILLE",
      "LITCHFIELD",
      "VALLEY CITY",

      // Ashland County
      "MEDINA"

  };
}
