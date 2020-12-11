package net.anei.cadpage.parsers.KY;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Carter County,KY
 */
public class KYCarterCountyParser extends DispatchEmergitechParser {

  public KYCarterCountyParser() {
    super(":", 0, CITY_LIST, "CARTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Carter911@windstream.net,911@carterems.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("MM")) return true;
    if (apt.endsWith("MM")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "6",       "Aircraft Crash",
      "12",      "Burglary",
      "12A",     "Burglary in Progress",
      "17",      "Overdose",
      "18",      "Dog Bite",
      "18A",     "Animal Call",
      "20A",     "Neighbor Complaint",
      "21",      "Prisoner Transport",
      "22",      "Drowning",
      "28",      "Fire - Other",
      "28A",     "Vehicle Fire",
      "28B",     "Structure Fire",
      "28D",     "Explosion",
      "29",      "Ambulance - Non-emergency",
      "34",      "Missing Juvenile",
      "34A",     "Unruly Juvenile",
      "34B",     "Runaway Juvenile",
      "36",      "Theft",
      "36A",     "Theft in Progress",
      "36S",     "Shoplifting",
      "38",      "Adult Missing Person",
      "40",      "Person with a Gun",
      "40A",     "Person with a Knife",
      "42",      "Nature Unknown",
      "46A",     "Trespasser",
      "48A",     "Sex Offense",
      "48B",     "Person Exposing Themself",
      "50",      "Robbery",
      "50A",     "Robbery in Progress",
      "54",      "Stabbing",
      "60",      "Suspicious Person",
      "60A",     "Suspicious Vehicle",
      "62",      "Traffic Detail",
      "63",      "Investigation / Follow - Up",
      "64",      "Vandalism",
      "66",      "Escaped Prisoner",
      "68",      "Livestock on Roadway",
      "74",      "Hazardous Spill",
      "80",      "Traffic Jam, Roadway Blocked",
      "84",      "Open Door",
      "84A",     "Open Window",
      "91",      "911 Hang Up",
      "107",     "Dead on Arrival",
      "202A",    "Mental",
      "911",     "Misuse of 911",
      "1012",    "Visitor Present",
      "1014",    "Civil Disturbance",
      "1016",    "Domestic",
      "1017",    "Stolen Vehicle",
      "1029",    "Warrant Service",
      "1037",    "Pursuit",
      "1038",    "Traffic Stop",
      "1043",    "General Information",
      "1044",    "Motorist Assist",
      "1045",    "Motor Vehicle Accident Non-Injury",
      "1046",    "Motor Vehicle Accident with Injuries",
      "1060",    "Jail Break",
      "AOA",     "Assisting other Agency",
      "ATL",     "Attempt to Locate",
      "ATV",     "ATV on Roadway",
      "C50",     "Bomb Threat",
      "CB",      "Counterfeit Bill",
      "CD3",     "Ambulance - Emergency",
      "CI",      "Custodial Interference",
      "CR",      "Coroner Needed",
      "CTRS",    "Cancelled Traffic Stop",
      "D",       "Drive Off",
      "DA",      "Drug Activity",
      "DN7",     "Death Notice",
      "DUP",     "Cancelled Duplicate Address",
      "EP",      "Extra Patrol",
      "FA",      "Fire Alarm",
      "FF123",   "Forest / Brush Fire",
      "FL",      "Flooding",
      "HAZ",     "Haz-mat Incident",
      "LZ",      "Landing Zone",
      "MU",      "Move Up",
      "PH",      "Pan Handler",
      "PS",      "Public Service",
      "REPO",    "Vehicle Repossessed",
      "RH",      "Road Hazard",
      "ROA",     "Referred to Other Agency",
      "S17",     "Drunk Person",
      "S2",      "Shooting",
      "S20",     "Hostage - Civilian",
      "S21",     "Hostage - Police",
      "S23",     "Hit and Run",
      "S25",     "Arson Investigation",
      "S26",     "Alarm Drop",
      "S29",     "Shots Fired",
      "S3",      "Fight",
      "S32",     "Assault",
      "S34",     "Prowler",
      "S40",     "Wrecker Needed",
      "S45",     "Escort",
      "S57",     "Threats or Harassment",
      "S58",     "Threat of Suicide",
      "SERVICE", "Paper / EPO Service",
      "T",       "Tornado Touchdown",
      "TD",      "Tree Down",
      "VA",      "Verbal Argument",
      "WC",      "Weather Crisis",
      "WD",      "Wreckless Driver",
      "WELL",    "Welfare Check"
  });

  private static final String[] CITY_LIST = new String[]{
      "CARTER",
      "GRAYSON",
      "OLIVE HILL",

      "ELLIOTT CO"
  };
}
