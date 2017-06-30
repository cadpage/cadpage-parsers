package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MDTalbotCountyBParser extends DispatchA48Parser {
  
  public MDTalbotCountyBParser() {
    super(CITY_LIST, "TALBOT COUNTY", "MD", FieldType.NONE, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  public String getFilter() {
    return "@talbotdes.org,4702193527";
  }
 
  private static final Pattern PREFIX = Pattern.compile("([ A-Za-z0-9]+)(?: Page - Dispatch| - Page)(?=:)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "TALBOTDES:");
    Matcher match = PREFIX.matcher(body);
    if (match.lookingAt()) {
      subject = match.group(1);
      body = body.substring(match.end());
    }
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BAILEYS NECK",
      "CROUSE MILL",
      "ST MICHAELS",
      "THIRD HAVEN"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALARMS COMMERCIAL/INDUSTRIAL BUILDING",
      "ALARMS RESIDENTIAL (SINGLE)",
      "ASSAULT NOT IN PROGRESS",
      "ASSIST EMS",
      "ASSIST FIRE DEPARTMENT",
      "BREATHING PROBLEM DIFFICULTY SPEAKING",
      "CHEST PAINS - BREATHING NORMALLY >35",
      "CHEST PAINS COCAINE",
      "CHEST PAINS - HEART ATTACK/ANGINA HX",
      "CITIZEN ASSIST/SERVICE CALL LOCKED IN/OUT BUILDING (NON MEDICAL ASSISTANCE)",
      "COURT ON DUTY",
      "FALLS NOT DANGEROUS BODY AREA",
      "FALLS POSSIBLY DANGEROUS BODY AREA",
      "FALLS - PUBLIC ASSIST NO INJURIES",
      "FALLS PUBLIC ASSIST NO INJURIES",
      "HEART PROBLEMS/A.I.C.D HEART RATE >50 OR 130",
      "MEDICAL ALARM",
      "MEDICAL CALL NO INFO",
      "MISCELLANEOUS",
      "Paramount ProQA",
      "POLICE ASK FOR MEDICAL",
      "SICK PERSON 2-28 NON-PRIORITY COMPLAINTS",
      "SICK PERSON ABNORMAL BREATHING",
      "SICK PERSON NO PRIORITY SYMPTOMS",
      "STROKE (CVA) STROKE HISTORY",
      "UNCONSCIOUS/FAINTING ALERT W ABNORMAL BREATHING",
      "UNCONSCIOUS/FAINTING FAINTING EPISODE W ALERT 35 W/O CARDIAC HISTORY",
      "UNCONSCIOUS/FAINTING UNCONSCIOUS AGONAL/INEFFECTIVE BREATHING",
      "UNCONSCIOUS/FAINTING UNCONSCIOUS BREATHING EFFECTIVE"
  );

  private static final String[] CITY_LIST = new String[]{
      // Towns
      "EASTON",
      "OXFORD",
      "QUEEN ANNE",
      "SAINT MICHAELS",
      "ST MICHAELS",
      "TRAPPE",

      // Census-designated places
      "CORDOVA",
      "TILGHMAN ISLAND",

      // Unincorporated communities
      "BOZMAN",
      "CLAIBORNE",
      "FAIRBANKS",
      "MCDANIEL",
      "NEAVITT",
      "NEWCOMB",
      "ROYAL OAK",
      "SHERWOOD",
      "WITTMAN",
      "WINDY HILL",
      "WYE MILLS"
  };

}
