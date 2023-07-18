package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MICalhounCountyBParser extends DispatchOSSIParser {

  private static final Pattern MANGLED_SUBJECT_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} \\d{2}");

  public MICalhounCountyBParser() {
    super(MICalhounCountyParser.CITY_CODES, "CALHOUN COUNTY", "MI",
          "( CANCEL ADDR! | FYI? EMPTY? DATETIME CODE ADDR! ) CITY? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@calhouncountymi.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Fix some mangled messages
    if (subject.equals("Text Message")) {
      if (!body.startsWith("CAD:")) body = "CAD:" + body;
    }
    else if (MANGLED_SUBJECT_PTN.matcher(subject).matches() && body.startsWith("CAD:")) {
      body = "CAD:" + subject + body.substring(3);
    }
    if (!super.parseMsg(body, data)) return false;
    MICalhounCountyParser.cleanup(data);

    data.strCall = convertCodes(data.strCode, CALL_CODES);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL");
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "911HU",  "911 Hang Up / Abandoned Call",
      "99",     "Signal 99 / Officer In Trouble",
      "ABDVEH", "Abandoned Vehicle",
      "ABSCON", "Parole Absconder",
      "ABUSE",  "Abuse or Neglect Complaint",
      "AGGANI", "Aggressive Animal",
      "AIREMG", "Air Emergency",
      "ALARM",  "Alarm",
      "ALERT1", "Non Emergency Standby",
      "ALERT2", "Emergency Standby",
      "ALERT3", "Crash or Imminent Crash",
      "ANICOM", "Animal Complaint",
      "APPLIA", "Appliance Fire",
      "ARSON",  "Arson Investigation",
      "ASSALT", "Assault",
      "ASSIST", "Assist Another Agency",
      "ASSTPD", "Assist Police Department",
      "BATF",   "Bat In The House",
      "BE",     "Breaking and Entering",
      "BOLO",   "Broadcast",
      "BOMB",   "Bomb Threat",
      "BOXCAR", "Box Car Fire",
      "BRUSH",  "Brush Fire",
      "BURN",   "Burning Complaint",
      "CALARM", "Commercial Fire Alarm",
      "CARBON", "Carbon Monoxide Alarm",
      "CCW",    "Carrying a Concealed Weapon",
      "CHIMNY", "Chimney Fire",
      "CITAST", "Citizen Assist",
      "CITIZF", "Citizen Assist For Fire",
      "CIVILP", "Civil Problem or Complaint",
      "CIVTOW", "Civil Private Property Tow",
      "CKAREA", "Check Area",
      "CKPREM", "Check Premise",
      "CKSUBJ", "Check Subject",
      "CKVEH",  "Check Vehicle",
      "CONFIN", "Confined Stray",
      "CONFSP", "Confined Space Rescue",
      "CROWD",  "Crowd Gathering",
      "CSC",    "Criminal Sexual Conduct",
      "DEATH",  "Unattended Death",
      "DELMSG", "Deliver Message",
      "DISORD", "Disorderly Person",
      "DNRVIO", "DNR Violation",
      "DOMEST", "Domestic Trouble",
      "DRIVE",  "Driving Complaint",
      "DUMPST", "Dumpster Fire",
      "DWLS",   "Driving While License Suspended",
      "EMBEZZ", "Embezzlement Complaint",
      "EXPLOS", "Explosion",
      "EXTRIC", "Extrication",
      "FIGHT",  "Fight",
      "FIREOC", "Occupied Structure Fire",
      "FIRWRX", "Fireworks Complaint",
      "FLEE",   "Fleeing and Eluding",
      "FOUNDC", "Found Child",
      "FOUNDP", "Found Property",
      "FRAUD",  "Fraud Complaint",
      "FTP",    "Fail To Pay",
      "FTPTRL", "Foot Patrol",
      "FTRBV",  "Fail To Return Borrowed Vehicle",
      "GASLK",  "Gas Leak",
      "HARASS", "Harassment Complaint",
      "HAZARD", "Hazard",
      "HAZMAT", "Hazardous Materials",
      "HO",     "Hindering and Opposing",
      "HRACC",  "Hit and Run Accident",
      "INDEXP", "Indecent Exposure",
      "INFO",   "Information Only Call",
      "JUVTBL", "Juvenile Trouble",
      "KIDNAP", "Kidnapping",
      "LARC",   "Larceny",
      "LIMB",   "Limb or Tree Down In Road",
      "LIQUOR", "Liquor Inspection",
      "LOSTP",  "Lost Property Report",
      "MANDWN", "Man Down",
      "MANGUN", "Man With A Gun",
      "MDOP",   "Malicious Destruction Of Property",
      "MEDAST", "Medical Assist",
      "MEDP1",  "Priority 1 Medical",
      "MEDP2",  "Priority 2 Medical",
      "MEDP3",  "Priority 3 Medical",
      "MEREQ",  "Medical Examiner Request",
      "MISSIN", "Missing Person",
      "MTRAST", "Motorist Assist",
      "NOISE",  "Noise Complaint",
      "ODORI",  "Odor Investigation Inside",
      "ODORO",  "Odor Investigation Outside",
      "OTHER",  "Unclassified Complaint",
      "OVRDSE", "Overdose",
      "OWI",    "Operating While Intoxicated",
      "PAPER",  "Paper Service",
      "PARK",   "Parking Complaint",
      "PDA",    "Property Damage Accident",
      "PDROLL", "Property Damage Rollover Accident",
      "PEACE",  "Peace Officer",
      "PIA",    "Personal Injury Accident",
      "PIROLL", "Personal Injury Rollover Accident",
      "PROWL",  "Prowler",
      "PSO",    "Public Safety Officer Response",
      "PUBSRV", "Public Service Event",
      "RALARM", "Residential Fire Alarm",
      "RAW",    "Runaway",
      "REPO",   "Repossession",
      "RETAIL", "Retail Fraud",
      "RO",     "Resisting and Obstructing",
      "ROBBER", "Robbery",
      "SHOOT",  "Shooting Victim",
      "SHOTS",  "Shots Fired Heard",
      "STAB",   "Stabbing Victim",
      "STACOV", "Station Cover",
      "STALK",  "Stalking Complaint",
      "STEMI",  "Priority STEMI Transfer Request",
      "STNDBY", "Standby Request",
      "STRUCF", "Structure Fire",
      "SUICID", "Suicidal Subject",
      "SUSPIC", "Suspicious Situation",
      "TESTFI", "Test Fire Call",
      "TESTLA", "Test Law Call",
      "TESTME", "Test Medical Call",
      "THREAT", "Threats Complaint",
      "TRAFF",  "Traffic Stop",
      "TRANSF", "Transfer Request",
      "TRASH",  "Trash Dumping",
      "TRBL",   "Other Person Trouble",
      "TRESP",  "Trespassing",
      "UDAA",   "Stolen Vehicle",
      "UNKACC", "Unknown Accident",
      "UNKN",   "Unknown Trouble",
      "UNKROL", "Unknown Rollover Accident",
      "UNWANT", "Unwanted Person",
      "VEHFIR", "Vehicle Fire",
      "VEHINS", "Vehicle Inspection",
      "VIOL",   "Violation Of Court Order",
      "VPHC",   "Violation Of Public Health Code",
      "WARRAN", "Warrant Arrest",
      "WATER",  "Water Rescue",
      "WELFAR", "Welfare Check",
      "WIRES",  "Wires Down"
  });
}
