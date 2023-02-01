package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;




public class OHLawrenceCountyAParser extends DispatchEmergitechParser {
  
  public OHLawrenceCountyAParser() {
    super("alert:", -26, CITY_LIST, "LAWRENCE COUNTY", "OH", TrailAddrType.INFO);
  }
  
  @Override
  public String getFilter() {
    return "alert@lawco911.org";
  }
  
  private static final Pattern BAD_PREFIX_PTN = Pattern.compile("alert:\\w+(?=\\[)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = BAD_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      body = "alert:" + body.substring(match.end());
    }
    if (!super.parseMsg(body, data)) return false;
    String call = CALL_CODES.getProperty(data.strCall.replace(" ", ""));
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    if (data.strName.equals("LAWRENC E TWP")) {
      data.strCity = "LAWRENCE TWP";
      data.strName = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT CODE " + super.getProgram();
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    Matcher match = PVT_DR_PTN.matcher(sAddress);
    if (match.find()) sAddress = match.group();
    return sAddress;
  }
  private static final Pattern PVT_DR_PTN = Pattern.compile("^.*\\bPVT DR \\d+\\b");
  
  @Override
  public String adjustMapCity(String city) {
    
    // It works this way.  Don't ask why!
    if (city.equals("PERRY TWP")) city = "IRONTON";
    return city;
  }
  
  
  private static final String[] CITY_LIST = new String[]{
    
    "IRONTON",
    
    // Villages
    "ATHALIA",
    "CHESAPEAKE",
    "COAL GROVE",
    "HANGING ROCK",
    "PROCTORVILLE",
    "SOUTH POINT",
    
    // Townships
    "AID TWP",
    "DECATUR TWP",
    "ELIZABETH TWP",
    "FAYETTE TWP",
    "HAMILTON TWP",
    "LAWRENCE TWP",
    "MASON TWP",
    "PERRY TWP",
    "ROME TWP",
    "SYMMES TWP",
    "UNION TWP",
    "UPPER TWP",
    "WASHINGTON TWP",
    "WINDSOR TWP",
    
    // Census- designated place
    "BURLINGTON",
    
    // Other communities
    "ETNA",
    "KITTS HILL",
    "PEDRO",
    "ROCK CAMP",
    "SCOTTOWN",
    "WATERLOO",
    "WILLOW WOOD",
  };
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "A/B",      "Animal Bite",
      "ABD",      "Abandoned 911 Call",
      "ABP",      "Abdominal Pain",
      "ALARM",    "Alarm",
      "ANA",      "Anaphylaxis",
      "ARSON",    "Arson",
      "ASPH",     "Asphyxiation",
      "ASSAULT",  "Assault Victim",
      "ASTH",     "Asthma",
      "AVU",      "Avulsion",
      "B/I",      "Back Injury",
      "BLE",      "Bleeding",
      "BOMB",     "Bomb Threat",
      "BRE",      "Breathing Problem",
      "BU",       "Burn",
      "C/A",      "Cardiac Arrest",
      "C/P",      "Chest Pain",
      "CHO",      "Choking",
      "CO",       "Carbon Monoxide",
      "CON",      "Seizure",
      "COR",      "Coronary",
      "CRU",      "Crushing",
      "CVA",      "Stroke",
      "D/R",      "Drug Reaction",
      "DIA",      "Diabetic",
      "DISL",     "Dislocation",
      "DOA",      "Dead on Arrival",
      "DROW",     "Drowning",
      "DUP",      "Duplicate Call",
      "E/I",      "Eye Injury",
      "E/S",      "Electrical Shock",
      "EPI",      "Nosebleed",
      "EVI",      "Environmental Injury",
      "EXP",      "Explosion",
      "F5",       "Needs assistance",
      "F6",       "Aero Medical",
      "F7",       "Take Report",
      "F8",       "Red Cross",
      "F9",       "Explosion",
      "F10",      "Unknown Problem",
      "F11",      "Smoke Investigation",
      "F12",      "Carbon Monoxide",
      "F13",      "Special Detail",
      "F14",      "Water Rescue",
      "F15",      "Vehicle Crash",
      "F15B",     "Motorcycle/ATV Crash",
      "F15C",     "Train Crash",
      "F15D",     "Bus Crash",
      "F15E",     "Aircraft Crash",
      "F15F",     "Off Road Crash",
      "F15G",     "Farm Accident",
      "F16",      "Dead on Arrival",
      "F16A",     "DOA Suspicious",
      "F24",      "Haz-Mat/Chemical Leak",
      "F25",      "Unknown Alarm",
      "F33",      "Structure Fire",
      "F33A",     "Fire Alarm",
      "F33B",     "Brush Fire",
      "F33C",     "Vehicle Fire",
      "F33D",     "Chimney Fire",
      "F38",      "Missing Person",
      "F40",      "Jaws of Life/Extrication",
      "F41",      "Arson",
      "F45",      "Severe Weather",
      "F46",      "Utility lines Down",
      "F47",      "Drowning",
      "F48",      "Bomb Threat",
      "F50",      "Gas Leak",
      "F53",      "Evacuation",
      "F56",      "Victim Trapped",
      "F100",     "Full Scale Disaster",
      "F/A",      "Fire Alarm Activation",
      "F/S/D",    "Full Scale Disaster",
      "FA",       "Fall",
      "FAIN",     "Fainting",
      "FARM",     "Farm Accident",
      "FIRE",     "Structure Fire",
      "FIREB",    "Brush Fire",
      "FIREC",    "Chimney Fire",
      "FIREV",    "Vehicle Fire",
      "FRA",      "Fracture",
      "FS",       "Fuel Spill",
      "G/I",      "General Illness",
      "GS",       "Gun Shot",
      "H/C",      "Heart Condition",
      "H/I",      "Head Injury",
      "H/M",      "HAZ-MAT",
      "MISC",     "Miscellaneous Call",
      "IMP",      "Impalement",
      "INT",      "Intoxication",
      "IP11",     "Missing Person",
      "IP25",     "Trouble",
      "IP26",     "Out Of Service",
      "IP33",     "Fire Call",
      "IP3A",     "Motor Vehicle Accident",
      "IP3B",     "MVA w/Injuries",
      "JOL",      "Extrication",
      "LAC",      "Laceration",
      "MA4",      "Motorcycle/ATV Accident",
      "MAT",      "Maternity",
      "MCA-P",    "Motor Vehicle Accident",
      "MISINC",   "Miscellaneous Call",
      "MVA",      "with Injuries",
      "MVA-I",    "Motor Vehicle Accident w/Injuries",
      "MVA-N",    "MVA Non-Injury",
      "MVC",      "Motor Vehicle Accident",
      "MVCB",     "Bus Accident",
      "NAU",      "Nausea",
      "NGL",      "Gas Leak",
      "O30",      "Fatal MVA",
      "O31",      "MVA",
      "O31A",     "Motor Vehicle Accident",
      "O49",      "Forced Landing",
      "O56",      "Train Accident",
      "O57",      "Bomb Threat",
      "O79",      "Mental Issue",
      "O88",      "Officer In Trouble",
      "O33",      "Drowning",
      "O34",      "Car Fire",
      "O36",      "Vehicle Fire/Other",
      "O40",      "Emergency Call",
      "O51",      "Plane Crash",
      "O54",      "Explosive Truck Accident",
      "OS",       "Over Dose",
      "P/W",      "Puncture Wound",
      "PARA",     "Paralysis",
      "PC",       "Plane Crash",
      "POIS",     "Poisoning",
      "PSY",      "Psychiatric",
      "R/A",      "Respiratory Arrest",
      "RAPE",     "Rape",
      "S2",       "Non-injury MVA",
      "S2A",      "Hit Skip",
      "S4",       "MVA with Injuries",
      "S6",       "Aircraft Accident",
      "S8",       "Assault",
      "S16",      "DOA",
      "S18",      "Dog Bite",
      "S18A",     "Animal Call",
      "S22",      "Drowning",
      "S24",      "Intoxicated Person",
      "S28",      "Fire",
      "S28A",     "Vehicle Fire",
      "S28B",     "Structure Fire",
      "S29C",     "Structure Fire/ENTRAPMENT",
      "S38",      "Missing Person",
      "S52",      "Shooting",
      "S58",      "Suicide",
      "S62",      "Traffic Detail",
      "S68",      "Live Stock In Roadway",
      "S70",      "Emergency Notification",
      "S74",      "HAZ-MAT",
      "S76",      "Mental Issue",
      "S78",      "Alarm",
      "S82",      "Disabled Vehicle",
      "S86A",     "Motorcycle/ATV Accident",
      "S88",      "Bomb Threat",
      "S90",      "Train Derailment",
      "S/I",      "Smoke Investigation",
      "S/S",      "Strain / Sprain",
      "S/W",      "Sever Weather",
      "SHO",      "Shock",
      "STAB",     "Stabbing",
      "SUI",      "Suicide",
      "TERM",     "Terminal Illness",
      "TEST",     "Test Call",
      "TRAC",     "Train Accident",
      "TREE",     "Tree Down",
      "ULD",      "Utility Lines Down",
      "UNC",      "Unconscious",
      "UNK",      "Unknown Problem",
      "UNKM",     "Unknown Medical",
      "UNKT",     "Unknown Trauma",
      "URI",      "Urinary Problems",
      "V/P",      "Victim In Pain",
      "V/T",      "Victim Trapped",
      "VOM",      "Vomiting",
      "W/A",      "Watercraft Accident",
      "W/R",      "Water Rescue",
  });
}
