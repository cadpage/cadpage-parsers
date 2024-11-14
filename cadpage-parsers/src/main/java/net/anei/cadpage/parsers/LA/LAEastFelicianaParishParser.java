package net.anei.cadpage.parsers.LA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * E Feliciana Parish, LA
 */
public class LAEastFelicianaParishParser extends SmartAddressParser {


  public LAEastFelicianaParishParser() {
    super(CITY_LIST, "EAST FELICIANA PARISH", "LA");
    setFieldList("ID CODE CALL DATE TIME ADDR X APT GPS PLACE CITY ST");
  }

  @Override
  public String getFilter() {
    return "EASTFELICIANA911@PAGINGPTS.COM";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Autopage EventID:(\\d{10})");
  private static final Pattern MASTER_PTN =
      Pattern.compile("Call (\\d{10}) ([A-Z0-9]+) (\\d\\d?/\\d\\d?/\\d{4}), (\\d\\d?:\\d\\d [AP]M) {2,}([^,]*?)(?:, *([^,]*))??(?:, +([A-Z]{2}))?(?: +(\\d{5}))?(?:,([^,]*,[^,]*))?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|LOT) *(.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);

    match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    if (!match.group(1).equals(data.strCallId)) return false;
    data.strCode =  match.group(2);
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    data.strDate = match.group(3);
    setTime(TIME_FMT, match.group(4), data);
    String addr = match.group(5);
    String aptCity = match.group(6);
    data.strState = getOptGroup(match.group(7));
    String zip = match.group(8);
    String gps = match.group(9);
    if (gps != null) setGPSLoc(gps, data);

    if (aptCity != null) {
      parseAddress(addr, data);
      aptCity = stripFieldEnd(aptCity, " EF");
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, aptCity.trim(), data);
      String apt = getStart();
      match = APT_PTN.matcher(apt);
      if (match.matches()) apt = match.group(1);
      data.strApt = append(data.strApt, "-", apt);
    } else {
      addr = stripFieldEnd(addr, " EF");
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      if (data.strApt.equals("ST") || data.strApt.equals("LN")) data.strApt = "";
      if (data.strCity.isEmpty() && zip !=  null) data.strCity = zip;
    }
    if (data.strCity.equals("EAST FELIC")) data.strCity = "EAST FELICIANA";
    return true;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "911HANGUP",    "911 HANGUP",
      "ALARMCM",      "CO ALARM",
      "ALARMF",       "FIRE ALARM",
      "ANIMALBITE",   "ANIMAL BITE",
      "BATTERYA",     "ASSAULT/BATTERY",
      "FIREE",        "ELECTRICAL FIRE",
      "FIREGRASS",    "GRASS FIRE",
      "FIRES",        "STRUCTURE FIRE",
      "FIRESMOKE",    "SMOKE/FIRE",
      "FIREVEH",      "VEHICLE FIRE",
      "GASLEAK",      "GAS LEAK",
      "GENERALCOM",   "ANNOUNCEMENT",
      "HITANDRUN",    "MVA - HIT AND RUN",
      "LIFTASSIST",   "LIFT ASSIST",
      "MEDABDPAIN",   "ABDOMINAL PAIN",
      "MEDALARM",     "MEDICAL ALARM",
      "MEDALTMENT",   "ALTERED MENTAL STATUS",
      "MEDAR",        "ALLERGIC REACTION",
      "MEDARMP",      "ARM PAIN",
      "MEDASP",       "RESPIRATORY EMERGENCY",
      "MEDASTHMA",    "ASTHMA ATTACK",
      "MEDATTS",      "SUICIDAL PATIENT",
      "MEDBC",        "BLOOD CLOT",
      "MEDBLEED",     "HEMORRHAGE/LACERATION",
      "MEDBLS",       "BLOODY STOOL",
      "MEDBNORES",    "UNRESPONSIVE",
      "MEDBP",        "BACK PAIN",
      "MEDBURN",      "BURN",
      "MEDBV",        "BLURRED VISION",
      "MEDCHOK",      "CHOKING",
      "MEDCOVID",     "COVID-19",
      "MEDCP",        "CHEST PAIN",
      "MEDDB",        "DIFFICULTY BREATHING",
      "MEDDEHY",      "HEAT EMERGENCY",
      "MEDDIAB",      "DIABETIC EMERGENCY",
      "MEDEMER",      "MEDICAL EMERGENCY",
      "MEDFAIN",      "FAINTING/SYNCOPAL EPISODE",
      "MEDFALL",      "FALL - POSSIBLE INJURIES",
      "MEDFEV",       "SICK PERSON",
      "MEDGENI",      "GENERAL ILLNESS",
      "MEDHA",        "HEART ATTACK",
      "MEDHEADA",     "HEADACHE",
      "MEDHEADINJ",   "HEAD TRAUMA",
      "MEDHIGHBS",    "HIGH BLOOD SUGAR",
      "MEDHIGHHR",    "HIGH HEART RATE",
      "MEDINSTING",   "INSECT BITE/STING",
      "MEDLEGP",      "LEG PAIN",
      "MEDLOWBP",     "LOW BLOOD PRESSURE",
      "MEDLOWBS",     "LOW BLOOD SUGAR",
      "MEDLOWHR",     "LOW HEART RATE",
      "MEDNECK",      "HEAD/NECK TRAUMA",
      "MEDNOBNOR",    "CARDIAC ARREST",
      "MEDNONEMER",   "NON-EMERGENCY MEDICAL",
      "MEDNUMB",      "NUMBNESS",
      "MEDO2",        "DIFFICULTY BREATHING",
      "MEDOD",        "OVERDOSE",
      "MEDOH",        "HEAT EMERGENCY",
      "MEDPOSTOP",    "MEDICAL - POST OP",
      "MEDPREGNAN",   "PREGNANCY EMERGENCY",
      "MEDSEIZURE",   "SEIZURE",
      "MEDSOB",       "DIFFICULTY BREATHING",
      "MEDSPORT",     "MEDICAL - SPORT INJURY",
      "MEDSTHREAT",   "SUICIDAL PATIENT",
      "MEDSTROKE",    "STROKE/CVA",
      "MEDUP",        "CARDIAC ARREST",
      "MEDVOMIT",     "NAUSEA/VOMITING",
      "MEDWEAK",      "GENERAL WEAKNESS",
      "MISSINGPER",   "MISSING PERSON",
      "MVA",          "MVA - UNKNOWN INJURIES",
      "MVAA",         "PLANE/HELICOPTER CRASH",
      "MVAANIMAL",    "MVA - ANIMAL",
      "MVAATV",       "MVA - ATV",
      "MVAFAT",       "MVA - FATALITY",
      "MVAINJ",       "MVA WITH INJURIES",
      "MVAT",         "MVA - TRAIN",
      "MVAUKINJ",     "MVA - UNKNOWN INJURIES",
      "NCICHIT",      "NCIC HIT",
      "NEGLECTA",     "ANIMAL NEGLECT",
      "NEGLECTC",     "CHILD NEGLECT",
      "NEGLECTELD",   "ELDERLY NEGLECT",
      "NOISE",        "NOISE COMPLAINT",
      "PROPDAMAGE",   "DAMAGE TO PROPERTY",
      "PUBLASSIST",   "PUBLIC ASSIST",
      "SHOTSFIRED",   "SHOTS FIRED",
      "SUSPICIOUS",   "SUSPICIOUS PERSON/VEHICLE",
      "TREEDOWN",     "TREE REMOVAL",
      "TS",           "TRAFFIC STOP",
      "VEHDITCH",     "MVA - VEHICLE IN DITCH"

  });

  private static final String[] CITY_LIST = new String[]{

      // Towns
      "CLINTON",
      "JACKSON",
      "SLAUGHTER",

      // Villages
      "NORWOOD",
      "WILSON",

      // Unincorporated communities
      "BLUFF CREEK",
      "ETHEL",
      "GURLEY",

      // East Baton Rouge Parish
      "ZACHARY",
      
      // East Feliciana Parish
      "EAST FELIC",

      // West Feliciana Parish
      "ST FRANCESVILLE",
      "ST FRANCISVILLE"
  };
}
