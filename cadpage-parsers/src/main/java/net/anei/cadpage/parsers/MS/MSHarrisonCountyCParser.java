package net.anei.cadpage.parsers.MS;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MSHarrisonCountyCParser extends DispatchA48Parser {
  
  public MSHarrisonCountyCParser() {
    super(MSHarrisonCountyParser.CITY_LIST, "HARRISON COUNTY", "MS", FieldType.NONE, A48_ONE_WORD_CODE);
  }
  
  @Override
  public String getFilter() {
    return "S6ONDISPATCH@harrisoncountysheriff.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCode = data.strCall;
    String code = data.strCode;
    String call = CALL_CODES.getProperty(code);
    if (call == null) {
      int len = code.length();
      if (len > 0 && Character.isAlphabetic(code.charAt(len-1))) {
        code = code.substring(0,len-1);
        call = CALL_CODES.getProperty(code);
      }
    }
    if (call != null) data.strCall = call;
    return true;
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "S1",   "ACCIDENT",
      "S1S",  "ACCIDENT WITH INJURY",
      "S1F",  "ACCIDENT WITH FATALITY",
      "S2",   "ALARM",
      "S2F",  "FIRE ALARM",
      "S2M",  "MEDICAL ALARM",
      "S3",   "STOPPING VIOLATOR",
      "S4",   "SERVING WARRANT",
      "S4S",  "SERVING PROCESS",
      "S5",   "CHECKING VACANT HOUSE",
      "S6",   "ASSISTING MOTORIST",
      "S7",   "ARMED ROBBERY",
      "S8",   "ARMED ROBBERY IN PROGRESS",
      "S9",   "ARSON",
      "S10",  "AUTO THEFT",
      "S11",  "ASSAULT & BATTERY W/INTENT",
      "S12",  "ASSAULT & BATTERY",
      "S13",  "ASSAULT & BATTERH W/INSTRUMENT",
      "S14",  "ASSAULT BY THREAT",
      "S15",  "BOMB SCARE",
      "S16",  "BURGLARY",
      "S17",  "BURGLARY IN PROGRESS",
      "S18",  "CUP OF COFFEE",
      "S19",  "CARRYING CONCEALED WEAPON",
      "S20",  "COMPLAINT",
      "S21",  "CONTRIBUTING TO DELINQUENCY",
      "S22",  "CHECKING BUILDINGS",
      "S23",  "CITIZEN HOLDING SUSPECT",
      "S24",  "CUTTING",
      "S25",  "DEATH",
      "S26",  "DEMONSTRATION",
      "S27",  "DISCHARGING FIREARMS",
      "S28",  "DISCHARGING FIREWORKS",
      "S29",  "DISTRUBING THE PEACE",
      "S30",  "DRUG VIOLATION",
      "S31",  "DRUNK",
      "S32",  "DRUNK AND DISORDERLY",
      "S33",  "ESCAPE",
      "S34",  "FIGHT",
      "S35",  "PARKING VIOLATION",
      "S36",  "FIRE",
      "S36W", "WOODS FIRE",
      "S37",  "FORGERY",
      "S38",  "GAMBLING",
      "S39",  "HIT & RUN",
      "S40",  "IMPERSONATING OFFICER",
      "S41",  "INDECENT EXPOSURE",
      "S42",  "ABANDONED/STRIPPED VEHICLE",
      "S43",  "KIDNAPPING",
      "S44",  "MAD ANIMAL",
      "S45",  "MALICIOUS MISCHIEF",
      "S46",  "MAN WITH A GUN",
      "S47",  "MENTAL CASE",
      "S48",  "MURDER",
      "S49",  "MAN WITH A KNIFE",
      "S50",  "OFFICER NEEDS ASSISTANCE",
      "S51",  "D.U.I",
      "S52",  "PEEPING TOM",
      "S53",  "DISCHARGING BB/PELLET GUN",
      "S54",  "POSSESSION OF STOLEN PROPERTY",
      "S55",  "PROSTITUTION",
      "S56",  "PROWLER",
      "S57",  "RAPE",
      "S58",  "RECKLESS DRIVING",
      "S59",  "RESISTING ARREST",
      "S60",  "RIOT",
      "S61",  "STRONG ARMED ROBBERY",
      "S62",  "SHOOTING",
      "S63",  "DROWNING",
      "S64",  "SUICIDE",
      "S64A", "SUICIDE ATTEMPT",
      "S65",  "REFUELING AT STATION",
      "S66",  "SUSPICIOUS PERSON/VEHICLE",
      "S67",  "THEFT",
      "S68",  "TRESPASSING",
      "S70",  "TRAFFIC CONGESTION",
      "S71",  "VIOLATION OF LIQUOR LAWS",
      "S72",  "MEDICAL EMERGENCY",
      "S73",  "ANIMAL BITE",
      "S74",  "ANIMAL WARDEN NEEDED",
      "S75",  "U.F.O",
      "S76",  "DELIVER EMERGENCY MESSAGE",
      "S77",  "JUVENILE PROBLEM",
      "S78",  "LIVESTOCK COMPLAINT",
      "S79",  "DOMESTIC DISPUTE",
      "S80",  "MISSING/FOUND CHILD",
      "S81",  "MAIL TAMPERING",
      "S82",  "ACCIDENT (OTHER THAN VEHICULAR)",
      "S83",  "OBSCENE./HARRASSING PHONE CALL",
      "S84",  "LOST/FOUND ITEM",
      "S85",  "AIRCRAFT CRASH",
      "S86",  "CIVIL DISTURBANCE",
      "S911", "911 CALL"
  });
}
