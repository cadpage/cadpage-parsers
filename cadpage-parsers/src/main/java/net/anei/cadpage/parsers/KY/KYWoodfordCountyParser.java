package net.anei.cadpage.parsers.KY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class KYWoodfordCountyParser extends SmartAddressParser {

  public KYWoodfordCountyParser() {
    super(CITY_LIST, "WOODFORD COUNTY", "KY");
    setFieldList("SRC CODE CALL ADDR APT CITY DATE TIME GPS INFO ID");
  }

  @Override
  public String getFilter() {
    return "CAD@vpd.versaillesky.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z][A-Z0-9]+|");
  private static final Pattern MBLANK_PTN = Pattern.compile("\\s+");
  private static final Pattern MASTER = Pattern.compile("CAD:([A-Z0-9]+) (.*?) (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)(?: (.*?))??(?: (\\d{12}))?");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("E911 CLASS: [A-Z0-9]+ .*? LAT: ([-+]\\d{2,3}\\.\\d{6}) LON: ([-+]\\d{2,3}\\.\\d{6})\\b *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (SUBJECT_PTN.matcher(subject).matches()) data.strSource = subject;

    body = MBLANK_PTN.matcher(body).replaceAll(" ");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    data.strCode = match.group(1);
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(2), data);
    data.strDate = match.group(3);
    data.strTime = match.group(4);
    String info = getOptGroup(match.group(5));
    data.strCallId = getOptGroup(match.group(6));

    match = INFO_GPS_PTN.matcher(info);
    if (match.matches()) {
      setGPSLoc(match.group(1)+','+match.group(2), data);
      info = match.group(3);
    }
    data.strSupp = info;
    return true;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "E01",    "Abdominal Pain",
      "E02",    "Alleries/Hives/Sting",
      "E03",    "Animal Bites",
      "E04",    "Assault/Rape",
      "E05",    "Back Pain",
      "E06",    "Breathing Problems",
      "E07",    "Burns",
      "E08",    "Carbon Monoxide Poison",
      "E09",    "Cardiac/Respir Arrest",
      "E10",    "Chest Pain",
      "E1046",  "Ambulance to Accident",
      "E11",    "Choking",
      "E12",    "Convulsions/Seizures",
      "E13",    "Diabetic Problems",
      "E14",    "Drowning/Diving Accident",
      "E15",    "Electroction",
      "E16",    "Eye Problems",
      "E17",    "Falls",
      "E18",    "Headaches",
      "E19",    "Heart Problems",
      "E20",    "Hemorrhage",
      "E21",    "Indust/Machinery Accident",
      "E22",    "Multiple Complaints",
      "E23",    "Overdose/Poisoning",
      "E24",    "Pregnancy/Childbirth",
      "E25",    "Phychiatric/Behav Problem",
      "E26",    "Spec Diag-Sick Person",
      "E27",    "Stab/GSW",
      "E28",    "Stroke/CVA",
      "E30",    "Traumatic Inj-Specific",
      "E31",    "Unconscious/Fainting",
      "E32",    "Unknown Prob-Man Down",
      "E33",    "Broken/Fractured Limbs",
      "EALM",   "EMS Fire Alarm Run",
      "EASST",  "EMS Assitance",
      "EFIRE",  "Fire Dept. Run",
      "EPAGE",  "3rd Call",
      "ETRN",   "EMS-Training",
      "ETSP",   "EMS-Transport",
      "F1046",  "Fire Unit to Accident",
      "F1ACC",  "Fire Accident/Injury",
      "F1APT",  "Fire-Apartment",
      "F1FAC",  "Fire-Factory-Business",
      "F1HSE",  "Fire-House",
      "F1JAW",  "Jaws Run",
      "F1RES",  "Fire-Rescue",
      "F1SCH",  "Fire-School",
      "F1VEH",  "Fire-Vehicle",
      "FALRM",  "Fire Alarm",
      "FASST",  "Fire Assist Other Agency",
      "FEMS",   "FEMS",
      "FCARB",  "Carbon Monoxide Detector",
      "FGRAS",  "Fire-Grass Fire",
      "FHAZ",   "Hazmat Run",
      "FINVE",  "Fire Investigation",
      "FSPEC",  "Fire Dept. Special Detail",
      "FTRAN",  "Fire Training"

  });

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "LEXINGTON",
    "MIDWAY",
    "VERSAILLES",

    // Unincorporated communities
    "MILLVILLE",
    "NONESUCH",
    "MORTONSVILLE",
    "MILNER",
    "TROY"
  };
}
