package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class OHWayneCountyCParser extends DispatchEmergitechParser {
  
  public OHWayneCountyCParser() {
    super("Dispatch:", CITY_LIST, "WAYNE COUNTY", "OH", TrailAddrType.PLACE);
    addSpecialWords("COLUMBUS", "HARRISON");
    addExtendedDirections();
  }

  @Override
  public String getFilter() {
    return "@wcjustice-center.org";
  }
  
  private static final Pattern DASH_COUNTY_PTN = Pattern.compile(" - HC? ");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = DASH_COUNTY_PTN.matcher(body).replaceAll(" ");
    if (!super.parseMsg(body, data)) return false;
    
    String desc = CALL_CODES.getProperty(data.strCall);
    if (desc != null) {
      data.strCode = data.strCall;
      data.strCall = desc;
    }
    
    int pt = data.strCity.indexOf('-');
    if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
    
    // If no city found, see if we can find in a mutual aid
    // information
    if (data.strCity.length() == 0) {
      String tmp = data.strSupp;
      tmp = stripFieldStart(tmp, "MUTUAL AID");
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, tmp, data);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT CODE " + super.getProgram();
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "NORTON",
    "ORRVILLE",
    "RITTMAN",
    "WOOSTER",
    
    // Villages
    "APPLE CREEK",
    "BURBANK",
    "CONGRESS",
    "CRESTON",
    "DALTON",
    "DOYLESTOWN",
    "FREDERICKSBURG",
    "HOLMESVILLE",
    "MARSHALLVILLE",
    "MOUNT EATON",
    "MT EATON",
    "SHREVE",
    "SMITHVILLE",
    "WEST SALEM",

    // Townships
    "BAUGHMAN TWP",
    "CANAAN TWP",
    "CHESTER TWP",
    "CHIPPEWA TWP",
    "CLINTON TWP",
    "CONGRESS TWP",
    "EAST UNION TWP",
    "FRANKLIN TWP",
    "GREEN TWP",
    "MILTON TWP",
    "PAINT TWP",
    "PLAIN TWP",
    "SALT CREEK TWP",
    "SUGAR CREEK TWP",
    "WAYNE TWP",
    "WOOSTER TWP",
    
    // Unincorporated Communities
    "FUNK",
    "KIDRON",
    "STERLING",
    
    "MEDINA COUNTY",
    "SEVILLE",
    
    "WAYNE COUNTY",
    "RIPLEY TWP",
    "SALT CREEK",
  };
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "2",      "Accident:No Injury",
      "4",      "Accident:Injury",
      "16",     "Dead Person",
      "18",     "Dog Bite",
      "22",     "Drowning",
      "24",     "Intoxication",
      "26",     "Fight",
      "26A",    "Fight In Progress",
      "32",     "Homicide",
      "38",     "Missing Person",
      "44",     "Officer In Trouble",
      "48",     "Rape",
      "4P",     "Accident:Pin",
      "50",     "Robbery",
      "52",     "Shooting",
      "54",     "Stabbing",
      "58",     "Suicide",
      "63",     "Traffic Stop",
      "66",     "Escape",
      "72",     "Bomb Threat",
      "74",     "Mental",
      "84",     "Weather Incident",
      "8I",     "Assault W/Injury",
      "99",     "Undefined",
      "10F",    "Assist:Fire",
      "10M",    "Assist:Medical",
      "10P",    "Assist:Police",
      "2/4",    "Accident:Unknown Injuries",
      "20I",    "Domestic W/Injury",
      "21A",    "Pursuit",
      "26I",    "Fight W/Injury",
      "2HS",    "Accident:Hit Skip",
      "2PP",    "Accident:Private Property",
      "4XX",    "Accident:Multiple Injuries",
      "6AC",    "Crash:Aircraft",
      "6TR",    "Crash:Train",
      "8AI",    "Assault:IN PROGRESS W/Injury",
      "F99",    "Fire:Statewide Mutual Aid",
      "15SW",   "Warrant:Search",
      "17CA",   "Assist:Citizen",
      "2899",   "Fire:Undefined",
      "28AR",   "Arson",
      "28BA",   "Fire:Barn",
      "28CB",   "Fire:Controlled",
      "28CH",   "Fire:Church",
      "28CO",   "Fire:CO Activation",
      "28DU",   "Fire:Dumpster",
      "28EX",   "Fire:Explosion",
      "28HZ",   "Fire:Hazmat",
      "28IN",   "Fire:Inspection",
      "28LS",   "Fire:Structure",
      "28MA",   "Fire:Mutual Aid",
      "28MH",   "Fire:Mobile Home",
      "28NS",   "Fire:Non-Structure",
      "28OB",   "Fire:Open Burn",
      "28OD",   "Fire:Odor Investigation",
      "28OS",   "Fire:Occupied Residential",
      "28PS",   "Fire:Public Service",
      "28RK",   "Fire:Rekindle",
      "28SD",   "Fire:Special Detail",
      "28SM",   "Fire:Smoke Investigation",
      "28SS",   "Fire:Small Structure",
      "28VB",   "Fire:Vehicle In/Near Bldg",
      "28WR",   "Fire:Water Rescue",
      "3099",   "EMS:Undefined",
      "30AB",   "EMS:Abdominal Pain",
      "30AL",   "EMS:Allergic Reaction",
      "30AR",   "EMS:Arm/Hand Injury",
      "30BK",   "EMS:Back Injury",
      "30BL",   "EMS:Bleeding",
      "30BR",   "EMS:Difficulty Breathing",
      "30BU",   "EMS:Burns",
      "30CA",   "EMS:Citizen Assist",
      "30CH",   "EMS:Choking",
      "30CP",   "EMS:Chest Pain",
      "30DI",   "EMS:Diabetic",
      "30EN",   "EMS:Entrapment",
      "30FA",   "EMS:Full Arrest",
      "30FI",   "EMS:Farm Related",
      "30FL",   "EMS:Fall Injury",
      "30HD",   "EMS:Head Injury",
      "30HH",   "EMS:Heart Related",
      "30HT",   "EMS:Hospital Transfer",
      "30ID",   "EMS:Industrial Related",
      "30IL",   "EMS:General Illness",
      "30IN",   "EMS:General Injury",
      "30LF",   "EMS:Lifeflight",
      "30LG",   "EMS:Leg/Foot Injury",
      "30MA",   "EMS:Mutual Aid",
      "30NB",   "EMS:Non Breathing",
      "30OB",   "EMS:Maternity",
      "30OD",   "EMS:Overdose",
      "30PO",   "EMS:Poisoning",
      "30ST",   "EMS:Stroke",
      "30SZ",   "EMS:Seizure",
      "30UC",   "EMS:Unconscious",
      "30XX",   "EMS:Disaster",
      "38AB",   "Abduction",
      "52AC",   "Shooting:Accidental",
      "52XX",   "Shooting:Multiple",
      "58AT",   "Suicide:Attempted",
      "58TH",   "Suicide:Threats",
      "84LD",   "Fire:Lines Down",
      "911A",   "911:Abandoned",
      "911H",   "911:Hang Up",
      "911S",   "911:Open Line",
      "911X",   "911:Disconnect/Background Noise",
      "28ALM",  "Fire:Alarm",
      "28APP",  "Fire:Appliance",
      "28CHM",  "Fire:Chimney",
      "28COM",  "Fire:Commercial",
      "28GRS",  "Fire:Grass/Brush/Field",
      "28IND",  "Fire:Industrial",
      "28INV",  "Fire:Investigation Unit",
      "28RES",  "Fire:Residential",
      "28SCH",  "Fire:School",
      "28VEH",  "Fire:Vehicle",
      "30ALM",  "EMS:Alarm",
      "30AMP",  "EMS:Amputation",
      "30CUT",  "EMS:Laceration(s)",
      "30UNK",  "EMS:Unknown",
      "30UNR",  "EMS:Unresponsive",
      "FSTBY",  "Fire:Standby",
      "28POLE", "Fire:Pole/Wires",
      "30STBY", "EMS:Standby"

  });
}
