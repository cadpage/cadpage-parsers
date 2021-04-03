package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCAlexanderCountyParser extends DispatchOSSIParser {

  public NCAlexanderCountyParser() {
    super(CITY_CODES, "ALEXANDER COUNTY", "NC",
          "FYI? ( CALL2/Z ADDR/Z CITY X+? ID? CODE? INFO+ " +
               "| SRC? ADDR ( CALL CODE? | CALL/Z! ( END | CODE | X ) | PLACE CALL! CODE? ) X+? INFO+ )");
    addRoadSuffixTerms("LP", "PARK", "PVT");
    removeWords("AVENUE", "BEND", "COVE");
    setupProtectedNames("B AND S");
  }

  @Override
  public String getFilter() {
    return "CAD@alexandercountync.gov,6504224256";
  }

  private static final Pattern CITY_DIST_PTN = Pattern.compile("( [A-Z]{2,3})(DIST:)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (body.length() == 0) body = subject;
    body = stripFieldStart(body, "|");
    if (subject.length() == 0 && body.startsWith("Text Message / ")) {
      subject = "Text Message";
      body = body.substring(15).trim();
    }
    if (!body.startsWith("CAD:") &&
        (subject.equals("Text Message") || isPositiveId())) {
      body = "CAD:" + body;
    }

    body = CITY_DIST_PTN.matcher(body).replaceAll("$1;$2");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("SRC")) return new SourceField("[A-Z]+", true);
    if (name.equals("ID")) return new IdField("\\d{10}");
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Za-z]?", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL_LIST.contains(field)) return false;
      super.parse(field, data);
      return true;
    }
  }

  private static final Pattern CALL_CH_PTN = Pattern.compile("(.+?) CHANNEL # (\\d+)");
  private class MyCall2Field extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // See if this is one of our special call fields
      if (!field.startsWith("CANCEL") && !field.equals("UNDER CONTROL") &&
          !field.startsWith("FIRE OPS") &&
          !field.startsWith("ROUTINE FURTHER RESPONSE")) {
        if (!field.startsWith("{")) return false;
        int pt = field.indexOf('}');
        if (pt < 0) return false;
        data.strSource = field.substring(1,pt).trim();
        field = field.substring(pt+1).trim();
      }
      Matcher match = CALL_CH_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1) + ' ' +  match.group(2);
      } else {
        data.strCall = field;
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("{")) {
        int pt = field.indexOf('}');
        if (pt >= 0) {
          data.strSource = field.substring(1,pt).trim();
          field = field.substring(pt+1).trim();
        }
      }
      Matcher match = CALL_CH_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1) + ' ' +  match.group(2);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL CH";
    }
  }

  private static final Pattern CROSS_MARK_PTN = Pattern.compile("\\b(?:NC|US)\\b");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (CROSS_MARK_PTN.matcher(field).find()) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }

  @Override
  public boolean checkCall(String call) {
    return CALL_LIST.contains(call);
  }

  private static final Set<String> CALL_LIST = new HashSet<String>(Arrays.asList(
      "ABDOMINAL PAIN",
      "ADDITIONAL CREW REQUESTED",
      "ALLERGIES",
      "ANIMAL RESCUE",
      "ASSAULT OR SEXUAL ASSAULT",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BRUSH GRASS WOODS FIRE",
      "BUILDING AND BRUSH",
      "CANCEL",
      "CANCEL FURTHER RESPONSE",
      "CARBON MONOXIDE DETECTOR",
      "CARDIAC OR RESPIRATORY ARREST",
      "CARRY OUT",
      "CHEST PAIN",
      "CODE 44",
      "CONFIRMED PIN IN",
      "CONTROL TIME",
      "CONVULSIONS OR SEIZURES",
      "CONVULSIONS SEIZURES CHARLIE",
      "CPR IN PROGESS",
      "DIABETIC PROBLEMS",
      "DIABETIC PROBLEMS DELTA",
      "ELECTROCUTION OR LIGHTNING",
      "EMS STAND BY",
      "FALL",
      "FIRE SERVICE CALL",
      "FIRE STAND BY",
      "FIRE STRUCTURE",
      "FIRE TRAINING",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT OR COLD EXPOSURE",
      "HEMMORRHAGE OR LACERATION",
      "HIGH ANGLE RESCUE",
      "ILLEGAL BURNING",
      "INDUSTRIAL OR MACHINE ACCIDENT",
      "LARGE ALARM",
      "LARGE FIRE ALARM",
      "LARGE STRUCTURE FIRE",
      "LINES DOWN\\TRANSFORMER",
      "LP/NATURAL GAS",
      "LZ SETUP",
      "MANPOWER NEEDED",
      "MARSHALL PVT",
      "MEDICAL-99",
      "MUTUAL AID - FIRE",
      "MVA PROPERTY DAMAGE",
      "NORTHWOOD PARK",
      "OVERDOSE OR POISONING",
      "PATIENT FREE",
      "PREGNANCY OR CHILDBIRTH",
      "PSYCHIATRIC OR SUICIDE ATTEMPT",
      "ROADWAY/TRAFFIC HAZARD",
      "ROUTINE FURTHER RESPONSE",
      "SICK PERSON",
      "SMOKE  IN AREA REPORT",
      "SMOKE SHOWING",
      "STAB OR GUNSHOT",
      "STANDARD FIRE ALARM",
      "STANDARD STRUCTURE FIRE",
      "STROKE",
      "STRUCTURE FIRE",
      "TAYCODE 401",
      "TRAFFIC ACCIDENT",
      "TRAFFIC CONTROL",
      "TRAUMATIC INJURIES",
      "TREE BLOCKING THE ROADWAY",
      "UNCONSC FAINT NEAR DELTA",
      "UNCONSCIOUS OR FAINTING",
      "UNDER CONTROL",
      "UNKNOWN PROBLEM",
      "VEHICLE FIRE",
      "VENT GOING BACK TO VALLEY",
      "WATER RESCUE",
      "WEATHER RELATED",
      "WORKING FIRE"
  ));

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GF",  "GRANITE FALLS",
      "HID", "HIDDENITE",
      "HKA", "HICKORY",
      "MOR", "MORAVIAN FALLS",
      "STP", "STONY POINT",
      "SVA", "STATESVILLE",
      "TAY", "TAYLORSVILLE TWP"
  });

}
