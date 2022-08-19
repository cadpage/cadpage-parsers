package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Henry County, VA
 */
public class VAHenryCountyAParser extends DispatchSouthernParser {
  
  public VAHenryCountyAParser() {
    super(CALL_LIST, CITY_LIST, "HENRY COUNTY", "VA", DSFLAG_CROSS_NAME_PHONE | DSFLAG_FOLLOW_CROSS);
    allowBadChars("()");
  }

  @Override
  public String getFilter() {
    return "@co.henry.va.us";
  }
  
  private static final Pattern UNIT_PRI_PTN = Pattern.compile(" +([A-Z0-9]+)-\\((\\d)\\) +");
  private static final Pattern APT_X_PTN = Pattern.compile("BW (.*) AND (.*)");
  private static final Pattern LEAD_PRIORITY_PTN = Pattern.compile("(0?\\d)\\b");
  private static final Pattern APT_MAP_PTN = Pattern.compile("(?:(.*) )?([NSEW]{1,2} SECTOR?)");
  private static final Pattern APT_MAP_PTN2 = Pattern.compile("SECTOR?");
  private static final Pattern ADDR_DIR_PTN = Pattern.compile("(.*) ([NSEW]{1,2})");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = UNIT_PRI_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = match.group(1);
      data.strPriority = match.group(2);
      body = body.substring(0,match.start()) + " " + body.substring(match.end());
    }
    if (!super.parseMsg(body, data)) return false;
    
    // Fix cross streets in apt field
    if (data.strCross.length() == 0) {
      match = APT_X_PTN.matcher(data.strApt);
      if (match.matches()) {
        data.strCross = match.group(1).trim() + " / " + match.group(2).trim();
        data.strApt = "";
      }
    }
    
    // Dispatch - Google spat
    data.strAddress = data.strAddress.replace("WILLIAM F STONE PARK ", "WILLIAM F STONE ");
    
    // Fix DEER TRL RD
    data.strAddress = data.strAddress.replace("DEER TRL & RD", "DEER TRL RD");
    
    // The apparently have eliminated all blanks from call descriptions recently
    // so if we did not find a call match, split call description at first blank
    if (data.strCall.length() == 0) {
      data.strCall = data.strSupp;
      data.strSupp = "";
    }
    data.strCall = stripFieldStart(data.strCall, "CallType Changed to ");
    data.strCall = stripFieldStart(data.strCall, "-");
    String code = CALL_LIST.getCode(data.strCall);
    if (code == null || code.length() != data.strCall.length()) {
      int pt = data.strCall.indexOf(' ');
      if (pt >= 0) {
        data.strSupp = append(data.strCall.substring(pt+1).trim(), " ", data.strSupp);
        data.strCall = data.strCall.substring(0,pt);
      }
    }
    
    // See if we can find a priority in front of what is left
    if (data.strPriority.length() == 0 && (match = LEAD_PRIORITY_PTN.matcher(data.strSupp)).lookingAt()) {
      data.strPriority = match.group(1);
      data.strSupp = data.strSupp.substring(match.end()).trim();
    }
    
    match = APT_MAP_PTN.matcher(data.strApt);
    if (match.matches()) {
      data.strApt = getOptGroup(match.group(1));
      data.strMap = match.group(2);
    }
    
    else if (APT_MAP_PTN2.matcher(data.strApt).matches()) {
      match = ADDR_DIR_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strAddress = match.group(1).trim();
        data.strMap = match.group(2) + ' ' + data.strApt;
        data.strApt = "";
      }
    }
    return true;
  }
  
  @Override
  protected void parseExtra(String sExtra, Data data) {
    if (sExtra.startsWith("-")) sExtra = sExtra.substring(1).trim();
    super.parseExtra(sExtra, data);
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CALL PRI").replace("APT", "APT MAP");
  }

  private static final String[] CITY_LIST = new String[]{
      "MARTINSVILLE",
      "RIDGEWAY",

      "AXTON",
      "BASSETT",
      "CHATMOSS",
      "COLLINSVILLE",
      "FIELDALE",
      "HORSEPASTURE",
      "LAUREL PARK",
      "OAK LEVEL",
      "SANDY LEVEL",
      "SPENCER",
      "STANLEYTOWN",
      "VILLA HEIGHTS",

      // Franklin County
      "FRANKLIN",
      "FRANKIN CO",
      "HENRY",
      
      // Patrick County
      "PATRICK CO",
      "PATRICK SPRINGS",


  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "0 - DISPATCHER HANDLED",
      "911 - 911 OPEN LINE - HANG UP",
      "911---911-OPEN-LINE---HANG-UP",
      "ABANDONED VEHICLE",
      "ABDOMINAL / BACK PAIN",
      "ABDOMINAL-/-BACK-PAIN",
      "ABUSE-CHILD/ADULT/ELDERLY",
      "ADDRESS VIOLATION",
      "ADMIN MAINTENANCE",
      "ADMIN-MAINTENANCE",
      "ADVICE - INFORMATION",
      "AIRCRAFT INCIDENT",
      "ALARM (BURG)",
      "ALARM - BUSINESS",
      "ALARM - RESIDENCE",
      "ALARM---RESIDENCE",
      "ALARM - VEHICLE",
      "ALLERGIC REACTION",
      "ALLERGIC-REACTION",
      "AMBULANCE NEEDED",
      "ANIMAL BITES",
      "ANIMAL OTHER",
      "ANIMAL / SNAKE BITE",
      "ANIMAL-/-SNAKE-BITE",
      "ARRIVED AT OFFICE",
      "ARRIVED AT SCENE",
      "ASSAULT AGGRAVATED",
      "ASSAULT-AGGRAVATED",
      "ASSAULT GENERAL",
      "ASSAULT-GENERAL",
      "ASSAULT INTIMIDATION",
      "ASSAULT/SEXUAL ASSAULT",
      "ASSAULT SIMPLE",
      "ASSAULT WITH INJURY",
      "ASSAULT-WITH-INJURY",
      "ASSIST MOTORIST",
      "ASSIST-MOTORIST",
      "ASSIST OTHER AGENCY",
      "ASSIST STATE POLICE",
      "ASSIST-STATE-POLICE",
      "BAD CHECKS",
      "BE ON LOOK OUT FOR",
      "BIKE PATROL",
      "BLEEDING (NON-TRAUMATIC)",
      "BLEEDING-(NON-TRAUMATIC)",
      "BOMB THREAT",
      "BOND-TAKE PRISONER FOR BOND",
      "BRANDISHING A FIREARM",
      "BREAK & ENTER BUSINESS",
      "BREAK & ENTER OUT BUILDING",
      "BREAK & ENTER RESIDENCE",
      "BREATHALIZER REPORT",
      "BREATHING DIFFICULTY",
      "BRIBERY",
      "BURNING BAN VIOLATION",
      "BURNS",
      "BUSY",
      "CALL BY PHONE",
      "CALL-BY-PHONE",
      "CARDIAC (WITH PREVIOUS HISTORY)",
      "CARDIAC-(WITH-PREVIOUS-HISTORY)",
      "CAUTION",
      "CHASE IN PROGRESS",
      "CHECKING BUILDING",
      "CHEST PAINS",
      "CHEST-PAINS",
      "CHEST PAINS/HEART PROBLEMS/HIGH BP",
      "CHEST PAINS/HEART PROBLEMS/HIGH-LOW BP",
      "CHEST-PAINS/HEART-PROBLEMS/HIGH-BP",
      "CHEST PAINS/HEART-PROBLEMS/HIGH-LOW BP",
      "CHOKING",
      "CIVIL MATTER (Explain In Notes)",
      "CLEAR CALL",
      "CLEAR LOT",
      "CODE-BLUE-(CARDIAC-ARREST)",
      "CODE-GRAY-(SUBJECT-DECEASED)",
      "CODE GREEN",
      "CODE RED",
      "CODE YELLOW",
      "CONSPIRACY",
      "CRIMESTOPPERS REPORT",
      "CYBER CRIME",
      "DECAL VIOLATION",
      "DELIVER MESSAGE",
      "DELIVER-MESSAGE",
      "DIABETIC ILLNESS",
      "DIABETIC-ILLNESS",
      "DIFFICULTY BREATHING",
      "DIFFICULTY-BREATHING",
      "DIRECTED PATROL",
      "DIRECT TRAFFIC",
      "DISORDERLY CONDUCT",
      "DISPATCH INFORMATION",
      "DISTURBANCE",
      "DIVERSION - HOSPITAL",
      "DIZZINESS, WEAKNESS",
      "DIZZINESS,-WEAKNESS",
      "DOG CASE",
      "DOMESTIC - ASSAULT",
      "DOMESTIC---ASSAULT",
      "DOMESTIC - JUVENILE",
      "DOMESTIC TROUBLE",
      "DRAG RACING",
      "DRIVING UNDER INFLUENCE",
      "DRUG - NARCOTICS VIOLATIONS",
      "DRUNK - DRINKING IN PUBLIC",
      "ELECTROCUTION / LIGHTNING",
      "EMBEZZLEMENT",
      "ENVIRONMENTAL-EMERGENCIES",
      "ESCORT",
      "EXTORTION - BLACKMAIL",
      "EXTRA PATROL - VACATION CHECKS",
      "EYE PROBLEM / INJURY",
      "EYE-PROBLEM-/-INJURY",
      "F-AIRCRAFT STANDBY/CRASH",
      "FALL",
      "FALL - FRACTURE",
      "FALL---FRACTURE",
      "FAR-ARCING-SHORTED ELEC EQPT",
      "FAR-ARCING-SHORTED-ELEC-EQPT",
      "F-ARSON",
      "F-BRUSH FIRE",
      "F-BRUSH-FIRE",
      "F-CHIMNEY FIRE",
      "F-CHIMNEY-FIRE",
      "F-CONTROLLED BURN",
      "F-CONTROLLED-BURN",
      "F-DUMPSTER FIRE",
      "F-DUMPSTER-FIRE",
      "F-EXPLOSION W-FIRE",
      "F-EXPLOSION-W-FIRE",
      "F-FIRE ALARM",
      "F-FIRE-ALARM",
      "F-GAS RUPTURE",
      "F-GAS SMELL-FUMES",
      "F-GAS-SMELL-FUMES",
      "F-HAZMAT INCIDENT",
      "F-HELICOPTER LANDING ZONE",
      "F-HELICOPTER-LANDING-ZONE",
      "FIGHT IN PROGRESS",
      "FINGERPRINT SUBJECT",
      "FLOODING-ASSIST",
      "FOLLOW UP INVESTIGATION",
      "FOOT PATROL",
      "FORGERY - COUNTERFITING",
      "F-OTHER FIRE - EXPLAIN",
      "F-OTHER-FIRE---EXPLAIN",
      "FOUND PROPERTY",
      "F-POWER LINE DOWN",
      "F-POWER-LINE-DOWN",
      "F-PUBLIC SERVICE/ENGINE INVESTIGATION",
      "FRAUD FALSE PRETENSE",
      "FRAUD GENERAL",
      "FRAUD SWINDLE",
      "F-SMOKE INVESTIGATION",
      "F-SMOKE-INVESTIGATION",
      "F-SPILL-LEAK - NO FIRE",
      "F-SPILL-LEAK W-FIRE",
      "F-STEAM RUPTURE",
      "F-STRUCTURE FIRE",
      "F-STRUCTURE-FIRE",
      "F-UNAUTHORIZED BURNING",
      "F-UNAUTHORIZED-BURNING",
      "FUNERAL TRAFFIC",
      "F-UNKNOWN FIRE - EXPLAIN",
      "F-VEHICLE FIRE",
      "F-VEHICLE-FIRE",
      "GAMBLING - BETTING - WAGERING",
      "GAS DRIVEOFF",
      "GENERATOR TEST",
      "GYNECOLOGY/OB/PREGNANCY",
      "HARASSMENT",
      "HEADACHE",
      "HEAD INJURY / NEUROLOGICAL",
      "HEAD-INJURY-/-NEUROLOGICAL",
      "HEAT/COLD ENVIRONMENTAL EMERGENCIES",
      "HIGH BLOOD PRESSURE",
      "HIGH-BLOOD-PRESSURE",
      "HIT & RUN",
      "HIT-&-RUN",
      "HOME VISIT",
      "HOMICIDE",
      "HOUSE CHECK",
      "IMPERSONATION",
      "IMPROPERLY PARKED VEHICLE",
      "INCEST",
      "INDECENT EXPOSURE",
      "INDUSTRIAL ACCIDENTS",
      "INTIMIDATION",
      "JUVENILES CAUSING PROBLEM",
      "KIDNAPPING - ABDUCTION",
      "LARCENY - ALL OTHER",
      "LARCENY FROM BUILDING",
      "LARCENY FROM COIN OPERATED MACHINE",
      "LARCENY FROM VEHICLE",
      "LARCENY OF VEHICLE",
      "LARCENY PARTS OF VEHICLE",
      "LARCENY - PICKPOCKET",
      "LARCENY - PURSE SNATCHING",
      "LARCENY - SHOPLIFTING",
      "LIQUOR LAW VIOLATIONS",
      "LITTERING",
      "LIVESTOCK ON HIGHWAY",
      "LOG FOR RECORD",
      "LOG-FOR-RECORD",
      "LOITERING",
      "LOUD MUSIC",
      "LOUD NOISE - BARKING",
      "MAJOR CRIME ALERT",
      "MANSLAUGHTER",
      "MAN WITH GUN",
      "MEDICAL ALARM",
      "MEDICAL EMERGENCY (DR OFFICE/HOSP)",
      "MEDICAL EMERGENCY (NURSING HOME)",
      "MEDICAL-ALARM",
      "MENTAL / EMOTIONAL / PSYCHOLOGICAL/SUICIDE ATTEMP",
      "MENTAL SUBJECT",
      "MISC LAW ENFORCEMENT",
      "MISSING PERSON - ADULT",
      "MISSING PERSON - JUVENILE",
      "MISS UTILITY NOTIFICATION",
      "MOTOR VEHICL CRASH / PEDESTRIAN",
      "MOTOR-VEHICL-CRASH-/-PEDESTRIAN",
      "MOTOR VEHICLE CRASH",
      "MOTOR-VEHICLE-CRASH",
      "MOTOR VEHICLE CRASH W/INJURY",
      "MOTOR-VEHICLE-CRASH-W/INJURY",
      "MOTOR VEHICLE CRASH W/NO INJURY",
      "MOTOR-VEHICLE-CRASH-W/NO-INJURY",
      "MURDER",
      "NEAR DROWNING / DROWNING/WATER RELATED",
      "NEED ASSISTANCE",
      "OB-GYN (PREGNANCY - MISCARRIAGE)",
      "OB-GYN-(PREGNANCY---MISCARRIAGE)",
      "OBSCENE MATERIAL - PORNOGRAPHY",
      "OBTAINING WARRANTS",
      "OFFICER BACK-UP",
      "OUT OF SERVICE",
      "OVERDOSE",
      "OVERDOSE / POISONING",
      "OVERDOSE-/-POISONING",
      "PEEPING TOM",
      "PHONE PATCH REQUEST",
      "PHONE # REQUEST",
      "PHONE THREATS",
      "PICKUP/RETURN PAPERS (WARR-PO-ECO-TDO-ETC.)",
      "PRISON - JAIL BREAK",
      "PROBATION / PAROLE VISIT",
      "PROPERTY MAINT INSP",
      "PROSTITUTION OFFENSES",
      "PROWLER",
      "PSYCHIATRIC PATIENT MENTAL",
      "PSYCHIATRIC-PATIENT-/-MENTAL",
      "PSYCHIATRIC-PATIENT-MENTAL",
      "PUBLIC SERVICE",
      "PUBLIC WORKS-GENERAL",
      "PUBLIC-WORKS-GENERAL",
      "RADIO SYSTEM / TOWER LOG FOR RECORD",
      "RAILROAD ACCIDENT",
      "RAPE-FORCE - STATUTORY",
      "RECKLESS DRIVING",
      "REFUSING TO LEAVE",
      "REFUSING-TO-LEAVE",
      "REPORT IN PERSON",
      "REQUEST PERMISSION TO LEAVE",
      "REQUEST STATE POLICE ASSISTANCE",
      "RIOT",
      "ROAD BLOCKED",
      "ROAD CLOSED / REPAIRS",
      "ROBBERY",
      "ROBBERY (BANK ONLY)",
      "SEARCH",
      "SECURITY CHECK",
      "SEIZURES",
      "SERVING ECO/TDO",
      "SERVING WARRANT",
      "SERVING-WARRANT",
      "SEX ASSAULT (NON RAPE)",
      "SHELTER REQUEST",
      "SHOOT - OCCUPIED DWELLING",
      "SHOOT - OCCUPIED VEHICLE",
      "SHOTS FIRED",
      "SICK",
      "SICK FLU LIKE SYMPTOMS",
      "SICK-FLU-LIKE-SYMPTOMS",
      "SICK / UNKNOWN",
      "SICK-/-UNKNOWN",
      "STABBING / GUNSHOT VICTIM",
      "STABBING-/-GUNSHOT-VICTIM",
      "STALKING",
      "STANDBY",
      "STORM DAMAGE / TREE DOWN",
      "STREET SIGN REPAIR LOG",
      "STROKE",
      "SUICIDE",
      "SUICIDE SUICIDE ATTEMPT",
      "SUICIDE-/-SUICIDE-ATTEMPT",
      "SUICIDE-SUICIDE-ATTEMPT",
      "SUSPECTED DEATH",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS-CIRCUMSTANCES",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS-PERSON",
      "SUSPICIOUS VEHICLE",
      "SUSPICIOUS-VEHICLE",
      "SWIFT WATER INCIDENTS",
      "TAG # - SOCIAL REQUEST",
      "TARGET",
      "TERRORIST ACTIVITY",
      "TOWED VEHICLE",
      "TRAFFIC HAZARD",
      "TRAFFIC STOP",
      "TRAILER INSPECTION - HOME MADE",
      "TRANSFER 911 CALL",
      "TRANSPORT (FROM DR. OFFICE / HOSPITAL)",
      "TRANSPORT-(FROM-DR.-OFFICE-/-HOSPITAL)",
      "TRANSPORT (HOSPITAL-DR OFFICE ETC.)",
      "TRANSPORT-(HOSPITAL-DR-OFFICE-ETC.)",
      "TRANSPORT (LOCAL)",
      "TRANSPORT MENTAL (LOCAL)",
      "TRANSPORT MENTAL (OUT OF TOWN)",
      "TRANSPORT (NURSING HOME)",
      "TRANSPORT (OUT OF TOWN)",
      "TRAUMA",
      "TRESPASSING",
      "UNAUTHORIZED USE",
      "UNCONSCIOUS / UNRESPONSIVE SYNCOPE",
      "UNCONSCIOUS-/-UNRESPONSIVE-SYNCOPE",
      "UNCONSCIOUS / UNRESPONSIVE & SYNCOPE OR CODE BLUE",
      "UNCONSCIOUS-/-UNRESPONSIVE-&-SYNCOPE-OR-CODE-BLUE",
      "UNDERAGE POSSESSION",
      "UNLOCK CAR DOOR",
      "VANDALISM (DESTRUCTION - DAMAGE)",
      "VCIN-CROSS CHECK",
      "VCIN-ENTRY",
      "VCIN-MODIFICATION",
      "VCIN-OTHER",
      "WANTED - STOLEN INDICATED",
      "WATER RESTRICTION VIOLATION",
      "WEAPONS VIOLATION",
      "WEATHER ALERTS",
      "WEATHER-ALERTS",
      "WELLBEING CHECK",
      "WELLBEING-CHECK",
      "WORK DETAIL",
      "WRECKER NEEDED"

  );
}
