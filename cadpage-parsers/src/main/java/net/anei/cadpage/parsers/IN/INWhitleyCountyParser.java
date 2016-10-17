package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class INWhitleyCountyParser extends SmartAddressParser {
  
  public INWhitleyCountyParser() {
    super(CITY_LIST, "WHITLEY COUNTY", "IN");
    setFieldList("ID ADDR APT CITY CALL INFO");
  }
  
  private static final Pattern ID_PREFIX_PTN = Pattern.compile("(?:(\\d{3};*\\d{8,9})|604)(?:\\b[ 0-9;]*;(?:\\d+ {2,}|(?<= ;)\\d+ )? *(?:\\d{14} {2,})*| (?:\\d{14} {2,})+|  +(?:\\d{14} {2,})*)");
  private static final Pattern ZIP_CODE_PTN = Pattern.compile(" IN \\d{5}; *");
  private static final Pattern CITY_CODE_PTN = Pattern.compile("(.*) \\([A-Z]{4}\\) (.*)");
  private static final Pattern MBLANK_PTN = Pattern.compile(" +");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = ID_PREFIX_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = getOptGroup(match.group(1)).replace(";", "");
    body = body.substring(match.end()).trim();
    
    // Check for and strip off duplicate message
    match = ID_PREFIX_PTN.matcher(body);
    if (match.find()) {
      String tmp = match.group(1).replace(";", "");
      if (tmp.equals(data.strCallId)) {
        body = body.substring(0,match.start()).trim();
      }
    }
    
    // Strip off possible leading !
    body = stripFieldStart(body, "!");
    
    // Look for a zip code
    match = ZIP_CODE_PTN.matcher(body);
    if (match.find()) {
      
      // Finding one makes things so much easer
      String addr = body.substring(0,match.start()).trim();
      String left = body.substring(match.end()).trim();
      
      // Even easier if there is a city code in front of the city
      match = CITY_CODE_PTN.matcher(addr);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = match.group(2).trim();
      }
      
      // Otherwise use address parser to split off city
      else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
        if (data.strCity.length() == 0) return false;
      }
      
      // Break trailer into call and other information
      String call = CALL_LIST.getCode(left, true);
      if (call != null) {
        data.strCall = call;
        data.strSupp = left.substring(call.length()).trim();
      } else {
        int pt = left.indexOf("  ");
        if (pt >= 0) {
          data.strCall = left.substring(0,pt);
          data.strSupp = left.substring(pt+2).trim();
        } else if (left.length() <= 40) {
          data.strCall = left;
        } else {
          data.strSupp = left;
        }
      }
      return true;
      
    } else {
      
      // No city makes things harder
      // See if we can find an call description to mark the
      // end of the address
      match = MBLANK_PTN.matcher(body);
      while (match.find()) {
        int pt = match.end();
        String call = CALL_LIST.getCode(body.substring(pt), true);
        if (call != null) {
          data.strCall = call;
          parseAddress(body.substring(0,pt).trim(), data);
          data.strSupp = body.substring(pt+call.length()).trim();
          return true;
        }
      }
      
      // No such luck,  Nothing can save us know but the smart address parser
      parseAddress(StartType.START_ADDR, FLAG_NO_CITY, body, data);
      if (!isValidAddress()) return false;
      String left = getLeft();
      if (left.length() == 0) return false;
      
      // Break trailer into call and other information
      String call = CALL_LIST.getCode(left, true);
      if (call != null) {
        data.strCall = call;
        data.strSupp = left.substring(call.length()).trim();
      } else {
        int pt = left.indexOf("  ");
        if (pt >= 0) {
          data.strCall = left.substring(0,pt);
          data.strSupp = left.substring(pt+2).trim();
        } else if (left.length() <= 40) {
          data.strCall = left;
        } else {
          data.strSupp = left;
        }
      }
      return true;
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "1050 HIT/RUN P",
      "911 HANG UP",
      "ABANDONED VEHICLE",
      "ABD PAIN-PR 1",
      "ABD PAIN-PR 6",
      "AIRCRAFT -CRASH",
      "AIRCRAFT CRASH",
      "ALARM BANK",
      "ALARM BUSINESS",
      "ALARM OTHER",
      "ALARM RESIDENCE",
      "ALLERGY/BITES/STINGS-PR 1",
      "ALLERGY/STINGS/BITES-PR 2",
      "ALLERGY/STINGSIBITES-PR 6",
      "ALOC- PR1",
      "ALOC- PR2",
      "ANIM BITE-PR 1",
      "ANIM BITE-PR 2",
      "ANIM BITE-PR 6",
      "ANIMAL BITE",
      "ANIMAL COMPLAIN",
      "ASSAULT - PR 0",
      "ASSAULT-PR 1",
      "ASSAULT-PR 2",
      "ASSAULT-PR 3",
      "ASSAULT-PR1",
      "ASSAULT-PR2",
      "ASSAULT-PR6",
      "ASSIST ANOTHER",
      "ASSIST ANOTHER AGENCY",
      "ASSIST FIRE DEP",
      "ATL",
      "ATV COMPLAINT",
      "ATV CRASH - PI",
      "ATV CRASH-PP",
      "AUTO THEFT",
      "BACK PAIN-PR 1",
      "BACK PAIN-PR 2",
      "BACK PAIN-PR 6",
      "BACK PAIN-PR1",
      "BANK DETAIL",
      "BATTERY",
      "BLEEDING-PR 1",
      "BLEEDING-PR 2",
      "BLEEDING-PR 6",
      "BLOOD RUN",
      "BOAT CRASH - PI",
      "BOMB THREAT",
      "BURGLARY",
      "BURN - PR 1",
      "BURN - PR 2",
      "BURN - PR 6",
      "C/R ARREST- PR1",
      "C/R ARREST- PR2",
      "C/R ARREST- PR6",
      "C/R ARREST- PRO",
      "C/R ARREST-PR 1",
      "C/R ARREST-PR 2",
      "C/R ARREST-PR 6",
      "C/R ARREST-PR1",
      "C/R ARREST-PR2",
      "C/R ARREST-PR6",
      "CELL PHONE STOLEN",
      "CHEST PAIN-PR 1",
      "CHEST PAIN-PR 2",
      "CHEST PAIN-PR 6",
      "CHILD ABUSE/NEGLECT",
      "CHILD MOLESTING",
      "CHILD SAFETY",
      "CHILD SEAT INSTALL",
      "CHOKING-PR 1",
      "CHOKING-PR 2",
      "CHOKING-PR 6",
      "CITIZEN ASSIST",
      "CIVIL MATTER",
      "CIVIL SERVICE",
      "CO/HAZMAT-PR 1",
      "CO/HAZMAT-PR 2",
      "CO/HAZMAT-PR 6",
      "CONTRIBUTING TO A MINOR",
      "CRIM MISCHIEF",
      "CRIMINAL MISCHIEF",
      "DAS",
      "DEATH INVESTIGATION",
      "DEATH-NATURAL",
      "DEER KILL PERMIT",
      "DIABETIC-PR 1",
      "DIABETIC-PR 2",
      "DIABETIC-PR 6",
      "DIRECT TRAFFIC",
      "DISABLED VEHICL",
      "DISABLED VEHICLE",
      "DNR VIOLATION",
      "DOG RUNNING AT",
      "DOG-BARKING",
      "DOMESTIC",
      "DRIVE OFF",
      "DRIVING COMPLAINT",
      "DROWNING",
      "DROWNING-PR 1",
      "DROWNING-PR 2",
      "DROWNING-PR 6",
      "DRUG COMPLAINT",
      "ELECTROC-PR 1",
      "ELECTROC-PR 2",
      "ELECTROC-PR 6",
      "EMERGENCY MSG NOTIFICATION",
      "ENTRAPMENT-PR 1",
      "ENTRAPMENT-PR 2",
      "ENTRAPMENT-PR 6",
      "ESCAPE-PRISONER",
      "EVIDENCE PROCESSING",
      "EYE INJURY-PR 1",
      "EYE INJURY-PR 2",
      "EYE INJURY-PR 6",
      "FALL - PR 1",
      "FALL - PR 2",
      "FALL - PR 6",
      "FALL-PR 1",
      "FALL-PR 2",
      "FALL-PR 5",
      "FALL-PR1",
      "FALL-PR2",
      "FALL-PR6",
      "FALL-PRO",
      "FIGHT",
      "FIRE ALARM",
      "FIRE CALLS ALL",
      "FIRE INVESTIGATION",
      "FIRE SERVICE CA",
      "FIRE SERVICE CALLS",
      "FIRE STANDBY",
      "FIREARMS COMPLAINT",
      "FIREWORKS",
      "FOLLOW UP INVESTIGATION",
      "FORGERY",
      "FRAUD",
      "FUNERAL DETAIL",
      "HARRASSMENT",
      "HAZMAT",
      "HEADACHE-PR 1",
      "HEADACHE-PR 2",
      "HEADACHE-PR 6",
      "HEART/AICD-PR 1",
      "HEART/AICD-PR 2",
      "HEART/AICD-PR 6",
      "HEAT/COLD-PR 1",
      "HEAT/COLD-PR 2",
      "HEAT/COLD-PR 6",
      "HOSTAGE",
      "ID THEFT",
      "ILLEGAL DUMPING",
      "ILLNESS - PR 1",
      "ILLNESS - PR 2",
      "ILLNESS - PR 6",
      "ILLNESS-PR 1",
      "ILLNESS-PR 2",
      "ILLNESS-PR 6",
      "ILLNESS-PR1",
      "ILLNESS-PR2",
      "ILLNESS-PR6",
      "INDECENT EXPOSURE",
      "INFORMATION",
      "INTIMIDATION",
      "INTOXICATED DRIVER",
      "INTOXICATED PERSON, PUBLIC INTOX",
      "JAIL PROBLEM",
      "JUVENILE",
      "K-9 ASSIST",
      "K-9 DETAIL",
      "KIDNAPPING",
      "LOCKOUT",
      "LOST PROPERTY",
      "LOST/STOLEN PLATE",
      "MAILBOX",
      "MEDIC ASSIST",
      "MEDICAL ASSIST",
      "MENTAL SUBJECT",
      "MIP",
      "MISSING PERSON",
      "MURDER",
      "MUTUAL AID",
      "MVC- PR 1",
      "MVC- PR 2",
      "MVC- PR 6",
      "MVC- PR1",
      "MVC- PR2",
      "MVC- PR6",
      "MVC-PR 1",
      "MVC-PR 2",
      "MVC-PR 6",
      "NOISE COMPLAINT",
      "OB - PR 1",
      "OB - PR 2",
      "OB - PR 6",
      "OB-PR1",
      "OB-PR2",
      "OB-PR6",
      "OD/POISON-PR 0",
      "OD/POISON-PR 1",
      "OD/POISON-PR 2",
      "OD/POISON-PR 6",
      "OFFICER DOWN",
      "OPEN DOOR",
      "ORDINANCE VIOLATION",
      "OTHER CRIMES",
      "PAID STANDBY",
      "PO SERVICE",
      "PO VIOLATION",
      "POLICE ASSIST",
      "PROBATION CHECK",
      "PROWLER",
      "PSY/SUICID-PR 1",
      "PSY/SUICID-PR 2",
      "PSY/SUICID-PR 6",
      "PUBLIC SERVICE",
      "PURSUIT",
      "RAPE",
      "RECKLESS DRIVIN",
      "RECKLESS DRIVING",
      "RECOVERED PROPERTY",
      "REPOSSESSION",
      "RESP DIST- PR 1",
      "RESP DIST- PR 2",
      "RESP DIST- PR 6",
      "ROAD HAZARD",
      "ROBBERY",
      "RUNAWAY",
      "SCHOOL WALK-THRU",
      "SECURITY CHECK",
      "SEIZURE-PR 1",
      "SEIZURE-PR 2",
      "SEIZURE-PR 6",
      "SEXUAL ASSAULT",
      "SHOOTING -ACCIDENT",
      "SHOOTING-ACCIDENT",
      "SHOPLIFTING",
      "SHOTS FIRED",
      "SLIDE OFF",
      "SNOWMOBILE CRASH - PI",
      "SOLICITOR",
      "SPECIAL DETAIL",
      "STROKE/CVA-PR 1",
      "STROKE/CVA-PR 2",
      "STROKE/CVA-PR 6",
      "SUICIDE",
      "SUICIDE -ATTEMPT",
      "SUICIDE ATTEMPT",
      "SUSPICIOUS",
      "TEST",
      "THEFT",
      "TRAFFIC STOP",
      "TRAIN COMPLAINT",
      "TRAINING EXERCISE",
      "TRANSFER",
      "TRANSPORT",
      "TRAUMA - PR 1",
      "TRAUMA - PR 2",
      "TRAUMA - PR 6",
      "TRAUMA INJ- PR1",
      "TRAUMA INJ- PR2",
      "TRAUMA INJ- PR6",
      "TRAUMA INJ-PR 1",
      "TRAUMA INJ-PR 2",
      "TRAUMA INJ-PR 6",
      "TRAUMA-GUN,STAB",
      "TRAUMA-PR1",
      "TRAUMA-PR2",
      "TRAUMA-PR6",
      "TRESPASSING",
      "UNAUTHORIZED CONTROL",
      "UNK/PROB- PR1",
      "UNK/PROB- PR2",
      "UNK/PROB- PR6",
      "UNWANTED PARTY",
      "VEH CRASH - HIT/RUN PI",
      "VEH CRASH - PD",
      "VEH CRASH - PI",
      "VEH CRASH - PRIVATE PROPERTY",
      "VEH CRASH - UNKNOWN",
      "VEH CRASH - VEH/DEER",
      "VEH CRASH -UNKNOWN",
      "VEH CRASH -VEH/DEER",
      "VEH CRASH, HIT/RUN PD",
      "VEHICLE MAINTENANCE",
      "VERBAL DISPUTE",
      "VIN CHECK",
      "WARRANT SERVICE",
      "WATER RESCUE",
      "WEATHER SPOTTER",
      "WELFARE CHECK"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities and towns
    "CHURUBUSCO",
    "COLUMBIA CITY",
    "LARWILL",
    "SOUTH WHITLEY",

    // Unincorporated towns
    "COESSE",
    "COLLAMER",
    "COLLINS",
    "ETNA",
    "LAUD",
    "PEABODY",
    "RABER",
    "TRI-LAKES",
    "TUNKER",
    "LORANE",

    // Townships

    "CLEVELAND",
    "COLUMBIA",
    "ETNA-TROY",
    "JEFFERSON",
    "RICHLAND",
    "SMITH",
    "THORNCREEK",
    "UNION",
    "WASHINGTON"
    
  };

}
