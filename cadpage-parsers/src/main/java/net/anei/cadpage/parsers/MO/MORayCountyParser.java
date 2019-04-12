package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;



public class MORayCountyParser extends SmartAddressParser {
  
  public MORayCountyParser() {
    super("RAY COUNTY", "MO");
    setFieldList("ADDR APT CALL X CITY DATE TIME");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@raycounty911.com";
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d)\\b");
  private static final Pattern CROSS_DELIM_PTN = Pattern.compile(" +(\\d+(?:\\.\\d+)? mi [NSEW]{1,2})\\b");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Parser p = new Parser(body);
    String sAddr = p.getOptional(" CrossStreets:");
    if (sAddr.length() == 0) return false;
    
    // Leave address for a moment.  We won't know which how to parser it until
    // we figure out which of  two possible formats this is
    
    String sLeft = p.get(" Dispatch: ");
    String sTimes = p.get();
    if (sTimes.length() > 0) {
      Matcher match = DATE_TIME_PTN.matcher(sTimes);
      if (match.lookingAt()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
      }
    }
    
    while (true) {
      Matcher match = CROSS_DELIM_PTN.matcher(sLeft);
      if (!match.find()) break;
      String sCross = sLeft.substring(0,match.start()) + "/" + match.group(1);
      data.strCross = append(data.strCross, " / ", sCross);
      sLeft = sLeft.substring(match.end()).trim();
    }
    
    data.strCity = sLeft;
    String call = CALL_TABLE.getCode(sAddr, true);
    if (call != null) {
      data.strCall = call;
      sAddr = sAddr.substring(0, sAddr.length()-call.length()).trim();
      parseAddress(StartType.START_ADDR, FLAG_NO_CITY | FLAG_ANCHOR_END, sAddr, data);
    }
    
    else {
      parseAddress(StartType.START_ADDR, FLAG_NO_CITY, sAddr, data);
      data.strCall = getLeft();
    }
    
    // No matter how we got it, a call with out a call description is a failure
    return (data.strCall.length() > 0);
  }
  
  
  @Override
  public CodeSet getCallList() {
    return CALL_TABLE;
  }

  private static final CodeSet CALL_TABLE = new ReverseCodeSet(
      "10-100 SUICIDAL PERSON / ATTEMPTED",
      "10-50 TRAFFIC/TRANSPORTATION INCIDENTS",
      "10-50 TRAFFIC/TRANSPORT INCIDENT (CRASH)",
      "10-50 TRAFFIC & TRANSPORTATION INCIDENTS",
      "10-96 MENTAL DISORDER (BEHAVIORAL)",
      "9-1-1 HANG UP",
      "ABDOMINAL PAIN / PROBLEMS",
      "ALARMS",
      "ALLERGIES / ENVENOMATIONS",
      "ANIMAL",
      "ANIMAL BITES / ATTACKS",
      "ASSAULT",
      "ASSAULT/SEXUAL ASSAULT",
      "BACK PAIN (NON TRAUMATIC / NON RECENT)",
      "BREATHING PROBLEMS",
      "CARBON MONOXIDE/INHALTION/HAZMAT/CBRN",
      "CARDIAC OR RESPIRATORY ARREST / DEATH",
      "CHEST PAIN (NON-TRAUMATIC)",
      "CONVULSIONS / SEIZURES",
      "DETAIL EMS",
      "DETAIL FIRE",
      "DIABETIC PROBLEM",
      "DISTURBANCE / NUISANCE",
      "DOMESTIC DISTURBANCE / VIOLENCE",
      "DRUGS",
      "ELECTRICAL HAZARD",
      "FALLS",
      "FIRE ALARM",
      "FOLLOW UP",
      "GAS LEAK / GAS ODOR (NATURAL & LP GAS)",
      "HAZMAT",
      "JUVENILE",
      "HEADACHE",
      "HEART PROBLEMS / A.I.C.D.",
      "HEMORRHAGE / LACERATIONS",
      "MUTUAL AIDE / ASSIST OUTSIDE AGENCY EMS",
      "MISCELLANEOUS",
      "MUTUAL AIDE / ASSIST OUTSIDE AGENCY",
      "NAME AND NUMBER",
      "ODOR (STRANGE/UNKNOWN)",
      "OUTSIDE FIRE",
      "OVERDOSE / POISONING (INGESTION)",
      "PREGNANCY/CHILDBIRTH/MISCARRIAGE",
      "PSYCHIATRIC/ABNORMAL BEHAVIOR",
      "PUBLIC SERVICE (WELFARE CHECK)",
      "SICK PERSON (SPECIFIC DIAGNOSIS)",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "STAB/GUNSHOT/PENETRATING TRAUMA",
      "STAND BY",
      "STROKE (CVA)",
      "STROKE (CVA) / TRANSIENT ATTACK (TIA)",
      "STRUCTURE FIRE",
      "SUSPICIOUS / WANTED (PERSON, VEHICLE)",
      "TEST CALL",
      "TRAFFIC/TRANSPORTATION INCIDENTS",
      "TRAUMATIC INJURIES (SPECIFIC)",
      "TRANSFER/INTERFACILITY/PALLIATIVE CARE",
      "UNCONSCIOUS / FAINTING (NEAR)",
      "UNKNOWN (3RD PARTY CALLER)",
      "UNKNOWN PROBLEM (MAN DOWN)",
      "VEHICLE FIRE",
      "WATER RESCUE"
  );
}
