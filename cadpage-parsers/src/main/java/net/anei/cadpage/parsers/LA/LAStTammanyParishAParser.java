package net.anei.cadpage.parsers.LA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class LAStTammanyParishAParser extends FieldProgramParser {

  public LAStTammanyParishAParser() {
    super("ST TAMMANY PARISH", "LA",
          "CODE CALL_PFX+? ADDR/SL X/Z? MAP! END");
    setupCallList(CALL_SET);
  }

  @Override
  public String getFilter() {
    return "stfpd1@stfpd1.dapage.net";
  }

  private String callPrefix = "";

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty() || !body.startsWith(subject)) return false;

    callPrefix = "";
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("[A-Z0-9]{1,5}", true);
    if (name.equals("CALL_PFX")) return new MyCallPrefixField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MapField("\\d{4,5}[A-Z]?|COV\\d|OUT");
    return super.getField(name);
  }

  private static final Pattern PFX_CALL_PTN = Pattern.compile("(?:BITES|The) ");

  private class MyCallPrefixField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!PFX_CALL_PTN.matcher(getRelativeField(+1)).lookingAt()) return false;
      callPrefix = append(callPrefix, ", ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return null;
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(callPrefix, ", ", field);
      super.parse(field, data);
    }
  }

  private static final CodeSet CALL_SET = new CodeSet(
      "AB PAIN / PROBLEMS",
      "ALLERGIES / STINGS, BITES",
      "ASSAULT / SEXUAL / STUN GUN",
      "BACK PAIN NON RECENT OR TRAUMATIC",
      "BREATHING PROBLEMS",
      "BURNS / EXPLOSION MINOR",
      "CARDIAC / RESPIRATORY ARREST",
      "CHEST PAIN ABNORMAL BREATHING",
      "CHEST PAIN / CLAMMY OR COLD SWEATS",
      "CHOKING / COMPLETE OBSTRUCTION",
      "CHOKING / PARTIAL OBSTRUCTION",
      "DIABETIC NOT ALERT",
      "DIABETIC PROBLEM / ALERT",
      "ELECTRICAL HAZARD",
      "ELECTRICAL HAZARD C",
      "ELECTRICAL HAZARD SAW",
      "EXPLOSION",
      "FAINTING",
      "FAINTING (ALERT)",
      "FALL / EXTREME",
      "FALL / NON RECENT/ NOT DANGEROUS",
      "FALL / SERIOUS",
      "FIRE ALARM (HIGH LIFE HAZARDS)",
      "FIRE ALARMS (LOW LIFE HAZARDS)",
      "FIRE INCIDENT",
      "FUEL SPILL/ FUEL ODOR",
      "GAS LEAK/GAS ODOR (NATURAL AND LP GAS)",
      "HEART PROBLEMS",
      "HEAT/COLD EXPOSURE /NOT ALERT",
      "HEMORRHAGE/LACERATION MAJOR",
      "HEMORRHAGE / LACERATION MINOR",
      "HEMORRHAGE / LACERATION POSSIBLY DANGEROUS",
      "INEFFECTIVE BREATHING",
      "MARINE/BOAT FIRE",
      "MEDICAL",
      "MEDICAL INCIDENT",
      "MVA",
      "MVC",
      "OBVIOUS DEATH",
      "ON AIR",
      "OUT OF SERVICE",
      "OUTSIDE FIRE (EXTINGUISHED)",
      "OUTSIDE FIRE (LARGE)",
      "OUTSIDE FIRE (SMALL)",
      "OVERDOSE NOT ALERT",
      "OVERDOSE UNCONSCIOUS",
      "PHYSICAL FITNESS",
      "POISONING (W/O PRIORITY SYMPTOMS)",
      "PREGNANCY/CHILDBIRTH",
      "PSYCHIATRIC/SUICIDE ATTEMPT",
      "PUBLIC LIFT ASSIST",
      "SEIZURE",
      "SEIZURE / CONVULSION",
      "SEIZURE / NOT SEIZING",
      "SERVICE CALL (53A)",
      "SERVICE CALL (53B)",
      "SERVICE CALL (53O)",
      "SICK PERSON",
      "SICK PERSON (NON PRIORITY)",
      "SICK PERSON (NOT ALERT)",
      "SMOKE INVESTIGATION (OUTSIDE) HEAVY",
      "STROKE / TIA",
      "STRUCTURE FIRE",
      "TEST CALL",
      "TRAFFIC INCIDENT",
      "TRAUMATC INJURIES",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS / FAINTING",
      "UNKNOWN PROBLEM",
      "VEHICLE FIRE",
      "VEHICLE FIRE (LARGE)",
      "WILDLAND/BRUSH/GRASS FIRE (INVESTIGATION)",
      "WILDLAND/BRUSH/GRASS FIRE (LARGE)",
      "WILDLAND/BRUSH/GRASS FIRE (SMALL)",
      "WILDLAND/BRUSH/GRASS FIRE (SMALL) CLEAR"
 );
}
