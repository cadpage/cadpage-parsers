package net.anei.cadpage.parsers.FL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;



public class FLCollierCountyAParser extends DispatchPrintrakParser {
  
  private static final Pattern MARKER = Pattern.compile("^FCC\\d{12} TYP: ");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(?: +CC[A-Z0-9]*)+(?: +C)?$");
  private static final Pattern TRAIL_INFO_PTN = Pattern.compile("(?:\n+(?:FIRE|EMS)(?: - .*)?)+$");
  private static final Pattern MULTI_BRK_PTN = Pattern.compile("\n{2,}");
  
  public FLCollierCountyAParser() {
    super("COLLIER COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "ccsocad@colliersheriff.org,ccsocad@colliersheriff.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Strip off disclaimer
    int pt = body.indexOf("\nThis communication ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // String unit designation off end of string
    String units = "";
    String info = "";
    Matcher match = TRAIL_UNIT_PTN.matcher(body);
    if (match.find()) {
      units = match.group().trim();
      body = body.substring(0,match.start());
    }
    
    // Ditto for odd information unit/map block
    else {
      match = TRAIL_INFO_PTN.matcher(body);
      if (match.find()) {
        info =  match.group().trim();
        info = MULTI_BRK_PTN.matcher(info).replaceAll("\n");
        body = body.substring(0,match.start());
      }
    }
    if (MARKER.matcher(body).find()) body = "INC:" + body; 
    if (!super.parseMsg(body, data)) {
      if (body.startsWith("CC/")) {
        data.msgType = MsgType.GEN_ALERT;
        return true;
      }
      return false;
    }

    String code1 = data.strCode;
    String code2 = data.strCall;
    if (code1.length() > 0) {
      data.strCode = append(code1, "-", code2);
      data.strCall = convertCodes(code1, CALL_CODES);
      String call = CALL_MOD_CODES.getProperty(data.strCode);
      if (call == null) call = MOD_CODES.getProperty(code2);
      if (call != null) data.strCall = append(data.strCall, " - ", call);
      String pri = CALL_CODE_PRI.getProperty(data.strCode);
      if (pri != null) data.strPriority = pri;
    }
    
    data.strUnit = append(data.strUnit, " ", units);
    data.strSupp = append(data.strSupp, "\n", info);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " INFO UNIT PRI";
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "01",    "Drunk Driver",
      "02",    "Drunk Person",
      "04I",   "Crash w/Injuries",
      "04N",   "Crash - Neg Injuries",
      "04U",   "Crash - Unknown Injuries",
      "05",    "Murder",
      "06",    "Prisoner",
      "07",    "Dead Person",
      "08",    "Missing",
      "09",    "Checks",
      "10",    "Stolen",
      "11",    "Abandoned",
      "12",    "Reckless",
      "13",    "Suspicious",
      "14",    "Illegal Dumping",
      "15",    "Details",
      "16",    "Obstruction",
      "17",    "Attempt To Contact",
      "18",    "Follow Up Investigation/Supp",
      "19",    "Civil",
      "20",    "Mentally Ill Person",
      "21N",   "Burglary Neg IP",
      "21I",   "Burglary In Progress",
      "22",    "Disturbance",
      "24I",   "Robbery IP",
      "24N",   "Robbery Neg IP",
      "25",    "Fire",
      "26",    "Drowning",
      "27",    "Legal Advice",
      "28",    "COPS",
      "29",    "Crime Prevention",
      "30",    "Bomb",
      "31",    "Pursuit",
      "32",    "Alarm",
      "33",    "Theft",
      "34",    "Sex Crime",
      "35",    "Criminal Mischief",
      "36",    "Animal Complaint",
      "37",    "Explosion",
      "38",    "Traffic Problem/Direction",
      "39",    "Assault/Battery",
      "40",    "Hazardous Incident",
      "41",    "Extra Patrol",
      "42",    "Obscene/Harassing Communication",
      "43",    "Trespassing",
      "44",    "Property",
      "45",    "Property Damage",
      "46",    "Fraud",
      "47",    "Kidnapping",
      "48",    "Vice",
      "49",    "Police Information",
      "50",    "Drug Related",
      "51",    "NORAD Assist (NPD)",
      "52",    "Disabled",
      "53",    "Assist",
      "54",    "Rescue",
      "55",    "School Crossing",
      "56",    "Abuse",
      "57",    "Verify/Inspection",
      "58",    "Parking Violation",
      "59",    "Barricade/Hostage Situation",
      "60",    "Suicide",
      "61",    "Noise Complaint",
      "62",    "Shooting",
      "63",    "Stabbing",
      "64",    "Stalking",
      "66",    "Severe Weather",
      "67",    "City Maintenance (NPD)",
      "68",    "Impersonating an LEO",
      "69",    "Gang Related Incident",
      "70",    "Relay",
      "71",    "Medical Emergency",
      "72",    "Unknown Problem",
      "73",    "Convoy or Escort",
      "74",    "Communication System Failure (NPD)",
      "75",    "Traffic Stop",
      "76",    "Jail Break/Trouble in Jail",
      "77",    "Code Violation",
      "78",    "Warrant Related",
      "79",    "Surveillance Activities",
      "80",    "MERT (Intracoastal or Gulf of Mexico)",
      "81",    "Aircraft Alert",
      
      "MCI1",  "MCI Level 1",
      "MCI2",  "MCI Level 2",
      "MCI3",  "MCI Level 3",
      "MCI4",  "MCI Level 4",
      "MCI5",  "MCI Level 5",
  });
  
  private static final Properties MOD_CODES = buildCodeTable(new String[]{
      "ABA",  "Abandoned",
      "ACS",  "Active Shooter",
      "ACT",  "Activity",
      "AGG",  "Aggressive Driving",
      "AGR",  "Agriculture",
      "AIR",  "Aircraft",
      "AL1",  "Alert 1 (MINOR EMERGENCY)",
      "AL2",  "Alert 2 (TROUBLE LANDING)",
      "AL3",  "Alert 3 (PLANE CRASH)",
      "APT",  "Airport",
      "ARM",  "Armed Person",
      "ATV",  "ATV",
      "BAR",  "Bar Check",
      "BEA",  "Bear",
      "BIC",  "Bicycle",
      "BIK",  "Bicycle",
      "BOI",  "Boat/Inland",
      "BOM",  "Bomb",
      "BOO",  "Boat/Off Shore",
      "BRE",  "Breathalyzer operator",
      "BRU",  "Brush/Woods",
      "BUC",  "Burglary Commercial",
      "BUI",  "Building",
      "BUR",  "Burglary Residential",
      "CAR",  "Cardiac Emergency",
      "CHE",  "Chemcial",
      "CIT",  "Citizen Contact",
      "CMB",  "Commercial Vehicle",
      "CMV",  "Commercial Vehicle",
      "CNT",  "County Code Violation",
      "COM",  "Commercial",
      "CON",  "Construction",
      "COS",  "Confined Space",
      "COU",  "Court Ordered - Enforceable",
      "CTY",  "City Code Violation",
      "CVA",  "Stroke",
      "DCF",  "DCF assist",
      "DEV",  "Device Present",
      "DIA",  "Diabetic",
      "DIH",  "Disabled/Handicap",
      "DIS",  "District",
      "DOG",  "K-9",
      "DOM",  "Domestic",
      "DUI",  "DUI detail",
      "DUM",  "Dumpster/Trash",
      "EID",  "Elevator - in distress",
      "ELD",  "Elderly",
      "ELE",  "Electrical Pole",
      "EME",  "Emergency Transport",
      "END",  "Elevator - not in distress",
      "EQU",  "Construction/Farm Equipment",
      "ESC",  "Escaped Prisoner",
      "EVI",  "Eviction",
      "FAL",  "Fall",
      "FIC",  "Fire Commercial",
      "FIG",  "Fight",
      "FIH",  "Fire High Rise",
      "FIR",  "Fire Residential",
      "FIW",  "Fire Works",
      "FLE",  "Flex Op",
      "FLO",  "Flooding",
      "FLS",  "Florida Statute Violation",
      "FLV",  "Flooded Vehicle/sub trapped",
      "FTP",  "Foot patrol",
      "FUN",  "Funnel Cloud/Sighting",
      "GAM",  "Gambling",
      "GAS",  "Gas",
      "GEN",  "General",
      "GLI",  "Gas Leak/Smell Inside Structure",
      "GLO",  "Gas Leak/Smell Outside Structure",
      "GUN",  "Gunshots",
      "HAN",  "Hazards only - no injuries",
      "HAZ",  "Hazardous Materials",
      "HIA",  "High Angle",
      "HIG",  "High Rise",
      "HIT",  "Hit and Run",
      "HOT",  "via Hotline",
      "IDE",  "Identity Theft",
      "ILL",  "Illegal Burn",
      "IND",  "Indigent Aid",
      "INJ",  "Injuction for Protection",
      "INT",  "Intelligence Report",
      "JAR",  "Jar/Truancy",
      "JUV",  "Juvenile Party",
      "LAK",  "Lake/Canal/River",
      "LAL",  "Language Line",
      "LAN",  "Land",
      "LEO",  "LE involved",
      "LEW",  "Lewd & Lascivious",
      "LIV",  "Live Stock",
      "LOS",  "Lost",
      "MAR",  "Marine Mammal",
      "MDN",  "Median",
      "MED",  "Medical",
      "MOT",  "Motorcycle",
      "MOU",  "Mounted Patrol",
      "MRR",  "Marine Resource Related",
      "MUC",  "Music Commercial",
      "MUR",  "Music Residential",
      "MUT",  "Mutual Aid",
      "NEI",  "Neighborhood",
      "NEM",  "No 911",
      "NPH",  "No Telephones",
      "NRD",  "No Radio",
      "OPC",  "Open Container Violation",
      "OPE",  "Open Door",
      "OTH",  "Other Airborne Object",
      "OUT",  "Structure Fire - fire reported out",
      "PAC",  "Package/Letter/Device",
      "PAN",  "Panther",
      "PAR",  "Parking Lot",
      "PER",  "Person",
      "PLS",  "Project Lifesaver",
      "POW",  "Power Outage",
      "PRE",  "Pre-trail Release Violation",
      "PRI",  "Prisoner relay",
      "PRJ",  "Project",
      "PRO",  "Probation",
      "PRS",  "Prostitution",
      "PRW",  "Prowler",
      "PVI",  "Person Locked in Vehicle - in distress",
      "PVN",  "Person Locked in Vehicle - not in distress",
      "REC",  "Recovery",
      "RED",  "Red Light",
      "RES",  "Residential",
      "RIO",  "Riot",
      "ROA",  "Road",
      "ROC",  "Robbery Commercial",
      "ROL",  "Roll-over/Entrapment/ Ejection/ Head-on",
      "ROR",  "Robbery Residential",
      "ROU",  "Routine Transport",
      "ROV",  "Robbery Vehicle/panic",
      "RUN",  "Runaway (age 13-17)",
      "SCH",  "School",
      "SEA",  "Search Warrant",
      "SEC",  "Sector Information",
      "SEG",  "Segway",
      "SEI",  "Seizures",
      "SER",  "Attempt/Serve",
      "SHO",  "Shocap",
      "SMO",  "Smoke Investigate - outside",
      "SOB",  "Shortness of Breath",
      "SOP",  "Sexual Offender/Predator",
      "SOY",  "Sexual Offender/Predator - Youth Related",
      "SPD",  "Speed Enforcement",
      "SPE",  "Special/Contract",
      "STL",  "Street Lights",
      "STO",  "Stolen Aircraft",
      "STR",  "Structure Collapse/cave-in",
      "SUB",  "Submerged Vehicle",
      "TEN",  "Tenant/Landlord Dispute",
      "TER",  "Terrorism Event",
      "THR",  "Threat",
      "TOR",  "Tornado",
      "TRA",  "Traffic",
      "TRE",  "Fallen Trees",
      "TRI",  "Traumatic Injury",
      "TWL",  "Terrorist Watch List",
      "UND",  "Underground Utility Damage",
      "UNK",  "Unknown",
      "UNV",  "Unverified 911",
      "VAC",  "Vacant Home",
      "VEH",  "Vehicle",
      "VER",  "Verbal",
      "VES",  "Vessel",
      "VIN",  "VIN Verification",
      "VIO",  "Protection Detail/VIP",
      "WAN",  "Wanted Person Check",
      "WAT",  "Waterway",
      "WEE",  "Weekender Program",
      "WEL",  "Welfare",
      "WOR",  "Worthless Checks",
      "WTR",  "Water Restriction Violation",
      "YOU",  "Young child/infant"
  });
  
  private static final Properties CALL_MOD_CODES = buildCodeTable(new String[]{
      "26-GEN",  "General (hotel/residential)"
  });
  
  private static final Properties CALL_CODE_PRI = buildCodeTable(new String[]{
      "04I-ATV",  "1",
      "04I-VES",  "1",
      "04I-BUI",  "3",
      "04I-CMV",  "2",
      "04I-ELE",  "2",
      "04I-ROL",  "1",
      "04I-HAZ",  "2",
      "04I-HIT",  "3",
      "04I-SUB",  "1",
      "04I-LEO",  "2",
      "04I-MOT",  "1",
      "04I-PAR",  "3",
      "04I-PER",  "2",
      "04I-VEH",  "2",
      "04N-ROL",  "3",
      "04N-HAZ",  "2",
      "04N-HAN",  "4",
      "04N-SUB",  "3",
      "04U-ATV",  "1",
      "04U-VES",  "1",
      "04U-BUI",  "3",
      "04U-CMV",  "2",
      "04U-ELE",  "2",
      "04U-ROL",  "1",
      "04U-HAZ",  "2",
      "04U-HIT",  "3",
      "04U-SUB",  "1",
      "04U-LEO",  "2",
      "04U-MOT",  "1",
      "04U-PAR",  "3",
      "04U-PER",  "2",
      "04U-VEH",  "3",
      "05",       "4",
      "07",       "3",
      "08-VES",   "3",
      "08-ELD",   "3",
      "08-YOU",   "3",
      "15-SPE",   "4",
      "20",       "3",
      "22-GEN",   "4",
      "22-DOM",   "4",
      "22-FIG",   "4",
      "22-LEO",   "4",
      "22-TEN",   "4",
      "22-RIO",   "4",
      "22-JUV",   "4",
      "22-NEI",   "4",
      "22-VER",   "4",
      "24I-VEH",  "4",
      "24I-COM",  "4",
      "24I-RES",  "4",
      "24I-PER",  "4",
      "24N-VEH",  "4",
      "24N-COM",  "4",
      "24N-RES",  "4",
      "24N-PER",  "4",
      "25-VES",   "1",
      "25-COM",   "1",
      "25-CMV",   "2",
      "25-ELE",   "3",
      "25-HIG",   "1",
      "25-ILL",   "4",
      "25-MDN",   "4",
      "25-MUT",   "1",
      "25-RES",   "1",
      "25-SMO",   "3",
      "25-OUT",   "3",
      "25-DUM",   "3",
      "25-UNK",   "2",
      "25-VEH",   "2",
      "25-BRU",   "2",
      "26-GEn",   "1",
      "26-LAK",   "1",
      "30-MUT",   "4",
      "30-DEV",   "3",
      "30-THR",   "4",
      "32-FIC",   "3",
      "32-FIH",   "3",
      "32-FIR",   "3",
      "32-GEN",   "3",
      "32-MED",   "3",
      "34",       "3",
      "37-BOM",   "1",
      "37-CHE",   "1",
      "37-GAS",   "1",
      "37-UNK",   "1",
      "39",       "2",
      "40-GLI",   "1",
      "40-GLO",   "3",
      "40-LAN",   "2",
      "40-LAK",   "3",
      "40-UND",   "4",
      "53-MUT",   "3",
      "53-GEN",   "3",
      "54-COS",   "1",
      "54-EID",   "2",
      "54-END",   "3",
      "54-HIA",   "2",
      "54-PVI",   "2",
      "54-PVN",   "3",
      "54-STR",   "2",
      "54-LAK",   "2",
      "56-YOU",   "3",
      "56-DIH",   "3",
      "56-ELD",   "3",
      "59",       "4",
      "60-CHE",   "1",
      "60-GEN",   "2",
      "60-ARM",   "4",
      "60-GAS",   "1",
      "62-ACS",   "2",
      "62-GEN",   "2",
      "62-LEO",   "2",
      "63",       "2",
      "66-TRE",   "4",
      "66-FLO",   "4",
      "66-POW",   "4",
      "66-FUN",   "4",
      "66-TOR",   "2",
      "71-CAR",   "1",
      "71-DIA",   "2",
      "71-EME",   "3",
      "71-FAL",   "3",
      "71-GEN",   "3",
      "71-ROU",   "4",
      "71-SEI",   "2",
      "71-SOB",   "1",
      "71-CVA",   "1",
      "71-TRI",   "2",
      "81-AL1",   "3",
      "81-AL2",   "2",
      "81-AL3",   "1"
  }); 

}
